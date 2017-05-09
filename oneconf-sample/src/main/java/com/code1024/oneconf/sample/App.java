package com.code1024.oneconf.sample;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.code1024.oneconf.ConfServer;

public class App {

	public static void main(String[] args) throws IOException {
		//start a conf server
		ConfServer.main(args);
		
		//init spring load conf from conf server.
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		TestBean testBean = ctx.getBean(TestBean.class);
		
		System.out.println(testBean.getUrl());
		System.out.println(testBean.getUsername());
		System.out.println(testBean.getPassword());
		
		ctx.close();
		System.exit(0);
	}

}
