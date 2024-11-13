package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.service;

import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dao.InitialQuizCategoryRepository;
import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dao.InitialQuizRepository;
import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.domain.InitialQuizCategory;
import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.domain.InitialQuizInfo;
import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dto.*;
import com.ssafy.meshroom.backend.domain.user.dao.UserRepository;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitialQuizService {
    private final UserRepository userRepository;

    private final RedisTemplate<String, Integer> redisTemplate;
    private final RedisTemplate<String, String> wordRedisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final InitialQuizRepository initialQuizRepository;
    private final InitialQuizCategoryRepository initialQuizCategoryRepository;

    public Response<IniQuizCategoryResponse> getIniQuizCategory(String sessionId) {
        InitialQuizCategory randomCategory = initialQuizCategoryRepository.findRandomCategory()
                .orElseThrow(()-> new RuntimeException("카테고리가 없습니다."));

        IniQuizCategoryResponse iniQuizCategoryResponse = IniQuizCategoryResponse.builder()
                .categoryName(randomCategory.getCategoryName())
                .build();

        return new Response<IniQuizCategoryResponse>(true, 2000L, "SUCCESS",
                iniQuizCategoryResponse
        );
    }

    public Response<IniQuizInfoCreateResponse> insertIniQuizInfo(String mainSessionId, String subSessionId, String userSid, IniQuizInfoCreateRequest iniQuizInfoCreateRequest) {
        String userName = userRepository.findById(userSid)
                .orElseThrow(() -> new RuntimeException("유저 없음"))
                .getUsername();

        InitialQuizInfo newInitialQuizInfo = InitialQuizInfo.builder()
                .userName(userName)
                .ovToken(iniQuizInfoCreateRequest.getOvToken())
                .sessionId(subSessionId)
                .categoryName(iniQuizInfoCreateRequest.getCategoryName())
                .word(iniQuizInfoCreateRequest.getQuizWord())
                .build();

        if (initialQuizRepository.existsByOvTokenAndSessionId(iniQuizInfoCreateRequest.getOvToken(), subSessionId)) {
            InitialQuizInfo foundInitialQuizInfo = initialQuizRepository.findByOvTokenAndSessionId(iniQuizInfoCreateRequest.getOvToken(), subSessionId)
                    .orElseThrow(() -> new RuntimeException("정보가 존재하지 않음"));
            foundInitialQuizInfo.setUserName(userName);
            foundInitialQuizInfo.setCategoryName(iniQuizInfoCreateRequest.getCategoryName());
            foundInitialQuizInfo.setWord(iniQuizInfoCreateRequest.getQuizWord());
            initialQuizRepository.save(foundInitialQuizInfo);
        } else {
            initialQuizRepository.save(newInitialQuizInfo);
        }

        setQuizWordToRedis(subSessionId, iniQuizInfoCreateRequest.getOvToken(), iniQuizInfoCreateRequest.getQuizWord());

        sendSignalToOtherUsers(subSessionId);
        sendSignalToAdministrator(mainSessionId, subSessionId, 1, true);

        return new Response<IniQuizInfoCreateResponse>(true, 2010L, "SUCCESS",
                IniQuizInfoCreateResponse.builder()
                        .isCreated(true)
                        .build()
        );
    }

    public Response<AllIniQuizInfosResponse> getAllIniQuizInfos(String sessionId) {
        List<InitialQuizInfo> initialQuizInfoList = initialQuizRepository.findAllBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("해당 세션ID이 없습니다."));
        log.info("initial quiz info : " + initialQuizInfoList.stream().toString());

        List<IniQuizInfoResponse> allIniQuizInfos = initialQuizInfoList.stream()
                .map(IniQuizInfoResponse::from)
                .toList();

        return new Response<AllIniQuizInfosResponse>(true, 2000L, "SUCCESS",
                new AllIniQuizInfosResponse(allIniQuizInfos)
        );
    }

    public boolean isGuessWordCorrect(String mainSessionId, String subSessionId, String ownerOvToken, String guessWord) {
        String redisKey = "iq-" + subSessionId + "-" + ownerOvToken;
        String answerWord = wordRedisTemplate.opsForValue().get(redisKey);

        if (answerWord == null || guessWord == null) return false;

        log.info("answer : " + answerWord + " / received : " + guessWord);

        boolean result = answerWord.equals(guessWord);

        if (result) {
            sendSignalToAdministrator(mainSessionId, subSessionId, 2, true);
        }

        return result;
    }

    private void setQuizWordToRedis(String sessionId, String ovToken, String quizWord) {
        String redisKey = "iq-" + sessionId + "-" + ovToken;

        wordRedisTemplate.opsForValue().set(redisKey, quizWord);
    }

    public void sendSignalToOtherUsers(String sessionId) {
        messagingTemplate.convertAndSend("/subscribe/game/ini-quiz/word/" + sessionId, true);
    }

    public void sendSignalToAdministrator(String mainSessionId, String subSessionId, int curStep, Boolean isDone) {
        String redisKey = "iq-" + subSessionId + "-" + curStep;

        Integer curCount = redisTemplate.opsForValue().get(redisKey);

        if (curCount == null) {
            curCount = 1;
        } else {
            curCount += 1;
        }
        redisTemplate.opsForValue().set(redisKey, curCount);

        IniQuizAdministratorSignal signalForAdministrator = new IniQuizAdministratorSignal();
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

        messagingTemplate.convertAndSend("/subscribe/manage/game/ini-quiz/" + mainSessionId, signalForAdministrator);
    }

}
