package GUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.NumberFormatter;

public class TakeOutALoan_Panel extends JPanel implements FocusListener, ActionListener{

	private JTextField addrlineF;
	private JTextField citylineF;
	private JTextField statelineF;
	private JFormattedTextField zipcodeF;
	private JFormattedTextField numberOfDependantsF;
	private JTextField specialServicesF;
	private JButton w4F;
	private JButton w2F;

	private File user2File;
	private File user4File;

	private static final long serialVersionUID = -8540800614201944442L;

	public TakeOutALoan_Panel() {		

		setLayout(null);
		
		JPanel formContainer = new JPanel();
		formContainer.setLayout(new GridLayout(19, 1));
		formContainer.setBounds(25, 25, (Client_GUI.WIDTH)/2 + 112, Client_GUI.HEIGHT - 150);

		JLabel heading = new JLabel("Enter your address so the bank of Maryland can process your loan accurately to give you the best intrest rate possible.");
		formContainer.add(heading);

		JLabel addrline = new JLabel("Address Line 1");
		formContainer.add(addrline);

		addrlineF = new JTextField();
		addrlineF.setName("Address");
		addrlineF.addFocusListener(this);
		formContainer.add(addrlineF);

		JLabel cityline = new JLabel("City");
		formContainer.add(cityline);

		citylineF = new JTextField();
		citylineF.setName("City");
		citylineF.addFocusListener(this);
		formContainer.add(citylineF);

		JLabel stateline = new JLabel("State");
		formContainer.add(stateline);

		statelineF = new JTextField();
		statelineF.setName("State");
		statelineF.addFocusListener(this);
		formContainer.add(statelineF);

		JLabel zipcodeLine = new JLabel("Zip Code");
		formContainer.add(zipcodeLine);

		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		NumberFormatter zipcodeFormatter = new NumberFormatter(format);
		zipcodeFormatter.setValueClass(Integer.class);
		zipcodeFormatter.setMinimum(0);
		zipcodeFormatter.setMaximum(99950);
		zipcodeFormatter.setAllowsInvalid(false);
		zipcodeFormatter.setCommitsOnValidEdit(true);

		zipcodeF = new JFormattedTextField(zipcodeFormatter);
		zipcodeF.setName("Zip Code");
		zipcodeF.addFocusListener(this);
		formContainer.add(zipcodeF);

		JLabel numberOfDependants = new JLabel("How many dependants (children plus spouses) do you take care of? You can only have a maximum of 5.");
		numberOfDependants.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		formContainer.add(numberOfDependants);

		NumberFormatter numberOfDependantsFormatter = new NumberFormatter(format);
		numberOfDependantsFormatter.setMinimum(0);
		numberOfDependantsFormatter.setMaximum(5);
		numberOfDependantsFormatter.setAllowsInvalid(false);
		numberOfDependantsFormatter.setCommitsOnValidEdit(true);

		numberOfDependantsF = new JFormattedTextField(numberOfDependantsFormatter);
		numberOfDependantsF.addFocusListener(this);
		numberOfDependantsF.setName("Number of Dependants");
		formContainer.add(numberOfDependantsF);

		JLabel specialServices = new JLabel("What financial aid services do you need?");
		formContainer.add(specialServices);

		specialServicesF = new JTextField();
		specialServicesF.addFocusListener(this);
		specialServicesF.setName("Special Services");
		formContainer.add(specialServicesF);

		JLabel w4 = new JLabel("Please Upload W4 Tax Document Here:");
		formContainer.add(w4);

		w4F = new JButton("Lastest W4 Return");
		w4F.addActionListener(this);
		w4F.setName("Lastest W4 Return");
		formContainer.add(w4F);

		JLabel w2 = new JLabel("Please Upload W2 Tax Document Here:");
		formContainer.add(w2);

		w2F = new JButton("Lastest W2 Return");
		w2F.addActionListener(this);
		w2F.setName("Lastest W2 Return");
		formContainer.add(w2F);

		formContainer.add(new JPanel()); //filler

		JButton submitAddress = new JButton("Submit | Let's get increase our funds!");
		submitAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		submitAddress.addActionListener(this);
		formContainer.add(submitAddress);

		add(formContainer);
	}

	class PicturePanel extends JPanel{

		private static final long serialVersionUID = 7015692348072382301L;
		private Image image;

		public PicturePanel(String fileName) {

			image = Toolkit.getDefaultToolkit().getImage(fileName);
		}

		public void rescale(int height, int width) {
			image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}

		public void crop(int height, int width) {

			BufferedImage img = (BufferedImage)image;
			img = img.getSubimage(0, 0, width, height);
			image = img;
		}

		public void paint(Graphics g) {

			g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Lastest W4 Return")) {

			w4F.setBackground(Color.gray);
			w4F.setForeground(Color.black);
			user4File = null;

			new Thread(new FileW4UploadListener()).start();
			JFileChooser fileIN = new JFileChooser(new File("user.dir"),FileSystemView.getFileSystemView());
			fileIN.addChoosableFileFilter(new FileNameExtensionFilter("Only W4 Related Forms", "pdf", "html", "txt", "docx", "rtf", "xslx"));
			fileIN.setMultiSelectionEnabled(false);
			fileIN.removeChoosableFileFilter(fileIN.getChoosableFileFilters()[0]);
			fileIN.showOpenDialog(null);
			user4File = fileIN.getSelectedFile();
			return;
		}else if(e.getActionCommand().equals("Lastest W2 Return")) {

			w2F.setBackground(Color.gray);
			w2F.setForeground(Color.black);
			user2File = null;

			new Thread(new FileW2UploadListener()).start();
			JFileChooser fileIN = new JFileChooser(new File("user.dir"),FileSystemView.getFileSystemView());
			fileIN.addChoosableFileFilter(new FileNameExtensionFilter("Only W4 Related Forms", "pdf", "html", "txt", "docx", "rtf", "xslx"));
			fileIN.removeChoosableFileFilter(fileIN.getChoosableFileFilters()[0]);
			fileIN.setMultiSelectionEnabled(false);
			fileIN.showOpenDialog(null);
			user2File = fileIN.getSelectedFile();
			return;
		}else if(e.getActionCommand().equals("\"Submit | Let's go increase our funds!\"")) {

		}
	}


	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	public class FileW4UploadListener implements Runnable{

		private  boolean shouldRun = true;
		@Override
		public void run() {

			while(shouldRun) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
				if(user4File != null) {
					
					w4F.setBackground(Color.green);
					w4F.setForeground(Color.blue);
					return;
				}
			}
		}
	}

	public class FileW2UploadListener implements Runnable{

		private boolean shouldRun = true;
		@Override
		public void run() {

			while(shouldRun) {
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
				if(user2File != null) {
					w2F.setBackground(Color.green);
					w2F.setForeground(Color.blue);
					return;
				}
			}
		}
	}
}