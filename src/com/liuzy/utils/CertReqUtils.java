package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

/**
 * 证书请求
 * @author liuzy
 * @version 2016-3-21
 */
public class CertReqUtils {
	/**
	 * 生成证书请求
	 * @param publicKey
	 * @param privateKey
	 * @param signatureAlgorithm
	 * @return
	 */
	public static PKCS10CertificationRequest create(PublicKey publicKey, PrivateKey privateKey, String subjectDN, String signatureAlgorithm) {
		InputStream in = null;
		try {
			X500Name x500Name = new X500Name(subjectDN);
			PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(x500Name, publicKey);
			ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(privateKey);
			PKCS10CertificationRequest req = builder.build(contentSigner);
			return req;
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

	/**
	 * 读证书请求文件
	 * @param csrFile
	 * @return
	 */
	public static PKCS10CertificationRequest read(String csrFile) {
		InputStream pemIn = null;
		InputStreamReader inReader = null;
		PEMParser pemParser = null;
		try {
			pemIn = new FileInputStream(new File(csrFile));
			inReader = new InputStreamReader(pemIn);
			pemParser = new PEMParser(inReader);
			return (PKCS10CertificationRequest) pemParser.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
	 * 证书请求写入文件
	 * <pre>-----BEGIN CERTIFICATE REQUEST-----
	 * xxx
	 * -----END CERTIFICATE REQUEST-----</pre>
	 * @param csr
	 * @param path
	 */
	public static void write(PKCS10CertificationRequest req, String path) {
		FileWriter fw = null;
		JcaPEMWriter pw = null;
		try {
			fw = new FileWriter(new File(path));
			pw = new JcaPEMWriter(fw);
			pw.writeObject(req);
			pw.flush();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
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
	
}
