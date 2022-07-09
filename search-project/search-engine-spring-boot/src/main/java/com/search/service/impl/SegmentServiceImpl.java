package com.search.service.impl;

import com.search.dao.SegmentDao;
import com.search.entity.Segment;
import com.search.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SegmentServiceImpl implements SegmentService {

    @Autowired
    private SegmentDao segmentDao;

    // 供Initializer使用
    @Override
    public List<Segment> getAllSeg() {
        return segmentDao.getAllSeg();
    }

}
