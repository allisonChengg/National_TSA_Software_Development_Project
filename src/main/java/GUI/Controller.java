package GUI;
/**	
 * Archie Garg
 */

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.print.attribute.standard.Severity;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;
public class Controller {

	private static Controller theInstance = null;

	private String fileName;

	private Controller(String fileName) {

		this.fileName = fileName;
	}

	public static Controller Intialize(String fileName) {

		if(theInstance != null)
			return theInstance;
		else {

			theInstance = new Controller(fileName);
			return theInstance;
		}
	}

	public boolean writeCredentials(String username, String password) {

		if(theInstance == null)
			throw new IllegalAccessError("You haven't intialiized the controller!");

		if(username.isEmpty() || password.isEmpty())
			return false;
		try {
			HttpRequest req = HttpRequest.newBuilder(new URI("http://localhost:8001/createUser?Username=" + URLEncoder.encode(username) + "&Password=" + URLEncoder.encode(password))).POST(BodyPublishers.ofString("")).build();
			HttpClient toServer  = HttpClient.newHttpClient();
			String response = toServer.send(req, BodyHandlers.ofString()).body();

			try {
				
				if(new JSONObject(response).getString("Error").equals("Username already exists")) {
					return false;
				}
			}catch (JSONException e) {
				return true;
			}
		} catch (URISyntaxException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkIfExists(String username, String password) {

		if(theInstance == null)
			throw new IllegalAccessError("You haven't intialiized the controller!");

		try {

			HttpRequest req = HttpRequest.newBuilder(new URI("http://localhost:8001/login?Username=" + URLEncoder.encode(username) + "&Password=" + URLEncoder.encode(password))).POST(BodyPublishers.ofString("")).build();
			HttpClient toServer  = HttpClient.newHttpClient();
			String response = toServer.send(req, BodyHandlers.ofString()).body();
			JSONObject respObject = new JSONObject(response);
			try {
				boolean booleanResp = respObject.getBoolean("Success");
				if(booleanResp == false)
					return false;
			}catch(JSONException e) {
				String token = respObject.getString("JWT");
				new File("lib").mkdir();
				FileWriter tokenWriter = new FileWriter(new File("lib/token"), false);
				tokenWriter.write(token);
				tokenWriter.flush();
				tokenWriter.close();
			}
		} catch (URISyntaxException | InterruptedException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
