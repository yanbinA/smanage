package com.temple.manage.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wx")
@Data
@Slf4j
public class WxCpConfig {
    private String corpSecret;
    private String corpId;
    private Integer agentId;
    private String appId;

    @Bean
    public WxCpConfigStorage wxCpConfigStorage() {
        log.debug("load wx config corpSecret:{}", corpSecret);
        log.debug("load wx config corpId:{}", corpId);
        log.debug("load wx config agentId:{}", agentId);
        log.debug("load wx config appId:{}", appId);
        WxCpDefaultConfigImpl wxCpDefaultConfig = new WxCpDefaultConfigImpl();
        wxCpDefaultConfig.setCorpSecret(corpSecret);
        wxCpDefaultConfig.setCorpId(corpId);
        wxCpDefaultConfig.setAgentId(agentId);
        return wxCpDefaultConfig;
    }

    @Bean
    public WxCpService wxCpService() {
        WxCpServiceImpl tpService = new WxCpServiceImpl();
        tpService.setWxCpConfigStorage(wxCpConfigStorage());
        return tpService;
    }
}
