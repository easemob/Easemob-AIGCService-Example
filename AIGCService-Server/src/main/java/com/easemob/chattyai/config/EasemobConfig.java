package com.easemob.chattyai.config;

import com.easemob.im.server.EMProperties;
import com.easemob.im.server.EMService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.config
 * @Author: AnAnloneJaver
 * @CreateTime: 2023-11-25  00:37
 * @Description: 环信IM 的 EasemobConfig
 * @Version: 1.0
 */
@Configuration
public class EasemobConfig {

    @Value("${easemob.appkey}")
    private String appkey;

    @Value("${easemob.clientId}")
    private String clientId;

    @Value("${easemob.clientSecret}")
    private String clientSecret;

    @Bean
    public EMService service() {
        EMProperties properties = EMProperties.builder()
                .setAppkey(appkey)
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
        return new EMService(properties);
    }
}
