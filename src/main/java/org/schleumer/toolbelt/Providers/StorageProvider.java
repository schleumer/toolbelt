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
public interface StorageProvider {
	public void Authenticate(AuthEvent callback);
	public void sendFile();
}
