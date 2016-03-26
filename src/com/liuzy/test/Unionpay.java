package com.liuzy.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.liuzy.ca.CACenter;
import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;

public class Unionpay {
	static String PM_CA_CRT = "E:/pm/cacert.crt";
	static String PM_CA_PEM = "E:/pm/cakey.pem";

	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		Terminal terminal = new Terminal("123456789004500", "12345601");

		CACenter pmCA = new CACenter(PM_CA_CRT, PM_CA_PEM);
		
		terminal.setCert(pmCA.sign(terminal.getPublicKey(), terminal.getSubjectDN(), "SHA1withRSA"));
		
		terminal.saveToDir("E:/");
		
	}
	
}

class Terminal {
	private String merId;
	private String terId;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private X509Certificate cert;
	public Terminal(String merId, String terId) {
		this.merId = merId;
		this.terId = terId;
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.generateKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void saveToDir(String path) {
		String pemFile = path + merId + "_" + terId + ".pem";
		KeyUtils.write2RsaKey(privateKey, pemFile);
		String certFile = path + merId + "_" + terId + ".crt";
		CertUtils.write(cert, certFile);
	}
	public String getSubjectDN() {
		return "CN="+merId+terId+",OU=中国银联,O=中国银联,L=shanghai,C=CN";
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getTerId() {
		return terId;
	}
	public void setTerId(String terId) {
		this.terId = terId;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public X509Certificate getCert() {
		return cert;
	}
	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
}
