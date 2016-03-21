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
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
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
	private String issuerDN = "CN=ca.com,OU=CA,O=证书签发中心,L=shanghai,ST=shanghai,C=cn";
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

	public CACenter(String CN, String OU, String O, String L, String ST, String C) {
		issuerDN = String.format("CN=%s,OU=%s,O=%s,L=%s,ST=%s,C=%s", CN, OU, O, L, ST, C);
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
		notBefore.setHours(0);
		notBefore.setMinutes(0);
		notBefore.setSeconds(0);
		Date notAfter = new Date();
		notAfter.setYear(notBefore.getYear() + 10);
		notAfter.setHours(23);
		notAfter.setMinutes(59);
		notAfter.setSeconds(59);
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
		//新版本包写法，需要bcmail-jdk15-1.46.jar
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
	 * 根据使用者公钥、使用者信息、CA默认签名算法，给别人签发证书
	 * @param hisPublicKey
	 * @param SubjectDN
	 * @return
	 */
	public Certificate sign(PublicKey hisPublicKey, String subjectDN) throws Exception {
		return sign(hisPublicKey, subjectDN, signatureAlgorithm);
	}

	/**
	 * 根据使用者公钥、使用者信息、签名算法，给别人签发证书
	 * @param hisPublicKey
	 * @param subjectDN
	 * @param signatureAlgorithm
	 * @return
	 * @throws Exception
	 */
	public Certificate sign1(PublicKey hisPublicKey, String subjectDN, String signatureAlgorithm) throws Exception {
		Date notBefore = new Date();
		notBefore.setHours(0);
		notBefore.setMinutes(0);
		notBefore.setSeconds(0);
		Date notAfter = new Date();
		notAfter.setYear(notBefore.getYear() + 10);
		notAfter.setHours(23);
		notAfter.setMinutes(59);
		notAfter.setSeconds(59);
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		certGen.setSerialNumber(new BigInteger("" + serialNumber++));
		certGen.setIssuerDN(new X500Principal(issuerDN));
		certGen.setNotBefore(notBefore);
		certGen.setNotAfter(notAfter);
		certGen.setSubjectDN(new X500Principal(subjectDN));
		certGen.setPublicKey(hisPublicKey);
		certGen.setSignatureAlgorithm(signatureAlgorithm);
		X509Certificate newcert = certGen.generateX509Certificate(privateKey, "BC");
		return newcert;
	}

	public Certificate sign(PublicKey hisPublicKey, String subjectDN, String signatureAlgorithm) throws Exception {
		Date notBefore = new Date();
		notBefore.setHours(0);
		notBefore.setMinutes(0);
		notBefore.setSeconds(0);
		Date notAfter = new Date();
		notAfter.setYear(notBefore.getYear() + 10);
		notAfter.setHours(23);
		notAfter.setMinutes(59);
		notAfter.setSeconds(59);

	    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
	    AuthorityKeyIdentifier auth = extUtils.createAuthorityKeyIdentifier(publicKey);
	    SubjectKeyIdentifier subj = extUtils.createSubjectKeyIdentifier(hisPublicKey);
	    
		X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(new X500Name(issuerDN), new BigInteger("" + serialNumber++), notBefore, notAfter, new X500Name(subjectDN), hisPublicKey);
		certBuilder.addExtension(X509Extension.authorityKeyIdentifier, false, auth);
		certBuilder.addExtension(X509Extension.basicConstraints, false, new BasicConstraints(false));
		certBuilder.addExtension(X509Extension.subjectKeyIdentifier, false, subj);
		
		ContentSigner caSigner = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(privateKey);
		X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(caSigner));
		
//		AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(signatureAlgorithm);
//	    AlgorithmIdentifier digestAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
//		AsymmetricKeyParameter paramAsymmetricKeyParameter = PrivateKeyFactory.createKey(privateKey.getEncoded());
//		ContentSigner contentSigner = new BcRSAContentSignerBuilder(sigAlgId, digestAlgId).build(paramAsymmetricKeyParameter);
//		
//		X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);
//		X509CertificateStructure certificate = certificateHolder.toASN1Structure();
//		
//		CertificateFactory cf = CertificateFactory.getInstance("X.509");
//		InputStream bis = new ByteArrayInputStream(certificate.getEncoded());
//		X509Certificate cert = (X509Certificate) cf.generateCertificate(bis);
//		bis.close();

		cert.checkValidity(new Date());
		cert.verify(cert.getPublicKey());
		System.out.println(cert);
		return cert;
	}

	/**
	 * 根据证书签发请求，给别人签发证书
	 * @param csr
	 * @return
	 */
	public Certificate sign(CertificationRequest csr) throws Exception {
		CertificationRequestInfo csrinfo = csr.getCertificationRequestInfo();
		// 使用者信息
		X509Name x509Name = csrinfo.getSubject();
		String subjectDN = x509Name.toString();
		// 使用者公钥
		SubjectPublicKeyInfo info = csrinfo.getSubjectPublicKeyInfo();
		KeyFactory kf = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new X509EncodedKeySpec(info.getEncoded());
		PublicKey hisPublicKey = kf.generatePublic(keySpec);
		// 使用者的签名算法标识
		String OID = csr.getSignatureAlgorithm().toString();
		if ("1.2.840.113549.1.1.5".equals(OID)) {
			return sign(hisPublicKey, subjectDN, "SHA1withRSA");
		} else if("1.2.840.113549.1.1.11".equals(OID)) {
			return sign(hisPublicKey, subjectDN, "SHA256withRSA");
		} else {
			return sign(hisPublicKey, subjectDN);
		}
	}

	public Certificate getCacert() {
		return cacert;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
}
