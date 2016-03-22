package com.liuzy.test;

import java.security.cert.X509Certificate;

import com.liuzy.ca.CACenter;
import com.liuzy.ca.Subject;
import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;

public class MyServer {
	public static void main(String[] args) throws Exception {
		signClient();
	}
	public static void signAll() throws Exception {
		CACenter CA = new CACenter("CN=liuzy.com,OU=liuzy.com,O=liuzy.com,L=shanghai,ST=shanghai,C=cn");
		CA.init();
		CertUtils.write(CA.getCacert(), "E:/ca.crt");
		KeyUtils.write2RsaKey(CA.getPrivateKey(), "E:/ca.pem");
		
		Subject nginx = new Subject("CN=liuzy.com,OU=liuzy.com,O=liuzy.com,L=shanghai,ST=shanghai,C=cn");
		X509Certificate nginxCert = CA.sign(nginx.getPublicKey(), nginx.getSubjectDN());
		CertUtils.write(nginxCert, "E:/nginx.crt");
		KeyUtils.write2RsaKey(nginx.getPrivateKey(), "E:/nginx.pem");
		
		Subject client = new Subject("CN=client,OU=client,O=liuzy.com,L=shanghai,ST=shanghai,C=cn");
		X509Certificate clientCert = CA.sign(client.getPublicKey(), client.getSubjectDN());
		CertUtils.write(clientCert, "E:/client.crt");
		KeyUtils.write2RsaKey(client.getPrivateKey(), "E:/client.pem");
		
	}
	public static void signClient() throws Exception {
		CACenter CA = new CACenter();
		CA.init("E:/cacert.pem", "E:/cakey.pem");
		
		Subject client = new Subject("CN=client,OU=client,O=liuzy.com,L=shanghai,ST=shanghai,C=cn");
		X509Certificate clientCert = CA.sign(client.getPublicKey(), client.getSubjectDN());
		CertUtils.write(clientCert, "E:/client.crt");
		KeyUtils.write2RsaKey(client.getPrivateKey(), "E:/client.pem");
	}
}
