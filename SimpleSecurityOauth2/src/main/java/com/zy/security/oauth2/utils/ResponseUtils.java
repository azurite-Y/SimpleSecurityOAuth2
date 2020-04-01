package com.zy.security.oauth2.utils;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: zy;
 * @DateTime: 2020年3月22日 下午11:16:45;
 * @Description: 响应内容设置工具类
 */
public class ResponseUtils {
	/**
	 * 将响应内容保存的response中
	 * @param requestDetails - 存储当前请求详细信息的对象
	 * @param responseBody - 要保存的响应内容
	 * @param contextType - 响应内容的解析类型【ps:”application/json;charset=UTF-8“ 或 "text/html;charset=UTF-8"】
	 */
	public void reply(HttpServletResponse resp,Object responseBody,String contextType) {
		resp.setHeader("Cache-Control", "no-store");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Content-Type", contextType);
		try {
			ServletOutputStream outputStream = resp.getOutputStream();
			// json化
			String toJson = JsonUtils.objectToJson(responseBody);
			outputStream.write(toJson.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置JSON式的响应内容
	 * @param requestDetails
	 * @param responseBody
	 */
	public void replyJson(HttpServletResponse resp,Object responseBody) {
		resp.setStatus(200);
		resp.setHeader("Cache-Control", "no-store");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Content-Type", "application/json;charset=UTF-8");
		try {
			ServletOutputStream outputStream = resp.getOutputStream();
			// json化
			String toJson = JsonUtils.objectToJson(responseBody);
			outputStream.write(toJson.getBytes("utf-8"));
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置html式的响应内容
	 * @param requestDetails
	 * @param responseBody
	 */
	public void replyHtml(HttpServletResponse resp,Object responseBody) {
//		SecurityHttpServletResponse response = (SecurityHttpServletResponse) resp;
		resp.setStatus(200);
		resp.setHeader("Cache-Control", "no-store");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Content-Type", "text/html;charset=UTF-8");
		try {
			ServletOutputStream outputStream = resp.getOutputStream();
			byte[] bytes = responseBody.toString().getBytes("UTF-8");
			resp.setContentLength(bytes.length);
			outputStream.write(bytes);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
