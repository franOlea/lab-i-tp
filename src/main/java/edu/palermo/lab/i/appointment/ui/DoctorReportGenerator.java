package edu.palermo.lab.i.appointment.ui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.SecurityContext;
import edu.palermo.lab.i.appointment.AppointmentDto;
import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.ui.components.ActionAcknowledgePanel;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class DoctorReportGenerator extends ManagedPanel {
  
  @NonNull
  private final AppointmentDao appointmentDao;

  public DoctorReportGenerator(final @NonNull ScreenManager screenManager, @NonNull final AppointmentDao appointmentDao) {
    super(screenManager);
    this.appointmentDao = appointmentDao;
  }

  @Override
  protected void doInitialize() {
    this.setLayout(new GridLayout(3,1));

    DatePickerSettings fromDatePickerSettings = new DatePickerSettings();
    DatePickerSettings toDatePickerSettings = new DatePickerSettings();
    DatePicker fromDatePicker = new DatePicker(fromDatePickerSettings);
    DatePicker toDatePicker = new DatePicker(toDatePickerSettings);
    fromDatePickerSettings.setAllowEmptyDates(true);
    fromDatePickerSettings.setVetoPolicy(localDate -> !(
        localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
            localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)));
    toDatePickerSettings.setAllowEmptyDates(true);
    toDatePickerSettings.setVetoPolicy(localDate -> !(
        localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
            localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)));

    ActionAcknowledgePanel actionAcknowledgePanel = new ActionAcknowledgePanel(
        acceptListener(fromDatePicker, toDatePicker), cancelListener());
    actionAcknowledgePanel.initialize();
    actionAcknowledgePanel.setAcceptLabel("Generar");
    actionAcknowledgePanel.getAcceptButton().setEnabled(false);

    fromDatePicker.addDateChangeListener(
        e -> validateCompleteForm(fromDatePicker, toDatePicker, actionAcknowledgePanel));
    toDatePicker.addDateChangeListener(
        e -> validateCompleteForm(fromDatePicker, toDatePicker, actionAcknowledgePanel));

    this.add(fromDatePicker);
    this.add(toDatePicker);
    this.add(actionAcknowledgePanel);
  }

  private void validateCompleteForm(final DatePicker fromDatePicker, final DatePicker toDatePicker, final ActionAcknowledgePanel actionAcknowledgePanel) {
    if(fromDatePicker.getDate() != null && toDatePicker.getDate() != null) {
      actionAcknowledgePanel.getAcceptButton().setEnabled(true);
    } else {
      actionAcknowledgePanel.getAcceptButton().setEnabled(false);
    }
  }

  private ActionListener acceptListener(final DatePicker fromDatePicker, final DatePicker toDatePicker) {
    return e -> {
      LocalDate from = fromDatePicker.getDate();
      LocalDate to = toDatePicker.getDate();
      List<AppointmentDto> appointments = appointmentDao.getDoctorsAppointments(
          SecurityContext.getInstance().getCurrentUser(), from, to);
      long count = appointments.size();
      float earnings = count * SecurityContext.getInstance().getCurrentUser().getHourlyFee();
      String message = String.format("El medico ha cobrado $%.02f en %d turnos entre %s y %s.",
          earnings, count, from.toString(), to.toString());
      JOptionPane.showMessageDialog(this, message, "Reporte", JOptionPane.INFORMATION_MESSAGE);
    };
  }

  private ActionListener cancelListener() {
    return e -> goBack();
  }

}
