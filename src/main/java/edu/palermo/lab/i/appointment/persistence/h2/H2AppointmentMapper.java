package edu.palermo.lab.i.appointment.persistence.h2;

import edu.palermo.lab.i.appointment.AppointmentDto;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class H2AppointmentMapper {

  @SneakyThrows
  List<AppointmentDto> map(@NonNull final ResultSet resultSet) {
    List<AppointmentDto> appointments = new LinkedList<>();
    while(resultSet.next()) {
      AppointmentDto appointment = new AppointmentDto();
      appointment.setId(resultSet.getLong("id"));
      appointment.setDoctorId(resultSet.getString("doctor"));
      appointment.setPatientId(resultSet.getString("patient"));
      appointment.setTimestamp(resultSet.getLong("timestamp"));
      appointment.setCanceled(resultSet.getBoolean("canceled"));
      appointments.add(appointment);
    }
    return appointments;
  }

}
