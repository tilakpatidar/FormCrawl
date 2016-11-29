/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.ml;

import form.Input;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Performs naive bayes classification to determine input category
 *
 * @author tilak
 */
public class InputClassifier {

	/**
	 * String that stores the text to classify
	 */
	private String text;
	/**
	 * Object that stores the instance.
	 */
	private Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	private FilteredClassifier classifier;

	/**
	 * This method loads the text to be classified.
	 *
	 * @param data
	 */
	public void load(String data) {
		this.text = data;
	}

	/**
	 * This method loads the model to be used as classifier.
	 *
	 * @param fileName The name of the file that stores the text.
	 */
	public void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
			Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
			System.out.println(classifier);
			in.close();
			System.out.println("===== Loaded model: " + fileName + " =====");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Problem found when reading: " + fileName);
		}
	}

	/**
	 * This method creates the instance to be classified, from the text that
	 * has been read.
	 */
	public void makeInstance() throws Exception {
		// Create the attributes, class and text
		FastVector fvNominalVal = new FastVector();

		//classes
		
		for(Input.CATEGORIES cat : Input.CATEGORIES.values()){
			System.out.println(cat.toString());
			fvNominalVal.addElement(cat.toString());
		}
		
		//fvNominalVal.addElement(cat.toString());

		Attribute attribute1 = new Attribute("category", fvNominalVal);
		Attribute attribute2 = new Attribute("text", (FastVector) null);

		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		instances = new Instances("input", fvWekaAttributes, 1);
		System.out.println(attribute1);
		System.out.println(attribute2);
		System.out.println(instances.numAttributes());
		// Set class index
		instances.setClassIndex(0);
		
		
		// Create ansetd add the instance
		Instance instance = new Instance(2);
		System.out.println(this.text);
		instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		
		
		System.out.println(instance);
		
		instances.add(instance);
		
		// Set the tokenizer
			NGramTokenizer tokenizer = new NGramTokenizer();
			tokenizer.setNGramMinSize(1);
			tokenizer.setNGramMaxSize(3);
			tokenizer.setDelimiters("\\s");
			
			// Set the filter
			StringToWordVector filter = new StringToWordVector();
			filter.setTokenizer(tokenizer);
			filter.setInputFormat(instances);
			filter.setWordsToKeep(1000000);
			filter.setDoNotOperateOnPerClassBasis(true);
			filter.setLowerCaseTokens(true);
			
			// Filter the input instances into the output ones
			instances = Filter.useFilter(instances,filter);
		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
	}

	/**
	 * This method performs the classification of the instance. Output is
	 * done at the command-line.
	 */
	public void classify() {
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
		} catch (Exception e) {
			System.out.println("Problem found when classifying the text");
		}
	}

	/**
	 * Main method. It is an example of the usage of this class.
	 *
	 * @param args Command-line arguments: fileData and fileModel.
	 */
	public static void main(String[] args) throws Exception {

		InputClassifier classifier = new InputClassifier();

		classifier.load("job search");
		classifier.loadModel("/home/tilak/NetBeansProjects/FormCrawl/corpus/inputs_o.arff");
		classifier.makeInstance();
		classifier.classify();
	}
}
