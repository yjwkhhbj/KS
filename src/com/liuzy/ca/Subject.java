package com.liuzy.ca;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyStoreUtils;
import com.liuzy.utils.KeyUtils;

/**
 * 证书使用者
 * 
 * @author liuzy
 * @version 2016-3-22
 */
public class Subject {
	private String subjectDN;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private X509Certificate cert;
	public Subject(String subjectDN) throws NoSuchAlgorithmException {
		this.subjectDN = subjectDN;
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair kp = kpg.generateKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
	}
	public void saveCert(String path) {
		CertUtils.write(cert, path);
	}
	public void saveRsaKey(String path) {
		KeyUtils.write2RsaKey(privateKey, path);
	}
	public void savePkcs8Key(String path) {
		KeyUtils.write2PKCS8Key(privateKey, path);
	}
	public void saveKeyStore(String alias, String keyPwd, String ksPwd, String path) {
		KeyStoreUtils.writeJks(cert, alias, privateKey, keyPwd, ksPwd, path);
	}
	public void saveTrustStore(String alias, String ksPwd, String path) {
		KeyStoreUtils.writeJks(cert, alias, ksPwd, path);
	}
	public String getSubjectDN() {
		return subjectDN;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public X509Certificate getCert() {
		return cert;
	}
	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
}
