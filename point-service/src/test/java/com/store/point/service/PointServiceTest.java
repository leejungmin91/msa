package com.store.point.service;

import com.store.point.dto.MemberDto;
import com.store.point.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {

    private WebClient webClient;
    private PointRepository pointRepository;
    private PointHistoryService pointHistoryService;
    private PointService pointService; // getUserInfo가 포함된 클래스

    @BeforeEach
    void setUp() {
        pointRepository = mock(PointRepository.class);
        pointHistoryService = mock(PointHistoryService.class);
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS); // 중첩 mock 자동처리
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), anyLong())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        MemberDto mockMember = MemberDto.builder()
                .id(1L)
                .name("홍길동")
                .build();
        when(responseSpec.bodyToMono(MemberDto.class)).thenReturn(Mono.just(mockMember));

        pointService = new PointService(pointRepository, pointHistoryService, webClient); // 생성자 주입 가정
    }

    @Test
    void 고객정보_가져오기() {
        MemberDto result = pointService.getUserInfo(1L);
        assertEquals(1L, result.getId());
        assertEquals("홍길동", result.getName());
    }
}
