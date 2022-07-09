package com.search.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
// Data表的原始Java对象
public class Data {
    private Integer id;
    private String url;
    private String caption;
}
