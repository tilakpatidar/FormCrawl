package form.autofill.fillers;

import form.Form;
import form.autofill.data.Record;

import java.util.ArrayList;

/**
 * Abstract class to be extended for all types of fillers
 */
public abstract class AutoFill {

    private final Form form;
    private ArrayList<Record> records;

    /**
     * Public constructor to pass Form
     * @param f
     */
    public AutoFill(Form f){
        this.records = new ArrayList<Record>();
        this.form = f;
    }
}
