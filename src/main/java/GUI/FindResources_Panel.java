package GUI;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

//for find resources
public class FindResources_Panel extends JPanel implements ActionListener, FocusListener{

	private static final long serialVersionUID = 8667602542743167805L;
	private static final int NUMBER_OF_PANELS = 7;

	private final String[] buttonNames = {
			"Home",
			"Shelter",
			"Public Services",
			"Banks",
			"Food Bank",
			"Thrift Stores",
			"Career Events"
	};

	private static final String ABOUT_FIND_RESOURCES = ""
			+ "Welcome, this is first place you should check out after confirming your settings in the settings tab. Here at Project Lift America"
			+ " we pride ourselves at serving the American People through robust technical solutions aimed at connecting people. Because we are the all in one assiatance search engine"
			+ "we will always be a nonprofit and reinvest in making this platform as free as possible free of government intervention, political interferance, or corporate greed."
			+ "Thank you for chosing Project Lift America and we look forward to helping you one meal, home, and bank account at a time."
			+ "You are our number one investment so please don't hesitate to email our development team dev.woottontsa.org@gmail.com. There are there 24/7 365.25 days a week to help you in any way possible.";
	private static final int NUMBER_OF_DEPENDANTS_FONT_SIZE = 7;
	private static final int SPECIAL_HEADING_FONT_SIZE = 10;

	private JPanel[] panels;
	
	private JPanel careerEvents;
	private JTextField addrlineF;
	private JTextField citylineF;
	private JTextField statelineF;
	private JFormattedTextField zipcodeF;
	private JFormattedTextField numberOfDependantsF;
	private JTextField desiredUtilitiesF;
	private JTextField specialServicesF;

	private int currentPanelIndex;

	public FindResources_Panel() {

		JPanel buttonContainerPanel = new JPanel();
		buttonContainerPanel.setLayout(new GridLayout(NUMBER_OF_PANELS, 1, 0, 10));

		JButton[] buttons = new JButton[NUMBER_OF_PANELS];

		//add buttons to container AFTER initializing them
		for(int i  = 1; i <= NUMBER_OF_PANELS; i ++){

			buttons[i-1] = new JButton(buttonNames[i-1]);
			buttons[i-1].addActionListener(this);
			buttonContainerPanel.add(buttons[i-1]);
		}

		buttonContainerPanel.setBounds(25, 25, Client_GUI.WIDTH/6, Client_GUI.HEIGHT - 125);
		add(buttonContainerPanel);

		Border border = BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.BLUE, Color.BLUE);
		panels = new JPanel[NUMBER_OF_PANELS];
		for(int i  = 1; i <= NUMBER_OF_PANELS; i ++) {
			panels[i-1] = new JPanel();
			panels[i-1].setBorder(border);
			panels[i-1].setBounds(buttonContainerPanel.getX() + buttonContainerPanel.getWidth() + 25, 25, 5 * (Client_GUI.WIDTH/6) - 75, Client_GUI.HEIGHT - 125);
		}

		//add components to each sub-category
		createHome();
		createShelter();
		createPublicServices();
		createBanks();
		createFoodBanks();
		createThriftStores();
		createCareerEvents();

		setLayout(null);

