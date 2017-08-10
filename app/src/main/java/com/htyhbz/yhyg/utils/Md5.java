package com.htyhbz.yhyg.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class Md5 {
	private String inStr;
	private MessageDigest md5;

	public Md5(String inStr) {
		this.inStr = inStr;
		try {
			this.md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public String compute() {
		byte[] byteArray;
		try {
			byteArray = this.inStr.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			byteArray = this.inStr.getBytes();
			e.printStackTrace();
		}
		byte[] md5Bytes = this.md5.digest(byteArray);
		StringBuilder hexValue = new StringBuilder();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static void main(String[] args) {
		Md5 md5 = new Md5("abc");
		String postString = md5.compute();
		System.out.println(postString);
		if (postString.equals("900150983cd24fb0d6963f7d28e17f72"))
			System.out.println("true");
		else
			System.out.println("false");
	}

}
