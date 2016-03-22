KS
---

# 双向认证完整示例
- 以下测试在VMware虚拟机中的CentOS6.5中完成
- 用openssl生成CA私钥、公钥、自签证书；
- 用openssl签发Nginx服务器证书
- 用openssl签发客户端证书
- 安装、配置Nginx服务器，一步一步从单向认证到双向认证
- 安装、配置NodeJS环境，生成Nodejs项目，返回JSON

# 用root登陆执行脚本

- 执行clear.sh

```
清除了CA密钥和签发库
```

- 执行create.sh

```
CA自签依次输入：cn sh sh liuzy CA CA，然后回车跳过其他输入；
生成Nginx证书请求依次输入：cn sh sh liuzy nginx nginx，然后回车跳过其他输入；
签发Nginx证书输入两次y回车确认
生成Client证书请求依次输入：cn sh sh liuzy client client，然后回车跳过其他输入；
签发Client证书输入两次y回车确认
```

- 执行nginx.sh

```
打开浏览器，输入主机IP，可以访问Nginx主页
```

- 执行nodejs.sh
```
安装了NodeJS、项目生成器、后台运行器
```

# 使用liuzy或普通用户登陆

- 创建项目

```shell
# 新建nodejs项目test
express test -e
# 进入项目目录
cd test
# 安装项目依赖
npm install
# 编辑文件，使访问/时，返回一个JSON字符串
vim routes/index.js
# 删除或用//注释res.render('index', { title: 'Express' });
# 改为
res.send({name:"liuzy",QQ:"416657468"});
```

- 启动项目

```shell
cd ~
forever start -o test.log test/bin/www
# 看是否运行，项目默认访问端口是3000
lsof -i :3000
# 用root配置nginx
```

- 查看日志

```shell
cd ~
tail -f test.log
```

- 访问项目

```
浏览器打开http://你主机的IP:3000
会看到服务器返回了一个JSON，内容是：
{"name":"liuzy","QQ":"416657468"}
```

# 配置Nginx

- 编辑/etc/nginx/nginx.conf，在http的大括号内的最后加上配置

```
server {
	listen 80;
	server_name liuzy.com www.liuzy.com;
	location / {
		rewrite (.*) https://www.liuzy.com$1 permanent;
	}
}
server {
	listen 443 ssl;
	server_name liuzy.com www.liuzy.com;
	ssl_certificate /etc/pki/CA/myssl/nginx.crt;
	ssl_certificate_key /etc/pki/CA/myssl/nginx.pem;
	#双向认证配置，现在注释
	#ssl_client_certificate /etc/pki/CA/cacert.pem;
	#ssl_verify_client on;
	location / {
		proxy_set_header Host $host;
		proxy_set_header X-Real-Ip $remote_addr;
		proxy_set_header X-Forwarded-For $remote_addr;
		proxy_pass http://127.0.0.1:3000;
		client_max_body_size 20m;
	}
}
```

- 测试配置是否正确，正确会输出successful

`nginx -t`
- 应用配置，没有任何返回信息说明成功了

`nginx -s reload`

# 修改host

- 修改本机Windows系统的host文件

```
(如果你的win8,win10，你先把hosts复制到桌面或项目地方，改好了再拖进来)
打开`C:\Windows\System32\drivers\etc`文件夹
编辑hosts，添加内容：(前面地址是你虚拟机的IP)
192.168.31.186 liuzy.com
192.168.31.186 www.liuzy.com
```

- 测试修改

```
打开cmd，输入ping liuzy.com，观察是否转到虚拟机IP
```

# 访问项目

- 这时，你用浏览器访问`http://liuzy.com`或者`http://www.liuzy.com`，请求会被系统DNS到你的虚拟机
- 虚拟机中的nginx收到请求，会将你的地址重定向到`https://www.liuzy.com`
- 然后转发到虚拟机的3000端口，NodeJS项目test会给你返回一个JSON字符串
- 到此已经完成了单向认证，双向认证请继续

# 双向认证

- 打开双向认证

```
用root用户登陆，编辑/etc/nginx/nginx.conf
解开双向认证注释：
ssl_client_certificate /etc/pki/CA/cacert.pem;
ssl_verify_client on;
应用配置还需要：
nginx -s reload
```

- 此时用浏览器访问，你会看到：(因为你没有被服务器信任的证书)

```
No required SSL certificate was sent
```

- 用程序加载证书请求服务器

```
把create.sh脚本签发的客户端证书下载到你的系统，保存到E盘；
/etc/pki/CA/myssl/client.pem
/etc/pki/CA/myssl/client.crt
/etc/pki/CA/myssl/nginx.crt
运行com.liuzy.test.HttpsTest类的main方法；
看，返回了结果！
```

# -----------------------------------------------------

# Java实现私钥、公钥、证书、证书请求，CA中心、证书签发、对象转文件、文件转对象等等

- 需要导入包
- bcpkix-jdk15on-1.54.jar
- bcprov-jdk15on-1.54.jar
- 运行com.liuzy.test.CaTest类的main方法看效果

# 输出me的私钥和pkcs8私钥
```java
KeyUtils.write2RsaKey(privateKey, testDir + "me.pem");
KeyUtils.write2PKCS8Key(privateKey, testDir + "me_pkcs8.pem");
```

# 生成me的证书请求文件
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

# 初始化证书中心，输CA私钥和CA自签证书，并使用me的公钥和me的信息给他签发证书
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

# 用CA私钥和CA自签证书初始化证书中心，并用me的证书请求给他签发证书
```java
// CA初始化2
CACenter caCenter = new CACenter();
caCenter.init(testDir + "ca.crt", testDir + "ca.pem");

// 签发2 根据证书请求文件
CertificationRequest csr = CertReqUtils.read(testDir + "me.csr");
Certificate newcert2 = caCenter.sign(csr);
CertUtils.write(newcert2, testDir + "newcert2.cer");
```

# 读证书，打印证书信息
```java
// 读证书
Certificate cert = CertUtils.read(testDir + "newcert1.cer");
// 查看证书信息
CertUtils.print((X509Certificate) cert);
// 验证证书
System.out.println(CertUtils.verify(cert));
```

