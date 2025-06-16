package com.store.member.config;

import com.store.member.event.SignupEvent;
import com.store.member.producer.SignupProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final SignupProducer signupProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSignup(SignupEvent event) {
        signupProducer.send(event);
    }
}
