package GUI;

import javax.imageio.ImageIO;
import javax.mail.Header;
import javax.swing.*;
import javax.swing.border.Border;

import org.json.JSONException;
import org.json.JSONObject;

import com.formdev.flatlaf.FlatLightLaf;
import com.sun.net.httpserver.Headers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Login_Screen extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = -6976151037565376135L;

	public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

	private static Login_Screen theInstance = null;

	private String fileName;
	private Scanner fileToken;

	private JTextField usernameField;
	private JPasswordField passwordField;

	private Login_Screen(String fileName) {

		this.fileName = fileName;

		setTitle("Login Screen");

		final int X_POS = SCREEN_WIDTH/3;
		final int Y_POS = SCREEN_HEIGHT/2 - 200;

		final int APPLICATION_WIDTH = SCREEN_WIDTH/3;
		final int APPLICATION_HEIGHT = SCREEN_HEIGHT/2;

		setLocation(new Point(X_POS, Y_POS));
		setSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
		getContentPane().setBackground(Color.BLACK);
		setResizable(false);

		//setIconImage(ImageIO.read(new File("/images/icons/LockIcon.png")));
		//setIconImage(Toolkit.getDefaultToolkit().getImage("images/icons/LockIcon.png"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/icons/LockIcon.png")));
		//setIconImage(ImageIO.read(getClass().getResourceAsStream("images/icons/LockIcon.png")));

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setLayout(null);

		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html>L<br>O<br>G<br>I<br>N<br><br>P<br>O<br>R<br>T<br>A<br>L");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		titleLabel.setForeground(Color.BLUE);
		titleLabel.setBounds(new Rectangle(412, 25, 50, 300));
		add(titleLabel);

		JLabel userNameLabel = new JLabel();
		userNameLabel.setText("Username: ");
		userNameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		userNameLabel.setForeground(Color.BLUE);
		userNameLabel.setBounds(new Rectangle(25, 25, 125, 50));
		add(userNameLabel);

		usernameField = new JTextField();
		usernameField.setBounds(new Rectangle(150, 25, 200, 50));
		usernameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		add(usernameField);

		JLabel passwordLabel = new JLabel();
		passwordLabel.setText("Password: ");
		passwordLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		passwordLabel.setForeground(Color.BLUE);
		passwordLabel.setBounds(new Rectangle(25, 125, 125, 50));
		add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(new Rectangle(150, 125, 200, 50));
		passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		passwordField.addKeyListener(this);
		add(passwordField);

		Border border = BorderFactory.createLineBorder(Color.BLUE);
		JButton submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.setBounds(new Rectangle(150, 200, 200, 50));
		submitButton.setBackground(Color.BLACK);
		submitButton.setForeground(Color.BLUE);
		submitButton.setContentAreaFilled(false);
		submitButton.addActionListener(this);
		submitButton.setBorder(border);
		add(submitButton);

		JButton signupButton = new JButton();
		signupButton.setText("Sign Up");
		signupButton.setBounds(new Rectangle(150, 275, 200, 50));
		signupButton.setBackground(Color.BLACK);
		signupButton.setForeground(Color.BLUE);
		signupButton.setContentAreaFilled(false);
		signupButton.addActionListener(this);
		signupButton.setBorder(border);
		add(signupButton);
		
		JButton reset=new JButton();
		reset.setText("Reset Password");
		reset.setBounds(new Rectangle(150,350,200,50));
		reset.setBackground(Color.BLACK);
		reset.setForeground(Color.blue);
		reset.setContentAreaFilled(false);
		reset.addActionListener(this);
		reset.setBorder(border);
		add(reset);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		if(checkIfAlreadyExist("lib/token")) {

			String token = getToken();
			if(isTokenValid(token)) {
				dispose();
				new Client_GUI(new User());
			}
		}
	}

	private boolean isTokenValid(String token) {

		try {
			HttpRequest req = HttpRequest.newBuilder(new URI("http://localhost:8001/validateToken")).setHeader("Authorization", "Bearer " +token).build();

			HttpClient toServer  = HttpClient.newHttpClient();
			String response = toServer.send(req, BodyHandlers.ofString()).body();

			try {
				
				return new JSONObject(response).getBoolean("Success");	
			}catch (JSONException e) {
				return false;
			}
		} catch (URISyntaxException | InterruptedException | IOException e) {
			return false;
		}
	}

	private String getToken() {//Archie

		try {
			String toRet = fileToken.nextLine();
			fileToken.close();
			return toRet;
		}catch (NoSuchElementException e) {
			return "";
		}
	}

	private boolean checkIfAlreadyExist(String string) {//Archie

		try {

			fileToken = new Scanner(new File(("lib/token")));
		} catch (FileNotFoundException e) {
			new File("lib").mkdir();
			return false;
		}
		return true;
	}

	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("Submit")) {

			Controller accessController = Controller.Intialize(fileName);
			if(accessController.checkIfExists(usernameField.getText(), new String(passwordField.getPassword()))) {

				JOptionPane.showMessageDialog(this, "Success, you have successfully logged in!", "Success", JOptionPane.INFORMATION_MESSAGE, null);
				dispose();

				new Client_GUI(new User(usernameField.getText(), "", "", "", "", new String(passwordField.getPassword())));
			}else {

				JOptionPane.showMessageDialog(this, "Error, log in failure", "Error", JOptionPane.ERROR_MESSAGE, null);
			}
		}else if(actionCommand.equals("Sign Up")) {

			System.out.println("signing up user");
			new Signup_Screen(fileName);
		}
	}

	public void keyTyped(KeyEvent e) {} //not used

	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Submit"));
	}

	public void keyReleased(KeyEvent e) {} //not used

	public static Login_Screen Intialize(String fileName) {

		if(theInstance != null)
			return theInstance;
		else {

			theInstance = new Login_Screen(fileName);
			return theInstance;
		}
	}
}
