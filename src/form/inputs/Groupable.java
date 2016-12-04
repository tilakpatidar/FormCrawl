package form.inputs;

import form.Form;
import form.Input;
import org.jsoup.nodes.Element;
import java.io.IOException;

/**
 * Created by tilak on 4/12/16.
 */
public abstract class Groupable extends Input {

    private String groupClassName;

    public Groupable(Form f, Element ip, Input.FIELDTYPES type) throws IOException, Exception {
        super(f, ip, type);

        //now create the group
        this.addToGroup(type);

    }


    private void addToGroup(Input.FIELDTYPES type){
        Group gp = this.getAssociatedForm().findGroupByName(this.groupClassName, this.getName());
        if(gp == null){
            //group not exists create one
            switch (type){
                case CHECKBOX_INPUT:
                    gp = new CheckBoxGroup(this.getName());
                    gp.addElement(this);
                    this.getAssociatedForm().getInputGroups().add(gp);
                    break;
                case RADIO_INPUT:
                    gp = new RadioGroup(this.getName());
                    gp.addElement(this);
                    this.getAssociatedForm().getInputGroups().add(gp);
                    break;
            }
        }else{
            gp.addElement(this);
        }

    }

    public Group getGroup(){
        Group gp = this.getAssociatedForm().findGroupByName(this.groupClassName, this.getName());
        return gp;
    }


}
