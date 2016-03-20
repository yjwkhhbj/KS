package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.PrivateKey;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 * 私钥文件读写
 * 
 * @author liuzy
 * @version 2016-3-20
 */
public class KeyUtils {

	/**
	 * 读私钥文件
	 * @param pemFile
	 * @return
	 */
	public static KeyPair read(String pemFile) {
		InputStream pemIn = null;
		InputStreamReader inReader = null;
		PEMReader pemReader = null;
		try {
			pemIn = new FileInputStream(new File(pemFile));
			inReader = new InputStreamReader(pemIn);
			pemReader = new PEMReader(inReader);
			return (KeyPair) pemReader.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (pemReader != null) {
					pemReader.close();
				}
				if (inReader != null) {
					inReader.close();
				}
				if (pemIn != null) {
					pemIn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 把私钥储存为普通RSA格式的文件
	 * <pre>-----BEGIN RSA PRIVATE KEY-----
	 * xxx
	 * -----END RSA PRIVATE KEY-----</pre>
	 * @param pk
	 * @param path
	 */
	public static void write2RsaKey(PrivateKey pk, String path) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			fw.write(toRsaKeyStr(pk));
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 输出普通RSA格式字符串
	 * <pre>-----BEGIN RSA PRIVATE KEY-----
	 * xxx
	 * -----END RSA PRIVATE KEY-----</pre>
	 * @param pk
	 */
	public static String toRsaKeyStr(PrivateKey pk) {
		StringWriter sw = null;
		PEMWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PEMWriter(sw);
			pw.writeObject(pk);
			pw.flush();
			sw.flush();
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (pw != null) {
					pw.close();
				}
				if (sw != null) {
					sw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 把私钥储存为PKCS8格式的文件
	 * <pre>-----BEGIN PRIVATE KEY-----
	 * xxx
	 * -----END PRIVATE KEY-----</pre>
	 * @param pk
	 * @param path
	 */
	public static void write2PKCS8Key(PrivateKey pk, String path) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			fw.write(toPKCS8KeyString(pk));
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 输出私钥为PKCS8格式字符串
	 * <pre>-----BEGIN PRIVATE KEY-----
	 * xxx
	 * -----END PRIVATE KEY-----</pre>
	 * @param pk
	 */
	public static String toPKCS8KeyString(PrivateKey pk) {
		StringWriter sw = null;
		PemWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PemWriter(sw);
			PKCS8Generator generator = new PKCS8Generator(pk);
			pw.writeObject(generator);
			pw.flush();
			sw.flush();
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (pw != null) {
					pw.close();
				}
				if (sw != null) {
					sw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
