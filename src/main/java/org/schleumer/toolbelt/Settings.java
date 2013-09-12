/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schleumer.toolbelt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author
 * wesley.goes
 */
public class Settings {

	String name;
	File file;
	Map<String, Object> json;

	Settings(String name) {
		this.name = name;
		file = new File(App.settingsVault, name + ".json");
		if(file.exists()){
			json = org.schleumer.toolbelt.utils.File.readFile(file, Charset.forName("utf-8"));
		}
		json = new HashMap<String, Object>();
	}

	public void set(String name, SettingsHash value) {
		json.put(name, value);
	}

	public void set(String name, String value) {
		json.put(name, value);
	}

	public String getJSON() {
		JSONObject ljson = new JSONObject(this.json);
		return ljson.toString();
	}

	public void save() {
		try {
			BufferedWriter writer = null;
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(this.getJSON());
			writer.close();
		} catch (IOException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public class SettingsHash extends HashMap<String, Object> {

		public SettingsHash() {
		}
	}
}
