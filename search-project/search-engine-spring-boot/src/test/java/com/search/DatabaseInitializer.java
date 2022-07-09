
package com.search;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.search.dao.DataSegmentDao;
import com.search.entity.Data;
import com.search.entity.DataSegment;
import com.search.entity.Segment;
import com.search.service.DataService;
import com.search.service.SegmentService;
import com.search.utils.jieba.keyword.Keyword;
import com.search.utils.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

// 扫描data表把所有内容(caption)，对其挨个分词并加入分词表segment
@SpringBootTest
public class DatabaseInitializer {

    @Autowired
    private DataService dataService;
    @Autowired
    private SegmentService segmentService;
    @Autowired
    private DataSegmentDao dataSegmentDao;

    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
    static HashSet<String> stopWordsSet = new HashSet<>();

    @Test
    public void InitSegmentTable() {
        List<String> segs = new ArrayList<>();
        // 布隆过滤器
        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),10000000);

        // 加载停顿词
        loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));

        {
            // 将data里面的900个数据都获取到
            List<Data> datas = dataService.getSomeDatas(900, 0);

            for (int i = 0; i < 900; i++) {
                // 对每一条data数据都进行分词
                Data data = datas.get(i % 90);
                String caption = data.getCaption();

                // 将获取到的caption进行分词
                List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);
                for (SegToken segToken : segTokens) {
                    String word = segToken.word;
                    if (stopWordsSet.contains(word)) continue; // 判断是否是停用词
                    // 布隆过滤器判断是否已经包括了
                    if (!bf.mightContain(word)) {
                        bf.put(word);   // 放进布隆过滤器
                        segs.add(word); // 加入分词set中
                    }
                }
            }
        }
        dataSegmentDao.initSegmentTable(segs); // 将分词全部添加到segment表里面
    }

    @Test
    public void initDataSegRelationTable() {
        // 获取到所有的 segment 分词
        List<Segment> segments = segmentService.getAllSeg();

        // 将分词按照 word->id 的方式放入 map
        Map<String, Integer> wordToId = new HashMap<>(900);
        for (Segment seg : segments) {
            wordToId.put(seg.getWord(), seg.getId());
        }

        loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));

        // 一个List<DataSegment>代表一张DataSegment表
        // dataSegmentListMap代表很多张DataSegment表
        Map<Integer, List<DataSegment>> dataSegmentListMap = new HashMap<>(1);
        int cnt = 0;

        {
            List<Data> datas = dataService.getSomeDatas(900, 0);

            for (int i = 0; i < 900; i++) {
                // 取得每一条 data 并取出其中的caption
                Data data = datas.get(i % 900);
                String caption = data.getCaption();

                // 进行分词
                List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);

                // 获取返回的 tfidf 值最高的5个关键词
                List<Keyword> keywords = tfidfAnalyzer.analyze(caption,5);
                Map<String, DataSegment> segmentMap = new HashMap<>();

                for (SegToken segToken : segTokens) {
                    String word = segToken.word;

                    // 去掉停顿词
                    if (stopWordsSet.contains(word)) continue;

                    // 不在 segment 表中的分词，去掉
                    if (!wordToId.containsKey(word)) continue;

                    int segId = wordToId.get(word);
                    int dataId = data.getId();
                    double tf = 0;

                    // 如果是 tfidf 值最高的5个关键词之一，就将 tf 值保存起来
                    for (Keyword v : keywords) {
                        if (v.getName().equals(word)) {
                            tf = v.getTfidfvalue();
                            break;
                        }
                    }

                    if (!segmentMap.containsKey(word)){
                        int count = 1;
                        segmentMap.put(word, new DataSegment(dataId, segId, tf, count));
                    } else {
                        DataSegment dataSegment = segmentMap.get(word);
                        int count = dataSegment.getCount();
                        dataSegment.setCount(++count);
                        segmentMap.put(word, dataSegment);
                    }
                }

                // 将Segment放入DataSegment表中，但是按照Segment的Id来区分放入哪一张DataSegment表, idx就是区分键
                for (DataSegment dataSegment : segmentMap.values()) {
                    int segId = dataSegment.getSegId();
                    int idx = segId % 100;
                    List list = dataSegmentListMap.getOrDefault(idx, new ArrayList<>(90));
                    list.add(dataSegment);
                    dataSegmentListMap.put(idx, list);
                    cnt++;
                }

            }
        }
        
        // 最后通过 dataSegmentList 来创建所有的 DataSegment 表：data_seg_relation
        if (cnt > 0) {
            for (Integer idx : dataSegmentListMap.keySet()) {
                String tableName = "data_seg_relation_" + idx;
                dataSegmentDao.createNewTable(tableName);
                dataSegmentDao.initRelationTable(dataSegmentListMap.get(idx), tableName);
            }
        }
    }
    
    // 加载停顿词
    private void loadStopWords(Set<String> set, InputStream in){
        BufferedReader bufferReader;
        try
        {
            bufferReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufferReader.readLine())!=null) {
                set.add(line.trim());
            }
            bufferReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
