package com.search.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 分词实体类
 */
@Data
@AllArgsConstructor
// Segment表的Java原始数据
public class Segment {
    private int id;
    private String word;
}
