package com.ssafy.meshroom.backend.domain.contents.api;

import com.ssafy.meshroom.backend.domain.contents.application.ContentsOrderService;
import com.ssafy.meshroom.backend.domain.contents.application.ContentsService;
import com.ssafy.meshroom.backend.domain.contents.dto.ContentsListResponse;
import com.ssafy.meshroom.backend.domain.contents.dto.ContentsOrderSubscribe;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contents")
@RequiredArgsConstructor
@Slf4j
public class ContentsController {

    private final ContentsService contentsService;
    private final ContentsOrderService contentsOrderService;

    @GetMapping
    public ResponseEntity<Response<ContentsListResponse>> getContents() {
        return ResponseEntity.status(HttpStatus.OK).body(contentsService.getContents());
    }

    @GetMapping("/next/{isStart}")
    public ResponseEntity<Response<ContentsOrderSubscribe>> nextContents(@PathVariable("isStart") boolean isStart) {
        return ResponseEntity.status(HttpStatus.OK).body(contentsOrderService.nextContents(isStart));
    }

    @GetMapping("/reload")
    public ResponseEntity<Response<ContentsOrderSubscribe>> reloadCurrentContent() {
        Response<ContentsOrderSubscribe> response = contentsOrderService.reloadCurrentContent();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/finish/{subSessionId}")
    public ResponseEntity<Response<ContentsOrderSubscribe>> finishSubSession(@PathVariable("subSessionId") String subSessionId) {
        Response<ContentsOrderSubscribe> response = contentsOrderService.finishSubSession(subSessionId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
