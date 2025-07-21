package com.binqi.mamiantuwei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.binqi.mamiantuwei.mapper.QuestionViewsMapper;
import com.binqi.mamiantuwei.model.entity.QuestionViews;
import com.binqi.mamiantuwei.service.QuestionViewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class QuestionViewsServiceImpl extends ServiceImpl<QuestionViewsMapper, QuestionViews> implements QuestionViewsService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // Redis中题目浏览量的key前缀
    private static final String QUESTION_VIEW_COUNT_KEY_PREFIX = "question:view:count:";
    @Override
    public long getViewCount(Long questionId) {
        String key = QUESTION_VIEW_COUNT_KEY_PREFIX + questionId;
        Object viewCountObj = redisTemplate.opsForValue().get(key);
        long redisViewCount = 0;
        if (viewCountObj != null) {
            redisViewCount = Long.parseLong(viewCountObj.toString());
        }
        //redis中没有，从数据库中查询
        if(redisViewCount == 0){
            QuestionViews views = this.getById(questionId);
            if(views != null && views.getViewCount() != null){
                redisViewCount = views.getViewCount();
                redisTemplate.opsForValue().set(key, redisViewCount, 7 , TimeUnit.DAYS);
            }
        }
        return redisViewCount;
    }

    @Override
    public void syncViewCountToDb() {
        try {
            // 1. 获取所有需要同步的键
            Set<String> keys = redisTemplate.keys(QUESTION_VIEW_COUNT_KEY_PREFIX + "*");
            if (keys == null || keys.isEmpty()) {
                log.info("No view count data to sync");
                return;
            }

            log.info("Start syncing view count data, total: {}", keys.size());

            // 2. 遍历所有键，更新到数据库
            for (String key : keys) {
                try {
                    // 提取题目ID
                    String questionIdStr = key.substring(QUESTION_VIEW_COUNT_KEY_PREFIX.length());
                    Long questionId = Long.valueOf(questionIdStr);

                    // 获取Redis中的浏览量
                    Object viewCountObj = redisTemplate.opsForValue().get(key);
                    if (viewCountObj == null) {
                        continue;
                    }

                    long viewCount = Long.parseLong(viewCountObj.toString());

                    // 更新数据库
                    QuestionViews question = new QuestionViews();
                    question.setId(questionId);
                    question.setViewCount(viewCount);
                    this.updateById(question);

                    log.info("Synced view count for question {}: {}", questionId, viewCount);
                } catch (Exception e) {
                    log.error("Error syncing view count for key: " + key, e);
                }
            }

            log.info("View count sync completed");
        } catch (Exception e) {
            log.error("Error syncing view count data", e);
        }
    }

    @Override
    public boolean increaseViewCount(Long questionId) {
        String key = QUESTION_VIEW_COUNT_KEY_PREFIX + questionId;
        redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
        return true;
    }
}