package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.service;

import com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dao.TrueOrFalseRepository;
import com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.domain.TFInfo;
import com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dto.*;
import com.ssafy.meshroom.backend.domain.user.dao.UserRepository;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrueOrFalseService {
    private final TrueOrFalseRepository trueOrFalseRepository;
    private final UserRepository userRepository;

    private final RedisTemplate<String, Integer> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public Response<TFInfoCreateResponse> createTFInfo(String sessionId, String userSid, TFInfoCreateRequest tfInfoCreateRequest) {
        String userName = userRepository.findById(userSid)
                .orElseThrow(() -> new RuntimeException("유저 없음"))
                .getUsername();

        ArrayList<String> mixedStatements = new ArrayList<>(tfInfoCreateRequest.getTruths());
        int falseIndex = (int)(Math.random()*10) % tfInfoCreateRequest.getTruths().size();
        mixedStatements.add(falseIndex, tfInfoCreateRequest.getFalse1());

        TFInfo newTFInfo = TFInfo.builder()
                            .userName(userName)
                            .ovToken(tfInfoCreateRequest.getOvToken())
                            .sessionId(sessionId)
                            .statements(mixedStatements)
                            .falseIndex(falseIndex)
                        .build();

        if (trueOrFalseRepository.existsByOvTokenAndSessionId(tfInfoCreateRequest.getOvToken(), sessionId)) {
            TFInfo foundTFInfo = trueOrFalseRepository.findByOvTokenAndSessionId(tfInfoCreateRequest.getOvToken(), sessionId)
                            .orElseThrow(() -> new RuntimeException("정보가 존재하지 않음"));
            foundTFInfo.setStatements(mixedStatements);
            foundTFInfo.setFalseIndex(falseIndex);
            trueOrFalseRepository.save(foundTFInfo);
        } else {
            trueOrFalseRepository.save(newTFInfo);
        }

        return new Response<TFInfoCreateResponse>(true, 2010L, "SUCCESS",
                TFInfoCreateResponse.builder()
                        .isCreated(true)
                        .build()
        );
    }

    public Response<AllTFInfosResponse> getAllTFInfo(String sessionId) {
        List<TFInfo> tfInfoList = trueOrFalseRepository.findAllBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("해당 세션ID이 없습니다."));
        log.info("true or false info : " + tfInfoList.stream().toString());

        List<TFInfoResponse> allTFInfos = tfInfoList.stream()
                .map(TFInfoResponse::from)
                .toList();

        return new Response<AllTFInfosResponse>(true, 2000L, "SUCCESS",
                new AllTFInfosResponse(allTFInfos)
        );
    }

    public void sendSignalToAdministrator(String mainSessionId, String subSessionId, int curStep, Boolean isDone) {
        String redisKey = "tf-" + subSessionId + "-" + curStep;

        Integer curCount = redisTemplate.opsForValue().get(redisKey);

        if (curCount == null) {
            curCount = 1;
        } else {
            curCount += 1;
        }
        redisTemplate.opsForValue().set(redisKey, curCount);

        TFAdministratorSignal signalForAdministrator = new TFAdministratorSignal();
        signalForAdministrator.setSessionId(subSessionId);
        signalForAdministrator.setCurStep(curStep);
        switch (curStep) {
            case 1:
                signalForAdministrator.setSubmitCount(curCount);
                signalForAdministrator.setFinishCount(0);
                break;
            case 2:
                signalForAdministrator.setSubmitCount(0);
                signalForAdministrator.setFinishCount(curCount);
                break;
            default:
                signalForAdministrator.setSubmitCount(0);
                signalForAdministrator.setFinishCount(0);
        }

        messagingTemplate.convertAndSend("/subscribe/manage/game/tf/" + mainSessionId, signalForAdministrator);
    }
}
