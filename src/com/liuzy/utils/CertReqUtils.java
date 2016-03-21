package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

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
	 * @param signatureAlgorithm
	 * @return
	 */
	public static CertificationRequest create(PublicKey publicKey, PrivateKey privateKey, String subjectDN, String signatureAlgorithm) {
		InputStream in = null;
		try {
			// sun的写法
//			sun.security.pkcs.PKCS10 csr = new sun.security.pkcs.PKCS10(publicKey);
//			java.security.Signature signature = java.security.Signature.getInstance(signatureAlgorithm);
//			signature.initSign(privateKey);
//			sun.security.x509.X500Name x500Name = new sun.security.x509.X500Name(subjectDN);
//			sun.security.x509.X500Signer x500Signer = new sun.security.x509.X500Signer(signature, x500Name);
//			csr.encodeAndSign(x500Signer);
//			PrintStream pw = new PrintStream("E:/me.csr");
//			csr.print(pw);
//			pw.close();
			// 这样写可以，需要bcmail-jdk15-1.46.jar
//			SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
//			PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(new X500Name(subjectDN), info);
//			ContentSigner sigGen = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(privateKey);
//			PKCS10CertificationRequest scr = new PKCS10CertificationRequest(builder.build(sigGen).getEncoded());
//			return scr;
			// 这种更简单
			javax.security.auth.x500.X500Principal x500Principal = new javax.security.auth.x500.X500Principal(subjectDN);
			CertificationRequest csr = new PKCS10CertificationRequest(signatureAlgorithm, x500Principal, publicKey, new DERSet(), privateKey);
			return csr;
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
		InputStreamReader inReader = null;
		PemReader pr = null;
//		Scanner scanner = null;
		try {
			csrIn = new FileInputStream(new File(csrFile));
			inReader = new InputStreamReader(csrIn);
			pr = new PemReader(inReader);
			PemObject po = pr.readPemObject();
			byte[] bytes = po.getContent();
//			csrIn = new FileInputStream(new File(csrFile));
//			scanner = new Scanner(csrIn);
//			StringBuilder sb = new StringBuilder();
//			while (scanner.hasNextLine()) {
//				String line = scanner.nextLine();
//				if (!line.contains(" ")) {
//					sb.append(line);
//				}
//			}
//			byte[] bytes = Base64.decode(sb.toString().toCharArray());
			CertificationRequest csr = new PKCS10CertificationRequest(bytes);
			return csr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
//				if (scanner != null) {
//					scanner.close();
//				}
				if (pr != null) {
					pr.close();
				}
				if (inReader != null) {
					inReader.close();
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
