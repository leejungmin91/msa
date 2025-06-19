package com.store.point.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventListener {
    /*private final SignupProducer signupProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSignup(SignupEvent event) {
        signupProducer.send(event);
    }*/
}
