package com.search.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// DataSegment表的Java原始数据
public class DataSegment {
    Integer dataId;
    Integer segId;
    Double tidif;
    Integer count;

}
