package com.easemob.chattyai.task;

import cn.hutool.core.date.DateUtil;
import com.easemob.chattyai.bean.Constant;
import com.easemob.chattyai.chat.service.ChattyService;
import com.easemob.chattyai.chat.util.GreetUtil;
import com.easemob.chattyai.chat.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.task
 * @Author: AnAnloneJaver
 * @CreateTime: 2023-11-30  19:42
 * @Description: 问候语定时任务
 * @Version: 1.0
 */
@Component
@Slf4j
public class GreetTask {

    @Autowired
    private ChattyService chattyService;


    @Autowired
    private RedisUtil redisUtil;


    /**
     * 固定每天早上9点 中午12点，晚上21点 给用户发送消息
     */
    @Scheduled(cron = "0 0 9,12,21 * * ? ")
    public void test(){
        log.info("定时任务开始执行");
        List<String> res = redisUtil.getKeys(Constant.CHAT_HISTORY_REDIS_KEY_PREFIX);
        if(res == null || res.size() == 0){
            return;
        }
        log.info("定时任务开始执行，共有{}个用户需要发送问候语",res.size());
        for (String re : res) {
            String to4From = re.replaceAll(Constant.CHAT_HISTORY_REDIS_KEY_PREFIX,"");
            String[] split = to4From.split(":");
            String from  = split[0];
            String to = split[1];
            String message = GreetUtil.getRomdanGreet();
            chattyService.snedMessageText(from,to,message);
        }
    }

}
