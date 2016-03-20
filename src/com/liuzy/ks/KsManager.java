package com.liuzy.ks;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.crypto.Cipher;

import org.bouncycastle.openssl.PEMReader;

import com.liuzy.util.Util;

/**
 * 生成KeyStore和TrustStore
 *
 * @author liuzy
 */
public class KsManager {
	private static String tag = "KeyStoreManager";

	/**
	 * @param crtFile client.crt文件
	 * @param pemFile client.pem文件
	 * @param keyStorePwd 生成的KeyStore密码
	 * @return
	 */
	public static KeyStore getKeyStoreByCrtPem(String crtFile, String pemFile, String keyStorePwd) {
		InputStream pem = null;
		InputStreamReader inReader = null;
		PEMReader pemReader = null;
		InputStream crt = null;
		try {
			// 客户端私钥
			pem = new FileInputStream(new File(pemFile));
			inReader = new InputStreamReader(pem);
			pemReader = new PEMReader(inReader);
			KeyPair keyPair = (KeyPair) pemReader.readObject();
			PrivateKey privateKey = keyPair.getPrivate();

			// 客户端证书
			crt = new FileInputStream(new File(crtFile));
			CertificateFactory cf = CertificateFactory.getInstance("x.509");
			Certificate cer = cf.generateCertificate(crt);
			Certificate[] chain = new Certificate[] { cer };

			// 验证公钥和私钥
			verify(cer.getPublicKey(), privateKey);
			
			// 初始化
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			keyStore.setKeyEntry("client", privateKey, keyStorePwd.toCharArray(), chain);
			
			// 打印KeyStore信息
			Util.show(keyStore, keyStorePwd);
			
			return keyStore;
		} catch (Exception e) {
			Util.log(tag, "创建服务器信任的证书仓库失败");
			e.printStackTrace();
		} finally {
			try {
				if (pem != null) {
					pem.close();
				}
				if (inReader != null) {
					inReader.close();
				}
				if (pemReader != null) {
					pemReader.close();
				}
				if (crt != null) {
					crt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 验证读取文件得到的公钥和私钥
	 * 
	 * @param publicKey
	 * @param privateKey
	 * @throws Exception
	 */
	private static void verify(PublicKey publicKey, PrivateKey privateKey) throws Exception {
		String msg = "测试字符串_test_string";
		Util.log("加密前", msg);
		Cipher cipher = Cipher.getInstance("RSA");
		// 公钥加密
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] mi = cipher.doFinal(msg.getBytes());
		// 私钥解密
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		String ming = new String(cipher.doFinal(mi));
		Util.log("解密后", ming);
		// 验证
		if (!msg.equals(ming)) {
			throw new RuntimeException("客户端公钥和私钥不匹配");
		}
	}

	/**
	 * @param crtFile server.crt
	 * @return
	 */
	public static KeyStore getTrustStoreByCrt(String crtFile) {
		InputStream crtIn = null;
		try {
			// 服务器证书
			crtIn = new FileInputStream(new File(crtFile));
			CertificateFactory cf = CertificateFactory.getInstance("x.509");
			Certificate cer = cf.generateCertificate(crtIn);

			// 初始化
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			// 把服务器证书放入keystore中
			keyStore.setCertificateEntry("server", cer);

			// 打印KeyStore信息
			Util.show(keyStore, null);

			return keyStore;
		} catch (Exception e) {
			Util.log(tag, "创建客户端信任的服务器证书仓库失败");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (crtIn != null) {
					crtIn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @param p12File
	 * @param p12Pwd
	 * @return
	 */
	public static KeyStore getKeyStoreByP12(String p12File, String p12Pwd) {
		InputStream p12In = null;
		try {
			p12In = new FileInputStream(new File(p12File));
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(p12In, p12Pwd.toCharArray());
			Util.show(keyStore, p12Pwd);
			return keyStore;
		} catch (Exception e) {
			Util.log(tag, "创建服务器信任的证书仓库失败");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (p12In != null) {
					p12In.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * @param bksFile
	 * @return
	 */
	public static KeyStore getTrustStoreByBks(String bksFile, String bksPwd) {
		InputStream bksIn = null;
		try {
			bksIn = new FileInputStream(new File(bksFile));
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(bksIn, bksPwd.toCharArray());
			Util.show(keyStore, bksPwd);
			return keyStore;
		} catch (Exception e) {
			Util.log(tag, "创建客户端信任的服务器证书仓库失败");
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

}
