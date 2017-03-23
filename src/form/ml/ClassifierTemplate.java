/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.ml;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;

/**
 * Create a classifier for inputs.
 *
 * @author tilak
 */
public class ClassifierTemplate implements Serializable {

  /**
   * Instances object to handle the dataset content
   */
  private final Instances train;
  /**
   * Instances object to handle the dataset content after using a Filter
   */
  private final Instances trainFiltered;
  /**
   * the current classifier
   */
  private Classifier classifier;
  /**
   * Evaluation object to evaluate the current classifier
   */
//  private Evaluation evaluation;
  /**
   * the weka TF*IDF vector generator
   */
  private final StringToWordVector wordVector;
  /**
   * use this to tokenize text as words
   */
  private final WordTokenizer tokenizer;

  /**
   * Return the bayes classifier instances
   *
   * @return weka.classifiers.Classifier
   */

  public Classifier getClassifier() {
    return classifier;
  }

  /**
   * Create bayes classifier instance
   *
   * @param data_set_path
   * @param stop_words_path
   * @param class_index
   * @throws FileNotFoundException
   * @throws IOException
   * @throws Exception
   */
  public ClassifierTemplate(String data_set_path, String stop_words_path, int class_index) throws Exception {

    /**
     * loading the arff file content
     */
    BufferedReader reader = new BufferedReader(new FileReader(data_set_path));
    ArffReader arff = new ArffReader(reader);
    train = arff.getData();
    train.setClassIndex(class_index);
    /**
     * initializing the filter
     */
    wordVector = new StringToWordVector();
    wordVector.setInputFormat(train);
    tokenizer = new WordTokenizer();
    wordVector.setStopwords(new File(stop_words_path));
    wordVector.setTokenizer(tokenizer);
    wordVector.setIDFTransform(true);
    wordVector.setLowerCaseTokens(true);
    /**
     * generating the TF*IDF Vector
     */
    trainFiltered = Filter.useFilter(train, wordVector);


  }

  /**
   * make the Instance weka object from a String
   *
   * @param text the String to be converted
   * @return Instance object
   */
  private Instance makeInstance(String text) {
    Instance instance = new Instance(2);
    Attribute attribute = train.attribute("text");
    instance.setValue(attribute, attribute.addStringValue(text));
    instance.setDataset(train);
    return instance;
  }

  /**
   * classify text
   *
   * @param text a text message to be classified
   * @return a class label
   * @throws java.lang.Exception
   */
  public String classifyLabel(String text) {

    String result;
    Instance instance = makeInstance(text);
    try {
      wordVector.input(instance);
      Instance filteredInstance = wordVector.output();
      double predicted = classifier.classifyInstance(filteredInstance);
      result = train.classAttribute().value((int) predicted);
      return result;
    } catch (Exception e) {
      return "UNDEFINED";
    }
  }

  /**
   * train the current classifier with the Training data and print the
   * evaluation results to the screen
   *
   * @throws java.lang.Exception
   */
  void train() throws Exception {
    /**
     * build the classifier
     */
    //classifier = new NaiveBayes();

    classifier.buildClassifier(trainFiltered);
    /**
     * Initialize the evaluation by the training data (test)
     */
//    evaluation = new Evaluation(trainFiltered);
    /**
     * evaluate the current classifier
     */
//    evaluation.evaluateModel(classifier, trainFiltered);
//    System.out.println(evaluation.toSummaryString());
  }

  protected void setClassifier(Classifier classifier) {
    this.classifier = classifier;
  }
}
