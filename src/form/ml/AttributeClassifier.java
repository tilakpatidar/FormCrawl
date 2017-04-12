package form.ml;

import form.autofill.data.DomainAttribute;
import weka.classifiers.functions.Logistic;

import java.io.*;

public class AttributeClassifier extends ClassifierTemplate implements Serializable {

  private final static String DATA_SET_PATH = "./corpus/domain_attributes_movie.arff";
  private final static String STOP_WORD_PATH = "./corpus/stopwords_en.txt";
  private static final String SERIALIZED_MODEL = "./domain_attribute_classifier.model";
  private static int CLASS_INDEX = 0;

  public AttributeClassifier() throws Exception {
    super(DATA_SET_PATH, STOP_WORD_PATH, CLASS_INDEX);
    super.setClassifier(new Logistic());
    super.train();
  }

  public DomainAttribute domainAttribute(String label) {
    return DomainAttribute.valueOf(this.classifyLabel(label));
  }

  public static void main(String[] args) throws Exception {
//    serialize();
    AttributeClassifier classifier = load();
    System.out.println(classifier.classifyLabel("movie title"));
  }
  public static void serialize() throws Exception {
    AttributeClassifier classifier = new AttributeClassifier();
    System.out.println(classifier.domainAttribute("sony"));
    ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(SERIALIZED_MODEL));
    oos.writeObject(classifier);
    oos.flush();
    oos.close();
  }
  public static AttributeClassifier load() {
    ObjectInputStream ois;
    try {
      ois = new ObjectInputStream(
          new FileInputStream(SERIALIZED_MODEL));
      AttributeClassifier cls = (AttributeClassifier) ois.readObject();
      ois.close();
      return cls;
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
