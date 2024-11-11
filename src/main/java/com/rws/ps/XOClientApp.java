package com.rws.ps;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * @author pmedcraft
 */
@SpringBootApplication
@Slf4j
public class XOClientApp {
    public static void main(String[] args) {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        log.info("retrieving VM arguments");
        for (String value : arguments) {
            log.info("argument value:" + value);
        }
        SpringApplication.run(XOClientApp.class, args);
    }
}
