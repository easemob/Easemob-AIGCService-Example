package com.easemob.chattyai.chat.service;

import com.easemob.im.server.model.EMSentMessageIds;
import com.easemob.im.server.model.EMUser;

import java.util.Map;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.service
 * @Author: AnAnloneJaver
 * @CreateTime: 2023-11-25  00:43
 * @Description: ChattyService
 * @Version: 1.0
 */
public interface ChattyService {

    /**
     * 创建用户
     * @param username
     * @param password
     * @return
     */
    EMUser createUser(String username, String password);


    /**
     *
     * @param to  接收人
     * @param from 发送人
     * @param text 发送内容
     * @return
     */
    Map<String, String> chatToMiniMax(String to, String from, String text);


    /**
     * 发送消息
     * @param to
     * @param from
     * @param message
     * @return
     */
    EMSentMessageIds snedMessageText(String to, String from, String message);
}
