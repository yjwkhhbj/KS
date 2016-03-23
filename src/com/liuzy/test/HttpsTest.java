package com.liuzy.test;

import java.security.KeyStore;

import com.liuzy.http.HTTPS;
import com.liuzy.http.KsManager;

public class HttpsTest {
	static String testDir = "D:/";
	public static void main(String[] args) {
		String clientCrt = testDir + "client.crt";
		String clientPem = testDir + "client.pem";
		String keyStorePwd = "123456";
		String serverCrt = testDir + "tomcat.crt";
		
		KeyStore keyStore = KsManager.getKeyStoreByCrtPem(clientCrt, clientPem, keyStorePwd);
		KeyStore trustStore = KsManager.getTrustStoreByCrt(serverCrt);
		
		HTTPS.init(keyStore, keyStorePwd, trustStore);
		
		new HTTPS().doGET("https://www.liuzy.com");
	}
}