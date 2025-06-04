package com.store.member.service;

import com.store.api.member.domain.MemberSignUpDomain;
import com.store.api.member.dto.MemberResponseDto;
import com.store.api.member.entity.MemberEntity;
import com.store.api.member.repository.MemberRepository;
import com.store.api.member.service.MemberService;
import com.store.api.order.domain.OrderCreateDomain;
import com.store.api.order.dto.OrderOverviewResponseDto;
import com.store.api.order.entity.OrderEntity;
import com.store.api.order.repository.OrderRepository;
import com.store.api.order.service.OrderService;
import com.store.common.util.RedisService;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MemberServiceTest.class);
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderRepository orderRepository;

    /*@Test
    @DisplayName("회원가입 테스트")
    void 회원가입_테스트() {
        MemberSignUpDomain memberSignUpDomain = MemberSignUpDomain.builder()
                .email("leejungmin03@gmail.com")
                .name("이정민")
                .password("1234")
                .phone("01021220227")
                .zipcode("00000")
                .address("KT&G대치타워")
                .build();

        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        MemberEntity member = MemberEntity.signUp(memberSignUpDomain, passwordEncoder.encode(memberSignUpDomain.password()));

        when(memberRepository.save(any(MemberEntity.class))).thenReturn(member);

        //when
        MemberResponseDto memberResponseDto = memberService.register(memberSignUpDomain);

        //then
        assertThat(member.getId()).isEqualTo(memberResponseDto.getId());

        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }*/

    @Test
    @DisplayName("비밀번호 암호화 테스트")
    void 비밀번호_암호화_테스트() {
        // given
        String rawPassword = "1234";

        // when
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // then
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("레디스 테스트")
    void 레디스_테스트() {

        String transactionId = "TX"+ UUID.randomUUID();
        OrderCreateDomain order = OrderCreateDomain.builder()
                .memberId(1L)
                .orderName("orderName")
                .build();
        redisService.save(transactionId, order, 60 * 60 * 24 * 3);

        // given
        Object data = redisService.get(transactionId);

        assertThat(data).isNotNull();
    }

    @Test
    @DisplayName("주문번호 조회 테스트")
    void 주문_번호_조회_테스트() {
        // member Id 12
        // order No 202504272119341942
        OrderEntity orderEntity = orderRepository.findByOrderNo("202504281513038370")
                .orElseThrow(EntityNotFoundException::new);

        assertThat(orderEntity.getOrderNo()).isEqualTo("202504281513038370");
    }

}
