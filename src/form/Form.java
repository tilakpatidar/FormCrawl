/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form;
import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * @author tilak
 */
public class Form {
    
    /**
     * List of all the inputs in the Form
     */
    private ArrayList<Input> inputs;
    
    /**
     * HashMap to categorize the various Input types
     */
    private HashMap<Input.FIELDTYPES, ArrayList<Input>> field_map;
    
    public Form(String FormData){
        
    }
    
    
}
