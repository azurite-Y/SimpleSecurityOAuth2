package com.zy.security.oauth2.utils;

import java.util.Random;

/**
 * @author: zy;
 * @DateTime: 2020年3月19日 下午3:17:04;
 * @Description: 随机值获取类 - [0-9A-Za-z]
 */
public class RandomValueStringGenerator {
	private final String DEFAULT_CODEC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private int randomLength;
	
	public RandomValueStringGenerator(int randomLength) {
		super();
		if(DEFAULT_CODEC.length()<randomLength) {
			throw new IndexOutOfBoundsException("数组长度："+this.DEFAULT_CODEC+" ,index："+randomLength);
		}
		this.randomLength = randomLength;
	}
	
	public String generate() {
		Random rand = new Random();
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<this.randomLength;i++){
			int randomNum = rand.nextInt(DEFAULT_CODEC.length());
			builder.append(this.DEFAULT_CODEC.charAt(randomNum));
		}
		return builder.toString();
	}
	
	public int getRandomLength() {
		return randomLength;
	}
	public void setRandomLength(int randomLength) {
		this.randomLength = randomLength;
	}
	
	public static void main(String[] args) {
		RandomValueStringGenerator randomValueStringGenerator = new RandomValueStringGenerator(6);
		String generate = randomValueStringGenerator.generate();
		System.out.println(generate);
	}
}
