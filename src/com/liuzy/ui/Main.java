package com.liuzy.ui;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.liuzy.ca.CACenter;
import com.liuzy.ca.Subject;
import com.liuzy.http.KsManager;
import com.liuzy.utils.CertUtils;
import com.liuzy.utils.KeyUtils;
import com.liuzy.utils.KsUtils;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class Main {
	static CACenter CA = null;
	static String outDir = "D:/KS/";
	static boolean isFormFile = true;
	static String caCertKsPwd = "123456";
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Display display;
	private static Shell shlJavaLiuzy;
	private static Text textCaCertFile;
	private static Text textCaPemFile;
	private static Text txtDN;
	private static Text tcaSF;
	private static Text tcaSJ;
	private static Text tcaXH;
	private static Text txtSubjectCN;
	private static Text txtSubjectOU;
	private static Text txtSubjectO;
	private static Text txtSubjectL;
	private static Text txtSubjectST;
	private static Text txtSubjectC;
	private static Text tcaC;
	private static Text tcaST;
	private static Text tcaL;
	private static Text tcaO;
	private static Text tcaOU;
	private static Text tcaCN;
	private static Text tcaE;
	private static Text txtSubjectE;
	private static Button button;
	private static TabFolder tabFolder;
	private static TabFolder tabFolder_1;
	private static TabItem tabFormFile;
	private static TabItem tabByHand;
	private static TabItem tabStart;
	private static Text txtDisplay;
	private static Text txtCerFile;
	private static Text txtServer;
	private static Text text_12;
	private static Text text_13;
	private static Text text_15;
	private static Text txtClient;
	private static Text text_16;
	private static Text text_2;
	private static Button btnCaSignClient;
	private static Button btnCaSignNginx;
	private static Button btnCaSignTomcat;
	private static Button btnCaSignApache;
	private static Button btnCertOutJks;
	private static Button btnCertOutBks;
	private static Button btnKeyOutJks;
	private static Button btnKeyOutBks;
	private static Button btnKeyOutP12;
	private static Button btnCaSaveCert;
	private static Button btnCaSaveCert2Ks;
	private static Button btnCaSaveAll;
	private static Button btnbks_1;
	private static Button btnCaSaveKey;
	private static Button btnCaSavePkcs8Key;
	private static Combo comboCAsf;
	private static Combo comboCAkeylength;
	private static Combo comboCAyear;
	static KeyStore ks;
	private static Text txtKsFile;
	private static Text txtKsPwd;
	private static Button btnKsShow;
	private static Combo comboAlias;
	private static Text txtKeyPwd;

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		display = Display.getDefault();

		shlJavaLiuzy = new Shell(display, SWT.CLOSE | SWT.MIN);
		shlJavaLiuzy.setModified(true);
		shlJavaLiuzy.setText("JAVA证书签发和证书库工具 —— Liuzy制作 QQ416657468");
		shlJavaLiuzy.setSize(600, 400);
		shlJavaLiuzy.setLocation(display.getClientArea().width / 2 - shlJavaLiuzy.getShell().getSize().x / 2, display.getClientArea().height / 2 - shlJavaLiuzy.getSize().y / 2);
		shlJavaLiuzy.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabFolder = new TabFolder(shlJavaLiuzy, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch (tabFolder.getSelectionIndex()) {
				case 0:
					CA = null;
					if (button != null) {
						button.setEnabled(true);
						tabStart.setText(" 开 始 ");
					}
					break;
				case 1:
				case 2:
					if (CA == null) {
						tabFolder.setSelection(0);
					}
					break;
				case 3:
					break;
				}
			}
		});

		tabStart = new TabItem(tabFolder, SWT.NONE);
		tabStart.setText(" 开 始 ");

		Composite compositeStart = new Composite(tabFolder, SWT.NONE);
		tabStart.setControl(compositeStart);

		Label lblca = new Label(compositeStart, SWT.NONE);
		lblca.setBounds(44, 27, 164, 17);
		lblca.setText("创建CA中心，请选择：");

		tabFolder_1 = new TabFolder(compositeStart, SWT.NONE);
		tabFolder_1.setBounds(44, 61, 500, 198);

		tabFormFile = new TabItem(tabFolder_1, SWT.NONE);
		tabFormFile.setText(" 来自文件 ");

		Composite compositeFormFile = new Composite(tabFolder_1, SWT.NONE);
		tabFormFile.setControl(compositeFormFile);

		textCaCertFile = new Text(compositeFormFile, SWT.BORDER);
		textCaCertFile.setBounds(102, 39, 218, 23);

		textCaPemFile = new Text(compositeFormFile, SWT.BORDER);
		textCaPemFile.setBounds(102, 96, 218, 23);

		Label lblCa = new Label(compositeFormFile, SWT.NONE);
		lblCa.setBounds(35, 42, 61, 17);
		lblCa.setText("CA证书");

		Label lblCa_1 = new Label(compositeFormFile, SWT.NONE);
		lblCa_1.setText("CA私钥");
		lblCa_1.setBounds(35, 99, 61, 17);

		Button button_1 = new Button(compositeFormFile, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileOpen = new FileDialog(shlJavaLiuzy, SWT.OPEN);
				fileOpen.setFilterNames(new String[] { "证书文件(*.crt;*.cer;*.pem)", "所有文件(*.*)" });
				fileOpen.setFilterExtensions(new String[] { "*.crt;*.cer;*.pem", "*.*" });
				String path = fileOpen.open();
				if (path != null) {
					textCaCertFile.setText(path);
				}
			}
		});
		button_1.setBounds(326, 39, 80, 27);
		button_1.setText("选择...");

		Button button_2 = new Button(compositeFormFile, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileOpen = new FileDialog(shlJavaLiuzy, SWT.OPEN);
				fileOpen.setFilterNames(new String[] { "私钥文件(*.pem;*.key)", "所有文件(*.*)" });
				fileOpen.setFilterExtensions(new String[] { "*.pem;*.key", "*.*" });
				String path = fileOpen.open();
				if (path != null) {
					textCaPemFile.setText(path);
				}
			}
		});
		button_2.setText("选择...");
		button_2.setBounds(326, 92, 80, 27);

		tabByHand = new TabItem(tabFolder_1, SWT.NONE);
		tabByHand.setText(" 自定义 ");

		Composite compositeByHand = new Composite(tabFolder_1, SWT.NONE);
		tabByHand.setControl(compositeByHand);

		txtDN = new Text(compositeByHand, SWT.BORDER);
		txtDN.setText("CN=ca.com,OU=CA,O=证书签发中心,L=shanghai,ST=shanghai,C=cn");
		txtDN.setBounds(106, 12, 290, 23);

		comboCAsf = new Combo(compositeByHand, SWT.NONE);
		comboCAsf.setItems(new String[] { "SHA1withRSA", "SHA256withRSA" });
		comboCAsf.setBounds(106, 51, 171, 25);
		comboCAsf.select(0);

		Label lblDn = new Label(compositeByHand, SWT.NONE);
		lblDn.setBounds(39, 15, 61, 17);
		lblDn.setText("DN");

		Label label = new Label(compositeByHand, SWT.NONE);
		label.setBounds(39, 54, 61, 17);
		label.setText("签名算法");

		Button btnNewButton = new Button(compositeByHand, SWT.NONE);
		btnNewButton.setBounds(402, 10, 51, 27);
		btnNewButton.setText("...");

		Label label_1 = new Label(compositeByHand, SWT.NONE);
		label_1.setBounds(39, 96, 61, 17);
		label_1.setText("密钥长度");

		Label label_3 = new Label(compositeByHand, SWT.NONE);
		label_3.setText("有效期");
		label_3.setBounds(39, 138, 61, 17);

		comboCAyear = new Combo(compositeByHand, SWT.NONE);
		comboCAyear.setItems(new String[] { "1年", "5年", "10年", "20年" });
		comboCAyear.setBounds(106, 130, 73, 25);
		comboCAyear.select(2);

		comboCAkeylength = new Combo(compositeByHand, SWT.NONE);
		comboCAkeylength.setItems(new String[] { "1024", "2048", "4096" });
		comboCAkeylength.setBounds(106, 93, 73, 25);
		comboCAkeylength.select(1);

		button = new Button(compositeStart, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				button.setEnabled(false);
				try {
					if (tabFolder_1.getSelectionIndex() == 0) {
						String caCertFile = textCaCertFile.getText();
						String caPemFile = textCaPemFile.getText();
						if (!isEmpty(caCertFile) && !isEmpty(caCertFile)) {
							CA = new CACenter(caCertFile, caPemFile);
						}
					} else {
						CACenter.year = Integer.parseInt(comboCAyear.getItem(comboCAyear.getSelectionIndex()).replace("年", ""));
						int keyLength = Integer.parseInt(comboCAkeylength.getItem(comboCAkeylength.getSelectionIndex()));
						String signatureAlgorithm = comboCAsf.getItem(comboCAsf.getSelectionIndex());
						CA = new CACenter(txtDN.getText(), keyLength, signatureAlgorithm);
//						CA.setSignatureAlgorithm(comboCAsf.getItem(comboCAsf.getSelectionIndex()));
//						CA.setKeyLength(Integer.parseInt(comboCAkeylength.getItem(comboCAkeylength.getSelectionIndex())));
					}
					if (CA != null) {
						tabStart.setText(" 重新开始 ");
						tabFolder.setSelection(1);
						reloadCA();
					} else {
						button.setEnabled(true);
					}
				} catch (Exception e2) {
					alertMsg("文件或信息错误，初始化CA失败！");
					button.setEnabled(true);
					e2.printStackTrace();
				}
			}
		});
		button.setBounds(464, 275, 80, 27);
		button.setText(" 下 一 步 ");

		Button btnNewButton_1 = new Button(compositeStart, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Runtime.getRuntime().exec("cmd /c start https://git.oschina.net/liuzy1988/KS#第四部分-java证书签发窗口化工具");
				} catch (IOException e1) {
					try {
						Runtime.getRuntime().exec("cmd /c start https://git.oschina.net/liuzy1988/KS");
					} catch (Exception e2) {
					}
				}
			}
		});
		btnNewButton_1.setBounds(464, 22, 80, 27);
		btnNewButton_1.setText("访 问 官 网");

		TabItem tabCa = new TabItem(tabFolder, SWT.NONE);
		tabCa.setText(" CA 中 心 ");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabCa.setControl(composite);

		Group grpCA = new Group(composite, SWT.NONE);
		grpCA.setText(" C A ");
		grpCA.setBounds(10, 10, 566, 228);

		btnCaSaveKey = new Button(grpCA, SWT.NONE);
		btnCaSaveKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSaveKey.setEnabled(false);
				try {
					CA.saveRsaKey(outDir + "ca.pem");
					alertMsg("已保存到" + outDir + "ca.pem");
				} catch (IOException e2) {
					alertMsg("保存失败");
					e2.printStackTrace();
				}
				btnCaSaveKey.setEnabled(true);
			}
		});
		btnCaSaveKey.setBounds(205, 166, 108, 27);
		btnCaSaveKey.setText("保存私钥");

		btnCaSavePkcs8Key = new Button(grpCA, SWT.NONE);
		btnCaSavePkcs8Key.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSavePkcs8Key.setEnabled(false);
				try {
					CA.savePkcs8Key(outDir + "ca_pkcs8.pem");
					alertMsg("已保存到" + outDir + "ca_pkcs8.pem");
				} catch (IOException e2) {
					alertMsg("保存失败");
					e2.printStackTrace();
				}
				btnCaSavePkcs8Key.setEnabled(true);
			}
		});
		btnCaSavePkcs8Key.setText("保存PKCS8私钥");
		btnCaSavePkcs8Key.setBounds(319, 166, 108, 27);

		btnCaSaveCert = new Button(grpCA, SWT.NONE);
		btnCaSaveCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSaveCert.setEnabled(false);
				try {
					CA.saveCert(outDir + "ca.crt");
					alertMsg("已保存到" + outDir + "ca.crt");
				} catch (Exception e2) {
					alertMsg("保存失败");
					e2.printStackTrace();
				}
				btnCaSaveCert.setEnabled(true);
			}
		});
		btnCaSaveCert.setText("保存证书");
		btnCaSaveCert.setBounds(205, 120, 108, 27);

		btnCaSaveCert2Ks = new Button(grpCA, SWT.NONE);
		btnCaSaveCert2Ks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSaveCert2Ks.setEnabled(false);
				CA.saveCert2Jks("ca", caCertKsPwd, outDir + "ca_cert.jks");
				alertMsg("默认密码为123456\r\n已保存到" + outDir + "ca_cert.jks");
				btnCaSaveCert2Ks.setEnabled(true);
			}
		});
		btnCaSaveCert2Ks.setText("保存JKS证书");
		btnCaSaveCert2Ks.setBounds(319, 120, 108, 27);

		btnCaSaveAll = new Button(grpCA, SWT.NONE);
		btnCaSaveAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSaveAll.setEnabled(false);
				try {
					CA.saveRsaKey(outDir + "ca.pem");
					CA.savePkcs8Key(outDir + "ca_pkcs8.pem");
					CA.saveCert(outDir + "ca.crt");
					CA.saveCert2Jks("ca", "123456", outDir + "ca_cert.jks");
					CA.saveCert2Bks("ca", "123456", outDir + "ca_cert.bks");
					alertMsg("默认密码为123456\r\n已保存到" + outDir);
				} catch (Exception e2) {
					alertMsg("保存失败");
					e2.printStackTrace();
				}
				btnCaSaveAll.setEnabled(true);
			}
		});
		btnCaSaveAll.setText("保存所有");
		btnCaSaveAll.setBounds(433, 166, 108, 27);

		tcaSF = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		tcaSF.setBounds(205, 21, 204, 23);

		tcaSJ = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		tcaSJ.setBounds(205, 50, 300, 23);

		tcaXH = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
		tcaXH.setBounds(432, 21, 73, 23);

		tcaC = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		tcaC.setBounds(44, 166, 73, 23);

		tcaST = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		tcaST.setBounds(44, 137, 100, 23);

		tcaL = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		tcaL.setBounds(44, 108, 100, 23);

		tcaO = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		tcaO.setBounds(44, 79, 136, 23);

		tcaOU = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		tcaOU.setBounds(44, 50, 136, 23);

		tcaCN = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		tcaCN.setBounds(44, 21, 136, 23);

		Label label_2 = new Label(grpCA, SWT.RIGHT);
		label_2.setText("CN");
		label_2.setBounds(10, 24, 28, 17);

		Label label_4 = new Label(grpCA, SWT.RIGHT);
		label_4.setText("OU");
		label_4.setBounds(10, 53, 28, 17);

		Label label_5 = new Label(grpCA, SWT.RIGHT);
		label_5.setText("O");
		label_5.setBounds(10, 82, 28, 17);

		Label label_6 = new Label(grpCA, SWT.RIGHT);
		label_6.setText("L");
		label_6.setBounds(10, 111, 28, 17);

		Label label_7 = new Label(grpCA, SWT.RIGHT);
		label_7.setText("ST");
		label_7.setBounds(10, 140, 28, 17);

		Label label_8 = new Label(grpCA, SWT.RIGHT);
		label_8.setText("C");
		label_8.setBounds(10, 169, 28, 17);

		tcaE = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		tcaE.setBounds(44, 195, 73, 23);

		Label lblE = new Label(grpCA, SWT.RIGHT);
		lblE.setText("E");
		lblE.setBounds(10, 198, 28, 17);

		btnbks_1 = new Button(grpCA, SWT.NONE);
		btnbks_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnbks_1.setEnabled(false);
				CA.saveCert2Jks("ca", caCertKsPwd, outDir + "ca_cert.bks");
				alertMsg("默认密码为123456\r\n已保存到" + outDir + "ca_cert.bks");
				btnbks_1.setEnabled(true);
			}
		});
		btnbks_1.setText("保存BKS证书");
		btnbks_1.setBounds(433, 120, 108, 27);

		Group group = new Group(composite, SWT.NONE);
		group.setText(" 签 发");
		group.setBounds(10, 244, 566, 88);

		btnCaSignNginx = new Button(group, SWT.NONE);
		btnCaSignNginx.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSignNginx.setEnabled(false);
				try {
					Subject nginx = new Subject("CN=myserver.com,OU=nginx,O=liuzy,L=shanghai,ST=shanghai,C=cn");
					nginx.setCert(CA.sign(nginx.getPublicKey(), nginx.getSubjectDN()));
					nginx.saveCert(outDir + "nginx.crt");
					nginx.saveRsaKey(outDir + "nginx.pem");
					CA.saveCert(outDir + "nginx_trust.crt");
					alertMsg("默认密码为123456\r\n已保存到" + outDir);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				btnCaSignNginx.setEnabled(true);
			}
		});
		btnCaSignNginx.setBounds(46, 20, 108, 27);
		btnCaSignNginx.setText("签发Nginx");

		btnCaSignApache = new Button(group, SWT.NONE);
		btnCaSignApache.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSignApache.setEnabled(false);
				try {
					Subject apache = new Subject("CN=myserver.com,OU=apache,O=liuzy,L=shanghai,ST=shanghai,C=cn");
					apache.setCert(CA.sign(apache.getPublicKey(), apache.getSubjectDN()));
					apache.saveCert(outDir + "apache.crt");
					apache.saveRsaKey(outDir + "apache.pem");
					CA.saveCert(outDir + "apache_trust.crt");
					alertMsg("默认密码为123456\r\n已保存到" + outDir);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				btnCaSignApache.setEnabled(true);
			}
		});
		btnCaSignApache.setBounds(46, 53, 108, 27);
		btnCaSignApache.setText("签发Apache");

		btnCaSignTomcat = new Button(group, SWT.NONE);
		btnCaSignTomcat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCaSignTomcat.setEnabled(false);
				try {
					Subject tomcat = new Subject("CN=myserver.com,OU=tomcat,O=liuzy,L=shanghai,ST=shanghai,C=cn");
					tomcat.setCert(CA.sign(tomcat.getPublicKey(), tomcat.getSubjectDN()));
					tomcat.saveKey2Jks("tomcat", "123456", "123456", outDir + "tomcat.jks");
					CA.saveCert2Jks("server", "123456", outDir + "tomcat_trust.jks");
					alertMsg("默认密码为123456\r\n已保存到" + outDir);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				btnCaSignTomcat.setEnabled(true);
			}
		});
		btnCaSignTomcat.setBounds(170, 20, 108, 27);
		btnCaSignTomcat.setText("签发Tomcat");

		btnCaSignClient = new Button(group, SWT.NONE);
		btnCaSignClient.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnCaSignClient.setEnabled(false);
					Subject client = new Subject("CN=client,OU=client,O=liuzy,L=shanghai,ST=shanghai,C=cn");
					client.setCert(CA.sign(client.getPublicKey(), client.getSubjectDN()));
					client.saveCert(outDir + "client.crt");
					client.saveRsaKey(outDir + "client.pem");
					client.savePkcs8Key(outDir + "client_pkcs8.pem");
					client.saveKey2Jks("client", "123456", "123456", outDir + "client.jks");
					client.saveKey2Bks("client", "123456", "123456", outDir + "client.bks");
					client.saveKey2P12("client", "123456", "123456", outDir + "client.p12");
					CA.saveCert(outDir + "client_trust.crt");
					CA.saveCert2Jks("server", "123456", outDir + "client_trust.jks");
					CA.saveCert2Bks("server", "123456", outDir + "client_trust.bks");
					alertMsg("默认密码为123456\r\n已保存到" + outDir);
					btnCaSignClient.setEnabled(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnCaSignClient.setBounds(170, 51, 108, 27);
		btnCaSignClient.setText("签发客户端");

		Button btnShowCaSignHand = new Button(group, SWT.NONE);
		btnShowCaSignHand.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tabFolder.setSelection(2);
			}
		});
		btnShowCaSignHand.setBounds(390, 34, 108, 27);
		btnShowCaSignHand.setText("自定义签发");

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("自定义签发");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);

		Button btnCaSignCustom = new Button(composite_1, SWT.NONE);
		btnCaSignCustom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					String CN = txtSubjectCN.getText();
					String OU = txtSubjectOU.getText();
					String O = txtSubjectO.getText();
					String L = txtSubjectL.getText();
					String ST = txtSubjectST.getText();
					String C = txtSubjectC.getText();
					Subject custom = new Subject(String.format("CN=%s,OU=%s,O=%s,L=%s,ST=%s,C=%s", CN, OU, O, L, ST, C));
					custom.setCert(CA.sign(custom.getPublicKey(), custom.getSubjectDN()));
					custom.saveCert(outDir + "custom.crt");
					custom.saveRsaKey(outDir + "custom.pem");
					alertMsg("custom.crt和custom.pem已保存到" + outDir);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnCaSignCustom.setBounds(426, 281, 80, 27);
		btnCaSignCustom.setText(" 签 发 ");

		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setText(" 使用者信息 ");
		group_1.setBounds(39, 10, 490, 254);

		txtSubjectCN = new Text(group_1, SWT.BORDER);
		txtSubjectCN.setBounds(53, 33, 136, 23);

		txtSubjectOU = new Text(group_1, SWT.BORDER);
		txtSubjectOU.setBounds(53, 62, 136, 23);

		txtSubjectO = new Text(group_1, SWT.BORDER);
		txtSubjectO.setBounds(53, 91, 136, 23);

		txtSubjectL = new Text(group_1, SWT.BORDER);
		txtSubjectL.setText("shanghai");
		txtSubjectL.setBounds(53, 120, 100, 23);

		txtSubjectST = new Text(group_1, SWT.BORDER);
		txtSubjectST.setText("shanghai");
		txtSubjectST.setBounds(53, 149, 100, 23);

		txtSubjectC = new Text(group_1, SWT.BORDER);
		txtSubjectC.setText("CN");
		txtSubjectC.setBounds(53, 178, 51, 23);

		Label lblCn = new Label(group_1, SWT.RIGHT);
		lblCn.setBounds(19, 36, 28, 17);
		lblCn.setText("CN");

		Label lblOu = new Label(group_1, SWT.RIGHT);
		lblOu.setBounds(19, 65, 28, 17);
		lblOu.setText("OU");

		Label lblO = new Label(group_1, SWT.RIGHT);
		lblO.setBounds(19, 94, 28, 17);
		lblO.setText("O");

		Label lblL = new Label(group_1, SWT.RIGHT);
		lblL.setBounds(19, 123, 28, 17);
		lblL.setText("L");

		Label lblSt = new Label(group_1, SWT.RIGHT);
		lblSt.setBounds(19, 152, 28, 17);
		lblSt.setText("ST");

		Label lblC = new Label(group_1, SWT.RIGHT);
		lblC.setBounds(19, 181, 28, 17);
		lblC.setText("C");

		Combo comboSubjectSF = new Combo(group_1, SWT.NONE);
		comboSubjectSF.setBounds(292, 33, 148, 25);
		comboSubjectSF.setItems(new String[] { "SHA1withRSA", "SHA256withRSA" });
		comboSubjectSF.select(0);

		Label label_9 = new Label(group_1, SWT.NONE);
		label_9.setBounds(225, 36, 61, 17);
		label_9.setText("签名算法");

		Label label_10 = new Label(group_1, SWT.NONE);
		label_10.setBounds(225, 65, 61, 17);
		label_10.setText("密钥长度");

		txtSubjectE = new Text(group_1, SWT.BORDER);
		txtSubjectE.setBounds(53, 207, 136, 23);

		Label label_11 = new Label(group_1, SWT.RIGHT);
		label_11.setText("E");
		label_11.setBounds(19, 210, 28, 17);

		Combo combo_2 = new Combo(group_1, SWT.NONE);
		combo_2.setItems(new String[] { "1024", "2048", "4096" });
		combo_2.setBounds(292, 62, 73, 25);
		combo_2.select(1);

		Button button_6 = new Button(composite_1, SWT.NONE);
		button_6.setText("打开证书请求文件");
		button_6.setBounds(92, 281, 136, 27);

		TabItem tbKsConvert = new TabItem(tabFolder, SWT.NONE);
		tbKsConvert.setText("证书和密钥导入到库");

		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tbKsConvert.setControl(composite_4);

		Group grpssltrustkeystore = new Group(composite_4, SWT.NONE);
		grpssltrustkeystore.setText(" 生成证书库 (把服务器证书放入库中，对客户端来说称之为\"TrustKeyStore\")");
		grpssltrustkeystore.setBounds(10, 10, 566, 145);

		txtCerFile = new Text(grpssltrustkeystore, SWT.BORDER | SWT.READ_ONLY);
		txtCerFile.setBounds(92, 26, 154, 23);

		txtServer = new Text(grpssltrustkeystore, SWT.BORDER);
		txtServer.setText("server");
		txtServer.setBounds(92, 55, 154, 23);

		Label label_12 = new Label(grpssltrustkeystore, SWT.RIGHT);
		label_12.setBounds(36, 29, 50, 17);
		label_12.setText("证书文件");

		Button button_5 = new Button(grpssltrustkeystore, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileOpen = new FileDialog(shlJavaLiuzy, SWT.OPEN);
				fileOpen.setFilterNames(new String[] { "证书文件(*.crt;*.cer;*.pem)", "所有文件(*.*)" });
				fileOpen.setFilterExtensions(new String[] { "*.crt;*.cer;*.pem", "*.*" });
				String path = fileOpen.open();
				if (path != null) {
					txtCerFile.setText(path);
				}
			}
		});
		button_5.setBounds(252, 24, 36, 27);
		button_5.setText("打开");

		Label label_14 = new Label(grpssltrustkeystore, SWT.RIGHT);
		label_14.setText("设置别名");
		label_14.setBounds(36, 58, 50, 17);

		Label label_13 = new Label(grpssltrustkeystore, SWT.RIGHT);
		label_13.setText("库密码");
		label_13.setBounds(36, 87, 50, 17);

		text_2 = new Text(grpssltrustkeystore, SWT.BORDER | SWT.PASSWORD);
		text_2.setText("123456");
		text_2.setBounds(92, 84, 154, 23);

		btnCertOutJks = new Button(grpssltrustkeystore, SWT.NONE);
		btnCertOutJks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCertOutJks.setEnabled(false);
				try {
					String cerFile = txtCerFile.getText();
					String alias = txtServer.getText();
					String ksPwd = text_2.getText();
					if (!isEmpty(cerFile) && !isEmpty(alias) && !isEmpty(ksPwd)) {
						X509Certificate cert = CertUtils.read(cerFile);
						if (cert != null) {
							KsUtils.writeJks(cert, alias, ksPwd, outDir + "create_cert.jks");
							alertMsg("已保存到" + outDir + "create_cert.jks");
						}
					}
				} catch (Exception e2) {
					alertMsg("信息错误，保存失败！");
					e2.printStackTrace();
				}
				btnCertOutJks.setEnabled(true);
			}
		});
		btnCertOutJks.setText("保存JKS");
		btnCertOutJks.setBounds(390, 26, 108, 27);

		btnCertOutBks = new Button(grpssltrustkeystore, SWT.NONE);
		btnCertOutBks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCertOutBks.setEnabled(false);
				try {
					String cerFile = txtCerFile.getText();
					String alias = txtServer.getText();
					String ksPwd = text_2.getText();
					if (!isEmpty(cerFile) && !isEmpty(alias) && !isEmpty(ksPwd)) {
						X509Certificate cert = CertUtils.read(cerFile);
						if (cert != null) {
							KsUtils.writeBks(cert, alias, ksPwd, outDir + "create_cert.bks");
							alertMsg("已保存到" + outDir + "create_cert.bks");
						}
					}
				} catch (Exception e2) {
					alertMsg("信息错误，保存失败！");
					e2.printStackTrace();
				}
				btnCertOutBks.setEnabled(true);
			}
		});
		btnCertOutBks.setText("保存BKS");
		btnCertOutBks.setBounds(390, 89, 108, 27);

		Group grpkeystore = new Group(composite_4, SWT.NONE);
		grpkeystore.setText(" 生成密钥库 (把客户端的证书和密钥放入库中，对客户端来说称之为\"KeyStore\")");
		grpkeystore.setBounds(10, 161, 566, 171);

		Label label_16 = new Label(grpkeystore, SWT.RIGHT);
		label_16.setText("证书文件");
		label_16.setBounds(36, 27, 50, 17);

		text_12 = new Text(grpkeystore, SWT.BORDER | SWT.READ_ONLY);
		text_12.setBounds(92, 24, 154, 23);

		Button button_7 = new Button(grpkeystore, SWT.NONE);
		button_7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileOpen = new FileDialog(shlJavaLiuzy, SWT.OPEN);
				fileOpen.setFilterNames(new String[] { "证书文件(*.crt;*.cer;*.pem)", "所有文件(*.*)" });
				fileOpen.setFilterExtensions(new String[] { "*.crt;*.cer;*.pem", "*.*" });
				String path = fileOpen.open();
				if (path != null) {
					text_12.setText(path);
				}
			}
		});
		button_7.setText("打开");
		button_7.setBounds(252, 22, 36, 27);

		Label label_17 = new Label(grpkeystore, SWT.RIGHT);
		label_17.setText("密钥文件");
		label_17.setBounds(36, 54, 50, 17);

		text_13 = new Text(grpkeystore, SWT.BORDER | SWT.READ_ONLY);
		text_13.setBounds(92, 52, 154, 23);

		Button button_8 = new Button(grpkeystore, SWT.NONE);
		button_8.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileOpen = new FileDialog(shlJavaLiuzy, SWT.OPEN);
				fileOpen.setFilterNames(new String[] { "私钥文件(*.pem;*.key)", "所有文件(*.*)" });
				fileOpen.setFilterExtensions(new String[] { "*.pem;*.key", "*.*" });
				String path = fileOpen.open();
				if (path != null) {
					text_13.setText(path);
				}
			}
		});
		button_8.setText("打开");
		button_8.setBounds(252, 50, 36, 27);

		Label label_19 = new Label(grpkeystore, SWT.RIGHT);
		label_19.setText("密钥密码");
		label_19.setBounds(36, 113, 50, 17);

		text_15 = new Text(grpkeystore, SWT.BORDER | SWT.PASSWORD);
		text_15.setText("123456");
		text_15.setBounds(92, 110, 154, 23);

		txtClient = new Text(grpkeystore, SWT.BORDER);
		txtClient.setText("client");
		txtClient.setBounds(92, 81, 154, 23);

		Label label_18 = new Label(grpkeystore, SWT.RIGHT);
		label_18.setText("设置别名");
		label_18.setBounds(36, 84, 50, 17);

		Label label_20 = new Label(grpkeystore, SWT.RIGHT);
		label_20.setText("库密码");
		label_20.setBounds(36, 142, 50, 17);

		text_16 = new Text(grpkeystore, SWT.BORDER | SWT.PASSWORD);
		text_16.setText("123456");
		text_16.setBounds(92, 139, 154, 23);

		btnKeyOutJks = new Button(grpkeystore, SWT.NONE);
		btnKeyOutJks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnKeyOutJks.setEnabled(false);
				try {
					String cerFile = text_12.getText();
					String pemFile = text_13.getText();
					String alias = txtClient.getText();
					String keyPwd = text_15.getText();
					String ksPwd = text_16.getText();
					if (!isEmpty(cerFile) && !isEmpty(pemFile) && !isEmpty(alias) && !isEmpty(keyPwd) && !isEmpty(ksPwd)) {
						X509Certificate cert = CertUtils.read(cerFile);
						PrivateKey key = KeyUtils.read(pemFile).getPrivate();
						if (!KsManager.verify(cert.getPublicKey(), key)) {
							alertMsg("证书和密钥不匹配！");
						} else {
							if (cert != null || key == null) {
								KsUtils.writeJks(cert, alias, key, keyPwd, ksPwd, outDir + "create_key_cert.jks");
								alertMsg("已保存到" + outDir + "create_key_cert.jks");
							}
						}
					}
				} catch (Exception e2) {
					alertMsg("信息错误，保存失败！");
					e2.printStackTrace();
				}
				btnKeyOutJks.setEnabled(true);
			}
		});
		btnKeyOutJks.setText("保存JKS");
		btnKeyOutJks.setBounds(390, 27, 108, 27);

		btnKeyOutBks = new Button(grpkeystore, SWT.NONE);
		btnKeyOutBks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnKeyOutBks.setEnabled(false);
				try {
					String cerFile = text_12.getText();
					String pemFile = text_13.getText();
					String alias = txtClient.getText();
					String keyPwd = text_15.getText();
					String ksPwd = text_16.getText();
					if (!isEmpty(cerFile) && !isEmpty(pemFile) && !isEmpty(alias) && !isEmpty(keyPwd) && !isEmpty(ksPwd)) {
						X509Certificate cert = CertUtils.read(cerFile);
						PrivateKey key = KeyUtils.read(pemFile).getPrivate();
						if (!KsManager.verify(cert.getPublicKey(), key)) {
							alertMsg("证书和密钥不匹配！");
						} else {
							if (cert != null || key == null) {
								KsUtils.writeBks(cert, alias, key, keyPwd, ksPwd, outDir + "create_key_cert.bks");
								alertMsg("已保存到" + outDir + "create_key_cert.bks");
							}
						}
					}
				} catch (Exception e2) {
					alertMsg("信息错误，保存失败！");
					e2.printStackTrace();
				}
				btnKeyOutBks.setEnabled(true);
			}
		});
		btnKeyOutBks.setText("保存BKS");
		btnKeyOutBks.setBounds(390, 76, 108, 27);

		btnKeyOutP12 = new Button(grpkeystore, SWT.NONE);
		btnKeyOutP12.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnKeyOutBks.setEnabled(false);
				try {
					String cerFile = text_12.getText();
					String pemFile = text_13.getText();
					String alias = txtClient.getText();
					String keyPwd = text_15.getText();
					String ksPwd = text_16.getText();
					if (!isEmpty(cerFile) && !isEmpty(pemFile) && !isEmpty(alias) && !isEmpty(keyPwd) && !isEmpty(ksPwd)) {
						X509Certificate cert = CertUtils.read(cerFile);
						PrivateKey key = KeyUtils.read(pemFile).getPrivate();
						if (!KsManager.verify(cert.getPublicKey(), key)) {
							alertMsg("证书和密钥不匹配！");
						} else {
							if (cert != null || key == null) {
								KsUtils.writeP12(cert, alias, key, keyPwd, ksPwd, outDir + "create_key_cert.p12");
								alertMsg("已保存到" + outDir + "create_key_cert.p12");
							}
						}
					}
				} catch (Exception e2) {
					alertMsg("信息错误，保存失败！");
					e2.printStackTrace();
				}
				btnKeyOutBks.setEnabled(true);
			}
		});
		btnKeyOutP12.setText("保存P12");
		btnKeyOutP12.setBounds(390, 125, 108, 27);

		TabItem tbCreateKs = new TabItem(tabFolder, SWT.NONE);
		tbCreateKs.setText("从库导出证书和密钥");

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbCreateKs.setControl(composite_3);

		Group group_2 = new Group(composite_3, SWT.NONE);
		group_2.setText(" 选择文件 输入库密码");
		group_2.setBounds(10, 10, 566, 321);

		txtKsFile = new Text(group_2, SWT.BORDER | SWT.READ_ONLY);
		txtKsFile.setBounds(194, 33, 154, 23);

		Label label_15 = new Label(group_2, SWT.RIGHT);
		label_15.setText("库文件");
		label_15.setBounds(125, 36, 50, 17);

		Button button_3 = new Button(group_2, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileOpen = new FileDialog(shlJavaLiuzy, SWT.OPEN);
				fileOpen.setFilterNames(new String[] { "密钥库文件(*.keystore;*.jks;*.bks;*.p12)", "所有文件(*.*)" });
				fileOpen.setFilterExtensions(new String[] { "*.keystore;*.jks;*.bks;*.p12", "*.*" });
				String path = fileOpen.open();
				if (path != null) {
					txtKsFile.setText(path);
					reloadKs();
				}
			}
		});
		button_3.setText("选择");
		button_3.setBounds(368, 31, 36, 27);

		Label label_22 = new Label(group_2, SWT.RIGHT);
		label_22.setText("库密码");
		label_22.setBounds(125, 92, 50, 17);

		txtKsPwd = new Text(group_2, SWT.BORDER | SWT.PASSWORD);
		txtKsPwd.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String ksPwd = txtKsPwd.getText();
				if (txtKeyPwd != null && ksPwd.length() > 0) {
					reloadKs();
				}
			}
		});
		txtKsPwd.setText("123456");
		txtKsPwd.setBounds(194, 89, 154, 23);

		Button button_4 = new Button(group_2, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String alias = comboAlias.getItem(comboAlias.getSelectionIndex());
				String c = "", k = "";
				try {
					Certificate cert = ks.getCertificate(alias);
					if (cert instanceof X509Certificate) {
						String path = "D:/KS/ks_" + alias + ".crt";
						CertUtils.write((X509Certificate) cert, path);
						c = "别名为" + alias + "的证书已保存到" + path + "\n";
					}
				} catch (Exception e2) {
				}
				try {
					String keyPwd = txtKeyPwd.getText();
					if (keyPwd != null && !keyPwd.isEmpty()) {
						Key key = ks.getKey(alias, keyPwd.toCharArray());
						if (key != null) {
							String path = "D:/KS/ks_" + alias + ".pem";
							String path_pkcs8 = "D:/KS/ks_" + alias + "_pkcs8.pem";
							KeyUtils.write2RsaKey((PrivateKey) key, path);
							KeyUtils.write2PKCS8Key((PrivateKey) key, path_pkcs8);
							k = "别名为" + alias + "的密钥已保存到" + path + "和" + path_pkcs8 + "\n";
						}
					}
				} catch (Exception e2) {
				}
				alertMsg(c + k);
			}
		});
		button_4.setText(" 导 出 ");
		button_4.setBounds(194, 259, 154, 27);

		comboAlias = new Combo(group_2, SWT.READ_ONLY);
		comboAlias.setItems(new String[] {});
		comboAlias.setBounds(194, 145, 154, 25);

		Label label_23 = new Label(group_2, SWT.RIGHT);
		label_23.setText("密钥密码");
		label_23.setBounds(125, 204, 50, 17);

		Label label_24 = new Label(group_2, SWT.RIGHT);
		label_24.setText("选择别名");
		label_24.setBounds(125, 148, 50, 17);

		txtKeyPwd = new Text(group_2, SWT.BORDER | SWT.PASSWORD);
		txtKeyPwd.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (txtKsPwd != null && txtKeyPwd != null && txtDisplay != null && txtKsPwd.getText().length() > 0 && txtKeyPwd.getText().length() > 0) {
					reloadKs();
				}
			}
		});
		txtKeyPwd.setText("123456");
		txtKeyPwd.setBounds(194, 201, 154, 23);

		Label label_21 = new Label(group_2, SWT.RIGHT);
		label_21.setAlignment(SWT.LEFT);
		label_21.setText("（密码正确才能导出密钥）");
		label_21.setBounds(354, 204, 154, 17);

		btnKsShow = new Button(group_2, SWT.NONE);
		btnKsShow.setEnabled(false);
		btnKsShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tabFolder.setSelection(5);
			}
		});
		btnKsShow.setText("查看");
		btnKsShow.setBounds(368, 143, 36, 27);

		TabItem tabSet = new TabItem(tabFolder, SWT.NONE);
		tabSet.setText(" 查 看 ");

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tabSet.setControl(composite_2);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		txtDisplay = new Text(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtDisplay.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		txtDisplay.setText("");

		File out = new File(outDir);
		if (!out.isDirectory()) {
			out.mkdirs();
		}

		shlJavaLiuzy.open();
		shlJavaLiuzy.layout();
		while (!shlJavaLiuzy.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	// 从库导出证书和密钥
	static void reloadKs() {
		String ksFile = txtKsFile.getText();
		String ksPwd = txtKsPwd.getText();
		String keyPwd = txtKeyPwd.getText();
		try {
			ks = KsUtils.readP12(ksFile, ksPwd);
		} catch (Exception e) {
			try {
				ks = KsUtils.readJks(ksFile, ksPwd);
			} catch (Exception e2) {
				try {
					ks = KsUtils.readBks(ksFile, ksPwd);
				} catch (Exception e3) {
					ks = null;
				}
			}
		}
		comboAlias.removeAll();
		txtDisplay.setText("");
		btnKsShow.setEnabled(false);
		if (ks != null) {
			try {
				boolean flag = false;
				Enumeration<String> en = ks.aliases();
				while (en.hasMoreElements()) {
					flag = true;
					comboAlias.add(en.nextElement());
				}
				if (flag) {
					comboAlias.select(0);
					btnKsShow.setEnabled(true);
				}
				txtDisplay.setText(KsUtils.print(ks, keyPwd));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static void reloadCA() {
		tcaCN.setText("");
		tcaOU.setText("");
		tcaO.setText("");
		tcaL.setText("");
		tcaST.setText("");
		tcaC.setText("");
		tcaE.setText("");
		String dn = CA.getSubjectDN();
		for (String kvs : dn.split(",")) {
			String[] kv = kvs.split("=");
			String k = kv[0].trim();
			String v = kv[1].trim();
			switch (k) {
			case "CN":
				tcaCN.setText(v);
				break;
			case "OU":
				tcaOU.setText(v);
				break;
			case "O":
				tcaO.setText(v);
				break;
			case "L":
				tcaL.setText(v);
				break;
			case "ST":
				tcaST.setText(v);
				break;
			case "C":
				tcaC.setText(v);
				break;
			case "E":
				tcaE.setText(v);
				break;
			default:
				break;
			}
		}
		tcaSF.setText(CA.getSignatureAlgorithm());
		tcaXH.setText("0" + CA.getCert().getSerialNumber().intValue());
		String begin = sdf.format(CA.getCert().getNotBefore());
		String end = sdf.format(CA.getCert().getNotAfter());
		tcaSJ.setText(begin + " 至 " + end);
	}

	private static void alertMsg(String msg) {
		MessageBox box = new MessageBox(shlJavaLiuzy, SWT.PRIMARY_MODAL | SWT.OK);
		box.setText("消息");
		box.setMessage(msg);
		box.open();
	}

	private static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
}
