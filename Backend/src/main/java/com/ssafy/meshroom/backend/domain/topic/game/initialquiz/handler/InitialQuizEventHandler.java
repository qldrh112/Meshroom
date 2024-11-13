package com.ssafy.meshroom.backend.domain.topic.game.initialquiz.handler;

import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.dto.*;
import com.ssafy.meshroom.backend.domain.topic.game.initialquiz.service.InitialQuizService;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "게임-초성게임 API", description = "초성 게임을 위한 API")
public class InitialQuizEventHandler {

    private final InitialQuizService initialQuizService;

    @Operation(
            summary = "초성 카테고리 하나를 랜덤하게 불러오기",
            description = "초성 문제 입력하기 전, 랜덤으로 카테고리 하나를 불러옵니다.",
            parameters = {
                    @Parameter(name = "sessionId", description = "그룹(하위) 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "2000",
                            description = "성공적으로 초성문제 정보들을 불러옴",
                            content = @Content(
                                    schema = @Schema(implementation = IniQuizCategoryResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/api/v1/game/ini-quiz/category/{sessionId}")
    public ResponseEntity<Response<IniQuizCategoryResponse>> getIniQuizCategory (
            @PathVariable String sessionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(initialQuizService.getIniQuizCategory(sessionId));
    }

    @Operation(
            summary = "초성게임 초성 문제 제출",
            description = "사용자별로 카테고리에 맞는 초성 문제를 생성하고, 그 현황을 진행자에게 알립니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "새 초성 문제 생성을 위한 요청 본문",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = IniQuizInfoCreateRequest.class)
                    )
            ),
            parameters = {
                    @Parameter(name = "mainSessionId", description = "메인 세션의 ID", required = true),
                    @Parameter(name = "subSessionId", description = "그룹(하위) 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "2010",
                            description = "성공적으로 초성 문제가 생성됨",
                            content = @Content(
                                    schema = @Schema(implementation = IniQuizInfoCreateResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping("/api/v1/game/ini-quiz/{mainSessionId}/{subSessionId}")
    public ResponseEntity<Response<IniQuizInfoCreateResponse>> createIniQuiz (
            @PathVariable String mainSessionId,
            @PathVariable String subSessionId,
            @RequestBody IniQuizInfoCreateRequest iniQuizInfoCreateRequest) {
        String userSid = SecurityContextHolder.getContext().getAuthentication().getName();

        Response<IniQuizInfoCreateResponse> iniQuizInfoCreateResponse = initialQuizService.insertIniQuizInfo(mainSessionId, subSessionId, userSid, iniQuizInfoCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(iniQuizInfoCreateResponse);
    }

    @Operation(
            summary = "모든 초성 문제 정보들 불러오기",
            description = "현재 세션에 포함된 모든 사용자의 기본 정보와 초성 문제 내용들을 불러옵니다.",
            parameters = {
                    @Parameter(name = "sessionId", description = "그룹(하위) 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "2000",
                            description = "성공적으로 초성문제 정보들을 불러옴",
                            content = @Content(
                                    schema = @Schema(implementation = AllIniQuizInfosResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/api/v1/game/ini-quiz/{sessionId}")
    public ResponseEntity<Response<AllIniQuizInfosResponse>> getAllIniQuiz (
            @PathVariable String sessionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(initialQuizService.getAllIniQuizInfos(sessionId));
    }

//    @MessageMapping("/game/ini-quiz/word/{sessionId}")
//    @SendTo("/subscribe/game/ini-quiz/word/{sessionId}")
//    public IniQuizAnswerRequestSignal handleSubmitIniQuiz(@DestinationVariable String sessionId, IniQuizAnswerRequestSignal iniQuizWordSignal) {
//        log.info("submit quiz signal received : " + sessionId + " - " + iniQuizWordSignal.toString());
//
//        setQuizWordToRedis(sessionId, iniQuizWordSignal);
//
//        String convertedWord = convertQuizWord(iniQuizWordSignal.getQuizWord());
//        iniQuizWordSignal.setQuizWord(convertedWord);
//
//        return iniQuizWordSignal;
//    }

    @MessageMapping("/game/ini-quiz/guess/{mainSessionId}/{subSessionId}")
    @SendTo("/subscribe/game/ini-quiz/guess/{mainSessionId}/{subSessionId}")
    public IniQuizAnswerResponseSignal handleAnswerIniQuiz(@DestinationVariable String mainSessionId,
                                                           @DestinationVariable String subSessionId,
                                                           IniQuizAnswerRequestSignal iniQuizAnswerRequestSignal) {
        log.info("guess quiz signal received : " + mainSessionId + "/" + subSessionId + " - " + iniQuizAnswerRequestSignal.toString());

        boolean guessResult = initialQuizService.isGuessWordCorrect(mainSessionId, subSessionId, iniQuizAnswerRequestSignal.getOwnerOvToken(), iniQuizAnswerRequestSignal.getGuessWord());

        IniQuizAnswerResponseSignal iniQuizAnswerResponseSignal = new IniQuizAnswerResponseSignal();
        iniQuizAnswerResponseSignal.setOvToken(iniQuizAnswerRequestSignal.getOvToken());
        iniQuizAnswerResponseSignal.setUserName(iniQuizAnswerRequestSignal.getUserName());
        iniQuizAnswerResponseSignal.setSubmittedWord(iniQuizAnswerRequestSignal.getGuessWord());
        iniQuizAnswerResponseSignal.setResult(guessResult);

        return iniQuizAnswerResponseSignal;
    }

}
