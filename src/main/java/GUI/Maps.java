package GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.swing.BrowserView;

public class Maps extends JPanel{

	public Maps() {
		System.setProperty("jxbrowser.license.key", "6P830J66YB541QQ3C4SU3OXDVM7IR0EPHY2UDY9GK8B3SHZ6KVQR44AAOCR4LY3W3PEN");
		EngineOptions options =
				EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).build();
		Engine engine = Engine.newInstance(options);
		Browser browser = engine.newBrowser();

		// Creating Swing component for rendering web content
		// loaded in the given Browser instance.
		BrowserView view = BrowserView.newInstance(browser);
		view.setBounds(0, 0, Client_GUI.WIDTH, Client_GUI.HEIGHT - 75);
		// Creating and displaying Swing app frame.
		setLayout(null);
		add(view);
		browser.navigation().loadUrl("maps.google.com");
	}
}
