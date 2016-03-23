package com.liuzy.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;

public class Main {
	private static Display display;
	private static Shell shell;
	private static Text text;
	private static Text text_1;
	private static Text txtCncacomoucaolshanghaistshanghaiccn;
	private static Text text_3;
	private static Text txtShawithrsa;
	private static Text text_4;
	private static Text text_5;
	private static Text text_6;
	private static Text text_7;
	private static Text text_8;
	private static Text text_9;
	private static Text text_10;
	private static Text text_11;
	private static Text text_2;
	private static Text text_12;
	private static Text text_13;
	private static Text text_14;
	private static Text text_15;
	private static Text text_16;
	private static Text text_17;

	public static void main(String[] args) {
		display = Display.getDefault();

		shell = new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setModified(true);
		shell.setText("KS");
		shell.setSize(600, 400);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

		TabItem tabStart = new TabItem(tabFolder, SWT.NONE);
		tabStart.setText(" 开 始 ");

		Composite compositeStart = new Composite(tabFolder, SWT.NONE);
		tabStart.setControl(compositeStart);

		Label lblca = new Label(compositeStart, SWT.NONE);
		lblca.setBounds(44, 27, 164, 17);
		lblca.setText("创建CA中心，请选择：");

		TabFolder tabFolder_1 = new TabFolder(compositeStart, SWT.NONE);
		tabFolder_1.setBounds(44, 61, 500, 198);

		TabItem tabFormFile = new TabItem(tabFolder_1, SWT.NONE);
		tabFormFile.setText(" 来自文件 ");

		Composite compositeFormFile = new Composite(tabFolder_1, SWT.NONE);
		tabFormFile.setControl(compositeFormFile);

		text = new Text(compositeFormFile, SWT.BORDER);
		text.setBounds(102, 39, 218, 23);

		text_1 = new Text(compositeFormFile, SWT.BORDER);
		text_1.setBounds(102, 96, 218, 23);

		Label lblCa = new Label(compositeFormFile, SWT.NONE);
		lblCa.setBounds(35, 42, 61, 17);
		lblCa.setText("CA证书");

		Label lblCa_1 = new Label(compositeFormFile, SWT.NONE);
		lblCa_1.setText("CA私钥");
		lblCa_1.setBounds(35, 99, 61, 17);

		Button button_1 = new Button(compositeFormFile, SWT.NONE);
		button_1.setBounds(326, 39, 80, 27);
		button_1.setText("选择...");

		Button button_2 = new Button(compositeFormFile, SWT.NONE);
		button_2.setText("选择...");
		button_2.setBounds(326, 92, 80, 27);

		TabItem tabByHand = new TabItem(tabFolder_1, SWT.NONE);
		tabByHand.setText(" 手动创建 ");

		Composite compositeByHand = new Composite(tabFolder_1, SWT.NONE);
		tabByHand.setControl(compositeByHand);

		txtCncacomoucaolshanghaistshanghaiccn = new Text(compositeByHand, SWT.BORDER);
		txtCncacomoucaolshanghaistshanghaiccn.setText("CN=ca.com,OU=CA,O=证书签发中心,L=shanghai,ST=shanghai,C=cn");
		txtCncacomoucaolshanghaistshanghaiccn.setBounds(106, 12, 290, 23);

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
		button.setBounds(464, 275, 80, 27);
		button.setText(" 下 一 步 ");

		TabItem tabCa = new TabItem(tabFolder, SWT.NONE);
		tabCa.setText(" CA 中 心 ");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabCa.setControl(composite);

		Group grpCA = new Group(composite, SWT.NONE);
		grpCA.setText(" C A ");
		grpCA.setBounds(10, 10, 566, 208);

		Button btnNewButton_1 = new Button(grpCA, SWT.NONE);
		btnNewButton_1.setBounds(205, 120, 108, 27);
		btnNewButton_1.setText("保存私钥");

		Button btnpkcs = new Button(grpCA, SWT.NONE);
		btnpkcs.setText("保存PKCS8私钥");
		btnpkcs.setBounds(205, 166, 108, 27);

		Button btnks = new Button(grpCA, SWT.NONE);
		btnks.setText("保存证书");
		btnks.setBounds(319, 120, 108, 27);

		Button btnks_1 = new Button(grpCA, SWT.NONE);
		btnks_1.setText("保存JKS证书");
		btnks_1.setBounds(319, 166, 108, 27);

		Button button_7 = new Button(grpCA, SWT.NONE);
		button_7.setText("保存所有");
		button_7.setBounds(448, 166, 108, 27);

		txtShawithrsa = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		txtShawithrsa.setText("SHA1withRSA");
		txtShawithrsa.setBounds(205, 23, 204, 23);

		text_4 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		text_4.setText("2016-3-23 ~ 2016-12-22");
		text_4.setBounds(205, 52, 187, 23);

		text_5 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
		text_5.setText("01");
		text_5.setBounds(432, 23, 73, 23);

		text_2 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		text_2.setBounds(44, 168, 73, 23);

		text_12 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		text_12.setBounds(44, 139, 100, 23);

		text_13 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		text_13.setBounds(44, 110, 100, 23);

		text_14 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		text_14.setBounds(44, 81, 136, 23);

		text_15 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		text_15.setBounds(44, 52, 136, 23);

		text_16 = new Text(grpCA, SWT.BORDER | SWT.READ_ONLY);
		text_16.setBounds(44, 23, 136, 23);

		Label label_2 = new Label(grpCA, SWT.RIGHT);
		label_2.setText("CN");
		label_2.setBounds(10, 26, 28, 17);

		Label label_4 = new Label(grpCA, SWT.RIGHT);
		label_4.setText("OU");
		label_4.setBounds(10, 55, 28, 17);

		Label label_5 = new Label(grpCA, SWT.RIGHT);
		label_5.setText("O");
		label_5.setBounds(10, 84, 28, 17);

		Label label_6 = new Label(grpCA, SWT.RIGHT);
		label_6.setText("L");
		label_6.setBounds(10, 113, 28, 17);

		Label label_7 = new Label(grpCA, SWT.RIGHT);
		label_7.setText("ST");
		label_7.setBounds(10, 142, 28, 17);

		Label label_8 = new Label(grpCA, SWT.RIGHT);
		label_8.setText("C");
		label_8.setBounds(10, 171, 28, 17);

		Group group = new Group(composite, SWT.NONE);
		group.setText(" 签 发");
		group.setBounds(10, 231, 566, 101);

		Button btnnginx = new Button(group, SWT.NONE);
		btnnginx.setBounds(10, 20, 108, 27);
		btnnginx.setText("签发Nginx");

		Button btnapache = new Button(group, SWT.NONE);
		btnapache.setBounds(10, 64, 108, 27);
		btnapache.setText("签发Apache");

		Button btntomcat = new Button(group, SWT.NONE);
		btntomcat.setBounds(134, 20, 108, 27);
		btntomcat.setText("签发Tomcat");

		Button button_5 = new Button(group, SWT.NONE);
		button_5.setBounds(134, 64, 108, 27);
		button_5.setText("签发客户端");

		Button button_4 = new Button(group, SWT.NONE);
		button_4.setBounds(373, 64, 108, 27);
		button_4.setText("手动签发");

		Button button_6 = new Button(group, SWT.NONE);
		button_6.setText("打开证书请求文件");
		button_6.setBounds(353, 20, 136, 27);

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

		TabItem tabSet = new TabItem(tabFolder, SWT.NONE);
		tabSet.setText(" 工 具 ");

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
