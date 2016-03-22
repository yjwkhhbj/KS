package com.liuzy.ca;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
	public Subject(String subjectDN) throws NoSuchAlgorithmException {
		this.subjectDN = subjectDN;
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair kp = kpg.generateKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
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
}
