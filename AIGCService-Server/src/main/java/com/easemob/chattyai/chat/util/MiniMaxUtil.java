package com.easemob.chattyai.chat.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.easemob.chattyai.bean.minimax.BotSetting;
import com.easemob.chattyai.bean.minimax.Messages;
import com.easemob.chattyai.bean.minimax.MiniMaxHttpEntity;
import com.easemob.chattyai.bean.minimax.ReplyConstraints;
import com.easemob.chattyai.config.MiniMaxConfig;

import java.util.*;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.util
 * @Author: AnAnloneJaver
 * @CreateTime: 2023-11-27  18:48
 * @Description: MiniMax AI模型工具类
 * @Version: 1.0
 */
public class MiniMaxUtil {


    private MiniMaxUtil(){}

    private static MiniMaxConfig miniMaxConfig;

    private static MiniMaxConfig getMiniMaxConfig(){
        if(miniMaxConfig == null){
            miniMaxConfig = SpringUtils.getBean(MiniMaxConfig.class);
        }
        return miniMaxConfig;
    }

    public static JSONObject sendMiniMaxMessage(List<Messages> messages, ReplyConstraints replyConstraints, BotSetting botSetting){
        MiniMaxHttpEntity miniMaxHttpEntity = new MiniMaxHttpEntity();
        miniMaxHttpEntity.setModel("abab5.5-chat");
        miniMaxHttpEntity.setTokensToGenerate(500);
        miniMaxHttpEntity.setTemperature(0.9);
        miniMaxHttpEntity.setTopP(0.95);
        miniMaxHttpEntity.setStream(false);
        miniMaxHttpEntity.setReplyConstraints(replyConstraints);
        miniMaxHttpEntity.setSampleMessages(Collections.emptyList());
        miniMaxHttpEntity.setPlugins(Collections.emptyList());
        miniMaxHttpEntity.setMessages(messages);
        List<BotSetting> botSettings = new ArrayList<>();
        botSettings.add(botSetting);
        miniMaxHttpEntity.setBotSetting(botSettings);

        MiniMaxConfig miniMaxConfig1 = getMiniMaxConfig();
        String url = miniMaxConfig1.getUrl()+miniMaxConfig1.getGroupId();

        Map<String,String> header = new HashMap<>();
        header.put("Authorization","Bearer "+miniMaxConfig1.getAppkey());
        header.put("Content-Type","application/json");
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(miniMaxHttpEntity, true, false);
        String body = JSONUtil.toJsonStr(stringObjectMap);
        System.out.println("发送给miniMax的消息为："+body);
        HttpResponse execute = HttpUtil.createPost(url).addHeaders(header).body(body).execute();
        /**
         * 返回格式
         * {
         *     "created":1701087162,
         *     "model":"abab5.5-chat",
         *     "reply":"哎呀，施主，切莫伤心，贫道给你讲个笑话吧。",
         *     "choices":[
         *         {
         *             "finish_reason":"stop",
         *             "messages":[
         *                 {
         *                     "sender_type":"BOT",
         *                     "sender_name":"江逝水",
         *                     "text":"哎呀，施主，切莫伤心，贫道给你讲个笑话吧。"
         *                 }
         *             ]
         *         }
         *     ],
         *     "usage":{
         *         "total_tokens":467
         *     },
         *     "input_sensitive":false,
         *     "output_sensitive":false,
         *     "id":"01b3bab93c1ac80f8216746289566b4f",
         *     "base_resp":{
         *         "status_code":0,
         *         "status_msg":""
         *     }
         * }
         */

        return JSONUtil.parseObj(execute.body());

    }
}
