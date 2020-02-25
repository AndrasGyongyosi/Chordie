package com.example.ChordCalculator.Helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private Class clazz;
	
	public Logger(Class clazz) {
		this.clazz = clazz;
	}
	
	public void info(String msg) {
		log(msg, "info");
	}
	public void trace(String msg) {
		log(msg, "trace");
	}
	public void debug(String msg) {
		log(msg, "debug");
	}
	public void warn(String msg) {
		log(msg, "warn");
	}
	public void error(String msg) {
		log(msg, "error");
	}
	public void log(String msg, String type) {
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
		System.out.println(dateFormat.format(now)+" "+type.toUpperCase()+" "+clazz.getName()+" "+msg);
	}
}
