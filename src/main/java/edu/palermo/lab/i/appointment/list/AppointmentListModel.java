package edu.palermo.lab.i.appointment.list;

import edu.palermo.lab.i.appointment.AppointmentDto;
import lombok.NonNull;

import javax.swing.*;
import java.util.List;

public class AppointmentListModel extends AbstractListModel<AppointmentDto> implements ComboBoxModel<AppointmentDto> {

  @NonNull
  private final List<AppointmentDto> appointments;

  private AppointmentDto selectedAppointment;

  public AppointmentListModel(@NonNull final List<AppointmentDto> appointments) {
    this.appointments = appointments;
    this.selectedAppointment = appointments.stream().findFirst().orElse(null);
  }

  @Override
  public void setSelectedItem(@NonNull final Object selectedAppointment) {
    this.selectedAppointment = (AppointmentDto) selectedAppointment;
  }

  @Override
  public Object getSelectedItem() {
    return selectedAppointment;
  }

  @Override
  public int getSize() {
    return appointments.size();
  }

  @Override
  public AppointmentDto getElementAt(final int index) {
    return appointments.get(index);
  }

}
