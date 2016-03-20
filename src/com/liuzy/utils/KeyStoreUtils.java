package com.liuzy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;

import com.liuzy.util.Util;

public class KeyStoreUtils {
	public static KeyStore read(String ksFile, String ksPwd) {
		InputStream bksIn = null;
		try {
			bksIn = new FileInputStream(new File(ksFile));
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(bksIn, ksPwd.toCharArray());
			Util.show(keyStore, ksPwd);
			return keyStore;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (bksIn != null) {
					bksIn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static void write(KeyStore ks, String ksPwd, String path) {
		OutputStream ksOs = null;
		try {
			ksOs = new FileOutputStream(new File(path));
			ks.store(ksOs, ksPwd.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ksOs != null) {
					ksOs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
