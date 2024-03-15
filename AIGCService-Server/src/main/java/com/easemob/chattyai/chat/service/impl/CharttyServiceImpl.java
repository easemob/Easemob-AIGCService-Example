package com.easemob.chattyai.chat.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.easemob.chattyai.bean.Constant;
import com.easemob.chattyai.bean.minimax.BotBean;
import com.easemob.chattyai.bean.minimax.BotSetting;
import com.easemob.chattyai.bean.minimax.Messages;
import com.easemob.chattyai.bean.minimax.ReplyConstraints;
import com.easemob.chattyai.chat.service.ChattyService;
import com.easemob.chattyai.chat.util.BotSettingUtil;
import com.easemob.chattyai.chat.util.MiniMaxUtil;
import com.easemob.chattyai.chat.util.RedisUtil;
import com.easemob.im.server.EMException;
import com.easemob.im.server.EMService;
import com.easemob.im.server.model.EMKeyValue;
import com.easemob.im.server.model.EMSentMessageIds;
import com.easemob.im.server.model.EMTextMessage;
import com.easemob.im.server.model.EMUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.service.impl
 * @Author: AnAnloneJaver
 * @CreateTime: 2023-11-25  00:43
 * @Description: CharttyServiceImpl
 * @Version: 1.0
 */
@Slf4j
@Service
public class CharttyServiceImpl implements ChattyService {


    @Autowired
    private EMService service;

    @Autowired
    private RedisUtil redisUtil;



    /**
     * 创建用户
     *
     * @return
     */
    public EMUser createUser(String username, String password) {
        EMUser user = null;
        try {
            user = service.user().create(username, password).block();
        } catch (EMException e) {
            e.getErrorCode();
            e.getMessage();
        }
        return user;
    }


    /**
     * 和miniMax进行聊天
     *
     * @param to   接收人
     * @param from 发送人
     * @param text 发送内容
     */
    public Map<String, String> chatToMiniMax(String to, String from, String text) {

        BotBean botBean = BotSettingUtil.bots.get(to);
        if (botBean == null) {
            botBean = BotSettingUtil.bots.get("boy0");
        }
        BotSetting botSetting = new BotSetting();
        botSetting.setBot_name(botBean.getName());
        botSetting.setContent(botBean.getContent());

        //处理历史消息
        String key = Constant.CHAT_HISTORY_REDIS_KEY_PREFIX + from + ":" + botBean.getAccount();
        List<Object> list = redisUtil.getAll(key);
        List<Messages> messages = new ArrayList<>(20);
        if (list != null && list.size() > 0) {
            for (Object o : list) {
                messages.add((Messages) o);
            }
        }
        //添加到当前消息
        Messages messageTarget = new Messages();
        messageTarget.setSender_name(from);
        messageTarget.setSender_type("USER");
        messageTarget.setText(text);
        messages.add(messageTarget);

        ReplyConstraints replyConstraints = new ReplyConstraints();
        replyConstraints.setSender_name(botSetting.getBot_name());
        replyConstraints.setSender_type("BOT");
        JSONObject res = MiniMaxUtil.sendMiniMaxMessage(messages, replyConstraints, botSetting);
        if (res == null) {
            log.error("miniMax返回有误");
            return null;
        }
        Object o = res.get("reply");
        if (o == null) {
            log.error("miniMax返回有误");
            return null;
        }
        System.out.println("miniMax 返回的消息是：" + JSONUtil.toJsonStr(res));
        /**
         * 这里需要反向发送消息，给minimax ai回复的内容反向发送给发送人
         */
        EMSentMessageIds messageIds = snedMessageText(from, to, o.toString());

        //处理历史消息
        redisUtil.rightPush(key, messageTarget);
        Messages miniMaxMessage = new Messages();
        miniMaxMessage.setSender_name(botSetting.getBot_name());
        miniMaxMessage.setSender_type("BOT");
        miniMaxMessage.setText(o.toString());
        redisUtil.rightPush(key, miniMaxMessage);

        long l = redisUtil.lGetListSize(key);
        if (l > 10) {
            redisUtil.leftpop(key, 2L);
        }

        return messageIds.getMessageIdsByEntityId();
    }


    /**
     * 向目标客户发送消息
     *
     * @param to      发给谁
     * @param from    发送人
     * @param message 发送的文本消息
     * @return
     */
    public EMSentMessageIds snedMessageText(String to, String from, String message) {
        EMSentMessageIds messageIds = null;

        Set<String> toUsers = new HashSet<>();
        toUsers.add(to);
        EMTextMessage textMessage = new EMTextMessage().text(message);
        Set<EMKeyValue> exts = new HashSet<>();
        exts.add(EMKeyValue.of("key", "value"));
        try {
            messageIds = service.message().send(from, "users", toUsers, textMessage, exts).block();
        } catch (EMException e) {
            e.getErrorCode();
            e.getMessage();
        }
        return messageIds;
    }
}
