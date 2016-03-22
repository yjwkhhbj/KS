package com.liuzy.util;

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * 打印KeyStore信息
 * 
 * @author liuzy
 */
public class Util {
	private static String tag = "test";

	public static void log(String tag, String msg) {
		System.out.println(String.format("[%5s] %s", tag, msg));
	}

	public static void show(KeyStore ks, String pwd) throws Exception {
		try {
			Util.log(tag, "--------------KeyStore信息---------------");
			Util.log(tag, "Provider : " + ks.getProvider().getName());
			Util.log(tag, "Type : " + ks.getType());
			Util.log(tag, "Size : " + ks.size());
			Enumeration<String> en = ks.aliases();
			while (en.hasMoreElements()) {
				String alias = en.nextElement();

				Certificate cer = ks.getCertificate(alias);
				if (cer != null) {
					Util.log(tag, "\nAlias: " + alias);
					byte[] cerb = cer.getEncoded();
					byte[] pkb = cer.getPublicKey().getEncoded();
					Util.log(tag, "Cer bytes:" + cerb.length);
					// Util.log(tag, new String(Base64.encode(cerb, Base64.DEFAULT)));
					Util.log(tag, "PublicKey bytes:" + pkb.length);
					// Util.log(tag, new String(Base64.encode(pkb,
					// Base64.DEFAULT)));
				}
				//TrustStore中，没有Key
				if (pwd != null) {
					Key k = ks.getKey(alias, pwd.toCharArray());
					if (k != null) {
						Util.log(tag, "\nAlias: " + alias);
						Util.log(tag, "Key Algorithm:" + k.getAlgorithm());
						Util.log(tag, "Key bytes:" + k.getEncoded().length);
						// Util.log(tag, new String(Base64.encode(k.getEncoded(), Base64.DEFAULT)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
