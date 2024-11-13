package com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.handler;

import com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.dto.*;
import com.ssafy.meshroom.backend.domain.topic.game.trueorfalse.service.TrueOrFalseService;
import com.ssafy.meshroom.backend.domain.user.application.UserDetailService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "게임-진실혹은거짓 API", description = "진실 혹은 거짓 게임을 위한 API")
public class TrueOrFalseEventHandler {
    private final TrueOrFalseService trueOrFalseService;
    private final UserDetailService userDetailService;

    @Operation(
            summary = "진실or거짓 진술서 생성",
            description = "사용자별로 새로운 진술서를 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "새 진술서 생성을 위한 요청 본문",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TFInfoCreateRequest.class)
                    )
            ),
            parameters = {
                    @Parameter(name = "sessionId", description = "그룹(하위) 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "2010",
                            description = "성공적으로 진술서가 생성됨",
                            content = @Content(
                                    schema = @Schema(implementation = TFInfoCreateResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping("/api/v1/game/tf/{sessionId}")
    public ResponseEntity<Response<TFInfoCreateResponse>> createTFInfo (
            @PathVariable String sessionId,
            @RequestBody TFInfoCreateRequest tfInfoCreateRequest) {
        String userSid = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trueOrFalseService.createTFInfo(sessionId, userSid, tfInfoCreateRequest));
    }

    @Operation(
            summary = "모든 진실or거짓 진술서들 불러오기",
            description = "현재 세션에 포함된 모든 사용자의 ovToken과 진술서 내용들을 불러옵니다.",
            parameters = {
                    @Parameter(name = "sessionId", description = "그룹(하위) 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "2000",
                            description = "성공적으로 진술서를 불러옴",
                            content = @Content(
                                    schema = @Schema(implementation = AllTFInfosResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/api/v1/game/tf/{sessionId}")
    public ResponseEntity<Response<AllTFInfosResponse>> getAllTFInfos (
            @PathVariable String sessionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(trueOrFalseService.getAllTFInfo(sessionId));
    }


    @MessageMapping("/game/tf/question/{mainSessionId}/{subSessionId}")
    @SendTo("/subscribe/game/tf/question/{mainSessionId}/{subSessionId}")
    public Boolean handleSubmitTF(@DestinationVariable String mainSessionId,
                                  @DestinationVariable String subSessionId,
                                  Boolean isDone) {
        log.info("submit signal recieved : " + mainSessionId+"/"+subSessionId + " - " + isDone);

        sendToAdministrator(mainSessionId, subSessionId, 1, isDone);

        return isDone;
    }

    @MessageMapping("/game/tf/answer/{sessionId}")
    @SendTo("/subscribe/game/tf/answer/{sessionId}")
    public TFAnswerResponseSignal handleAnswerTF(Principal p, @DestinationVariable String sessionId, TFAnswerRequestSignal answerSignal) {
        log.info("choose number signal recieved : " + sessionId + " - " + answerSignal.toString());

        String userSid = ((UsernamePasswordAuthenticationToken)p).getName();
        String userName = userDetailService.getUserName(userSid).getUsername();

        TFAnswerResponseSignal answerResponseSignal = new TFAnswerResponseSignal();
        answerResponseSignal.setOvToken(answerSignal.getOvToken());
        answerResponseSignal.setChosen(answerSignal.getChosen());
        answerResponseSignal.setUserName(userName);

        return answerResponseSignal;
    }

    @MessageMapping("/game/tf/next/{mainSessionId}/{subSessionId}")
    @SendTo("/subscribe/game/tf/next/{mainSessionId}/{subSessionId}")
    public Boolean handleNextTF(@DestinationVariable String mainSessionId,
                                @DestinationVariable String subSessionId,
                                Boolean isDone) {
        log.info("presentation finished signal recieved : " + mainSessionId+"/"+subSessionId + " - " + isDone);

        sendToAdministrator(mainSessionId, subSessionId, 2, isDone);

        return isDone;
    }

    // (DEPRECATED) 팀장이 진행자에게 자기 세션의 컨텐츠 진행이 끝났음을 알림
//    @MessageMapping("/game/tf/finish")
//    @SendTo("/subscribe/game/tf/finish")
//    public String handleFinishTF(String sessionId) {
//        log.info("finished signal recieved : " + sessionId);
//        return sessionId;
//    }

    public void sendToAdministrator(String mainSessionId, String subSessionId, int curStep, Boolean isDone) {
        trueOrFalseService.sendSignalToAdministrator(mainSessionId, subSessionId, curStep, isDone);
    }
}
