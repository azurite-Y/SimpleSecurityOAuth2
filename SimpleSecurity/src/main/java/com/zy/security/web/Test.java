
package com.zy.security.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author zy
 * @Date 2019-11-16 周六 下午 16:41:14
 * @Description
 * @version
 */
public class Test {
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		time += 1000L * 60*60*24*7;
		
		StringBuilder builder = new StringBuilder();
		//格式：  zs:123456:1000*60*60*24*7+curTime(l)
		builder.append("zs").append(":").append("123456").append(":").append(time);
		System.out.println(builder.toString());
		
		Date date = new Date(1574869253345l);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd E a hh:mm:ss");
		String format = simpleDateFormat.format(date);
		System.out.println(format);
		ArrayList<String> list = new ArrayList<>();
		list.add("asd");
		System.out.println(list.size());
		
//		Collections.sort(list, c);
	}
}
