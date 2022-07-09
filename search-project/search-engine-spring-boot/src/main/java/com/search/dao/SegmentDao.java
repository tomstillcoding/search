package com.search.dao;

import com.search.entity.Segment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentDao {
    // 查看所有分词
    List<Segment> getAllSeg();

    // 通过word获取一个Segment
    Segment getOneSeg(String word);
}
