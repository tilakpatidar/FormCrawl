/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formcrawl;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.*;

/**
 * Our custom logger which generates .log, .html and text area console output.
 *
 * @author tilak
 */
public class MyLogger {

  static private FileHandler fileTxt;
  static private SimpleFormatter formatterTxt;
  static private TextAreaHandler textAreaHandler;
  static private Handler consoleHandler;
  static private FileHandler fileHTML;
  static private Formatter formatterHTML;

  static public void setup(JTextArea area) throws IOException {

    // get the global logger to configure it
    boolean append = true;
    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    //prevent using parent holders
    logger.setUseParentHandlers(false);

    //Remove old handlers
    Handler[] handlers = logger.getHandlers();

    for (Handler old_handle : handlers) {
      logger.removeHandler(old_handle);
    }

    //set logger level here
    logger.setLevel(Level.ALL);

    //file, html and console handler
    fileTxt = new FileHandler("default.log", append);
    fileHTML = new FileHandler("default_log.html", append);
    textAreaHandler = new TextAreaHandler();
    consoleHandler = new ConsoleHandler();

    //set handler levels
    consoleHandler.setLevel(Level.FINEST);
    textAreaHandler.setLevel(Level.FINEST);
    fileTxt.setLevel(Level.FINEST);
    fileHTML.setLevel(Level.INFO);

    //create various formatters
    formatterTxt = new SimpleFormatter();
    formatterHTML = new MyHtmlFormatter();

    //setting formatters
    fileTxt.setFormatter(formatterTxt);
    consoleHandler.setFormatter(formatterTxt);
    fileHTML.setFormatter(formatterHTML);

    //add handlers
    logger.addHandler(fileTxt);
    logger.addHandler(consoleHandler);
    logger.addHandler(fileHTML);
    logger.addHandler(textAreaHandler);

    textAreaHandler.setTextArea(area);
  }
}
