package com.dtt.anms.syslog.processing.utils;

import java.text.DecimalFormat;

/**
 * 数字常用工具
 * 
 * @author morgan
 */
public class NumUtil {
	public static final DecimalFormat numfmt = new DecimalFormat("###,##0.00");
	public static final DecimalFormat numfmt2 = new DecimalFormat("0.00");

	public static byte[] short2ByteArr(short s) {
		byte[] shortBuf = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (shortBuf.length - 1 - i) * 8;
			shortBuf[i] = (byte) ((s >>> offset) & 0xff);
		}
		return shortBuf;
	}

	public static final int byteArr2Short(byte[] b) {
		return (b[0] << 8) + (b[1] & 0xFF);
	}

	public static byte[] int2ByteArr(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			// int offset = (b.length - 1 - i) * 8;
			int offset = i * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	public static byte[] int2ByteArr_old(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	public static final int byteArr2Int(byte[] b) {
		return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8)
				+ (b[3] & 0xFF);
	}

	public static short str2short(String str, short dftval) {
		short res = dftval;

		try {
			res = Short.parseShort(str);
		} catch (Exception ex) {
			res = -2;
		}

		return res;
	}

	public static short str2short(String str) {
		return str2short(str, (short) -1);
	}

	public static double get2PDouble(String str) {
		double res = 0.00;
		if (str != null) {
			str = str.replace(",", "");
			int idx = str.lastIndexOf('.');
			if (idx > 0 && str.length() >= idx + 3) {
				str = str.substring(0, idx + 3);
			}
			res = NumUtil.str2double(str);
		}
		return res;
	}

	public static float get2PFloat(String str) {
		float res = 0.00f;
		if (str != null) {
			str = str.replace(",", "");
			int idx = str.lastIndexOf('.');
			if (idx > 0 && str.length() >= idx + 2) {
				str = str.substring(0, idx + 2);
			}
			res = NumUtil.str2float(str);
		}
		return res;
	}

	public static boolean isFloat(String str) {
		boolean res = false;

		try {
			Float.parseFloat(str);
			res = true;
		} catch (Exception ex) {
			res = false;
		}

		return res;
	}

	public static float str2float(String str, long dftval) {
		float res = dftval;

		try {
			res = Float.parseFloat(str);
		} catch (Exception ex) {
			res = -2;
		}

		return res;
	}

	public static float str2float(String str) {
		return str2float(str, -1);
	}

	public static long str2long(String str, long dftval) {
		long res = dftval;

		try {
			res = Long.parseLong(str);
		} catch (Exception ex) {
			res = -2;
		}

		return res;
	}

	public static long str2long(String str) {
		return str2long(str, -1);
	}

	public static long obj2long(Object obj) {
		String str = String.valueOf(obj);
		return str2long(str, -1);
	}

	public static int str2int(String str, int dftval) {
		int res = dftval;

		try {
			res = Integer.parseInt(str);
		} catch (Exception ex) {
			res = -2;
		}

		return res;
	}

	public static int str2int(String str) {
		return str2int(str, -1);
	}

	public static int obj2int(Object obj) {
		String str = String.valueOf(obj);
		return str2int(str, -1);
	}

	public static double str2double(String str, double dfval) {
		double res = dfval;
		try {
			res = Double.parseDouble(str);
		} catch (Exception ex) {
			res = -2;
		}

		return res;
	}

	public static double str2double(String str) {
		return str2double(str, -1);
	}

	public static boolean isNUll(Object o) {
		if (o == null || o.toString().toUpperCase().equals("NULL")
				|| o.toString().trim().length() == 0) {
			return true;
		}
		return false;
	}
}
