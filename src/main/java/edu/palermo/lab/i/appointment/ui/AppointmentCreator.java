package edu.palermo.lab.i.appointment.ui;

import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.appointment.persistence.AppointmentDao;

public class AppointmentCreator extends ManagedPanel {

  private final AppointmentDao appointmentDao;

  public AppointmentCreator(final ScreenManager screenManager, final AppointmentDao appointmentDao) {
    super(screenManager);
    this.appointmentDao = appointmentDao;
  }

  @Override
  protected void doInitialize() {

  }
}
