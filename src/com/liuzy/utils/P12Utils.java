package com.liuzy.utils;

import java.io.InputStream;
import java.security.KeyStore;

public class P12Utils {
	public static KeyStore read(InputStream p12File, String p12Pwd) {
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(p12File, p12Pwd.toCharArray());
			return keyStore;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
