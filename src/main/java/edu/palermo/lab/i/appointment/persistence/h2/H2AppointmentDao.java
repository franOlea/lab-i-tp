package edu.palermo.lab.i.appointment.persistence.h2;

import edu.palermo.lab.i.appointment.AppointmentDto;
import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class H2AppointmentDao implements AppointmentDao {

  private static final String QUERY_ALL = "select `id`, `doctor`, `patient`, `timestamp`, `canceled` from `appointments` where `canceled` = 'false'";
  private static final String BY_DOCTOR = " and `doctor` = ?";
  private static final String BY_DOCTOR_AND_DATE = BY_DOCTOR + " and `timestamp` >= ? and `timestamp` < ?";
  private static final String BY_PATIENT = " and `patient` = ?";
  private static final String INSERT = "insert into `appointments` (`doctor`, `patient`, `timestamp`, `canceled`) values (?, ?, ?, ?)";
  private static final String CANCEL = "UPDATE `appointments` SET `canceled` = 'true' WHERE `id` = ?";

  private final Connection connection;
  private final H2AppointmentMapper mapper;

  @SneakyThrows
  @Override
  public void save(final @NonNull AppointmentDto appointment) {
    validate(appointment);
    try(PreparedStatement statement = connection.prepareStatement(INSERT)) {
      statement.setString(1, appointment.getDoctorId());
      statement.setString(2, appointment.getPatientId());
      statement.setLong(3, appointment.getTimestamp());
      statement.setBoolean(4, appointment.getCanceled());
      statement.executeUpdate();
    }
  }

  @SneakyThrows
  @Override
  public void cancel(final @NonNull Long appointmentId) {
    try(PreparedStatement statement = connection.prepareStatement(CANCEL)) {
      statement.setLong(1, appointmentId);
      statement.executeUpdate();
    }
  }

  @SneakyThrows
  @Override
  public List<AppointmentDto> getDoctorsAppointments(final @NonNull UserDto doctor) {
    try(PreparedStatement statement = connection.prepareStatement(QUERY_ALL + BY_DOCTOR)) {
      statement.setString(1, doctor.getId());
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet);
    }
  }

  @SneakyThrows
  @Override
  public List<AppointmentDto> getDoctorsAppointments(final @NonNull UserDto doctor, final @NonNull LocalDate localDate) {
    try(PreparedStatement statement = connection.prepareStatement(QUERY_ALL + BY_DOCTOR_AND_DATE)) {
      ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneOffset.UTC);
      long from = Instant.from(zonedDateTime).toEpochMilli();
      long to = Instant.from(zonedDateTime.plus(1, ChronoUnit.DAYS)).toEpochMilli();
      statement.setString(1, doctor.getId());
      statement.setLong(2, from);
      statement.setLong(3, to);
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet);
    }
  }

  @SneakyThrows
  @Override
  public List<AppointmentDto> getDoctorsAppointments(final @NonNull UserDto doctor, final @NonNull LocalDate fromDate, final @NonNull LocalDate toDate) {
    try(PreparedStatement statement = connection.prepareStatement(QUERY_ALL + BY_DOCTOR_AND_DATE)) {
      long from = Instant.from(fromDate.atStartOfDay(ZoneOffset.UTC)).toEpochMilli();
      long to = Instant.from(toDate.atStartOfDay(ZoneOffset.UTC)).toEpochMilli();
      statement.setString(1, doctor.getId());
      statement.setLong(2, from);
      statement.setLong(3, to);
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet);
    }
  }

  @SneakyThrows
  @Override
  public List<AppointmentDto> getPatientsAppointments(final @NonNull String userId) {
    try(PreparedStatement statement = connection.prepareStatement(QUERY_ALL + BY_PATIENT)) {
      statement.setString(1, userId);
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet);
    }
  }

  private void validate(final AppointmentDto appointmentDto) {
    if(appointmentDto.getDoctorId() == null || appointmentDto.getPatientId() == null || appointmentDto.getTimestamp() == null) {
      throw new IllegalArgumentException("The appointment must have a doctor, a patient and a timestamp.");
    }
  }
}
