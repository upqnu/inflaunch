package com.launcher.inflaunch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 스프링의 기본 Listener 작동시키기
@SpringBootApplication
public class InflaunchApplication {

	public static void main(String[] args) {
		SpringApplication.run(InflaunchApplication.class, args);
	}

}
