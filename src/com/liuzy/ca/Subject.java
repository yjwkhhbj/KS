package com.liuzy.ca;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;
import com.liuzy.utils.KsUtils;

/**
 * 证书使用者
 * 
 * @author liuzy
 * @version 2016-3-22
 */
public class Subject {
	protected String subjectDN;
	protected int keyLength = 2048;
	protected PublicKey publicKey;
	protected PrivateKey privateKey;
	protected X509Certificate cert;
	protected String signatureAlgorithm = "SHA1withRSA";

	public Subject() {

	}

	public Subject(String subjectDN) throws NoSuchAlgorithmException {
		this.subjectDN = subjectDN;
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(keyLength);
		KeyPair kp = kpg.generateKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
	}

	public Subject(String CN, String OU, String O, String L, String ST, String C) {
		subjectDN = String.format("CN=%s,OU=%s,O=%s,L=%s,ST=%s,C=%s", CN, OU, O, L, ST, C);
	}

	/** 保存证书文件 */
	public void saveCert(String path) {
		CertUtils.write(cert, path);
	}

	/** 保存私钥到文件 */
	public void saveRsaKey(String path) {
		KeyUtils.write2RsaKey(privateKey, path);
	}

	/** 保存PKCS8格式私钥到文件 */
	public void savePkcs8Key(String path) {
		KeyUtils.write2PKCS8Key(privateKey, path);
	}

	/** 保存证书到JavaKeyStore文件 */
	public void saveCert2Jks(String alias, String ksPwd, String path) {
		KsUtils.writeJks(cert, alias, ksPwd, path);
	}

	/** 保存证书到BcKeyStore文件 */
	public void saveCert2Bks(String alias, String ksPwd, String path) {
		KsUtils.writeBks(cert, alias, ksPwd, path);
	}

	/** 保存证书和私钥到JavaKeyStore文件 */
	public void saveKey2Jks(String alias, String keyPwd, String ksPwd, String path) {
		KsUtils.writeJks(cert, alias, privateKey, keyPwd, ksPwd, path);
	}

	/** 保存证书和私钥到BcKeyStore文件 */
	public void saveKey2Bks(String alias, String keyPwd, String ksPwd, String path) {
		KsUtils.writeBks(cert, alias, privateKey, keyPwd, ksPwd, path);
	}

	/** 保存证书和私钥到P12文件 */
	public void saveKey2P12(String alias, String keyPwd, String ksPwd, String path) {
		KsUtils.writeP12(cert, alias, privateKey, keyPwd, ksPwd, path);
	}

	public int getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}

	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	public void setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	public String getSubjectDN() {
		return subjectDN;
	}

	public String getIssuerDN() {
		return cert.getIssuerDN().toString();
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
}
