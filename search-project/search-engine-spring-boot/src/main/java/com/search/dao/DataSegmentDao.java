package com.search.dao;

import com.search.entity.DataSegment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSegmentDao {
    // 用于InitlizerTable使用
    boolean initSegmentTable(@Param("segs")List<String> segs);  // 添加分词表
    boolean initRelationTable(@Param("relations")List<DataSegment> relations, @Param("tableName")String tableName);  // 添加关系表
    int createNewTable(@Param("tableName")String tableName); // 创建表
}
