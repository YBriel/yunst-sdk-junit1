package com.allinpay.autotest.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;

/**
 * 获得全球不重复的序列号
 */
public class IDGenerator {
	private static String tHexServerIP = null;
	private static final SecureRandom tSecureRandom = new SecureRandom();

	/**
	 * 获得全球不重复的序列号
	 * @return String 全球不重复的序列号
	 */
	public static final String generateGUID() {
		Object tObject = new Object();
		StringBuffer tStringBuffer = new StringBuffer(16);
		if (tHexServerIP == null) {
			InetAddress tInetAddress = null;
			try {
				tInetAddress = InetAddress.getLocalHost();
			} catch (UnknownHostException uhe) {
				uhe.printStackTrace();
				//System.err.println("IDGenerator: Could not get the local IP address using InetAddress.getLocalHost()!");
				return null;
			}
			byte tServerIP[] = tInetAddress.getAddress();
			tHexServerIP = hexFormat(getInt(tServerIP), 8);
		}
		String tHashCode = hexFormat(System.identityHashCode(tObject), 8);
		tStringBuffer.append(tHexServerIP);
		tStringBuffer.append(tHashCode);
		long tTimeNow = System.currentTimeMillis();
		int tTimeLow = (int) tTimeNow & -1;
		int tNode = tSecureRandom.nextInt();
		StringBuffer tGuid = new StringBuffer(32);
		tGuid.append(hexFormat(tTimeLow, 8));
		tGuid.append(tStringBuffer.toString());
		tGuid.append(hexFormat(tNode, 8));
		return tGuid.toString().toUpperCase();
	}

	private static int getInt(byte bytes[]) {
		int i = 0;
		int j = 24;
		for (int k = 0; j >= 0; k++) {
			int l = bytes[k] & 0xff;
			i += l << j;
			j -= 8;
		}

		return i;
	}

	private static String hexFormat(int i, int j) {
		String tString = Integer.toHexString(i);
		return padHex(tString, j) + tString;
	}

	private static String padHex(String s, int i) {
		StringBuffer tStringBuffer = new StringBuffer();
		if (s.length() < i) {
			for (int j = 0; j < i - s.length(); j++)
				tStringBuffer.append('0');

		}
		return tStringBuffer.toString();
	}
	
	public static void main(String[] args) {
		System.out.println("IDGenerator="+IDGenerator.generateGUID());
	}
}

