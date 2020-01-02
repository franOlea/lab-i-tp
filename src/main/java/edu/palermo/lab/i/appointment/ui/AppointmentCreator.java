package edu.palermo.lab.i.appointment.ui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.user.persistence.UserDao;
import edu.palermo.lab.i.user.ui.list.UserListModel;
import edu.palermo.lab.i.user.ui.list.doctor.DoctorListRenderer;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
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

    this.add(doctorsList);
    this.add(datePicker);
    this.add(timePicker);
  }

}
