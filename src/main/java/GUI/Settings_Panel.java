package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;


public class Settings_Panel extends JPanel implements ActionListener{

	JPanel optionPanel=new JPanel();
	JPanel infoPanel=new JPanel();
	JLabel[] fieldNames= new JLabel[6];
	JTextField[] textFields= new JTextField[5];
	JPasswordField password=new JPasswordField("");
	ArrayList<String> properties=new ArrayList<String>();
	JButton edit;
	User current;



	public Settings_Panel(User currUser){

		current=currUser;

		this.setLayout(null);
		optionPanel.setLayout(new GridLayout(2,0));
		String[] options= {"Account Settings","Logout"};
		for(String button:options) {
			optionPanel.add(addButton(button));
		}

		infoPanel.setLayout(null);
		String[] labels= {"Name:","Address:","Date Of Birth(Year-Month-Date):","Email:","Phone Number:","Password:"};
		properties.add(current.getName());
		properties.add(current.getAddress());
		properties.add(current.getDateOfBirth());
		properties.add(current.getEmail());
		properties.add(current.getPhoneNumber());

		for(int i=0;i<fieldNames.length;i++) {

			fieldNames[i]=new JLabel(labels[i]);
			fieldNames[i].setFont(new Font("Arial", Font.PLAIN, 18));
			fieldNames[i].setBounds(30,90+75*i,300,50);
			infoPanel.add(fieldNames[i]);

			if(i!=fieldNames.length-1) {
				textFields[i]=new JTextField(properties.get(i));
				textFields[i].setFont(new Font("Arial", Font.PLAIN, 18));
				textFields[i].setBounds(330,100+75*i,250,30);
				infoPanel.add(textFields[i]);
			}

		}

		password=new JPasswordField(current.getPassword());
		password.setBounds(330,475,250,30);
		infoPanel.add(password);

		setEdit(false);

		edit=addButton("Edit");
		edit.setBounds(600, 20, 100, 50);
		infoPanel.add(edit);

		optionPanel.setBounds(0, 0, 250, 680);
		infoPanel.setBounds(250,0,(1024-250),750);		

		add(optionPanel);
		add(infoPanel);

	}

	private JButton addButton(String ButtonLabel) {
		JButton button=new JButton(ButtonLabel);
		button.setFont(new Font("Arial", Font.PLAIN, 20));
		button.setOpaque(false);
		button.setContentAreaFilled(true);
		button.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
		button.addActionListener(this);
		return button;
	}

	public void actionPerformed(ActionEvent ae) {

		if(ae.getActionCommand().equals("Edit")) {
			setEdit(true);
			edit.setText("Save");
		}
		else if(ae.getActionCommand().equals("Save")) {

			System.out.println("in");
			boolean[] areValid=new boolean[textFields.length+1];


			areValid[0]=current.setName(textFields[0].getText());
			areValid[1]=current.setAddress(textFields[1].getText());
			areValid[2]=current.setDateOfBirth(textFields[2].getText());
			areValid[3]=current.setEmail(textFields[3].getText());
			areValid[4]=current.setPhoneNumber(textFields[4].getText());
			areValid[5]=current.setPassword(new String(password.getPassword()));

			boolean isntValid=false;
			for(int i=0;i<areValid.length-1;i++) {
				if(!areValid[i]) {
					textFields[i].setBackground(Color.RED);
					isntValid=true;
				}
				else {
					textFields[i].setBackground(Color.white);
				}
			}

			if(!areValid[5]) {
				password.setBackground(Color.RED);
				isntValid=true;
			}
			else {
				password.setBackground(Color.white);
			}

			if(isntValid) {
				return;
			}

			setEdit(false);
			edit.setText("Edit");


		}
		else if(ae.getActionCommand().equals("Logout")) {

			try {
				
				FileWriter tokenWriter = new FileWriter(new File("lib/token"), false);
				tokenWriter.write("");
				tokenWriter.flush();
				tokenWriter.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
			System.exit(-1);
		}
	}

	private void setEdit(boolean editable) {
		password.setEditable(editable);
		for(int i=0;i<textFields.length;i++) {
			textFields[i].setEditable(editable);
		}
	}

}
