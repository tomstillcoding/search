package com.search.service;

import com.search.entity.Segment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SegmentService {
    List<Segment> getAllSeg();
}
