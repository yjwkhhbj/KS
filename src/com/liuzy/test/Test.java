package com.liuzy.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;
import com.liuzy.ca.CACenter;

public class Test {
	private static PublicKey publicKey;
	private static PrivateKey privateKey;

	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();

		// 测试KeyUtils
		testKeyUtils();

		// 测试CerUtils
		testCerUtils();
		
		// 测试CACenter
//		testCACenter();
	}

	public static void testKeyUtils() throws Exception {
		System.out.println(KeyUtils.toRsaKeyStr(privateKey));
		System.out.println(KeyUtils.toPKCS8KeyString(privateKey));
		KeyUtils.write2RsaKey(privateKey, "D:/rsa_private.key");
		KeyUtils.write2PKCS8Key(privateKey, "D:/pkcs8_private.key");
	}

	public static void testCerUtils() throws Exception {
		// 我公司的信息
		String subjectDN = "C=cn,ST=shanghai,L=shanghai,O=我公司,OU=我公司,CN=me.com";
		// 生成证书请求
		CertUtils.createReq(publicKey, privateKey, subjectDN);
//		CertUtils.write(csr, "D:/me.csr");
	}

	public static void testCACenter() throws Exception {
		// CA初始化1
		CACenter caCenter = new CACenter();
		caCenter.init();
		CertUtils.write(caCenter.getCacert(), "D:/ca.crt");
		KeyUtils.write2RsaKey(caCenter.getPrivateKey(), "D:/ca.pem");
		// CA初始化2
//		caCenter.init("D:/ca.crt", "ca.pem");
		
		// 签发1 根据公钥和公司信息
		String subjectDN = "C=cn,ST=shanghai,L=shanghai,O=我公司,OU=我公司,CN=me.com";
		Certificate newcert1 = caCenter.sign(publicKey, subjectDN);
		CertUtils.write(newcert1, "D:/newcert1.cer");
		
		// 签发2 根据证书请求文件
		Certificate csr = CertUtils.read("D:/me.csr");
		Certificate newcert2 = caCenter.sign(csr);
		CertUtils.write(newcert2, "D:/newcert2.cer");
	}
}
