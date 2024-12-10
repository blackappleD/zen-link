package com.mkc.common.utils;

/**
 * 字符串压缩
 *
 * @author tqlei
 * @date 2023/5/25 14:48
 */


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.*;

/**
 * 对字符串进行
 */
public class ZipStrUtils {
	/**
	 *
	 * 使用gzip进行压缩
	 */
	public static String gzip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return Base64Util.encode(out.toByteArray());
	}

	/**
	 * <p>Description:使用gzip进行解压缩</p>
	 *
	 * @param compressedStr
	 * @return
	 */
	public static String gunzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = Base64Util.decode(compressedStr);
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException ignored) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignored) {
				}
			}
			try {
				out.close();
			} catch (IOException ignored) {
			}
		}

		return decompressed;
	}

	/**
	 * 使用zip进行压缩
	 *
	 * @param str 压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String str) {
		if (str == null) {
			return null;
		}
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = Base64Util.encode(compressed);
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 *
	 * @param compressedStr 压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = Base64Util.decode(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}

	public static void main(String[] args) throws IOException {
//        String strOld= FileUtils.readFileToString(new File("D:/test.txt"),"utf-8");
		String strOld = "https://blog.csdn.net/RaoSK/article/details/121519355,https://blog.csdn.net/RaoSK/article/details/121519355,https://blog.csdn.net/RaoSK/article/details/121519355,https://blog.csdn.net/RaoSK/article/details/121519355,https://blog.csdn.net/RaoSK/article/details/121519355,";
		String zip = zip(strOld);
		String gzip = gzip(strOld);
		System.out.printf("old:=%d zip new:%d  gzip new:%d\r\n", strOld.length(), zip.length(), gzip.length());
		System.out.printf("new:=%s ", gzip);

		System.out.printf("\r\n...... ", gzip);
		String strOld1 = "www.baidu.com1234567890abcdefeaghodadfsdfasdf.sdf.sadfmsmadmfmsdmfmasmdfsdkfkaskdfsdfmamwefkksafs.df..asd.f.asdfwkefkkejuuwq@##&&**!^&!*!((!))!))!)!))!)!(*&^%%$%&!*!***!*!*!*!**!*!*!*!**!******!&&^^JJSSYDHDJoadsp;fddlslklsdlkflksdlwww.baidu.com1234567890abcdefeaghodadfsdfasdf.sdf.sadfmsmadmfmsdmfmasmdfsdkfkaskdfsdfmamwefkksafs.df..asd.f.asdfwkefkkejuuwq@##&&**!^&!*!((!))!))!)!))!)!(*&^%%$%&!*!***!*!*!*!**!*!*!*!**!******!&&^^JJSSYDHDJoadsp;fddlslklsdlkflksdlwww.baidu.com1234567890abcdefeaghodadfsdfasdf.sdf.sadfmsmadmfmsdmfmasmdfsdkfkaskdfsdfmamwefkksafs.df..asd.f.asdfwkefkkejuuwq@##&&**!^&!*!((!))!))!)!))!)!(*&^%%$%&!*!***!*!*!*!**!*!*!*!**!******!&&^^JJSSYDHDJoadsp;fddlslklsdlkflksdlwww.baidu.com1234567890abcdefeaghodadfsdfasdf.sdf.sadfmsmadmfmsdmfmasmdfsdkfkaskdfsdfmamwefkksafs.df..asd.f.asdfwkefkkejuuwq@##&&**!^&!*!((!))!))!)!))!)!(*&^%%$%&!*!***!*!*!*!**!*!*!*!**!******!&&^^JJSSYDHDJoadsp;fddlslklsdlkflksdl";
		String zip1 = zip(strOld1);
		String gzip1 = gzip(strOld1);

		System.out.printf("old:=%d zip new:%d  gzip new:%d \r\n", strOld1.length(), zip1.length(), gzip1.length());
		System.out.println("new:=%s " + zip1);
		System.out.println("new:=%s " + gzip1);
		System.out.println("new gzip1 :=%s %d" + gunzip(gzip1));

	}
}
