package com.store.common.event;

import lombok.Builder;

@Builder
public record SignupEvent(String email, String name) {

    public SignupEvent {
        if(email == null) email = "";
        if(name == null) name = "";
    }
}
