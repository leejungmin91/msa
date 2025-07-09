package com.store.point.service;

import com.store.point.dto.MemberDto;
import com.store.point.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {

    private RestClient restClient;
    private PointRepository pointRepository;
    private PointHistoryService pointHistoryService;
    private PointService pointService; // getUserInfo가 포함된 클래스

    @BeforeEach
    void setUp() {
        pointRepository = mock(PointRepository.class);
        pointHistoryService = mock(PointHistoryService.class);
        restClient = mock(RestClient.class, RETURNS_DEEP_STUBS); // 중첩 mock 자동처리
        RestClient.RequestHeadersUriSpec<?> uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), anyLong())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        MemberDto mockMember = MemberDto.builder()
                .id(1L)
                .name("홍길동")
                .build();
        when(responseSpec.body(MemberDto.class)).thenReturn(mockMember);

        pointService = new PointService(pointRepository, pointHistoryService, restClient); // 생성자 주입 가정
    }

    @Test
    void 고객정보_가져오기() {
        MemberDto result = pointService.getUserInfo(1L);
        assertEquals(1L, result.getId());
        assertEquals("홍길동", result.getName());
    }
}
