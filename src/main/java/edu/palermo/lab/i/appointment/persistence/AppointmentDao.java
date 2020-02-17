package edu.palermo.lab.i.appointment.persistence;

import edu.palermo.lab.i.appointment.AppointmentDto;
import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentDao {

  void save(@NonNull final AppointmentDto appointment);
  List<AppointmentDto> getDoctorsAppointments(@NonNull final UserDto doctor);
  List<AppointmentDto> getDoctorsAppointments(@NonNull final UserDto doctor, @NonNull final LocalDate localDate);
  List<AppointmentDto> getPatientsAppointments(@NonNull final String userId);

}
