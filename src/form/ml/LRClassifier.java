/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.ml;

import weka.classifiers.bayes.NaiveBayes;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Create a logistic regression classifier for inputs.
 *
 * @author tilak
 */
public class LRClassifier extends ClassifierTemplate {

  public final static String DATA_SET_PATH = "./corpus/labeled_forms.arff";
  public final static String STOP_WORD_PATH = "./corpus/stopwords_en.txt";
  public static int CLASS_INDEX = 0;
  /**
   * Create bayes classifier instance
   *
   * @throws FileNotFoundException
   * @throws IOException
   * @throws Exception
   */
  public LRClassifier() throws Exception {
    super(DATA_SET_PATH, STOP_WORD_PATH, CLASS_INDEX);
    super.setClassifier(new NaiveBayes());
    super.train();
  }

  public static void main(String[] args) throws Exception {
    LRClassifier classifier = new LRClassifier();
    System.out.println(classifier.classifyLabel("create account"));

  }
}
