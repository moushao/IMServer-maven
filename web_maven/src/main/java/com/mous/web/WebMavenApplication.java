package com.mous.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import main.java.com.mous.im.netty.IMServer;

@SpringBootApplication
public class WebMavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMavenApplication.class, args);
        IMServer.getInstance().init();
    }
}
