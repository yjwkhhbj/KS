package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
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
	 * 
	 * @param pemFile
	 * @return
	 * @throws IOException 
	 */
	public static KeyPair read(String pemFile) throws IOException {
		InputStream pemIn = null;
		InputStreamReader inReader = null;
		PEMParser pemParser = null;
		try {
			pemIn = new FileInputStream(new File(pemFile));
			inReader = new InputStreamReader(pemIn);
			pemParser = new PEMParser(inReader);
			PEMKeyPair kp = (PEMKeyPair) pemParser.readObject();
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
			return converter.getKeyPair(kp);
		} finally {
			try {
				if (pemParser != null) {
					pemParser.close();
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
	 * 
	 * <pre>
	 * -----BEGIN RSA PRIVATE KEY-----
	 * xxx
	 * -----END RSA PRIVATE KEY-----
	 * </pre>
	 * 
	 * @param pk
	 * @param path
	 * @throws IOException 
	 */
	public static void write2RsaKey(PrivateKey pk, String path) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			fw.write(toRsaKeyStr(pk));
			fw.flush();
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
	 * 
	 * <pre>
	 * -----BEGIN RSA PRIVATE KEY-----
	 * xxx
	 * -----END RSA PRIVATE KEY-----
	 * </pre>
	 * 
	 * @param pk
	 * @throws IOException 
	 */
	public static String toRsaKeyStr(PrivateKey pk) throws IOException {
		StringWriter sw = null;
		JcaPEMWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new JcaPEMWriter(sw);
			pw.writeObject(pk);
			pw.flush();
			sw.flush();
			return sw.toString();
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
	 * 
	 * <pre>
	 * -----BEGIN PRIVATE KEY-----
	 * xxx
	 * -----END PRIVATE KEY-----
	 * </pre>
	 * 
	 * @param pk
	 * @param path
	 * @throws IOException 
	 */
	public static void write2PKCS8Key(PrivateKey pk, String path) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			fw.write(toPKCS8KeyStr(pk));
			fw.flush();
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
	 * 
	 * <pre>
	 * -----BEGIN PRIVATE KEY-----
	 * xxx
	 * -----END PRIVATE KEY-----
	 * </pre>
	 * 
	 * @param pk
	 * @throws IOException 
	 */
	public static String toPKCS8KeyStr(PrivateKey pk) throws IOException {
		StringWriter sw = null;
		PemWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PemWriter(sw);
			JcaPKCS8Generator generator = new JcaPKCS8Generator(pk, null);
			pw.writeObject(generator);
			pw.flush();
			sw.flush();
			return sw.toString();
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
	
	public static String print(Key key) {
		StringBuilder sb = new StringBuilder();
		sb.append("算法：" + key.getAlgorithm());
		sb.append("\n格式：" + key.getFormat());
		sb.append("\n长度：" + key.getEncoded().length);
		sb.append("\n");
		return sb.toString();
	}
}
