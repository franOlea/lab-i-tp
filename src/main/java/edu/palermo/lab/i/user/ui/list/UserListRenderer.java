package edu.palermo.lab.i.user.ui.list;

import edu.palermo.lab.i.user.UserDto;

import javax.swing.*;
import java.awt.*;

public class UserListRenderer extends DefaultListCellRenderer {

  @Override
  public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
    if(value instanceof UserDto) {
      UserDto user = (UserDto) value;
      String displayValue = String.format("%s, %s", user.getLastName(), user.getFirstName());
      return super.getListCellRendererComponent(list, displayValue, index, isSelected, cellHasFocus);
    } else {
      return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
  }

}
