package edu.palermo.lab.i.appointment.ui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.SecurityContext;
import edu.palermo.lab.i.appointment.AppointmentDto;
import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.ui.components.ActionAcknowledgePanel;
import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.user.persistence.UserDao;
import edu.palermo.lab.i.user.ui.list.UserListModel;
import edu.palermo.lab.i.user.ui.list.doctor.DoctorListRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;

public class AppointmentCreator extends ManagedPanel {

  private final AppointmentDao appointmentDao;
  private final UserDao userDao;

  public AppointmentCreator(final ScreenManager screenManager, final AppointmentDao appointmentDao, final UserDao userDao) {
    super(screenManager);
    this.appointmentDao = appointmentDao;
    this.userDao = userDao;
  }

  @Override
  protected void doInitialize() {
    this.setLayout(new GridLayout(4,1));

    JComboBox<UserDto> doctorsList = new JComboBox<>(
        new UserListModel(userDao.getAllEnabledByRole(Role.DOCTOR)));
    doctorsList.setRenderer(new DoctorListRenderer());

    DatePickerSettings datePickerSettings = new DatePickerSettings();
    DatePicker datePicker = new DatePicker(datePickerSettings);
    datePickerSettings.setAllowEmptyDates(true);
    datePickerSettings.setVetoPolicy(localDate -> !(
        localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
        localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)));

    TimePickerSettings timePickerSettings = new TimePickerSettings();
    TimePicker timePicker = new TimePicker(timePickerSettings);
    timePickerSettings.generatePotentialMenuTimes(
        TimePickerSettings.TimeIncrement.OneHour, LocalTime.of(9, 0), LocalTime.of(18, 0));
    timePickerSettings.setAllowEmptyTimes(true);

    datePicker.addDateChangeListener(dateChangeEvent -> {
      UserDto selectedDoctor = (UserDto) doctorsList.getSelectedItem();
      List<Integer> doctorsBusyHours = appointmentDao
          .getDoctorsAppointments(selectedDoctor, dateChangeEvent.getNewDate())
          .stream()
          .map(appointment -> {
            Instant instant = Instant.ofEpochMilli(appointment.getTimestamp());
            return instant.get(HOUR_OF_DAY);
          })
          .collect(Collectors.toList());
      timePickerSettings.setVetoPolicy(
          localTime -> doctorsBusyHours.stream().noneMatch(hour -> localTime.getHour() == hour));
    });

    ActionAcknowledgePanel actionAcknowledgePanel = new ActionAcknowledgePanel(
        saveListener(doctorsList, datePicker, timePicker), cancelListener());
    actionAcknowledgePanel.initialize();
    actionAcknowledgePanel.getAcceptButton().setEnabled(false);

    doctorsList.addItemListener(
        itemEvent -> validateCompleteForm(doctorsList, datePicker, timePicker, actionAcknowledgePanel));
    datePicker.addDateChangeListener(
        dateChangeEvent -> validateCompleteForm(doctorsList, datePicker, timePicker, actionAcknowledgePanel));
    timePicker.addTimeChangeListener(
        timeChangeEvent -> validateCompleteForm(doctorsList, datePicker, timePicker, actionAcknowledgePanel));

    this.add(doctorsList);
    this.add(datePicker);
    this.add(timePicker);
    this.add(actionAcknowledgePanel);
  }

  private void validateCompleteForm(final JComboBox<UserDto> doctorsList, final DatePicker datePicker, final TimePicker timePicker, final ActionAcknowledgePanel actionAcknowledgePanel) {
    if(doctorsList.getSelectedItem() != null && datePicker.getDate() != null && timePicker.getTime() != null) {
      actionAcknowledgePanel.getAcceptButton().setEnabled(true);
    } else {
      actionAcknowledgePanel.getAcceptButton().setEnabled(false);
    }
  }

  private ActionListener saveListener(final JComboBox<UserDto> doctorsList,
                                      final DatePicker datePicker,
                                      final TimePicker timePicker) {
    return e -> createAppointment(doctorsList, datePicker, timePicker).ifPresent(appointmentDto -> {
      appointmentDao.save(appointmentDto);
      goBack();
    });
  }

  private ActionListener cancelListener() {
    return e -> goBack();
  }

  private Optional<AppointmentDto> createAppointment(final JComboBox<UserDto> doctorsList,
                                                     final DatePicker datePicker,
                                                     final TimePicker timePicker) {
    UserDto doctor = (UserDto) doctorsList.getSelectedItem();
    LocalDate date = datePicker.getDate();
    LocalTime time = timePicker.getTime();
    if(doctor == null || date == null || time == null) {
      return Optional.empty();
    }
    long timestamp = LocalDateTime.of(date, time)
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli();
    UserDto patient = SecurityContext.getInstance().getCurrentUser();
    AppointmentDto dto = new AppointmentDto();
    dto.setDoctorId(doctor.getId());
    dto.setTimestamp(timestamp);
    dto.setPatientId(patient.getId());
    dto.setCanceled(false);
    return Optional.of(dto);
  }

}
