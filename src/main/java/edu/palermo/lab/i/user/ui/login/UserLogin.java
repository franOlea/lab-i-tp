package edu.palermo.lab.i.user.ui.login;

import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.SecurityContext;
import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.user.persistence.UserDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;

public class UserLogin extends ManagedPanel {

  private final UserDao userDao;

  public UserLogin(final ScreenManager screenManager, final UserDao userDao) {
    super(screenManager);
    this.userDao = userDao;
  }

  @Override
  protected void doInitialize() {
    this.setLayout(new GridLayout(2, 1));

    JPanel form = new JPanel();
    form.setLayout(new GridLayout(2, 2));
    JLabel userLabel = new JLabel("Usuario:");
    JTextField userField = new JTextField();
    form.add(userLabel);
    form.add(userField);
    JLabel passwordLabel = new JLabel("Contraseña:");
    JPasswordField passwordField = new JPasswordField();
    form.add(passwordLabel);
    form.add(passwordField);

    JButton loginButton = new JButton("Ingresar");
    loginButton.addActionListener(loginButtonListener(userField.getText(), new String(passwordField.getPassword())));


    this.add(form);
    this.add(loginButton);
  }

  private ActionListener loginButtonListener(final String user, final String password) {
    return listener -> {
      Optional<UserDto> possibleUser = userDao.getByLogin(user, password);
      if(possibleUser.isPresent()) {
        SecurityContext.getInstance().setCurrentUser(possibleUser.get());
        screenManager.switchToDefault();
      } else {
        JOptionPane.showMessageDialog(this,
            "El usuario o la contraseña son incorrectos.", "Login invalido!",
            JOptionPane.ERROR_MESSAGE);
      }
    };
  }
}
