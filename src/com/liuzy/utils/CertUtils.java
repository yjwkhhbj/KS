package com.liuzy.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequestHolder;

import com.liuzy.util.Base64;

/**
 * 
 * @author liuzy
 * @version 2016-3-20
 */
public class CertUtils {
	/**
	 * 生成证书请求
	 * <pre>-----BEGIN CERTIFICATE REQUEST-----
	 * xxx
	 * -----END CERTIFICATE REQUEST-----<pre>
	 * @param publicKey
	 * @param privateKey
	 * @return
	 */
	public static String createReq(PublicKey publicKey, PrivateKey privateKey, String subjectDN) {
		InputStream in = null;
		try {
			// 1好像不行
			SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
			PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(new X500Name(subjectDN), info);
			ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(privateKey);
			PKCS10CertificationRequestHolder holder = builder.build(sigGen);
			System.out.println(Base64.encode(holder.getEncoded()));
//			CertificateFactory cf = CertificateFactory.getInstance("X.509");
//			in = new ByteArrayInputStream(holder.getEncoded());
//			Certificate req = cf.generateCertificate(in);
			// 2也不行
			sun.security.pkcs.PKCS10 csr = new sun.security.pkcs.PKCS10(publicKey);
			java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
			signature.initSign(privateKey);
			sun.security.x509.X500Name x500Name = new sun.security.x509.X500Name(subjectDN);
			sun.security.x509.X500Signer x500Signer = new sun.security.x509.X500Signer(signature, x500Name);
			csr.encodeAndSign(x500Signer);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PrintStream pw = new PrintStream("D:/a.csr");
			csr.print(pw);
			pw.close();
			// 3结果和2一样
			PKCS10CertificationRequest csr1 = new PKCS10CertificationRequest("SHA1withRSA", new X500Principal(subjectDN), publicKey, new DERSet(), privateKey);
			System.out.println(Base64.encode(csr1.getEncoded()));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static Boolean verify(Certificate cert) {
		return false;
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
			show(cer);
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
	private static void show(X509Certificate cert) {
		System.out.println(cert);
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
