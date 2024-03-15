package com.easemob.chattyai.chat.controller;

import com.easemob.chattyai.bean.minimax.BotBean;
import com.easemob.chattyai.chat.util.BotSettingUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.controller
 * @Author: AnAnloneJaver
 * @CreateTime: 2023-11-26  12:15
 * @Description: 获取bot 用户的信息
 * @Version: 1.0
 */
@RestController
public class BotsController {


    /**
     * 获取机器人用户的信息
     * @return
     */
    @GetMapping("/getBotUsers")
    public List<BotBean> getBotUsers(){
        Map<String, BotBean> bots = BotSettingUtil.bots;
        return new ArrayList<>(bots.values());
    }

}
