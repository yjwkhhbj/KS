package com.liuzy.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.liuzy.ca.CACenter;
import com.liuzy.utils.CertReqUtils;
import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;

public class Test {
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	
	private static String testDir = "E:/";

	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();

		// 测试KeyUtils
		testKeyUtils();
		
		// 测试CerReqUtils
		testCerReqUtils();
		
		// 测试CACenter
		testCACenter1();
		testCACenter2();
		
		// 测试CertUtils
		testCertUtils();
	}

	public static void testKeyUtils() throws Exception {
		KeyUtils.write2RsaKey(privateKey, testDir + "rsa_private.key");
		KeyUtils.write2PKCS8Key(privateKey, testDir + "pkcs8_private.key");
	}

	public static void testCerReqUtils() throws Exception {
		// 使用者的信息
		String subjectDN = "C=cn,ST=shanghai,L=shanghai,O=我公司,OU=我公司,CN=me.com";
		// 生成证书请求
		CertificationRequest csr = CertReqUtils.create(publicKey, privateKey, subjectDN);
		// 写入文件
		CertReqUtils.write(csr, testDir + "me.csr");
	}

	public static void testCACenter1() throws Exception {
		// CA初始化1
		CACenter caCenter = new CACenter();
		caCenter.init();
		CertUtils.write(caCenter.getCacert(), testDir + "ca.crt");
		KeyUtils.write2RsaKey(caCenter.getPrivateKey(), testDir + "ca.pem");
		
		// 签发1 根据公钥和使用者信息
		String subjectDN = "C=cn,ST=shanghai,L=shanghai,O=我公司,OU=我公司,CN=me.com";
		Certificate newcert1 = caCenter.sign(publicKey, subjectDN);
		CertUtils.write(newcert1, testDir + "newcert1.cer");
	}
	public static void testCACenter2() throws Exception {
		// CA初始化2
		CACenter caCenter = new CACenter();
		caCenter.init(testDir + "ca.crt", testDir + "ca.pem");
		
		// 签发2 根据证书请求文件
		CertificationRequest csr = CertReqUtils.read(testDir + "me.csr");
		Certificate newcert2 = caCenter.sign(csr);
		CertUtils.write(newcert2, testDir + "newcert2.cer");
	}
	
	public static void testCertUtils() throws Exception {
		// 读证书
		Certificate cert = CertUtils.read(testDir + "newcert1.cer");
		// 查看证书信息
		CertUtils.print((X509Certificate) cert);
		// 验证证书
		System.out.println(CertUtils.verify(cert));
	}
}
