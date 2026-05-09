package com.smarthr.common.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    private Integer page = 1;
    private Integer size = 10;
    private String sortBy;
    private String sortOrder = "desc";
}