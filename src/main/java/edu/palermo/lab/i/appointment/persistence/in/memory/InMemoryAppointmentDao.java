package edu.palermo.lab.i.appointment.persistence.in.memory;

import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.appointment.AppointmentDto;
import lombok.NonNull;

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
  public List<AppointmentDto> getDoctorsAppointments(final @NonNull String doctorId) {
    return appointments.stream()
        .filter(appointment -> appointment.getDoctorId().equals(doctorId) && !appointment.getCanceled())
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
