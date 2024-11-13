package com.ssafy.meshroom.backend.domain.contents.application;

import com.ssafy.meshroom.backend.domain.contents.dao.ContentsRepository;
import com.ssafy.meshroom.backend.domain.contents.dto.ContentsListResponse;
import com.ssafy.meshroom.backend.global.common.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentsService {

    private final ContentsRepository contentsRepository;

    public Response<ContentsListResponse> getContents(){
        log.info(contentsRepository.findAll().toString());
        return new Response<>(true,2000L,"SUCCESS",
                ContentsListResponse.builder()
                .contents(contentsRepository.findAll()).build());
    }

}
