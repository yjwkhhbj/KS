package com.liuzy.ca;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;

/**
 * CA证书签发中心
 * 
 * @author liuzy
 * @version 2016-3-20
 */
@SuppressWarnings("deprecation")
public class CACenter {
	// 序列号 每签发一次加1
	static int serialNumber = 1;
	// CA的信息
	private String issuerDN = "C=cn,ST=shanghai,L=shanghai,O=证书签发中心,OU=CA,CN=ca.com";
	// CA的私钥
	private PrivateKey privateKey;
	// CA的公钥
	private PublicKey publicKey;
	// CA的自签证书
	private X509Certificate cacert;
	// CA的签名算法
	private String signatureAlgorithm = "SHA1withRSA";

	public CACenter() {
	}
	public CACenter(String issuerDN) {
		this.issuerDN = issuerDN;
	}

	public CACenter(String C, String ST, String L, String O, String OU, String CN) {
		issuerDN = String.format("C=%s,ST=%s,L=%s,O=%s,OU=%s,CN=%s", C, ST, L, O, OU, CN);
	}

	/**
	 * 自己生成CA私钥和CA自签证书
	 */
	public void init() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
		
		Date notBefore = new Date();
		Date notAfter = new Date();
		notAfter.setYear(notBefore.getYear() + 10);
		// 旧版本包写法
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		certGen.setSerialNumber(new BigInteger("1"));
		certGen.setIssuerDN(new X500Principal(issuerDN));
		certGen.setNotBefore(notBefore);
		certGen.setNotAfter(notAfter);
		certGen.setSubjectDN(new X500Principal(issuerDN));
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm(signatureAlgorithm);
		cacert = certGen.generateX509Certificate(privateKey, "BC");
		//新版本包写法
//		Date notBefore = new Date();
//		Date notAfter = notBefore;
//		notAfter.setYear(notBefore.getYear() + 10);
//		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(new X500Name(issuerDN), new BigInteger("" + serialNumber++), notBefore, notAfter, new X500Name(issuerDN), publicKey);
//		ContentSigner sigGen = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(privateKey);
//		X509CertificateHolder holder = builder.build(sigGen);
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		InputStream in = new ByteArrayInputStream(holder.toASN1Structure().getEncoded());
//		cacert = (X509Certificate) cf.generateCertificate(in);
//		in.close();
	}

	/**
	 * 加载CA证书和CA私钥
	 * @param cacertFile
	 * @param capemFile
	 */
	public void init(String cacertFile, String capemFile) throws Exception {
		cacert = (X509Certificate) CertUtils.read(cacertFile);
		privateKey = KeyUtils.read(capemFile).getPrivate();
		publicKey = cacert.getPublicKey();
		issuerDN = cacert.getIssuerDN().toString();
	}

	/**
	 * 根据公钥和公司信息，给别人签发证书
	 * @param hisPublicKey
	 * @param SubjectDN
	 * @return
	 */
	public Certificate sign(PublicKey hisPublicKey, String subjectDN) throws Exception {
		Date notBefore = new Date();
		Date notAfter = new Date();
		notAfter.setYear(notBefore.getYear() + 10);
		// 旧版本包写法
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		certGen.setSerialNumber(new BigInteger("" + serialNumber++));
		certGen.setIssuerDN(new X500Principal(issuerDN));
		certGen.setNotBefore(notBefore);
		certGen.setNotAfter(notAfter);
		certGen.setSubjectDN(new X500Principal(subjectDN));
		certGen.setPublicKey(hisPublicKey);
		certGen.setSignatureAlgorithm(signatureAlgorithm);
		X509Certificate newcert = certGen.generateX509Certificate(privateKey, "BC");
		//新版本包写法
//		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(new X500Name(issuerDN), new BigInteger("" + serialNumber++), notBefore, notAfter, new X500Name(subjectDN), mPublicKey);
//		ContentSigner sigGen = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(privateKey);
//		X509CertificateHolder holder = builder.build(sigGen);
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		InputStream in = new ByteArrayInputStream(holder.toASN1Structure().getEncoded());
//		X509Certificate newcert = (X509Certificate) cf.generateCertificate(in);
//		in.close();
		return newcert;
	}

	/**
	 * 根据证书签发请求，给别人签发证书
	 * @param csr
	 * @return
	 */
	public Certificate sign(CertificationRequest csr) throws Exception {
		CertificationRequestInfo csrinfo = csr.getCertificationRequestInfo();
		X509Name x509Name = csrinfo.getSubject();
		String subjectDN = x509Name.toString();
		SubjectPublicKeyInfo info = csrinfo.getSubjectPublicKeyInfo();
		KeyFactory kf = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new X509EncodedKeySpec(info.getEncoded());
		PublicKey mPublicKey = kf.generatePublic(keySpec);
		return sign(mPublicKey, subjectDN);
	}

	public Certificate getCacert() {
		return cacert;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
}