		//load first panel
		currentPanelIndex = 0;
		add(panels[currentPanelIndex]);
	}


	private void createCareerEvents() {

		JPanel careerEventsPanel = panels[6];
		careerEventsPanel.setLayout(null);

		JLabel careerEventsTitle = new JLabel("Career Events - Jump Start Your New Career Today! | This is your one stop page to get your life together...");
		careerEventsTitle.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		careerEventsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		careerEventsTitle.setBounds(25, 25, careerEventsPanel.getWidth() - 50, 25);
		careerEventsPanel.add(careerEventsTitle);


		careerEvents = new JPanel();

		JScrollPane careerEventsScroller = new JScrollPane(careerEvents);
		careerEventsScroller.setBounds(25, 75, careerEventsPanel.getWidth() - 50, careerEventsPanel.getHeight() - 150);
		careerEventsPanel.add(careerEventsScroller);

		addJobs();

		JLabel careerEventsDisclaimer = new JLabel("This data is endorsed by the Government of Maryland but its data cannot be verified as it is from 3rd party job providers.");
		careerEventsDisclaimer.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		careerEventsDisclaimer.setHorizontalAlignment(SwingConstants.CENTER);
		careerEventsDisclaimer.setBounds(25, 75 + (careerEventsPanel.getHeight() - 150) + 25, careerEventsPanel.getWidth() - 50, 25);
		careerEventsPanel.add(careerEventsDisclaimer);
	}

	private void addJobs() {

		Scanner fileIn = null;
		//try {
			fileIn = new Scanner(getClass().getResourceAsStream("lib/JSON/Jobs.json"));
			/*
			 * } catch (FileNotFoundException e) {
			 * System.err.println("Cannot load Jobs File! fatal error!"); return; }
			 */

		String json = "";
		while(fileIn.hasNextLine())
			json += fileIn.nextLine();
		JSONArray jobsJSON = new JSONArray(json);
		Job[] jobs = new Job[jobsJSON.length()+1];

		careerEvents.setLayout(new BoxLayout(careerEvents, BoxLayout.Y_AXIS));
		careerEvents.setSize(careerEvents.getWidth() - 50, careerEvents.getHeight() - 150);

		jobs[0] = new Job();//makes header
		
		for(int j = 2; j <= jobsJSON.length()+1; j ++) {

			JSONObject jobObj = jobsJSON.getJSONObject(j-2);

			String name = jobObj.getString("Name");

			JSONObject jobDetailsObj = jobObj.getJSONObject("Details");
			double salary = Double.parseDouble(jobDetailsObj.getString("Salary"));

			JSONObject jobResponsibilites = jobDetailsObj.getJSONObject("Responsiblilites");
			String tenure = jobResponsibilites.getString("Tenure");
			String languages = jobResponsibilites.getString("Languages");
			String softSkills = jobResponsibilites.getString("Soft Skills");
			String promotionPotential = jobDetailsObj.getString("Promotion Potential");
			jobs[j-1] = new Job(name, salary, tenure, softSkills, languages, promotionPotential);
		}

		for (Job job : jobs) {
			careerEvents.add(job);
		}
	}


	private void createThriftStores(){

		JPanel thriftStoresPanel = panels[5];
		thriftStoresPanel.setLayout(null);

		JPanel formContainer = new JPanel();
		formContainer.setLayout(new GridLayout(11, 1));
		formContainer.setBounds(25, 25, (thriftStoresPanel.getWidth() - 50)/2, thriftStoresPanel.getHeight() - 175);

		JLabel heading = new JLabel("Enter your address and let's get some clothes on your back!");
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
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(99950);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(true);

		zipcodeF = new JFormattedTextField(formatter);
		zipcodeF.setName("Zip Code");
		zipcodeF.addFocusListener(this);
		formContainer.add(zipcodeF);

		formContainer.add(new JPanel()); //filler

		JButton submitAddress = new JButton("Submit | Let's get some clothes now...");
		submitAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		submitAddress.addActionListener(this);
		formContainer.add(submitAddress);

		PicturePanel thrifStoresPicture = new PicturePanel("images/ThriftStores.jpg");
		thrifStoresPicture.rescale(100, 360);
		thrifStoresPicture.setBounds(25, 25 + formContainer.getHeight() + 25, 360, 100);
		thriftStoresPanel.add(thrifStoresPicture);

		thriftStoresPanel.add(formContainer);
	}

	private void createFoodBanks() {
		
		JPanel foodBanksStoresPanel = panels[4];
		foodBanksStoresPanel.setLayout(null);

		JPanel formContainer = new JPanel();
		formContainer.setLayout(new GridLayout(13, 1));
		formContainer.setBounds(25, 25, (foodBanksStoresPanel.getWidth() - 50)/2, foodBanksStoresPanel.getHeight() - 175);

		JLabel heading = new JLabel("Enter your address and let's get some clothes on your back!");
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
		numberOfDependants.setFont(new Font(Font.DIALOG, Font.PLAIN, NUMBER_OF_DEPENDANTS_FONT_SIZE));
		formContainer.add(numberOfDependants);

		NumberFormatter numberOfDependantsFormatter = new NumberFormatter(format);
		numberOfDependantsFormatter.setMinimum(0);
		numberOfDependantsFormatter.setMaximum(5);
		numberOfDependantsFormatter.setAllowsInvalid(false);
		numberOfDependantsFormatter.setCommitsOnValidEdit(true);
		
		numberOfDependantsF = new JFormattedTextField(numberOfDependantsFormatter);
		numberOfDependantsF.setName("Number of Dependants");
		numberOfDependants.addFocusListener(this);
		formContainer.add(numberOfDependantsF);
		
		formContainer.add(new JPanel()); //filler

		JButton submitAddress = new JButton("Submit | Let's get some food to chow on now...");
		submitAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		submitAddress.addActionListener(this);
		formContainer.add(submitAddress);

		PicturePanel thrifStoresPicture = new PicturePanel("images/foodBank.jpg");
		thrifStoresPicture.rescale(100, 360);
		thrifStoresPicture.setBounds(25, 25 + formContainer.getHeight() + 25, 360, 100);
		foodBanksStoresPanel.add(thrifStoresPicture);

		foodBanksStoresPanel.add(formContainer);
	}

	private void createBanks() {

		JPanel banksPanel = panels[3];
		banksPanel.setLayout(null);

		JPanel formContainer = new JPanel();
		formContainer.setLayout(new GridLayout(15, 1));
		formContainer.setBounds(25, 25, (banksPanel.getWidth() - 50)/2, banksPanel.getHeight() - 175);

		JLabel heading = new JLabel("Enter your address and find you some resources to get back on your feet!");
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
		numberOfDependants.setFont(new Font(Font.DIALOG, Font.PLAIN, NUMBER_OF_DEPENDANTS_FONT_SIZE));
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
		
		formContainer.add(new JPanel()); //filler

		JButton submitAddress = new JButton("Submit | Let's get increase our funds!");
		submitAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		submitAddress.addActionListener(this);
		formContainer.add(submitAddress);

		PicturePanel thrifStoresPicture = new PicturePanel("images/banks.jpg");
		thrifStoresPicture.rescale(100, 360);
		thrifStoresPicture.setBounds(25, 25 + formContainer.getHeight() + 25, 360, 100);
		banksPanel.add(thrifStoresPicture);

		banksPanel.add(formContainer);
	}

	private void createPublicServices() {
		
		JPanel publicServicesPanel = panels[2];
		publicServicesPanel.setLayout(null);

		JPanel formContainer = new JPanel();
		formContainer.setLayout(new GridLayout(15, 1));
		formContainer.setBounds(25, 25, (publicServicesPanel.getWidth() - 50)/2, publicServicesPanel.getHeight() - 175);

		JLabel heading = new JLabel("Enter your address and find you some resources to get back on your feet!");
		heading.setFont(new Font(Font.DIALOG, Font.PLAIN, SPECIAL_HEADING_FONT_SIZE));
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
		numberOfDependants.setFont(new Font(Font.DIALOG, Font.PLAIN, NUMBER_OF_DEPENDANTS_FONT_SIZE));
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
		
		JLabel desiredUtilities = new JLabel("What Utlities do you need/want to have your new home hooked up with?");
		formContainer.add(desiredUtilities);

		desiredUtilitiesF = new JTextField();
		desiredUtilitiesF.addFocusListener(this);
		desiredUtilitiesF.setName("Desired Utilities");
		formContainer.add(desiredUtilitiesF);
		
		formContainer.add(new JPanel()); //filler

		JButton submitAddress = new JButton("Submit | Let's get something to help us up now...");
		submitAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		submitAddress.addActionListener(this);
		formContainer.add(submitAddress);

		PicturePanel thrifStoresPicture = new PicturePanel("images/publicServices.png");
		thrifStoresPicture.rescale(100, 360);
		thrifStoresPicture.setBounds(25, 25 + formContainer.getHeight() + 25, 360, 100);
		publicServicesPanel.add(thrifStoresPicture);

		publicServicesPanel.add(formContainer);
	}

	private void createShelter() {
		
		JPanel foodBanksStoresPanel = panels[1];
		foodBanksStoresPanel.setLayout(null);

		JPanel formContainer = new JPanel();
		formContainer.setLayout(new GridLayout(15, 1));
		formContainer.setBounds(25, 25, (foodBanksStoresPanel.getWidth() - 50)/2, foodBanksStoresPanel.getHeight() - 175);

		JLabel heading = new JLabel("Enter your address and let's get some clothes on your back!");
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
		numberOfDependants.setFont(new Font(Font.DIALOG, Font.PLAIN, NUMBER_OF_DEPENDANTS_FONT_SIZE));
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
		
		JLabel desiredUtilities = new JLabel("What Utlities do you need/want to have your new home!");
		formContainer.add(desiredUtilities);

		desiredUtilitiesF = new JTextField();
		desiredUtilitiesF.addFocusListener(this);
		desiredUtilitiesF.setName("Desired Utilities");
		formContainer.add(desiredUtilitiesF);
		
		formContainer.add(new JPanel()); //filler

		JButton submitAddress = new JButton("Submit | Let's get a roof over our head and be safe now...");
		submitAddress.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		submitAddress.addActionListener(this);
		formContainer.add(submitAddress);

		PicturePanel thrifStoresPicture = new PicturePanel("images/foodBank.jpg");
		thrifStoresPicture.rescale(100, 360);
		thrifStoresPicture.setBounds(25, 25 + formContainer.getHeight() + 25, 360, 100);
		foodBanksStoresPanel.add(thrifStoresPicture);

		foodBanksStoresPanel.add(formContainer);
	}

	private void createHome() {

		JPanel homePanel = panels[0];
		homePanel.setLayout(new GridLayout(2, 1));
		PicturePanel homePageImage = new PicturePanel("images/HelpingHand.jpg");
		homePageImage.rescale(Client_GUI.HEIGHT/2, 5 * (Client_GUI.WIDTH/6) - 75);
		homePanel.add(homePageImage);

		JTextArea aboutFindResourcesPanel = new JTextArea();
		aboutFindResourcesPanel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		aboutFindResourcesPanel.setEditable(false);
		aboutFindResourcesPanel.setLineWrap(true);
		aboutFindResourcesPanel.setBackground(Color.WHITE);
		aboutFindResourcesPanel.setForeground(Color.BLACK);
		aboutFindResourcesPanel.setText(ABOUT_FIND_RESOURCES);
		homePanel.add(aboutFindResourcesPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		
		if(e.getActionCommand().equals("Submit | Let's get some clothes now...")) {
			
			if(makeRequest("")) {	
			}else
				JOptionPane.showMessageDialog(this, "Cannot perform request for clothes as you are not connected to the internet", "You are not connected to the internet!", JOptionPane.ERROR_MESSAGE);
			return;
		}else if(e.getActionCommand().equals("Submit | Let's get some food to chow on now...")) {
			
			if(makeRequest("")) {	
			}else
				JOptionPane.showMessageDialog(this, "Cannot perform request for food as you are not connected to the internet", "You are not connected to the internet!", JOptionPane.ERROR_MESSAGE);
			return;
		}else if(e.getActionCommand().equals("Submit | Let's get a roof over our head and be safe now...")) {
			
			if(makeRequest("")) {	
			}else
				JOptionPane.showMessageDialog(this, "Cannot perform request for house as you are not connected to the internet", "You are not connected to the internet!", JOptionPane.ERROR_MESSAGE);
			return;
		}else if(e.getActionCommand().equals("Submit | Let's get something to help us up now...")) {
			
			if(makeRequest("")) {	
			}else
				JOptionPane.showMessageDialog(this, "Cannot perform request for public services as you are not connected to the internet", "You are not connected to the internet!", JOptionPane.ERROR_MESSAGE);
			return;
		}else if(e.getActionCommand().equals("Submit | Let's get increase our funds!")) {
			
			if(makeRequest("")) {	
			}else
				JOptionPane.showMessageDialog(this, "Cannot perform request for public services as you are not connected to the internet", "You are not connected to the internet!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//remove panel
		remove(panels[currentPanelIndex]);

		//find button that was pressed
		String buttonPressed = e.getActionCommand();
		int buttonId = 0;
		for(int i = 1; i <= NUMBER_OF_PANELS; i ++)
			if(buttonPressed.equals(buttonNames[i-1]))
				buttonId = i-1;

		currentPanelIndex = buttonId;

		//validate component hierarchy
		repaint();
		//add new panel
		add(panels[currentPanelIndex]);
	}
	
	//helper: makes requests based on string requested
	private static boolean makeRequest(String query) {
		
		HttpClient googleMapApiConnection = HttpClient.newHttpClient();
		try {
			googleMapApiConnection.sendAsync((HttpRequest) HttpRequest.newBuilder(new URI("https://maps.googleapis.com/maps/api/place/findplacefromtext/output?parameters")), null);
		} catch (URISyntaxException e1) {
			return false;
		}
		return true;
	}

	@Override
	public void focusGained(FocusEvent e) {} //not used

	@Override
	public void focusLost(FocusEvent e) {
		//validate fields for bad input
		JTextField referrer = (JTextField)(e.getComponent());
		String referrerName = referrer.getName();

		if(referrerName.equals("Address")) {			

			System.out.println("in");
			if(!referrer.getText().matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)")) {
				addrlineF.setBorder(BorderFactory.createDashedBorder(Color.RED));
			}else {
				addrlineF.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
			}
		}else if(referrerName.equals("City")) {

			if(!referrer.getText().matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)")) {
				citylineF.setBorder(BorderFactory.createDashedBorder(Color.RED));
			}else {
				citylineF.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
			}
		}else if(referrerName.equals("State")) {

			if(!referrer.getText().matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)")) {
				statelineF.setBorder(BorderFactory.createDashedBorder(Color.RED));
			}else {
				statelineF.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
			}
		}else if(referrerName.equals("Zip Code")) {
			
			if(!referrer.getText().matches("^\\d{5}$")) {
				zipcodeF.setBorder(BorderFactory.createDashedBorder(Color.RED));
			}else {
				zipcodeF.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
			}
		}else if(referrerName.equals("Number of Dependants")) {
		
			if(!referrer.getText().matches("/^[1-5]$/")) {
				numberOfDependantsF.setBorder(BorderFactory.createDashedBorder(Color.RED));
			}else {
				numberOfDependantsF.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
			}
		}else if(referrerName.equals("Desired Utilities")) {
			
			handleSimpleStringTextField(desiredUtilitiesF);
		}else if(referrerName.equals("Special Services")) {
			
			handleSimpleStringTextField(specialServicesF);
		}
	}
	
	private void handleSimpleStringTextField(JTextField object) {//helper used in validation
		
		if(object.getText().equals("")) {
			object.setBorder(BorderFactory.createDashedBorder(Color.RED));
		}else {
			object.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
		}
	}

	class PicturePanel extends JPanel{

		private static final long serialVersionUID = 7015692348072382301L;
		private Image image;

		public PicturePanel(String fileName) {

			image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(fileName));
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

	class Job extends JPanel{

		private static final long serialVersionUID = 1563540348774584765L;
		private String name;
		private double salary;
		private String tenure;
		private String softSkills;
		private String languages;
		private String promotionPotential;
		
		private boolean alreadyUsed;//kill switch if you know what I mean

		public Job(String name, double salary, String tenure, String softSkills, String languages, String promotionPotential) {

			this.name = name;
			this.salary = salary;
			this.tenure = tenure;
			this.softSkills = softSkills;
			this.languages = languages;
			this.promotionPotential = promotionPotential;

			//add components
			setLayout(new GridLayout(1, 6));

			JTextArea nTA = new JTextArea(name);
			nTA.setEditable(false);
			add(nTA);

			JTextArea sTA = new JTextArea("Salary: $" + salary + "");
			sTA.setEditable(false);
			sTA.setLineWrap(true);
			add(sTA);

			JTextArea tTA = new JTextArea("Tennure: " + tenure);
			tTA.setEditable(false);
			tTA.setLineWrap(true);
			add(tTA);

			JTextArea skTA = new JTextArea("Skills: " + softSkills);
			skTA.setEditable(false);
			skTA.setLineWrap(true);
			add(skTA);

			JTextArea lTA = new JTextArea("Everyday Skills Used: " + languages);
			lTA.setEditable(false);
			lTA.setLineWrap(true);
			add(lTA);

			JTextArea ppTA = new JTextArea("Promotion Potential: " + promotionPotential);
			ppTA.setEditable(false);
			add(ppTA);
		}
		
		
		//Only used ONCE: Do Not Use Under Normal Circumstances
		/**
		 * WARN: USE ONCE!
		 */
		public Job() {
			
			if(alreadyUsed == true)
				throw new IllegalArgumentException("You CANNOT Used this more than once");
			
			alreadyUsed = true;
			setLayout(new GridLayout(1, 6));
			
			JTextArea nTA = new JTextArea("---Name---");
			nTA.setEditable(false);
			add(nTA);

			JTextArea sTA = new JTextArea("---Salary---");
			sTA.setEditable(false);
			sTA.setLineWrap(true);
			add(sTA);

			JTextArea tTA = new JTextArea("---Tennure---");
			tTA.setEditable(false);
			tTA.setLineWrap(true);
			add(tTA);

			JTextArea skTA = new JTextArea("---Skills---");
			skTA.setEditable(false);
			skTA.setLineWrap(true);
			add(skTA);

			JTextArea lTA = new JTextArea("---Everyday Skills Used---");
			lTA.setEditable(false);
			lTA.setLineWrap(true);
			add(lTA);

			JTextArea ppTA = new JTextArea("---Promotion Potential---");
			ppTA.setEditable(false);
			add(ppTA);
		}

		public String getName() {
			return name;
		}

		public double getSalary() {
			return salary;
		}

		public String getTenure() {
			return tenure;
		}

		public String getSoftSkills() {
			return softSkills;
		}

		public String getLanguages() {
			return languages;
		}

		public String getPromotionPotential() {
			return promotionPotential;
		}
	}

}