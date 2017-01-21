/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.corpus;

/**
 * @author tilak
 */

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Corpus {

  public static final Gson INPUT_LABEL_KEYWORDS;
  private static final String INPUT_LABEL_FILE = "./corpus/input_tags.json";

  //for logging
  private static final Logger LOGGER;

  static {
    LOGGER = Logger.getGlobal();
  }

  static {
    INPUT_LABEL_KEYWORDS = new Gson();
    try {
      JsonReader reader = new JsonReader(new FileReader(Corpus.INPUT_LABEL_FILE));
      LOGGER.log(Level.INFO, "[DONE] Loaded input label json");
    } catch (FileNotFoundException ex) {
      LOGGER.log(Level.INFO, "[ERROR] Loading input label json {0}", ex.getMessage());
    }
  }
}
