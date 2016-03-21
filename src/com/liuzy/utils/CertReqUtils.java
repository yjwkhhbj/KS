package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;

import com.liuzy.util.Base64;

/**
 * 证书请求
 * @author liuzy
 * @version 2016-3-21
 */
public class CertReqUtils {
	/**
	 * 生成证书请求
	 * <pre>-----BEGIN CERTIFICATE REQUEST-----
	 * xxx
	 * -----END CERTIFICATE REQUEST-----<pre>
	 * @param publicKey
	 * @param privateKey
	 * @return
	 */
	public static CertificationRequest create(PublicKey publicKey, PrivateKey privateKey, String subjectDN) {
		InputStream in = null;
		try {
			// sun的写法
//			sun.security.pkcs.PKCS10 csr = new sun.security.pkcs.PKCS10(publicKey);
//			java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
//			signature.initSign(privateKey);
//			sun.security.x509.X500Name x500Name = new sun.security.x509.X500Name(subjectDN);
//			sun.security.x509.X500Signer x500Signer = new sun.security.x509.X500Signer(signature, x500Name);
//			csr.encodeAndSign(x500Signer);
//			PrintStream pw = new PrintStream("E:/me.csr");
//			csr.print(pw);
//			pw.close();
			// 这样写可以
			SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
			PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(new X500Name(subjectDN), info);
			ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(privateKey);
			PKCS10CertificationRequest scr = new PKCS10CertificationRequest(builder.build(sigGen).getEncoded());
			return scr;
			// 这种更简单
//			X500Principal x500Principal = new X500Principal(subjectDN);
//			CertificationRequest csr = new PKCS10CertificationRequest("SHA1withRSA", x500Principal, publicKey, new DERSet(), privateKey);
//			return csr;
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
	public static CertificationRequest read(String csrFile) {
		InputStream csrIn = null;
		Scanner scanner = null;
		try {
			csrIn = new FileInputStream(new File(csrFile));
			scanner = new Scanner(csrIn);
			StringBuilder sb = new StringBuilder();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.contains(" ")) {
					sb.append(line);
				}
			}
			byte[] bytes = Base64.decode(sb.toString().toCharArray());
			CertificationRequest csr = new PKCS10CertificationRequest(bytes);
			return csr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (scanner != null) {
					scanner.close();
				}
				if (csrIn != null) {
					csrIn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 证书请求写入文件
	 * <pre>-----BEGIN NEW CERTIFICATE REQUEST-----
	 * xxx
	 * -----END NEW CERTIFICATE REQUEST-----</pre>
	 * @param csr
	 * @param path
	 */
	public static void write(CertificationRequest csr, String path) {
		FileWriter fw = null;
		try {
			StringBuilder sb = new StringBuilder();
			int index = 0;
			sb.append("-----BEGIN NEW CERTIFICATE REQUEST-----");
			for (char c : Base64.encode(csr.getEncoded())) {
				if (index % 77 == 0) {
					sb.append("\n");
				}
				sb.append(c);
				index++;
			}
			sb.append("\n-----END NEW CERTIFICATE REQUEST-----\n");
			fw = new FileWriter(new File(path));
			fw.write(sb.toString());
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
	
}
