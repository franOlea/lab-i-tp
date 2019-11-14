package edu.palermo.lab.i.appointment;

import lombok.NonNull;

import java.util.List;

public interface AppointmentDao {

  void save(@NonNull final AppointmentDto appointment);
  List<AppointmentDto> getDoctorsAppointments(@NonNull final String doctorId);
  List<AppointmentDto> getPatientsAppoints(@NonNull final String userId);

}
