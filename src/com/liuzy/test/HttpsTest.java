package com.liuzy.test;

import java.security.KeyStore;

import com.liuzy.http.HTTPS;
import com.liuzy.http.KsManager;
import com.liuzy.utils.KsUtils;

public class HttpsTest {
	static String testDir = "D:/";

	public static void main(String[] args) {
		// String clientCrt = file("client.crt");
		// String clientPem = file("client.pem");
		// String pwd = "123456";
		// String serverCrt = file("nginx.crt");
		//
		// KeyStore keyStore = KsManager.getKeyStoreByCrtPem(clientCrt,
		// clientPem, pwd);
		// KeyStore trustStore = KsManager.getTrustStoreByCrt(serverCrt);

		String clientJks = file("client.jks");
		String pwd = "123456";
		String clientTrustJks = file("client_trust.jks");

		KeyStore keyStore = KsManager.getKeyStoreByJks(clientJks, pwd);
		KeyStore trustStore = KsManager.getTrustStoreByJks(clientTrustJks, pwd);

		KsUtils.print(keyStore, pwd);
		KsUtils.print(trustStore);

		HTTPS.init(keyStore, pwd, trustStore);

		new HTTPS().doGET("https://www.liuzy.com");
	}

	public static String file(String file) {
		return testDir + file;
	}
}