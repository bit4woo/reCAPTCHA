package custom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Image;
import java.awt.FlowLayout;
import javax.swing.JSplitPane;
import java.awt.Component;
import java.awt.Desktop;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import burp.BurpExtender;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IMessageEditor;
import burp.ITempFile;
import custom.RequestHelper;
import custom.myYunSu;



//////////////3 changes need to do, when debugging change to product!!!////////////////////

//change 1
//public class GUI extends JFrame { //change 1 for test
public class GUI extends JPanel {//change 1 for burp
	
    private String github = "https://github.com/bit4woo/reCAPTCHA";
    private String Abouttypeid = "http://www.ysdm.net/home/PriceType";

	private JPanel contentPane;
	private JPanel panel_2;
	private JLabel lblNewLabel_2;
	private JSplitPane splitPane;
	private JSplitPane splitPane_1;
	private JSplitPane splitPane_2;
	public JTextArea imgRequestRaws;
	public JTextArea APIRequestRaws;
	private JPanel panel;
	private JPanel panel_1;
	public JButton btnRequest;
	private JButton btnRequestAPI;
	private JLabel label_showimg;
	public JTextField imgHttpService;
	
	public IHttpRequestResponse MessageInfo;// always needed 
	public IMessageEditor imageMessageEditor;
	public IBurpExtenderCallbacks callbacks;//needed by method 2
	public IExtensionHelpers helpers;//needed by method 2
	public BurpExtender BurpExtender;//needed by method 3
	
	
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
	    
	    //change 2
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//change2 for test
		//setBounds(100, 100, 930, 497);//change2 for test
		contentPane = this; //change3 for burp
		//JPanel contentPane = new JPanel();////change3 for test
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		//setContentPane(contentPane);//change2 for test
		
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
		
		btnRequest = new JButton("Get Image");
		panel_6.add(btnRequest);
		
		panel_7 = new JPanel();
		panel.add(panel_7, BorderLayout.CENTER);
		
		label_showimg = new JLabel("");
		panel_7.add(label_showimg);
		label_showimg.setHorizontalAlignment(SwingConstants.RIGHT);
		
		imgPath = new JTextField();
		panel_7.add(imgPath);
		imgPath.setColumns(30);
		btnRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					//******method one: self-applied method RequestHelper*****
					//String httpservice =MessageInfo.getHttpService().toString();
					//String httpRaws =new String( MessageInfo.getRequest());
					String httpservice = imgHttpService.getText();
					String httpRaws = imgRequestRaws.getText();
					String imgpath = RequestHelper.download(httpservice, httpRaws);
					
					//******method two: imageDownloader*****
					//String imgpath = imageDownloader.download(callbacks, helpers, MessageInfo.getHttpService(), MessageInfo.getRequest());
					
					//******method three: getImage in BurpExtender*****
					//String imgpath = BurpExtender.getImage(MessageInfo);
					//java.lang.RuntimeException: Extensions should not make HTTP requests in the Swing event dispatch thread
					//https://support.portswigger.net/customer/portal/questions/16190306-burp-extensions-using-makehttprequest
					
					
					imgPath.setText(imgpath);
					
					//label_showimg.setIcon(new ImageIcon(imgpath));
					Image image = ImageIO.read(new File(imgpath));
					ImageIcon icon = new ImageIcon(image);
					label_showimg.setIcon(icon);
				} catch (Exception e) {
					imgPath.setText(e.getMessage());
				}
				//label_showimg.setIcon(new ImageIcon("D:\\eclipse-workspace\\reCAPTCHA\\www.cnhww.com1509530485395.bmp"));
			}
		});
		
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
		APIResulttextArea.setFont(new Font("ËÎÌו", Font.BOLD, 12));//use this to display Chinese correctly.!!!
		panel_1.add(APIResulttextArea);
		
		panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.NORTH);
		
		btnRequestAPI = new JButton("Get Answer");
		btnRequestAPI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String imgpath = imgPath.getText();
				String result = getAnswer(imgpath);
				APIResulttextArea.setText(result);
				}
		});
		
		lblAboutTypeid = new JLabel("About Typeid ?");
		lblAboutTypeid.setHorizontalAlignment(SwingConstants.LEFT);
		lblAboutTypeid.setFont(new Font("ËÎÌו", Font.BOLD, 12));
		lblAboutTypeid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					URI uri = new URI(Abouttypeid);
					Desktop desktop = Desktop.getDesktop();
					if(Desktop.isDesktopSupported()&&desktop.isSupported(Desktop.Action.BROWSE)){
						desktop.browse(uri);
					}
				} catch (Exception e2) {
					// TODO: handle exception
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
		panel_5.add(btnRequestAPI);
		btnRequestAPI.setHorizontalAlignment(SwingConstants.LEFT);
		
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
				if (APIcomboBox.getSelectedItem().equals("http://www.ysdm.net")) {
					APIRequestRaws.setText("username=%s&password=%s&typeid=%s");
				}else if (APIcomboBox.getSelectedItem().equals("GSA Captcha Breaker"))
				{
					APIRequestRaws.setText("http://127.0.0.1");
				}
			}
		});
		panel_4.add(APIcomboBox, BorderLayout.NORTH);
		APIcomboBox.addItem("GSA Captcha Breaker");
		APIcomboBox.addItem("http://www.ysdm.net");
		

		
		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		lblNewLabel_2 = new JLabel("    "+github);
		lblNewLabel_2.setFont(new Font("ËÎÌו", Font.BOLD, 12));
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
					// TODO: handle exception
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
		Object Method = this.APIcomboBox.getSelectedItem();
		String result = "";
		if(!imgpath.equals("")) {
			if (Method.equals("http://www.ysdm.net")) {
				String para = APIRequestRaws.getText();
				result = myYunSu.getCode(imgpath, para);
				
			}else if (Method.equals("GSA Captcha Breaker"))
			{	
				String httpService = APIRequestRaws.getText();
				result = myGSA.getCode(imgpath, httpService);
			}
		} else {
			result = "image path is null!";
		}
		return result;
	}
}
