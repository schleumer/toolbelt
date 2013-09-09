package org.schleumer.toolbelt;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

/**
 * Hello
 * world!
 *
 */
public class App extends JFrame {

	final static String LOOKANDFEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	Provider keyProvider = Provider.getCurrentProvider(true);

	public static void main(String[] args) {
		App app = new App();
	}

	App() {
		String home = System.getProperty("user.home");
		final File vault = new File(home, "Screenshots");
		
		if(!vault.exists()){
			vault.mkdirs();
		}
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}


		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		final PopupMenu popup = new PopupMenu();
		URL res = this.getClass().getClassLoader().getResource("camera.png");
		System.out.println(res);
		final TrayIcon trayIcon =
				new TrayIcon(Toolkit.getDefaultToolkit().getImage(res));
		trayIcon.setImageAutoSize(true);
		final SystemTray tray = SystemTray.getSystemTray();

		MenuItem aboutItem = new MenuItem("Auth");
		MenuItem exitItem = new MenuItem("Exit");

		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);
		
		exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

		trayIcon.setPopupMenu(popup);
		try {
			tray.add(trayIcon);
		} catch (AWTException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				keyProvider.reset();
				keyProvider.stop();
			}
		});

		keyProvider.register(KeyStroke.getKeyStroke("PRINTSCREEN"), new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				try {
					BufferedImage image;
					image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					;
					ImageIO.write(image, "png", new File(vault, "Screenshot-"+ System.nanoTime() + ".png"));
				} catch (AWTException ex) {
					Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
				}
				System.out.println("Screenshot Done");
			}
		});
	}
}
