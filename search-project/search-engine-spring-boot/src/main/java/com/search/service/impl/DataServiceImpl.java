package com.search.service.impl;

import com.search.dao.DataDao;
import com.search.entity.Data;
import com.search.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Autowired
    private DataDao dataDao;

    // 搜索Data
    @Override
    public List<Data> getSomeDatas(int limit, int offset) {
        return dataDao.getSomeDatas(limit, offset);
    }
}
