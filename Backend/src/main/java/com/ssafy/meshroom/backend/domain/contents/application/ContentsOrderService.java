package com.ssafy.meshroom.backend.domain.contents.application;

import com.ssafy.meshroom.backend.domain.OVToken.application.OVTokenService;
import com.ssafy.meshroom.backend.domain.contents.dao.ContentsOrderRepository;
import com.ssafy.meshroom.backend.domain.contents.dao.ContentsRepository;
import com.ssafy.meshroom.backend.domain.contents.domain.Contents;
import com.ssafy.meshroom.backend.domain.contents.domain.ContentsOrder;
import com.ssafy.meshroom.backend.domain.contents.dto.ContentsOrderSubscribe;
import com.ssafy.meshroom.backend.domain.contents.dto.GroupState;
import com.ssafy.meshroom.backend.domain.session.dao.SessionRepository;
import com.ssafy.meshroom.backend.domain.session.domain.Session;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentsOrderService {

    public final ContentsOrderRepository contentsOrderRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final OVTokenService ovTokenService;
    @Autowired
    private final RedisTemplate<String, Boolean> booleanRedisTemplate;
    private final SessionRepository sessionRepository;
    private final ContentsRepository contentsRepository;

    public void saveContentsOrder(String sessionSid, List<String> contents) {
        int idx = 1;
        for (String contentId : contents) {
            contentsOrderRepository.save(
                    ContentsOrder.builder()
                            .sessionId(sessionSid)
                            .contentsId(contentId)
                            .sequence((long) idx++)
                            .isDone(false)
                            .build()
            );

        }

    }

    public Response<ContentsOrderSubscribe> nextContents(boolean isStart) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Session session = ovTokenService.getMainSessionFromUserId(userId);

        List<ContentsOrder> li = contentsOrderRepository.findAllBySessionId(session.get_id());

        Long total = (long) li.size();
        Long sequence = 0L;
        String contentsId = "";

        for (int i = 0; i < li.size(); i++) {
            ContentsOrder con = li.get(i);
            if (isStart) {
                sequence = con.getSequence();
                contentsId = con.getContentsId();
                break;
            }
            if (!con.getIsDone()) {
                contentsOrderRepository.updateById(con.get_id(), true);
                if (i != li.size() - 1) {
                    contentsId = li.get(i + 1).getContentsId();
                    sequence = li.get(i + 1).getSequence();
                } else {
                    contentsId = "0";
                    sequence = -1L;
                }
                break;
            }
        }

        if (!contentsId.equals("0")) {
            Contents nowContent = contentsRepository.findById(contentsId).orElseThrow(NoSuchElementException::new);

            if (nowContent.getTimeOut() > 0) {

                Long timeLimit = nowContent.getTimeOut();
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

                scheduler.schedule(() -> {

                    simpMessagingTemplate.convertAndSend("/subscribe/contents/" + session.getSessionId() + "/finish", "true");
                    log.info("=========================================================");

                }, timeLimit, TimeUnit.SECONDS);

            }
        }


        log.info("contentsId {} , sequence {}   ", contentsId, sequence);

        // 모든 세션에 대한 status를 false로 갱신해주는 작업
        List<GroupState> groupStates = new ArrayList<>();
        List<Session> subsessions = sessionRepository.findAllByMainSession(session.getSessionId()).orElseThrow();

        for (Session sub : subsessions) {
            groupStates.add(GroupState.builder()
                    .sessionId(sub.getSessionId())
                    .isFinish(false)
                    .build()
            );
            booleanRedisTemplate.opsForValue().set("contents:" + sub.getSessionId(), Boolean.FALSE);
        }

        // 수정해야함
        ContentsOrderSubscribe ret = ContentsOrderSubscribe.builder()
                .totalContentsCount(total)
                .contentsId(contentsId)
                .contentsSequence(sequence)
                .currentGroupState(groupStates)
                .finishGroupCount(0L)
                .totalGroupCount((long) subsessions.size())
                .build();

        simpMessagingTemplate.convertAndSend("/subscribe/contents/" + session.getSessionId(), ret);
        return new Response<ContentsOrderSubscribe>(true, 200L, "ok", ret);
    }


    public Response<ContentsOrderSubscribe> reloadCurrentContent() {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Session session = ovTokenService.getMainSessionFromUserId(userId);
        List<ContentsOrder> contentsOrders = contentsOrderRepository.findAllBySessionId(session.get_id());
        List<Session> subsessions = sessionRepository.findAllByMainSession(session.getSessionId()).orElseThrow();

        ContentsOrder currentContent = null;

        for (ContentsOrder con : contentsOrders) {
            if (!con.getIsDone()) {
                currentContent = con;
                break;
            }
        }

        //모든 세션에 대한 status를 false로 갱신해주는 작업
        List<GroupState> groupStates = new ArrayList<>();

        for (Session sub : subsessions) {
            groupStates.add(GroupState.builder()
                    .sessionId(sub.getSessionId())
                    .isFinish(false)
                    .build());
            booleanRedisTemplate.opsForValue().set("contents:" + sub.getSessionId(), false);
        }


        ContentsOrderSubscribe ret = ContentsOrderSubscribe.builder()
                .contentsId(currentContent.getContentsId())
                .contentsSequence(currentContent.getSequence())
                .totalContentsCount((long) contentsOrders.size())
                .finishGroupCount(0L)
                .totalGroupCount((long) subsessions.size())
                .currentGroupState(groupStates)
                .build();


        simpMessagingTemplate.convertAndSend("/subscribe/contents/" + session.getSessionId(), ret);
        return new Response<>(true, 200L, "ok", ret);
    }

    // 팀장이 본인 해당하는 세션 완료 누르기
    public Response<ContentsOrderSubscribe> finishSubSession(String subSessionId) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Session mainSession = ovTokenService.getMainSessionFromUserId(userId);

        booleanRedisTemplate.opsForValue().set("contents:" + subSessionId, true);

        List<GroupState> groupStates = new ArrayList<>();
        List<Session> subSessions = sessionRepository.findAllByMainSession(mainSession.getSessionId()).orElseThrow();

        long cnt = 0;

        for (Session subSession : subSessions) {
            Boolean state = booleanRedisTemplate.opsForValue().get("contents:" + subSession.getSessionId());
            if (state == null) {
                state = Boolean.FALSE;
            } else if (state) {
                cnt++;
            }
            groupStates.add(GroupState.builder()
                    .sessionId(subSession.getSessionId())
                    .isFinish(state)
                    .build()
            );
        }


        List<ContentsOrder> li = contentsOrderRepository.findAllBySessionId(mainSession.get_id());
        ContentsOrder currentContent = null;
        for (ContentsOrder con : li) {
            if (!con.getIsDone()) {
                currentContent = con;
                break;
            }

        }
        ContentsOrderSubscribe ret = ContentsOrderSubscribe.builder()
                .contentsId(currentContent.getContentsId())
                .contentsSequence(currentContent.getSequence())
                .totalContentsCount((long) li.size())
                .finishGroupCount(cnt)
                .totalGroupCount((long) subSessions.size())
                .currentGroupState(groupStates)
                .build();

        simpMessagingTemplate.convertAndSend("/subscribe/contents/" + mainSession.getSessionId(), ret);
        return new Response<>(true, 200L, "ok", ret);
    }


}
