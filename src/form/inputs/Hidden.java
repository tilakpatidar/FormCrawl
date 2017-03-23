/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form.inputs;

import form.Form;
import form.Input;
import form.util.WElement;

import java.io.IOException;

/**
 * Hidden input implementation
 *
 * @author tilak
 */
public class Hidden extends Input {

  public Hidden(Form f, WElement ip) throws IOException, Exception {
    super(f, ip, FIELD_TYPES.HIDDEN_INPUT);
  }

  @Override
  public void fill(String s) throws UnsupportedOperationException {

    throw new UnsupportedOperationException("Hidden inputs cannot be filled");
  }
  @Override
  public boolean isBounded() {
    return false;
  }
  @Override
  public boolean isGroupAble() {
    return false;
  }
}

