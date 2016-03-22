package com.liuzy.test;

import java.security.KeyStore;

import com.liuzy.http.HTTPS;
import com.liuzy.http.KsManager;

public class HttpsTest {
	public static void main(String[] args) {
		String clientCrt = "D:/client.crt";
		String clientPem = "D:/client.pem";
		String keyStorePwd = "123456";
		String serverCrt = "D:/nginx.crt";
		
		KeyStore keyStore = KsManager.getKeyStoreByCrtPem(clientCrt, clientPem, keyStorePwd);
		KeyStore trustStore = KsManager.getTrustStoreByCrt(serverCrt);
		
		HTTPS.init(keyStore, keyStorePwd, trustStore);
		
		new HTTPS().doGET("https://www.liuzy.com");
	}
}