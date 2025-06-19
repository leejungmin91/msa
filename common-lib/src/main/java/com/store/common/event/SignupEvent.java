package com.store.common.event;

import lombok.Builder;

@Builder
public record SignupEvent(Long id, String email, String name) {

    public SignupEvent {
        if(id == null) id = 0L;
        if(email == null) email = "";
        if(name == null) name = "";
    }
}
