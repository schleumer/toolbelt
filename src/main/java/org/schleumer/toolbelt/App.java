package org.schleumer.toolbelt;

import org.schleumer.toolbelt.Providers.AuthEvent;
import org.schleumer.toolbelt.Providers.StorageProvider;
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
import org.schleumer.toolbelt.Providers.Imgur;

/**
 * Hello
 * world!
 *
 */
public class App extends JFrame {

	Provider keyProvider = Provider.getCurrentProvider(true);
	StorageProvider storageProvider;
	static File vault;
	static File settingsVault;

	public static void main(String[] args) {
		App app = new App();
	}

	App() {

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}

		storageProvider = new Imgur();

		String home = System.getProperty("user.home");
		App.vault = new File(home, "Screenshots");
		App.settingsVault = new File(home, ".schleumer-toolbelt");

		// Se a pasta de Screenshots não existir na ~ cria uma
		if (!vault.exists()) {
			vault.mkdirs();
		}

		// Se não deu pra criar, avisa pro cidadão e vaza
		if (!vault.exists()) {
			JOptionPane.showMessageDialog(null, "Você não possui permisão de escrita na pasta home(?)\n Vazando aqui, flw", "TRETA", JOptionPane.INFORMATION_MESSAGE);
			System.exit(1);
		} else {
			settingsVault.mkdirs();
			// Aqui tá tudo de boa
			this.tudoOk();
		}


	}

	private void tudoOk() {
		Settings settings = new Settings("test");
		settings.set("lel", "topLel");
		Settings.SettingsHash hs = settings.new SettingsHash();
		hs.put("lel", "topLellel");
		settings.set("lel2", hs);
		settings.save();
		
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			System.exit(1);
		}

		final PopupMenu popup = new PopupMenu();
		URL res = this.getClass().getClassLoader().getResource("toolkit.png");
		final TrayIcon trayIcon =
				new TrayIcon(Toolkit.getDefaultToolkit().getImage(res));
		trayIcon.setImageAutoSize(true);
		final SystemTray tray = SystemTray.getSystemTray();

		MenuItem settingsItem = new MenuItem("Configurações");
		MenuItem openScreenshotPathItem = new MenuItem("Abrir pasta dos screenshots");
		MenuItem exitItem = new MenuItem("Vazar");

		popup.add(settingsItem);
		popup.add(openScreenshotPathItem);
		popup.addSeparator();
		popup.add(exitItem);

		openScreenshotPathItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(vault);
				} catch (IOException ex) {
					Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});

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

		keyProvider.register(KeyStroke.getKeyStroke("ctrl PRINTSCREEN"), new HotKeyListener() {
			public void onHotKey(HotKey hotKey) {
				storageProvider.Authenticate(new AuthEvent() {
					@Override
					public void authOk() {
						try {
							BufferedImage image;
							image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
							ImageIO.write(image, "png", new File(vault, "Screenshot-" + System.nanoTime() + ".png"));
						} catch (AWTException ex) {
							Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
						} catch (IOException ex) {
							Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
						}
						System.out.println("Screenshot Done");
					}
				});
			}
		});
	}
}
