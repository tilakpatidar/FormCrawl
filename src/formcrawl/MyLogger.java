/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package formcrawl;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JTextArea;
/**
 * Our custom logger which generates .log, .html and text area console output.
 * @author tilak
 */
public class MyLogger {
        static private FileHandler fileTxt;
        static private SimpleFormatter formatterTxt;

        static private FileHandler fileHTML;
        static private Formatter formatterHTML;

        static public void setup(JTextArea area) throws IOException {
            
                // get the global logger to configure it
                boolean append = true;
                Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

                

                logger.setLevel(Level.INFO);
                fileTxt = new FileHandler("default.log", append);
                fileHTML = new FileHandler("default_log.html", append);

                // create a TXT formatter
                formatterTxt = new SimpleFormatter();
                fileTxt.setFormatter(formatterTxt);
                logger.addHandler(fileTxt);

                // create an HTML formatter
                formatterHTML = new MyHtmlFormatter();
                fileHTML.setFormatter(formatterHTML);
                logger.addHandler(fileHTML);
                
                TextAreaHandler textAreaHandler = new TextAreaHandler();
                textAreaHandler.setTextArea(area);
                logger.addHandler(textAreaHandler);
        }
}
