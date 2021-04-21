package GUI;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class Signup_Screen extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = -6976151037565376135L;

	public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

	private String fileName;

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField passwordFieldConfirm;

	public Signup_Screen(String fileName) {

		this.fileName = fileName;

		setTitle("Sign Up Screen");

		final int X_POS = SCREEN_WIDTH/3;
		final int Y_POS = SCREEN_HEIGHT/2 - 200;

		final int APPLICATION_WIDTH = SCREEN_WIDTH/3;
		final int APPLICATION_HEIGHT = SCREEN_HEIGHT/2;

		setLocation(new Point(X_POS, Y_POS));
		setSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
		getContentPane().setBackground(Color.BLACK);
		setResizable(false);

		//try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/icons/LockIcon.png")));
		/*} catch (IOException e) {
			System.err.println("Can't find icon image!");
			return;
		}*/
		
		setLayout(null);

		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html>S<br>i<br>g<br>n<br>u<br>p<br><br>P<br>o<br>r<br>t<br>a<br>l");
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
		passwordLabel.setBounds(new Rectangle(25, 102, 125, 50));
		add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(new Rectangle(150, 100, 200, 50));
		passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		add(passwordField);

		JLabel passwordConfirmLabel = new JLabel();
		passwordConfirmLabel.setText("Confirm Password: ");
		passwordConfirmLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		passwordConfirmLabel.setForeground(Color.BLUE);
		passwordConfirmLabel.setBounds(new Rectangle(25, 170, 125, 50));
		add(passwordConfirmLabel);

		passwordFieldConfirm = new JPasswordField();
		passwordFieldConfirm.setBounds(new Rectangle(150, 175, 200, 50));
		passwordFieldConfirm.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		passwordFieldConfirm.addKeyListener(this);
		add(passwordFieldConfirm);


		Border border = BorderFactory.createLineBorder(Color.BLUE);
		
		JButton submitButton = new JButton();
		submitButton.setText("Submit");
		submitButton.setBounds(new Rectangle(150, 250, 200, 50));
		submitButton.addActionListener(this);
		submitButton.setBackground(Color.BLACK);
		submitButton.setForeground(Color.BLUE);
		submitButton.setContentAreaFilled(false);
		submitButton.setBorder(border);
		add(submitButton);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public void keyTyped(KeyEvent e) {} //not used

	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Submit"));
	}

	public void keyReleased(KeyEvent e) {} //not used

	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("Submit")) {

			if(!new String(passwordField.getPassword()).equals(new String(passwordFieldConfirm.getPassword()))) {
				JOptionPane.showMessageDialog(this, "Passwords Don't Match, Please Try Again.", "Passwords Don't Match!", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			Controller accessController = Controller.Intialize(fileName);
			if(!accessController.writeCredentials(usernameField.getText(), new String(passwordField.getPassword()))) 
				JOptionPane.showMessageDialog(this, "Account Already Exists, Please Try A New Username", "Account alreeady Exist", JOptionPane.ERROR_MESSAGE, null);
			else
				JOptionPane.showMessageDialog(this, "Success! Account Successfully Created.", "Success", JOptionPane.INFORMATION_MESSAGE, null);
		}
	}
}
