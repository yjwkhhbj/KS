package com.liuzy.ca;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultAlgorithmNameFinder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;

import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyStoreUtils;
import com.liuzy.utils.KeyUtils;

/**
 * CA证书签发中心
 * 
 * @author liuzy
 * @version 2016-3-20
 */
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

		Calendar calendar = Calendar.getInstance();
		Date notBefore = calendar.getTime();
		calendar.add(Calendar.YEAR, 10);
		Date notAfter = calendar.getTime();

		JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(new X500Name(issuerDN), new BigInteger("1"), notBefore, notAfter, new X500Name(issuerDN), publicKey);
		ContentSigner caSigner = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(privateKey);
		cacert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(caSigner));

		cacert.checkValidity(new Date());
		cacert.verify(publicKey);
	}

	/**
	 * 加载CA证书和CA私钥
	 * 
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
	 * 
	 * @param hisPublicKey
	 * @param SubjectDN
	 * @return
	 */
	public X509Certificate sign(PublicKey hisPublicKey, String subjectDN) throws Exception {
		return sign(hisPublicKey, new X500Name(subjectDN), signatureAlgorithm);
	}

	public X509Certificate sign(PublicKey hisPublicKey, String subjectDN, String signatureAlgorithm) throws Exception {
		return sign(hisPublicKey, new X500Name(subjectDN), signatureAlgorithm);
	}

	/**
	 * 根据使用者公钥、使用者信息、签名算法，给别人签发证书
	 * 
	 * @param hisPublicKey
	 * @param subjectDN
	 * @param signatureAlgorithm
	 * @return
	 * @throws Exception
	 */
	public X509Certificate sign(PublicKey hisPublicKey, X500Name subject, String signatureAlgorithm) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date notBefore = calendar.getTime();
		calendar.add(Calendar.YEAR, 10);
		Date notAfter = calendar.getTime();

		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
		AuthorityKeyIdentifier auth = extUtils.createAuthorityKeyIdentifier(publicKey);
		SubjectKeyIdentifier subj = extUtils.createSubjectKeyIdentifier(hisPublicKey);

		JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(new X500Name(issuerDN), new BigInteger("" + serialNumber++), notBefore, notAfter, subject, hisPublicKey);
		certBuilder.addExtension(Extension.authorityKeyIdentifier, false, auth);
		certBuilder.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));
		certBuilder.addExtension(Extension.subjectKeyIdentifier, false, subj);

		ContentSigner caSigner = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(privateKey);
		X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(caSigner));

		cert.checkValidity(new Date());
		cert.verify(publicKey);
		return cert;
	}

	/**
	 * 根据证书签发请求，给别人签发证书
	 * 
	 * @param csr
	 * @return
	 */
	public X509Certificate sign(PKCS10CertificationRequest csr) throws Exception {
		JcaPKCS10CertificationRequest req = new JcaPKCS10CertificationRequest(csr);
		ASN1ObjectIdentifier OID = req.getSignatureAlgorithm().getAlgorithm();
		DefaultAlgorithmNameFinder finder = new DefaultAlgorithmNameFinder();
		return sign(req.getPublicKey(), req.getSubject(), finder.getAlgorithmName(OID));
	}

	/** 保存证书文件 */
	public void saveCert(String path) {
		CertUtils.write(cacert, path);
	}

	/** 保存私钥到文件 */
	public void saveRsaKey(String path) {
		KeyUtils.write2RsaKey(privateKey, path);
	}

	/** 保存PKCS8格式私钥到文件 */
	public void savePkcs8Key(String path) {
		KeyUtils.write2PKCS8Key(privateKey, path);
	}

	/** 保存证书到KeyStore文件 */
	public void saveCert2Ks(String alias, String ksPwd, String path) {
		KeyStoreUtils.writeJks(cacert, alias, ksPwd, path);
	}

	public X509Certificate getCacert() {
		return cacert;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

}
