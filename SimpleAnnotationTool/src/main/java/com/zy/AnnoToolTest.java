
package com.zy;

import com.zy.reflect.SecurityRelectHandler;
import com.zy.reflect.interfaces.RelectHandler;

/**
* @author zy
* @Date 2019-12-03 周二 下午 14:48:56
* @Description
* @version 
*/
public class AnnoToolTest {

	public static void main(String[] args) {
		// 文件扫描测试
//		List<String> search = FileSearch.search();
//		for (String string : search) {
//			System.out.println(string);
//		}
		
		// 
		RelectHandler handler = new SecurityRelectHandler();
		handler.invoke("com.zy.utils");
		
//		DefaultRelectHandler defaultRelectHandler = new DefaultRelectHandler();
//		defaultRelectHandler.invoke("");
	}

}
