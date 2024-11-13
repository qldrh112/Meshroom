package com.ssafy.meshroom.backend.domain.session.api;

import com.ssafy.meshroom.backend.domain.session.application.SessionService;
import com.ssafy.meshroom.backend.domain.session.dto.*;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "세션 API", description = "세션 관리를 위한 API")
public class SessionController {

    private final SessionService sessionService;

    @Operation(
            summary = "새 세션 생성",
            description = "새로운 세션을 생성하고 세션 ID와 URL을 포함한 세션 세부 정보를 반환합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "새 세션 생성을 위한 요청 본문",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SessionCreateRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "성공적으로 세션이 생성됨",
                            content = @Content(
                                    schema = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping
    public ResponseEntity<Response<SessionCreateResponse>> createSessions(
            @RequestBody SessionCreateRequest request)
            throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session createSessions API");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createSession(request.getContentsOrder()));
    }

    @Operation(
            summary = "새 하위 세션 생성",
            description = "지정된 세션 ID에 새로운 하위 세션을 생성하고 하위 세션 ID를 반환합니다.",
            parameters = {
                    @Parameter(name = "sessionId", description = "상위 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "성공적으로 하위 세션이 생성됨",
                            content = @Content(
                                    schema = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            }
    )
    @PostMapping("/{sessionId}")
    public ResponseEntity<Response<SubSessionCreateResponse>> createSubSessions(
            @PathVariable("sessionId") String sessionId)
            throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session createSubSessions API");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createSubSession(sessionId));
    }

    @Operation(
            summary = "세션 정보 조회",
            description = "세션에 대한 세부 정보를 반환합니다. 반환되는 정보에는 세부 세션 목록이 포함됩니다."
    )
    @GetMapping("/{sessionId}")
    public ResponseEntity<Response<SessionInfoResponse>> sessionInfo(@PathVariable("sessionId") String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session sessionInfo API");
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.getSessionInfo(sessionId));
    }

    @Operation(
        summary = "하위 세션 정보 조회",
        description = "지정된 하위 세션에 대한 세부 정보를 반환합니다. 반환되는 정보에는 세션 ID, 그룹 이름, 최대 사용자 수, 현재 사용자 수, 사용자 이름 목록이 포함됩니다.",
        parameters = {
                @Parameter(name = "sessionId", description = "상위 세션의 ID", required = true),
                @Parameter(name = "subSessionId", description = "조회할 하위 세션의 ID", required = true)
        },
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "성공적으로 하위 세션 정보를 반환함",
                        content = @Content(
                                schema = @Schema(implementation = Response.class)
                        )
                ),
                @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
        }
    )
    @GetMapping("/{sessionId}/{subSessionId}")
    public ResponseEntity<Response<SubSessionInfoResponse>> subSessionInfo(
            @PathVariable("sessionId") String sessionId, @PathVariable("subSessionId") String subSessionId)
            throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session subSessionInfo API");
        return ResponseEntity.status(HttpStatus.OK)
                .body(sessionService.getSubSessionInfo(sessionId, subSessionId));
    }

    @Operation(
            summary = "세션에 대한 연결 생성",
            description = "지정된 세션에 새로운 연결을 생성하고 연결 세부 정보를 반환합니다. 요청 본문에 사용자 이름을 포함해야 하며, 생성된 연결 정보로 JWT 토큰을 HTTP-Only 쿠키로 설정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "연결 생성을 위한 요청 본문. 사용자 이름을 포함해야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ConnectionCreateRequest.class)
                    )
            ),
            parameters = {
                    @Parameter(name = "sessionId", description = "연결을 생성할 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "성공적으로 연결이 생성됨",
                            content = @Content(
                                    schema = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping("/{sessionId}/connections")
    public ResponseEntity<Response<ConnectionCreateResponse>> createConnections(
            @PathVariable("sessionId") String sessionId,
            @RequestBody ConnectionCreateRequest req,
            HttpServletResponse response)
            throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session createConnections API");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createConnection(req.getUserName(), sessionId, response));
    }

    @Operation(
            summary = "세션 삭제",
            description = "지정된 세션을 삭제합니다.",
            parameters = {
                    @Parameter(name = "sessionId", description = "삭제할 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공적으로 세션이 삭제됨",
                            content = @Content(
                                    schema = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Response<?>> removeSession(@PathVariable("sessionId") String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session removeSession API");
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.deleteSession(sessionId));
    }

    @Operation(
            summary = "세션의 최대 사용자 수 수정",
            description = "지정된 세션의 최대 사용자 수와 최대 하위 사용자 수를 수정합니다.",
            parameters = {
                    @Parameter(name = "sessionId", description = "수정할 세션의 ID", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "세션의 최대 사용자 수와 최대 하위 사용자 수를 수정하기 위한 요청 본문",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateSessionRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공적으로 세션 정보가 수정됨",
                            content = @Content(
                                    schema = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PatchMapping("/{sessionId}")
    public ResponseEntity<Response<?>> updateSessionUserCounts(
            @PathVariable("sessionId") String sessionId,
            @RequestBody UpdateSessionRequest request) throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session updateSessionUserCounts API");
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.updateSessionUserCounts(sessionId, request));
    }

    @Operation(
            summary = "하위 세션 그룹 이름 수정",
            description = "지정된 하위 세션의 그룹 이름을 수정합니다.",
            parameters = {
                    @Parameter(name = "subsessionId", description = "수정할 하위 세션의 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공적으로 하위 세션의 그룹 이름이 수정됨",
                            content = @Content(
                                    schema = @Schema(implementation = Response.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "하위 세션을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/{subSessionId}/group-name")
    public ResponseEntity<Response<?>> updateSubSessionGroupName(
            @PathVariable("subSessionId") String subSessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session updateSubSessionGroupName API");
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.updateSubSessionGroupName(subSessionId));
    }

    @GetMapping("{subSessionId}/ready")
    public ResponseEntity<Response<?>> getReady(@PathVariable("subSessionId") String subSessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session Ready API");
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.subSessionReady(subSessionId));
    }

    @GetMapping("{subSessionId}/quit")
    public ResponseEntity<Response<?>> getQuit(@PathVariable("subSessionId") String subSessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        log.info("Session Quit API");
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.subSessionQuit(subSessionId));
    }

}
