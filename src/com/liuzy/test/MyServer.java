package com.liuzy.test;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.liuzy.ca.CACenter;
import com.liuzy.ca.Subject;

public class MyServer {
	static String testDir = "D:/";
	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		signAll();
	}
	public static void signAll() throws Exception {
		CACenter CA = new CACenter("CN=CA,OU=CA,O=liuzy,L=shanghai,ST=shanghai,C=cn");
		CA.init();
		CA.saveCert(testDir + "ca.crt");
		CA.saveRsaKey(testDir + "ca.pem");
		
		Subject nginx = new Subject("CN=*.liuzy.com,OU=nginx,O=liuzy,L=shanghai,ST=shanghai,C=cn");
		nginx.setCert(CA.sign(nginx.getPublicKey(), nginx.getSubjectDN()));
		nginx.saveCert(testDir + "nginx.crt");
		nginx.saveRsaKey(testDir + "nginx.pem");
		nginx.saveTrustStore("server", "123456", testDir + "client_trust.jks");
		
		Subject client = new Subject("CN=client1,OU=client,O=liuzy,L=shanghai,ST=shanghai,C=cn");
		client.setCert(CA.sign(client.getPublicKey(), client.getSubjectDN()));
		client.saveCert(testDir + "client.crt");
		client.saveRsaKey(testDir + "client.pem");
		client.saveKeyStore("client", "123456", "123456", testDir + "client.jks");
		
	}
	public static void signClient() throws Exception {
		CACenter CA = new CACenter();
		CA.init(testDir + "ca.crt", testDir + "ca.pem");
		
		Subject client = new Subject("CN=client2,OU=client,O=liuzy,L=shanghai,ST=shanghai,C=cn");
		client.setCert(CA.sign(client.getPublicKey(), client.getSubjectDN()));
		client.setCert(CA.sign(client.getPublicKey(), client.getSubjectDN()));
		client.saveCert(testDir + "client2.crt");
		client.saveRsaKey(testDir + "client2.pem");
	}
}
