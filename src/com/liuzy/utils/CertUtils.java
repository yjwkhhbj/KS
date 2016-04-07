package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

/**
 * 证书
 * 
 * @author liuzy
 * @version 2016-3-20
 */
public class CertUtils {

	/**
	 * 证书写入文件
	 * 
	 * <pre>
	 * -----BEGIN CERTIFICATE-----
	 * xxx
	 * -----END CERTIFICATE-----
	 * </pre>
	 * 
	 * @param cert
	 * @param path
	 * @throws IOException 
	 */
	public static void write(X509Certificate cert, String path) throws IOException {
		FileWriter fw = null;
		JcaPEMWriter pw = null;
		try {
			fw = new FileWriter(new File(path));
			pw = new JcaPEMWriter(fw);
			pw.writeObject(cert);
			pw.flush();
			fw.flush();
		} finally {
			try {
				if (pw != null) {
					pw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 读证书文件
	 * 
	 * @param cerFile
	 * @return
	 * @throws FileNotFoundException 
	 * @throws CertificateException 
	 */
	public static X509Certificate read(String cerFile) throws FileNotFoundException, CertificateException {
		InputStream crt = null;
		try {
			crt = new FileInputStream(new File(cerFile));
			CertificateFactory cf = CertificateFactory.getInstance("x.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(crt);
			return cert;
		} finally {
			try {
				if (crt != null) {
					crt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 打印X509证书信息
	 * 
	 * @param cert
	 */
	public static void print(X509Certificate cert) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("版本号:" + cert.getVersion() + "\t序列号:" + cert.getSerialNumber().toString(16));
		System.out.println("使用者：" + cert.getSubjectDN());
		System.out.println("颁发者：" + cert.getIssuerDN());
		System.out.println("有效期：" + sdf.format(cert.getNotBefore()) + " —— " + sdf.format(cert.getNotAfter()));
		System.out.println("签名算法：" + cert.getSigAlgName());
		System.out.println();
	}
}
