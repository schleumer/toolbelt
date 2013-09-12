/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schleumer.toolbelt.Providers;

/**
 *
 * @author
 * wesley.goes
 */
public class Imgur implements StorageProvider {

	private String clientID = "7bd5d5a7f7599bc";

	public void sendFile() {
	}

	public void Authenticate(AuthEvent callback) {
		callback.authOk();
	}
}
