package form.ml;

import weka.classifiers.bayes.NaiveBayes;

final public class InputClassifier extends ClassifierTemplate {

  private final static String DATA_SET_PATH = "./corpus/inputs.arff";
  private final static String STOP_WORD_PATH = "./corpus/stopwords_en.txt";
  private static int CLASS_INDEX = 0;

  public InputClassifier() throws Exception {
    super(InputClassifier.DATA_SET_PATH, InputClassifier.STOP_WORD_PATH, InputClassifier.CLASS_INDEX);
    super.setClassifier(new NaiveBayes());
    super.train();
  }
  public static void main(String[] args) throws Exception {
    InputClassifier classifier = new InputClassifier();
    System.out.println(classifier.getCategory("create new password"));
  }

  private String getCategory(String input_title) {
    return super.classifyLabel(input_title);
  }

  @Override
  public String classifyLabel(String text) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.);
  }
}
