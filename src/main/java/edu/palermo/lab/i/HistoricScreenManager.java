package edu.palermo.lab.i;

import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Deque;

import static edu.palermo.lab.i.user.Role.ADMIN;
import static edu.palermo.lab.i.user.Role.DOCTOR;
import static edu.palermo.lab.i.user.Role.USER;

@RequiredArgsConstructor
public class HistoricScreenManager implements ScreenManager {

  private final ManagedPanelFactory managedPanelFactory;

  private static final int MAX_SIZE = 10;

  private final JFrame frame;
  private Deque<ManagedPanel> previousPanels = new ArrayDeque<>(MAX_SIZE);

  @Override
  public void switchTo(@NonNull final ManagedPanel panel) {
    if(previousPanels.size() >= MAX_SIZE) {
      previousPanels.removeFirst();
    }
    previousPanels.addLast(panel);
    doSwitch(panel);
  }

  @Override
  public void switchToDefault() {
    UserDto currentUser = SecurityContext.getInstance().getCurrentUser();
    if(currentUser == null) {
      switchTo(managedPanelFactory.createUserLogin(this));
    } else {
      Role currentUserRole = currentUser.getRole();
      if(currentUserRole == ADMIN) {
        switchTo(managedPanelFactory.createUserManager(this));
      } else if(currentUserRole == USER) {
        switchTo(managedPanelFactory.createUserLogin(this));
      } else if(currentUserRole == DOCTOR) {
        switchTo(managedPanelFactory.createUserLogin(this));
      } else {
        throw new IllegalStateException(String.format("There's an illegal role [%s] for the user.", currentUserRole));
      }
    }
  }

  @Override
  public void goBack() {
    if(previousPanels.isEmpty()) {
      throw new IllegalStateException("There is no previous panel.");
    }
    previousPanels.removeLast();
    ManagedPanel previousPanel = previousPanels.peekLast();
    doSwitch(previousPanel);
  }

  private void doSwitch(@NonNull final ManagedPanel panel) {
    frame.getContentPane().removeAll();
    panel.initialize();
    frame.getContentPane().add(panel);
    frame.getContentPane().validate();
    frame.getContentPane().repaint();
  }

}
