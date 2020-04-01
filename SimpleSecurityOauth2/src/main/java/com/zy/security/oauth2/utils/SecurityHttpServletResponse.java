package com.zy.security.oauth2.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author: zy;
 * @DateTime: 2020年3月20日 下午9:27:59;
 * @Description: 包装HttpServletResponseWrapper和ServletOutputStream，以获得响应数据
 */
public class SecurityHttpServletResponse extends HttpServletResponseWrapper {
	private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	private PrintWriter pwrite;

	public SecurityHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new SecurityServletOutputStream(bytes); // 将数据写到 byte 中
	}

	/**
	 * 重写父类的 getWriter() 方法，将响应数据缓存在 PrintWriter 中
	 */
	@Override
	public PrintWriter getWriter() throws IOException {
		try {
			pwrite = new PrintWriter(new OutputStreamWriter(bytes, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pwrite;
	}

	/**
	 * 获取缓存在 PrintWriter 中的响应数据
	 * @return
	 */
	public byte[] getBytes() {
		if (null != pwrite) {
			pwrite.close();
			return bytes.toByteArray();
		}

		if (null != bytes) {
			try {
				bytes.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes.toByteArray();
	}

	class SecurityServletOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream baos;

		public SecurityServletOutputStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		@Override
		public void write(int b) throws IOException {
			baos.write(b); // 将数据写到 stream 中
		}

		/**
		 * 此方法可用于确定是否可以在不阻塞的情况下写入数据
		 * @return 如果对此servletoutputstream的写入成功，则返回true，否则返回false
		 */
		@Override
		public boolean isReady() {
			return false;
		}

		/**
		 * 指示ServletOutputStream在可能写入时调用提供的WriteListener
		 * @return listener在可能写入时应通知的WriteListener
		 */
		@Override
		public void setWriteListener(WriteListener listener) {}
	}
}
