/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.ml;

import form.Input;

/**
 * BayesClassifier for input labels
 * @author tilak
 */
final public class InputClassifier extends BayesClassifier {

	public final static String DATA_SET_PATH = "/home/tilak/NetBeansProjects/FormCrawl/corpus/inputs.arff";
	public final static String STOP_WORD_PATH  = "/home/tilak/NetBeansProjects/FormCrawl/corpus/stopwords_en.txt";
	public static int CLASS_INDEX = 0;
	

	public InputClassifier() throws Exception{
		super(InputClassifier.DATA_SET_PATH, InputClassifier.STOP_WORD_PATH, InputClassifier.CLASS_INDEX);
		
	}
	
	/**
	 * Get an Input category
	 * @param input_title
	 * @return Input.CATEGORIES
	 * @throws Exception 
	 */
	public Input.CATEGORIES getCategory(String input_title) throws Exception{
		String label = super.classifyLabel(input_title);
		return Input.CATEGORIES.valueOf(label);
		
	}
	
	public static void main(String[] args) throws Exception{
		InputClassifier classifier = new InputClassifier();
		System.out.println(classifier.getCategory("create new password"));
		
		
	}
	
	
	/**
	 * Overriden, to forbid usage user getCategory instead
	 * @param text
	 * @return String
	 * @override
	 */
	@Override
	public String classifyLabel(String text) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.);

	}
	
	
	
}
