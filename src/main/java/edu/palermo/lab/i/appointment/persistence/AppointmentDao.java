package edu.palermo.lab.i.appointment.persistence;

import edu.palermo.lab.i.appointment.AppointmentDto;
import lombok.NonNull;

import java.util.List;

public interface AppointmentDao {

  void save(@NonNull final AppointmentDto appointment);
  List<AppointmentDto> getDoctorsAppointments(@NonNull final String doctorId);
  List<AppointmentDto> getPatientsAppoints(@NonNull final String userId);

}
