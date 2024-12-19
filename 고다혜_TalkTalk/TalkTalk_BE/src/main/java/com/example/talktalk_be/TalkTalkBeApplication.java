package com.example.talktalk_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class TalkTalkBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkTalkBeApplication.class, args);
    }
}
