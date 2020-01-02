package edu.palermo.lab.i.user.ui.list.doctor;

import edu.palermo.lab.i.user.UserDto;

import javax.swing.*;
import java.awt.*;

public class DoctorListRenderer extends DefaultListCellRenderer {

  @Override
  public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
    if(value instanceof UserDto) {
      UserDto doctor = (UserDto) value;
      String displayValue = String.format("%s, %s [$%.2f]", doctor.getLastName(), doctor.getFirstName(), doctor.getHourlyFee());
      return super.getListCellRendererComponent(list, displayValue, index, isSelected, cellHasFocus);
    } else {
      return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
  }
}
