package com.store.common.dto;

import lombok.Builder;

@Builder
public record FileInfoResponse(String fileName, String fileUrl) {
}
