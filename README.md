KS
===
玩转私钥、公钥、证书、签发、CA、对象转文件、文件转对象。 

前言
---
费话不多说，直接看Test类，一清二楚

### testKeyUtils()
```java
KeyUtils.write2RsaKey(privateKey, testDir + "rsa_private.key");
KeyUtils.write2PKCS8Key(privateKey, testDir + "pkcs8_private.key");
```
### testCerUtils()
```java
// 使用者的信息
String subjectDN = "C=cn,ST=shanghai,L=shanghai,O=我公司,OU=我公司,CN=me.com";
// 生成证书请求
CertificationRequest csr = CertReqUtils.create(publicKey, privateKey, subjectDN);
// 写入文件
CertReqUtils.write(csr, testDir + "me.csr");
```

### testCACenter1()
```java
// CA初始化1
CACenter caCenter = new CACenter();
caCenter.init();
CertUtils.write(caCenter.getCacert(), testDir + "ca.crt");
KeyUtils.write2RsaKey(caCenter.getPrivateKey(), testDir + "ca.pem");
```

### testCACenter2()
```java
// CA初始化2
CACenter caCenter = new CACenter();
caCenter.init(testDir + "ca.crt", testDir + "ca.pem");

// 签发2 根据证书请求文件
CertificationRequest csr = CertReqUtils.read(testDir + "me.csr");
Certificate newcert2 = caCenter.sign(csr);
CertUtils.write(newcert2, testDir + "newcert2.cer");
```

### testCertUtils()
```java
// 读证书
Certificate cert = CertUtils.read(testDir + "newcert1.cer");
// 查看证书信息
CertUtils.print((X509Certificate) cert);
// 验证证书
System.out.println(CertUtils.verify(cert));
```