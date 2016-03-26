package com.liuzy.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import com.liuzy.ca.CACenter;
import com.liuzy.utils.CertReqUtils;
import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;
import com.liuzy.utils.KsUtils;

public class CaTest {
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	
	private static String testDir = "D:/";

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
		
		// 测试KsUtils
		testKsUtils();
	}

	public static void testKeyUtils() throws Exception {
		KeyUtils.write2RsaKey(privateKey, testDir + "me.pem");
		KeyUtils.write2PKCS8Key(privateKey, testDir + "me_pkcs8.pem");
	}

	public static void testCerReqUtils() throws Exception {
		// 使用者的信息
		String subjectDN = "CN=me.com,OU=我公司,O=我公司,L=shanghai,ST=shanghai,C=cn";
		// 签名算法
		String signatureAlgorithm = "SHA1withRSA";
		// 生成证书请求
		PKCS10CertificationRequest csr = CertReqUtils.create(publicKey, privateKey, subjectDN, signatureAlgorithm);
		// 写入文件
		CertReqUtils.write(csr, testDir + "me.csr");
	}

	public static void testCACenter1() throws Exception {
		// CA初始化1
		CACenter caCenter = new CACenter("CN=ca.com,OU=CA,O=证书签发中心,L=shanghai,ST=shanghai,C=cn");
		CertUtils.write(caCenter.getCert(), testDir + "ca.crt");
		KeyUtils.write2RsaKey(caCenter.getPrivateKey(), testDir + "ca.pem");
		
		// 签发1 根据公钥和使用者信息
		String subjectDN = "CN=me.com,OU=我公司,O=我公司,L=shanghai,ST=shanghai,C=cn";
		X509Certificate newcert1 = caCenter.sign(publicKey, subjectDN);
		CertUtils.write(newcert1, testDir + "me_cert1.crt");
	}

	public static void testCACenter2() throws Exception {
		// CA初始化2
		CACenter caCenter = new CACenter(testDir + "ca.crt", testDir + "ca.pem");
		
		// 签发2 根据证书请求文件
		PKCS10CertificationRequest csr = CertReqUtils.read(testDir + "me.csr");
		X509Certificate newcert2 = caCenter.sign(csr);
		CertUtils.write(newcert2, testDir + "me_cert2.crt");
	}
	
	public static void testCertUtils() throws Exception {
		// 读证书
		X509Certificate cert = CertUtils.read(testDir + "me_cert1.crt");
		// 查看证书信息
		CertUtils.print(cert);
	}

	private static void testKsUtils() throws Exception {
		// 读
		PrivateKey key = KeyUtils.read(testDir + "me.pem").getPrivate();
		X509Certificate cert = CertUtils.read(testDir + "me_cert1.crt");
		// 设置
		String alias = "client";
		String keyPwd = "123456";
		String ksPwd = "123456";
		// 保存
		KsUtils.writeJks(cert, alias, key, keyPwd, ksPwd, testDir + "me.jks");
		KsUtils.writeBks(cert, alias, key, keyPwd, ksPwd, testDir + "me.bks");
		KsUtils.writeP12(cert, alias, key, keyPwd, ksPwd, testDir + "me.p12");
		// 验证
		KsUtils.print(KsUtils.readJks(testDir + "me.jks", ksPwd), keyPwd);
		KsUtils.print(KsUtils.readBks(testDir + "me.bks", ksPwd), keyPwd);
		KsUtils.print(KsUtils.readP12(testDir + "me.p12", ksPwd), keyPwd);
	}
}
