package GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.HttpURLConnection;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

/*
 * Client GUI
 * 12-11-20
 * This is what the client sees
 */
public class Client_GUI extends JFrame implements ActionListener /*used to handles button clicks*/{

	private static final long serialVersionUID = -5176069324626341807L;

	public static int WIDTH = 1024;
	public static int HEIGHT = 750;

	private Home_Panel homePanel = new Home_Panel();
	private FindResources_Panel findResourcesPanel = new FindResources_Panel();
	private Maps mapsPanel = new Maps();
	private JPanel takeoutALoanPanel = new TakeOutALoan_Panel();
	private JPanel settingsPanel;


	public Client_GUI(User user) {

		setTitle("Client GUI");
		setName("Client GUI");

		setSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.BLUE);

		setDefaultLookAndFeelDecorated(false);


		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e1) {
			System.err.println("Couldn't load look and");
		}
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/icons/application.jpg")));

		settingsPanel = new Settings_Panel(user);
		//Add all of our components are add here
		JTabbedPane navBar = new JTabbedPane();
		navBar.addTab("Home", null, homePanel, "Home Tab");
		navBar.addTab("Find Resources", null, findResourcesPanel, "Find Resources Tab");
		navBar.addTab("Google Maps", null, mapsPanel, "Google Maps Tab");
		navBar.addTab("Take Out A Loan", null, takeoutALoanPanel, "Take Out A Loan");
		navBar.addTab("Settings", null, settingsPanel, "Settings");
		

		add(navBar);

		setVisible(true);
	}

	/* These are OUTER classes in /src
	 * will build the panels to the corresponding tabs automatically. All we need to do is to add components in each of the constructors
	 */
		
	//handles button clicks
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}
