package com.liuzy.ui;

import java.io.File;
import java.text.SimpleDateFormat;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.liuzy.ca.CACenter;

public class Main {
	static CACenter CA = null;
	static String outDir = "D:/KS/";
	static boolean isFormFile = true;
	static String caCertKsPwd = "123456";
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static Display display;
	private static Shell shell;
	private static Text textCaCertFile;
	private static Text textCaPemFile;
	private static Text txtDN;
	private static Text text_3;
	private static Text tcaSF;
	private static Text tcaSJ;
	private static Text tcaXH;
	private static Text text_6;
	private static Text text_7;
	private static Text text_8;
	private static Text text_9;
	private static Text text_10;
	private static Text text_11;
	private static Text tcaC;
	private static Text tcaST;
	private static Text tcaL;
	private static Text tcaO;
	private static Text tcaOU;
	private static Text tcaCN;
	private static Text text_17;
	private static Text tcaE;
	private static Text text;

	public static void main(String[] args) {
		display = Display.getDefault();

		shell = new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setModified(true);
		shell.setText("JAVA证书签发工具");
		shell.setSize(600, 400);
		shell.setLocation(display.getClientArea().width / 2 - shell.getShell().getSize().x / 2, display.getClientArea().height / 2 - shell.getSize().y / 2);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

		TabItem tabStart = new TabItem(tabFolder, SWT.NONE);
		tabStart.setText(" 开 始 ");

		Composite compositeStart = new Composite(tabFolder, SWT.NONE);
		tabStart.setControl(compositeStart);

		Label lblca = new Label(compositeStart, SWT.NONE);
		lblca.setBounds(44, 27, 164, 17);
		lblca.setText("创建CA中心，请选择：");

		final TabFolder tabFolder_1 = new TabFolder(compositeStart, SWT.NONE);
		tabFolder_1.setBounds(44, 61, 500, 198);

		final TabItem tabFormFile = new TabItem(tabFolder_1, SWT.NONE);
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
				FileDialog fileOpen = new FileDialog(shell, SWT.OPEN);
				fileOpen.setFilterNames(new String[]{"证书文件(*.crt;*.cer;*.pem)", "所有文件(*.*)"});
				fileOpen.setFilterExtensions(new String[]{"*.crt;*.cer;*.pem", "*.*"});
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
				FileDialog fileOpen = new FileDialog(shell, SWT.OPEN);
				fileOpen.setFilterNames(new String[]{"私钥文件(*.pem;*.key)", "所有文件(*.*)"});
				fileOpen.setFilterExtensions(new String[]{"*.pem;*.key", "*.*"});
				String path = fileOpen.open();
				if (path != null) {
					textCaPemFile.setText(path);
				}
			}
		});
		button_2.setText("选择...");
		button_2.setBounds(326, 92, 80, 27);

		final TabItem tabByHand = new TabItem(tabFolder_1, SWT.NONE);
		tabByHand.setText(" 手动创建 ");

		Composite compositeByHand = new Composite(tabFolder_1, SWT.NONE);
		tabByHand.setControl(compositeByHand);

		txtDN = new Text(compositeByHand, SWT.BORDER);
		txtDN.setText("CN=ca.com,OU=CA,O=证书签发中心,L=shanghai,ST=shanghai,C=cn");
		txtDN.setBounds(106, 12, 290, 23);

		Combo combo = new Combo(compositeByHand, SWT.NONE);
		combo.setItems(new String[] { "SHA1withRSA", "SHA256withRSA" });
		combo.setBounds(106, 51, 171, 25);
		combo.select(0);

		Label lblDn = new Label(compositeByHand, SWT.NONE);
		lblDn.setBounds(39, 15, 61, 17);
		lblDn.setText("DN");

		Label label = new Label(compositeByHand, SWT.NONE);
		label.setBounds(39, 54, 61, 17);
		label.setText("签名算法");

		Button btnNewButton = new Button(compositeByHand, SWT.NONE);
		btnNewButton.setBounds(402, 10, 51, 27);
		btnNewButton.setText("...");

		text_3 = new Text(compositeByHand, SWT.BORDER);
		text_3.setText("2048");
		text_3.setBounds(106, 93, 73, 23);

		Label label_1 = new Label(compositeByHand, SWT.NONE);
		label_1.setBounds(39, 96, 61, 17);
		label_1.setText("密钥长度");

		Label label_3 = new Label(compositeByHand, SWT.NONE);
		label_3.setText("有效期");
		label_3.setBounds(39, 138, 61, 17);

		Combo combo_1 = new Combo(compositeByHand, SWT.NONE);
		combo_1.setItems(new String[] { "1年", "5年", "10年", "20年" });
		combo_1.setBounds(106, 130, 73, 25);
		combo_1.select(2);

		Button button = new Button(compositeStart, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (tabFolder_1.getSelectionIndex() == 0) {
						String caCertFile = textCaCertFile.getText();
						String caPemFile = textCaPemFile.getText();
						if (caCertFile.length() > 3 && caPemFile.length() > 3) {
							CA = new CACenter();
							CA.init(caCertFile, caPemFile);
						}
					} else {
						CA = new CACenter(txtDN.getText());
						CA.init();
					}
					if (CA != null) {
						// 切换到CA中心
						tabFolder.setSelection(1);
						String dn = CA.getIssuerDN();
						for (String kvs : dn.split(",")) {
							String[] kv = kvs.split("=");
							String k = kv[0].trim();
							String v = kv[1].trim();
							switch (k) {
							case "CN":tcaCN.setText(v);break;
							case "OU":tcaOU.setText(v);break;
							case "O":tcaO.setText(v);break;
							case "L":tcaL.setText(v);break;
							case "ST":tcaST.setText(v);break;
							case "C":tcaC.setText(v);break;
							case "E":tcaE.setText(v);break;
							default:break;
							}
						}
						tcaSF.setText(CA.getSignatureAlgorithm());
						tcaXH.setText("0" + CA.getCacert().getSerialNumber().intValue());
						String begin = sdf.format(CA.getCacert().getNotBefore());
						String end = sdf.format(CA.getCacert().getNotAfter());
						tcaSJ.setText(begin + "~" + end);
					}
				} catch (Exception e2) {
					System.out.println("初始化CA异常");
					e2.printStackTrace();
				}
			}
		});
		button.setBounds(464, 275, 80, 27);
		button.setText(" 下 一 步 ");

		TabItem tabCa = new TabItem(tabFolder, SWT.NONE);
		tabCa.setText(" CA 中 心 ");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabCa.setControl(composite);

		Group grpCA = new Group(composite, SWT.NONE);
		grpCA.setText(" C A ");
		grpCA.setBounds(10, 10, 566, 228);

		Button btnCaSaveKey = new Button(grpCA, SWT.NONE);
		btnCaSaveKey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CA.saveRsaKey(outDir + "ca.pem");
			}
		});
		btnCaSaveKey.setBounds(205, 120, 108, 27);
		btnCaSaveKey.setText("保存私钥");

		Button btnCaSavePkcs8Key = new Button(grpCA, SWT.NONE);
		btnCaSavePkcs8Key.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CA.savePkcs8Key(outDir + "ca_pkcs8.pem");
			}
		});
		btnCaSavePkcs8Key.setText("保存PKCS8私钥");
		btnCaSavePkcs8Key.setBounds(205, 166, 108, 27);

		Button btnCaSaveCert = new Button(grpCA, SWT.NONE);
		btnCaSaveCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CA.saveCert(outDir + "ca.crt");
			}
		});
		btnCaSaveCert.setText("保存证书");
		btnCaSaveCert.setBounds(319, 120, 108, 27);

		Button btnCaSaveCert2Ks = new Button(grpCA, SWT.NONE);
		btnCaSaveCert2Ks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("11111111111111111111111");
				show(shell, "sdfsdfs");
				System.out.println("222222222222222");
				CA.saveCert2Ks("ca", caCertKsPwd, outDir + "ca_cert.jks");
			}
		});
		btnCaSaveCert2Ks.setText("保存JKS证书");
		btnCaSaveCert2Ks.setBounds(319, 166, 108, 27);

		Button btnCaSaveAll = new Button(grpCA, SWT.NONE);
		btnCaSaveAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CA.saveRsaKey(outDir + "ca.pem");
				CA.savePkcs8Key(outDir + "ca_pkcs8.pem");
				CA.saveCert(outDir + "ca.crt");
				CA.saveCert2Ks("ca", "123456", outDir + "ca_cert.jks");
			}
		});
		btnCaSaveAll.setText("保存所有");
		btnCaSaveAll.setBounds(448, 166, 108, 27);

		tcaSF = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		tcaSF.setBounds(205, 21, 204, 23);

		tcaSJ = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		tcaSJ.setBounds(205, 50, 187, 23);

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

		Group group = new Group(composite, SWT.NONE);
		group.setText(" 签 发");
		group.setBounds(10, 244, 566, 88);

		Button btnCaSignNginx = new Button(group, SWT.NONE);
		btnCaSignNginx.setBounds(46, 20, 108, 27);
		btnCaSignNginx.setText("签发Nginx");

		Button btnCaSignApache = new Button(group, SWT.NONE);
		btnCaSignApache.setBounds(46, 53, 108, 27);
		btnCaSignApache.setText("签发Apache");

		Button btnCaSignTomcat = new Button(group, SWT.NONE);
		btnCaSignTomcat.setBounds(170, 20, 108, 27);
		btnCaSignTomcat.setText("签发Tomcat");

		Button btnCaSignClient = new Button(group, SWT.NONE);
		btnCaSignClient.setBounds(170, 51, 108, 27);
		btnCaSignClient.setText("签发客户端");

		Button button_4 = new Button(group, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tabFolder.setSelection(2);
			}
		});
		button_4.setBounds(363, 53, 108, 27);
		button_4.setText("手动签发");

		Button btnOpenCsr = new Button(group, SWT.NONE);
		btnOpenCsr.setText("打开证书请求文件");
		btnOpenCsr.setBounds(363, 20, 136, 27);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(" 手 动 签 发 ");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);

		Button button_3 = new Button(composite_1, SWT.NONE);
		button_3.setBounds(427, 281, 80, 27);
		button_3.setText(" 签 发 ");

		Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setText(" 信 息 ");
		group_1.setBounds(39, 10, 490, 254);

		text_6 = new Text(group_1, SWT.BORDER);
		text_6.setBounds(53, 33, 136, 23);

		text_7 = new Text(group_1, SWT.BORDER);
		text_7.setBounds(53, 62, 136, 23);

		text_8 = new Text(group_1, SWT.BORDER);
		text_8.setBounds(53, 91, 136, 23);

		text_9 = new Text(group_1, SWT.BORDER);
		text_9.setBounds(53, 120, 100, 23);

		text_10 = new Text(group_1, SWT.BORDER);
		text_10.setBounds(53, 149, 100, 23);

		text_11 = new Text(group_1, SWT.BORDER);
		text_11.setBounds(53, 178, 73, 23);

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

		Combo combo_2 = new Combo(group_1, SWT.NONE);
		combo_2.setBounds(292, 33, 148, 25);
		combo_2.setItems(new String[] { "SHA1withRSA", "SHA256withRSA" });
		combo_2.select(0);

		text_17 = new Text(group_1, SWT.BORDER);
		text_17.setBounds(292, 62, 73, 23);
		text_17.setText("2048");

		Label label_9 = new Label(group_1, SWT.NONE);
		label_9.setBounds(225, 36, 61, 17);
		label_9.setText("签名算法");

		Label label_10 = new Label(group_1, SWT.NONE);
		label_10.setBounds(225, 65, 61, 17);
		label_10.setText("密钥长度");
		
		text = new Text(group_1, SWT.BORDER);
		text.setBounds(53, 207, 73, 23);
		
		Label label_11 = new Label(group_1, SWT.RIGHT);
		label_11.setText("E");
		label_11.setBounds(19, 210, 28, 17);

		TabItem tabSet = new TabItem(tabFolder, SWT.NONE);
		tabSet.setText(" 工 具 ");

		File out = new File(outDir);
		if (!out.isDirectory()) {
			out.mkdirs();
		}
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	static void show(Shell shell, String msg) {
		final Shell dialog = new Shell(shell, SWT.PRIMARY_MODAL);
		dialog.setSize(200, 100);
		dialog.setLocation(shell.getLocation().x + dialog.getSize().x / 2, shell.getLocation().y + dialog.getSize().y / 2);
		
		Label label = new Label(dialog, SWT.NONE);
		label.setBounds(21, 10, 61, 17);
		label.setText("输入密码");
		
		text = new Text(dialog, SWT.BORDER | SWT.PASSWORD);
		text.setBounds(20, 34, 168, 23);
		text.setText("123456");
		
		Button btnNewButton = new Button(dialog, SWT.NONE);
		btnNewButton.setBounds(108, 61, 80, 27);
		btnNewButton.setText("确定");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				caCertKsPwd = text.getText();
				dialog.dispose();
			}
		});
		
		dialog.open();
		dialog.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
