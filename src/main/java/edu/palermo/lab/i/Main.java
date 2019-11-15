package edu.palermo.lab.i;

import edu.palermo.lab.i.h2.H2ConnectionFactory;
import edu.palermo.lab.i.h2.H2DBInitializer;
import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.user.ui.list.UserAdmin;
import edu.palermo.lab.i.user.persistence.UserDao;
import edu.palermo.lab.i.user.persistence.h2.H2UserDao;
import edu.palermo.lab.i.user.persistence.h2.H2UserMapper;
import lombok.SneakyThrows;

import javax.swing.*;
import java.sql.Connection;

public class Main {


  public static void main(final String[] args) {
    ConnectionFactory h2ConnectionFactory = new H2ConnectionFactory();
    Connection connection = h2ConnectionFactory.create();
    initializeH2Tables(connection);

    UserDao userDao = new H2UserDao(connection, new H2UserMapper());

    UserDto userDto = new UserDto();
    userDto.setPassword("admin");
    userDto.setFirstName("nombre");
    userDto.setLastName("apellido");
    userDto.setId("admin");
    userDto.setRole(Role.ADMIN);
    userDao.save(userDto);

    JFrame frame = new JFrame("Turnera Medica");
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    ScreenManager screenManager = new HistoricScreenManager(frame);

    UserAdmin userEditForm = new UserAdmin(screenManager, userDao);
    screenManager.switchTo(userEditForm);
    frame.setBounds(100, 100, 500, 500);
  }

  @SneakyThrows
  private static void initializeH2Tables(final Connection connection) {
    H2DBInitializer.initializeTables(connection);
  }

}
