package com.ice.iceclientsdk;


import com.ice.iceclientsdk.client.IceApiClient;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("ice.client")
@ComponentScan
@Data
public class iceApiClientConfig {
    private String accessKey;
    private  String secretKey;
    @Bean
    public IceApiClient iceApiClient(){
        return new IceApiClient(accessKey,secretKey);
    }
}
