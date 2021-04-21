package GUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	public User(String n,String a,String dob, String e, String phone,String p) {
		setName(n);
		setAddress(a);
		setDateOfBirth(dob);
		setEmail(e);
		setPhoneNumber(phone);
		setPassword(p);
	}

	public User() {

	}

	public String getName() {
		return performGet("Name");
	}

	public String getAddress() {
		return performGet("Address");
	}

	public String getDateOfBirth() {
		return performGet("Birth");
	}

	public String getEmail() {
		return performGet("Email");
	}

	public String getPhoneNumber() {
		return performGet("Phone");
	}

	public String getPassword() {
		return performGet("Password");
	}

	public boolean setName(String other) {

		if(other.equals("")) {
			return false;
		}
		performChangeRequest("Name",other);
		return true;
	}

	public boolean setAddress(String other) {

		if(other.equals("")) {
			return false;
		}
		performChangeRequest("Address",other);
		return true;
	}

	public boolean setDateOfBirth(String other) {

		if(!other.matches("^(19|20)\\d\\d([- /.])(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])$")) {
			return false;
		}
		performChangeRequest("Birth",other);
		return true;
	}

	public boolean setEmail(String other) {

		if(other.equals("")||!other.contains("@")) {
			return false;
		}	
		performChangeRequest("Email", other);
		return true;
	}

	public boolean setPhoneNumber(String other) {

		try {
			Long.parseLong(other);
		}catch(NumberFormatException e) {
			return false;
		}

		if(other.equals("")||other.length()!=10) {
			return false;
		}
		performChangeRequest("Phone", other);
		return true;

	}

	public boolean setPassword(String other) {

		if(other.equals("")) {
			return false;
		}
		performPasswordChange(other);
		return true;

	}

	public String performGet(String forWhat) {
		System.out.println("performing get for: " + forWhat);
		HttpRequest req = null;
		try {
			req = HttpRequest.newBuilder(new URI("http://localhost:8001/get" + forWhat)).setHeader("Authorization","Bearer " + getToken()).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpClient toServer  = HttpClient.newHttpClient();
		String response = null;
		try {
			response = toServer.send(req, BodyHandlers.ofString()).body();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("get response: " + response);
		JSONObject respObject = new JSONObject(response);
		boolean succeded = respObject.getBoolean("Success");
		System.out.println(response);
		if(succeded) {
		}else {
			throw new IllegalAccessError("Bad Get Request");
		}
		try {
			return respObject.getString("Message");
		}catch (JSONException e) {
			return "";
		}
	}

	private void performChangeRequest(String forWhat, String credentialToChange) {

		System.out.println("for what: " + forWhat);
		System.out.println("cred: " + credentialToChange);
		System.out.println("token: " + getToken());
		HttpRequest req = null;
		try {
			URI reqUrl = new URI("http://localhost:8001/set"+ forWhat + "?" + forWhat + "=" + URLEncoder.encode(credentialToChange));
			req = HttpRequest.newBuilder(reqUrl).POST(BodyPublishers.ofString("")).setHeader("Authorization", "Bearer " +  getToken()).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpClient toServer  = HttpClient.newHttpClient();
		String response = null;
		try {
			response = toServer.send(req, BodyHandlers.ofString()).body();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject respObject = new JSONObject(response);

		boolean succeded = respObject.getBoolean("Success");
		if(succeded) {
		}else {
			String message = respObject.getString("Message");
			JOptionPane.showMessageDialog(null, message);
		}
	}

	private void performPasswordChange(String password) {

		HttpRequest req = null;
		try {
			req = HttpRequest.newBuilder(new URI("http://localhost:8001/setPassword?Password="+password)).POST(BodyPublishers.ofString("")).setHeader("Authorization", "Bearer " + getToken()).build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpClient toServer  = HttpClient.newHttpClient();
		String response = null;
		try {
			response = toServer.send(req, BodyHandlers.ofString()).body();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject respObject = new JSONObject(response);
		boolean succeded = respObject.getBoolean("Success");
		if(succeded) {
		}else {

			String message = respObject.getString("Message");
			JOptionPane.showMessageDialog(null, message);
		}
	}

	private int performEmailVerification() {
		//http://localhost:8001/sendVerification?Username=YOURUSERNAME; use email

		return 0;
	}


	private String getToken() {
		try {
			return new Scanner(new File("lib/token")).nextLine();
		}catch (FileNotFoundException e) {
			return null;
		}
	}
}
