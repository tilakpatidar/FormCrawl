/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package form.inputs;

import form.Input;

/**
 *
 * @author tilak
 */
interface InputRequirement {
    
    /**
     * For getting the FIELD_TYPE of any Input IS-A Object
     * @return 
     */
    public Input.FIELDTYPES getType();
    /**
     * 
     * Fill the Input IS-A object with some value
     */
    public void fill();
    
    /**
     * Get the label of the Input
     * @return 
     */
    public String getTitle();
    
    /**
     * 
     * For getting the category of the input for suggestion engine
     * @return 
     */
    public String getCategory();
}
