package edu.palermo.lab.i.ui.components;

import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.SecurityContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.swing.*;

@RequiredArgsConstructor
public class MenuBar extends JMenuBar {
  @NonNull
  private final ScreenManager manager;

  public void initialize() {
    JMenu session = new JMenu("Sesión");

    JMenuItem logOut = new JMenuItem("Cambiar Usuario");
    logOut.addActionListener(event -> {
      SecurityContext.getInstance().setCurrentUser(null);
      manager.switchToDefault();
    });

    session.add(logOut);
    add(session);
  }

}
