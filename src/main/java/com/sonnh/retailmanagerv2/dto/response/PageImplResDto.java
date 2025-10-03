package com.sonnh.retailmanagerv2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
@Data
@Builder
@NoArgsConstructor

public class PageImplResDto<T> implements Serializable {
    private List<T> data;
    private int number;
    private int size;
    private long totalElement;
    private long totalPage;
    private boolean first;
    private boolean last;

    public PageImplResDto(List<T> data, int number, int size, long totalElement, long totalPage, boolean first, boolean last) {
        this.data = data;
        this.number = number;
        this.size = size;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
        this.first = first;
        this.last = last;
    }

    public static <T> PageImplResDto<T> fromPage(Page<T> page) {
        return PageImplResDto.<T>builder().data(page.getContent()).number(page.getNumber() + 1).size(page.getSize()).totalElement(page.getTotalElements()).totalPage((long)page.getTotalPages()).first(page.isFirst()).last(page.isLast()).build();
    }
}
