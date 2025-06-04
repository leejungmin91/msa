package com.store.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> data;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;

    public PageResponse(Page<T> page) {
        this.data = page.getContent();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.isLast = page.isLast();
    }
}

