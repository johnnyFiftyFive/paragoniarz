package org.j55.paragoniarz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author johnnyFiftyFive
 */
@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan
@EnableAsync
public class ParagoniarzApp {
    public static void main(String[] args) {
        SpringApplication.run(ParagoniarzApp.class, args);
    }
}
