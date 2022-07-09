package com.search.service;

import java.util.Map;


public interface SearchService {
    // 通过
    Map<String, Object> getDataByKeyword(String keyword, int pageSize, int pageNum);
}