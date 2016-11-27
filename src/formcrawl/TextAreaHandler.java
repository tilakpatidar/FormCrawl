/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formcrawl;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;

/**
 * Custom TextAreaHandler for our MyLogger
 *
 * @author tilak
 */
public class TextAreaHandler extends StreamHandler {

	JTextArea textArea = null;

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public void publish(LogRecord record) {
		super.publish(record);
		flush();
		if (textArea != null) {
			textArea.append(getFormatter().format(record));
		}
	}
}
