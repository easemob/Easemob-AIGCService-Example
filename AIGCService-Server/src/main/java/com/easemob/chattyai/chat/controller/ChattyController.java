package com.easemob.chattyai.chat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.easemob.chattyai.bean.easemob.CallbackEntity;
import com.easemob.chattyai.bean.easemob.MessageEntity;
import com.easemob.chattyai.chat.service.ChattyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.controller
 * @Author: AnAnloneJaver
 * @CreateTime: 2023-11-26  12:15
 * @Description: ChattyController
 * @Version: 1.0
 */
@RestController
@RequestMapping("/chatty")
public class ChattyController {

    @Autowired
    private ChattyService chattyService;

    /**
     * 回调接口接口
     * @param params
     * @return
     */
    @RequestMapping(value = "callback.json", method = RequestMethod.POST, headers = { "content-type=application/json" })
    public CallbackEntity callback(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        MessageEntity messageEntity = BeanUtil.mapToBean(params, MessageEntity.class, true, new CopyOptions());
        //消息
        String msg = messageEntity.getPayload().getBodies().get(0).getMsg();
        chattyService.chatToMiniMax(messageEntity.getTo(),messageEntity.getFrom(),msg);
        return new CallbackEntity(false);
    }

}
