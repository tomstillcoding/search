package com.search.dao;

import com.search.entity.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataDao {
    // 通过 limit & offset 获取一些Data
    List<Data> getSomeDatas(@Param("limit") int limit, @Param("offset") int offset);
    List<Data> getDataBySplit(@Param("sql")String sql, @Param("pageSize")int pageSize, @Param("offset")int offset);
}
