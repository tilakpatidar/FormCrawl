package form.ml;

import weka.classifiers.functions.Logistic;

import java.io.*;

public class LRClassifier extends ClassifierTemplate implements Serializable {

  private final static String DATA_SET_PATH = "./corpus/labeled_forms.arff";
  private final static String STOP_WORD_PATH = "./corpus/stopwords_en.txt";
  private static int CLASS_INDEX = 0;

  public LRClassifier() throws Exception {
    super(DATA_SET_PATH, STOP_WORD_PATH, CLASS_INDEX);
    super.setClassifier(new Logistic());
    super.train();
  }

  public static void main(String[] args) throws Exception {
    LRClassifier classifier = new LRClassifier();
    //    System.out.println(classifier.classifyLabel("create account"));
    ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("./form_classifier.model"));
    oos.writeObject(classifier);
    oos.flush();
    oos.close();
  }
  public static LRClassifier load(){
    ObjectInputStream ois;
    try {
      ois = new ObjectInputStream(
          new FileInputStream("./form_classifier.model"));
      LRClassifier cls = (LRClassifier) ois.readObject();
      ois.close();
      return cls;
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;

  }
}
