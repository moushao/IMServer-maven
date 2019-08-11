package com.mous.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Handler;

import main.com.im.netty.IMServer;

@SpringBootApplication
public class ImServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImServerApplication.class, args);
        IMServer.getInstance().init();
        System.out.println("没到这里");
    }

}
