package com.search.service;

import com.search.entity.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataService {
    List<Data> getSomeDatas(int limit, int offset);
}
