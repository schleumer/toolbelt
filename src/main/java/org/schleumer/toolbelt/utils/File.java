/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.schleumer.toolbelt.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author
 * wesley.goes
 */
public class File {
	public static String readFile(File path, Charset encoding) throws IOException{
		return File.readFile(path.toString(), encoding);
	}
	public static String readFile(String path, Charset encoding)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
}
