package edu.palermo.lab.i;

import edu.palermo.lab.i.appointment.list.AppointmentList;
import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.user.persistence.UserDao;
import edu.palermo.lab.i.user.ui.list.UserAdmin;
import edu.palermo.lab.i.user.ui.login.UserLogin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManagedPanelFactory {
  @NonNull
  private final UserDao userDao;
  @NonNull
  private final AppointmentDao appointmentDao;

  public ManagedPanel createUserLogin(final ScreenManager manager) {
    return new UserLogin(manager, userDao);
  }

  public ManagedPanel createAppointmentManager(final ScreenManager manager) {
    return new AppointmentList(manager, appointmentDao, userDao);
  }

  public ManagedPanel createUserManager(final ScreenManager manager) {
    return new UserAdmin(manager, userDao);
  }

}
