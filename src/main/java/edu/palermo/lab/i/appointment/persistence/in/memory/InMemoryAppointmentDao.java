package edu.palermo.lab.i.appointment.persistence.in.memory;

import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.appointment.AppointmentDto;
import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryAppointmentDao implements AppointmentDao {

  private final List<AppointmentDto> appointments = new LinkedList<>();

  @Override
  public void save(final @NonNull AppointmentDto appointment) {
    Optional<AppointmentDto> potentialExistingAppointment = appointments.stream()
        .filter(savedAppointment -> savedAppointment.getId().equals(appointment.getId()))
        .findFirst();
    if(potentialExistingAppointment.isPresent()) {
      AppointmentDto savedAppointment = potentialExistingAppointment.get();
      savedAppointment.setCanceled(appointment.getCanceled());
      savedAppointment.setDoctorId(appointment.getDoctorId());
      savedAppointment.setPatientId(appointment.getPatientId());
      savedAppointment.setTimestamp(appointment.getTimestamp());
    } else {
      appointments.add(appointment);
    }
  }

  @Override
  public List<AppointmentDto> getDoctorsAppointments(final @NonNull UserDto doctor) {
    return appointments.stream()
        .filter(appointment -> appointment.getDoctorId().equals(doctor.getId()) && !appointment.getCanceled())
        .map(AppointmentDto::copy)
        .collect(Collectors.toList());
  }

  @Override
  public List<AppointmentDto> getDoctorsAppointments(final @NonNull UserDto doctor, final @NonNull LocalDate localDate) {
    return appointments.stream()
        .filter(appointment -> appointment.getDoctorId().equals(doctor.getId()) && !appointment.getCanceled())
        .filter(appointment -> {
          Instant instant = Instant.ofEpochMilli(appointment.getTimestamp());
          LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
          return localDateTime.getDayOfYear() == localDate.getDayOfYear();
        })
        .map(AppointmentDto::copy)
        .collect(Collectors.toList());
  }

  @Override
  public List<AppointmentDto> getPatientsAppoints(final @NonNull String patientId) {
    return appointments.stream()
        .filter(appointment -> appointment.getPatientId().equals(patientId) && !appointment.getCanceled())
        .map(AppointmentDto::copy)
        .collect(Collectors.toList());
  }
}
