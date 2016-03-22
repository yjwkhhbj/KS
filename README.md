KS
---
- 玩转私钥、公钥、证书、证书请求，CA中心、证书签发、对象转文件、文件转对象等等
- bcpkix-jdk15on-1.54.jar
- bcprov-jdk15on-1.54.jar

示例
---
### 运行com.liuzy.test.CaTest类的main方法看效果

### 输出me的私钥和pkcs8私钥
```java
KeyUtils.write2RsaKey(privateKey, testDir + "me.pem");
KeyUtils.write2PKCS8Key(privateKey, testDir + "me_pkcs8.pem");
```
### 生成me的证书请求文件
```java
// 使用者的信息
String subjectDN = "CN=me.com,OU=我公司,O=我公司,L=shanghai,ST=shanghai,C=cn";
// 签名算法
String signatureAlgorithm = "SHA1withRSA";
// 生成证书请求
PKCS10CertificationRequest csr = CertReqUtils.create(publicKey, privateKey, subjectDN, signatureAlgorithm);
// 写入文件
CertReqUtils.write(csr, testDir + "me.csr");
```

### 初始化证书中心，输CA私钥和CA自签证书，并使用me的公钥和me的信息给他签发证书
```java
// CA初始化1
CACenter caCenter = new CACenter();
caCenter.init();
CertUtils.write(caCenter.getCacert(), testDir + "ca.crt");
KeyUtils.write2RsaKey(caCenter.getPrivateKey(), testDir + "ca.pem");

// 签发1 根据公钥和使用者信息
String subjectDN = "C=cn,ST=shanghai,L=shanghai,O=我公司,OU=我公司,CN=me.com";
Certificate newcert1 = caCenter.sign(publicKey, subjectDN);
CertUtils.write(newcert1, testDir + "newcert1.cer");
```

### 用CA私钥和CA自签证书初始化证书中心，并用me的证书请求给他签发证书
```java
// CA初始化2
CACenter caCenter = new CACenter();
caCenter.init(testDir + "ca.crt", testDir + "ca.pem");

// 签发2 根据证书请求文件
CertificationRequest csr = CertReqUtils.read(testDir + "me.csr");
Certificate newcert2 = caCenter.sign(csr);
CertUtils.write(newcert2, testDir + "newcert2.cer");
```

### 读证书，打印证书信息
```java
// 读证书
Certificate cert = CertUtils.read(testDir + "newcert1.cer");
// 查看证书信息
CertUtils.print((X509Certificate) cert);
// 验证证书
System.out.println(CertUtils.verify(cert));
```

创建双向认证服务器
---



图形化界面
---
后面将实现图形化界面

### 运行com.liuzy.ui.Main类的main方法