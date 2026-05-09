package com.smarthr.common.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PageResponse<T> implements Serializable {
    private long total;
    private int page;
    private int size;
    private List<T> records;

    public static <T> PageResponse<T> of(long total, int page, int size, List<T> records) {
        PageResponse<T> response = new PageResponse<>();
        response.setTotal(total);
        response.setPage(page);
        response.setSize(size);
        response.setRecords(records);
        return response;
    }
}