package custom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import burp.BurpExtender;
import burp.Config;
import burp.IHttpRequestResponse;
import burp.IMessageEditor;
import recon.IHandler;


public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private String github = "https://github.com/bit4woo/reCAPTCHA";
	private String helpurl = github;


	private JPanel panel_2;
	private JLabel lblNewLabel_2;
	private JSplitPane splitPane;
	private JSplitPane splitPane_1;
	private JSplitPane splitPane_2;
	public static JTextArea imgRequestRaws;
	public static JTextArea APIRequestRaws;
	private JPanel panel;
	private JPanel panel_1;
	public JButton btnRequest;
	private JButton btnRequestAPI;
	private JLabel label_showimg;
	public static JTextField imgHttpService;

	public IMessageEditor imageMessageEditor;
	public static JRadioButton rdbtnUseSelfApi;
	public static JRadioButton rdbtnUseProxy;
	public static JTextField proxyUrl = new JTextField();

	private JPanel panel_IMessage;
	private JPanel panel_4;
	private JTextArea APIResulttextArea;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_7;
	private JTextField imgPath;
	private JLabel lblAboutTypeid;
	private JComboBox<String> APIcomboBox;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 930, 497);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setDividerLocation(0.5);
		contentPane.add(splitPane, BorderLayout.CENTER);

		splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setResizeWeight(0.6);
		splitPane.setLeftComponent(splitPane_1);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		splitPane_1.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		panel_6 = new JPanel();
		panel.add(panel_6, BorderLayout.NORTH);

		rdbtnUseSelfApi = new JRadioButton("Use Self Api with proxy");
		rdbtnUseSelfApi.setSelected(false);
		panel_6.add(rdbtnUseSelfApi);

		panel_6.add(proxyUrl);
		proxyUrl.setText("http://127.0.0.1:8080");
		proxyUrl.setEnabled(false);

		rdbtnUseSelfApi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnUseSelfApi.isSelected()) {
					proxyUrl.setEnabled(true);
				}else {
					proxyUrl.setEnabled(false);
				}
			}
		});


		btnRequest = new JButton("Get Image");
		panel_6.add(btnRequest);
		btnRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GetImageThread thread = new GetImageThread();
				thread.start();
				//btnRequest.setEnabled(true);
				//java.lang.RuntimeException: Extensions should not make HTTP requests in the Swing event dispatch thread
				//https://support.portswigger.net/customer/portal/questions/16190306-burp-extensions-using-makehttprequest
			}
		});

		panel_7 = new JPanel();
		panel.add(panel_7, BorderLayout.CENTER);

		label_showimg = new JLabel("");
		panel_7.add(label_showimg);
		label_showimg.setHorizontalAlignment(SwingConstants.RIGHT);

		imgPath = new JTextField();
		panel_7.add(imgPath);
		imgPath.setColumns(30);


		panel_IMessage = new JPanel();
		panel_IMessage.setBorder(new LineBorder(new Color(0, 0, 0)));
		splitPane_1.setLeftComponent(panel_IMessage);
		panel_IMessage.setLayout(new BorderLayout(0, 0));

		imgHttpService = new JTextField();
		panel_IMessage.add(imgHttpService, BorderLayout.NORTH);
		imgHttpService.setHorizontalAlignment(SwingConstants.LEFT);
		imgHttpService.setColumns(30);

		imgRequestRaws = new JTextArea();
		panel_IMessage.add(imgRequestRaws, BorderLayout.CENTER);
		imgRequestRaws.setLineWrap(true);


		splitPane_2 = new JSplitPane();
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_2.setResizeWeight(0.6);
		splitPane.setRightComponent(splitPane_2);

		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		splitPane_2.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		APIResulttextArea = new JTextArea();
		APIResulttextArea.setWrapStyleWord(true);
		APIResulttextArea.setFont(new Font("ו", Font.BOLD, 12));//use this to display Chinese correctly.!!!
		panel_1.add(APIResulttextArea);

		panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.NORTH);

		rdbtnUseProxy = new JRadioButton("Use Proxy");
		rdbtnUseProxy.setSelected(false);
		panel_5.add(rdbtnUseProxy);

		btnRequestAPI = new JButton("Get Answer");
		btnRequestAPI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//				//GUI依然会被卡住
				//				SwingUtilities.invokeLater(new Runnable()
				//				{
				//					public void run()
				//					{
				//						String imgpath = imgPath.getText();
				//						String result = getAnswer(imgpath);
				//						APIResulttextArea.setText(result);
				//					}
				//				});
				btnRequestAPI.setEnabled(false);
				GetAnswerThread thread = new GetAnswerThread(imgPath.getText());
				thread.start();
			}
		});
		panel_5.add(btnRequestAPI);
		btnRequestAPI.setHorizontalAlignment(SwingConstants.LEFT);

		lblAboutTypeid = new JLabel("Help?");
		lblAboutTypeid.setHorizontalAlignment(SwingConstants.LEFT);
		lblAboutTypeid.setFont(new Font("ו", Font.BOLD, 12));
		lblAboutTypeid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					URI uri = new URI(helpurl);
					Desktop desktop = Desktop.getDesktop();
					if(Desktop.isDesktopSupported()&&desktop.isSupported(Desktop.Action.BROWSE)){
						desktop.browse(uri);
					}
				} catch (Exception e2) {
					//callbacks.printError(e2.getMessage());
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblAboutTypeid.setForeground(Color.BLUE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblAboutTypeid.setForeground(Color.BLACK);
			}
		});
		panel_5.add(lblAboutTypeid);


		panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		splitPane_2.setLeftComponent(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		APIRequestRaws = new JTextArea();
		panel_4.add(APIRequestRaws);
		APIRequestRaws.setLineWrap(true);

		APIcomboBox = new JComboBox<String>();
		APIcomboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				IHandler han = getHanlderInstance(APIcomboBox.getSelectedItem().toString());
				APIRequestRaws.setText(han.getExampleUserInput());
				helpurl = han.getHelpLink();
			}
		});
		panel_4.add(APIcomboBox, BorderLayout.NORTH);

		for (String item:getHanlderNames()) {
			APIcomboBox.addItem(item);
		}

		panel_2 = new JPanel();
				panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
				FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				contentPane.add(panel_2, BorderLayout.SOUTH);

				lblNewLabel_2 = new JLabel("    "+github);
				lblNewLabel_2.setFont(new Font("ו", Font.BOLD, 12));
				lblNewLabel_2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							URI uri = new URI(github);
							Desktop desktop = Desktop.getDesktop();
							if(Desktop.isDesktopSupported()&&desktop.isSupported(Desktop.Action.BROWSE)){
								desktop.browse(uri);
							}
						} catch (Exception e2) {
							//callbacks.printError(e2.getMessage());
						}

					}
					@Override
					public void mouseEntered(MouseEvent e) {
						lblNewLabel_2.setForeground(Color.BLUE);
					}
					@Override
					public void mouseExited(MouseEvent e) {
						lblNewLabel_2.setForeground(Color.BLACK);
					}
				});
				panel_2.add(lblNewLabel_2);
	}


	public String getAnswer(String imgpath) {
		IHandler handler = getHanlderInstance(APIcomboBox.getSelectedItem().toString());
		String result = "";
		String proxyUrl = "";
		if (GUI.rdbtnUseProxy.isSelected()) {
			proxyUrl = GUI.proxyUrl.getText().trim();
		}
		
		if(!imgpath.equals("")) {
			result = handler.getImageText(imgpath, APIRequestRaws.getText(),proxyUrl);
		} else {
			result = "image path is null!";
		}
		
		if (result == null){
			result = "null";
		}
		return result;
	}

	public class GetAnswerThread extends Thread {
		private String imgpath;

		public GetAnswerThread(String imgpath) {
			this.imgpath = imgpath;
		}
		public void run() {
			String imgpath = imgPath.getText();
			String result = getAnswer(imgpath);
			APIResulttextArea.setText(result);
			btnRequestAPI.setEnabled(true);
		}
	}

	public class GetImageThread extends Thread {
		public void run() {
			try {
				//java.lang.RuntimeException: Extensions should not make HTTP requests in the Swing event dispatch thread
				//https://support.portswigger.net/customer/portal/questions/16190306-burp-extensions-using-makehttprequest
				btnRequest.setEnabled(false);
				String imageName = BurpExtender.getImage(BurpExtender.config);
				imgPath.setText(imageName);

				//label_showimg.setIcon(new ImageIcon(imgpath));
				Image image = ImageIO.read(new File(imageName));
				ImageIcon icon = new ImageIcon(image);
				label_showimg.setIcon(icon);
			} catch (Exception e) {
				e.printStackTrace(BurpExtender.stderr);
				imgPath.setText(e.getMessage());
			} finally {
				btnRequest.setEnabled(true);
			}
		}
	}

	public List<String> getHanlderNames () {
		List<String> n = getClassNamesFromPackage("recon.");
		List<String> handlers  = new ArrayList<String>();
		for (String module : n) {
			try {
				Constructor<?> c = Class.forName("recon."+module).getConstructor();
				IHandler cm = (IHandler) c.newInstance();
				if (cm instanceof IHandler) {
					handlers.add(module);
				}
			}catch(Exception e) {

			}
		}
		return handlers;
	}
	
	public IHandler getHanlderInstance (String handlerClassName) {
		try {
			Constructor<?> c = Class.forName("recon."+handlerClassName).getConstructor();
			IHandler cm = (IHandler) c.newInstance();
			return cm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从jar包中获取某个package中的类名称
	 * @param packageName
	 * @return
	 */
	public ArrayList<String> getClassNamesFromPackage(String packageName) {
		ArrayList<String> names = new ArrayList<>();

		try {
			packageName = packageName.replace(".", "/");
			URL packageURL = getClass().getClassLoader().getResource(packageName);
			if (packageURL == null) {
				return names;
			}

			if (packageURL.getProtocol().equals("jar")) {

				String jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
				jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
				JarFile jf = new JarFile(jarFileName);
				Enumeration<JarEntry> jarEntries = jf.entries();

				while (jarEntries.hasMoreElements()) {
					String entryName = jarEntries.nextElement().getName();
					//callbacks.printError(entryName);
					if (entryName.startsWith(packageName) && !entryName.replace(packageName, "").contains("/") && entryName.length() > packageName.length()) {
						entryName = entryName.substring(packageName.length(), entryName.lastIndexOf('.'));
						names.add(entryName.replace("/", ""));
					}
				}
				jf.close();

				// loop through files in classpath
			} else {
				File folder = new File(packageURL.getFile());//error here sometimes
				File[] contents = folder.listFiles();
				String entryName;
				for (File actual : contents) {
					entryName = actual.getCanonicalPath();
					names.add(entryName);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return names;
	}
}