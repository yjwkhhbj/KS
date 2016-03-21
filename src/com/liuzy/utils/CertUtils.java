package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.openssl.PEMWriter;

/**
 * 证书
 * @author liuzy
 * @version 2016-3-20
 */
public class CertUtils {

	/**
	 * 验证证书
	 * @param cert
	 * @return
	 */
	public static Boolean verify(Certificate cert) {
		return true;
	}

	/**
	 * 证书写入文件
	 * <pre>-----BEGIN CERTIFICATE-----
	 * xxx
	 * -----END CERTIFICATE-----</pre>
	 * @param cer
	 * @param path
	 */
	public static void write(Certificate cer, String path) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(path));
			fw.write(toCerStr(cer));
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
	 * 输出证书Base64字符串
	 * <pre>-----BEGIN CERTIFICATE-----
	 * xxx
	 * -----END CERTIFICATE-----</pre>
	 * @param cer
	 */
	public static String toCerStr(Certificate cer) {
		StringWriter sw = null;
		PEMWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PEMWriter(sw);
			pw.writeObject(cer);
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
	 * 读证书文件
	 * @param cerFile
	 * @return
	 */
	public static Certificate read(String cerFile) {
		InputStream crt = null;
		try {
			crt = new FileInputStream(new File(cerFile));
			CertificateFactory cf = CertificateFactory.getInstance("x.509");
			X509Certificate cer = (X509Certificate) cf.generateCertificate(crt);
			return cer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
	 * @param cer
	 */
	public static void print(X509Certificate cert) {
		System.out.println("输出证书信息:\n" + cert.toString());
		System.out.println("版本号:" + cert.getVersion());
		System.out.println("序列号:" + cert.getSerialNumber().toString(16));
		System.out.println("主体名：" + cert.getSubjectDN());
		System.out.println("签发者：" + cert.getIssuerDN());
		System.out.println("有效期：" + cert.getNotBefore());
		System.out.println("签名算法：" + cert.getSigAlgName());
		byte[] sig = cert.getSignature();// 签名值
		PublicKey pk = cert.getPublicKey();
		byte[] pkenc = pk.getEncoded();
		System.out.println("签名 ：");
		for (int i = 0; i < sig.length; i++)
			System.out.print(sig[i] + ",");
		System.out.println();
		System.out.println("公钥： ");
		for (int i = 0; i < pkenc.length; i++)
			System.out.print(pkenc[i] + ",");
		System.out.println();
	}
}
