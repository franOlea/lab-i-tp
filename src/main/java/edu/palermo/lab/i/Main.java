package edu.palermo.lab.i;

import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.appointment.persistence.h2.H2AppointmentDao;
import edu.palermo.lab.i.appointment.persistence.h2.H2AppointmentMapper;
import edu.palermo.lab.i.h2.H2ConnectionFactory;
import edu.palermo.lab.i.h2.H2DBInitializer;
import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
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
    AppointmentDao appointmentDao = new H2AppointmentDao(connection, new H2AppointmentMapper());
    initializeUsers(userDao);
    JFrame frame = createFrame();
    ManagedPanelFactory managedPanelFactory = new ManagedPanelFactory(userDao, appointmentDao);
    ScreenManager screenManager = new HistoricScreenManager(managedPanelFactory, frame);
    screenManager.initialize();
    screenManager.switchToDefault();
  }

  private static JFrame createFrame() {
    JFrame frame = new JFrame("Turnera Medica");
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setBounds(100, 100, 500, 500);
    return frame;
  }

  private static void initializeUsers(final UserDao userDao) {
    UserDto userDto = new UserDto();
    userDto.setPassword("user");
    userDto.setFirstName("Esteban");
    userDto.setLastName("Quito");
    userDto.setId("user");
    userDto.setRole(Role.USER);
    UserDto adminDto = new UserDto();
    adminDto.setPassword("admin");
    adminDto.setFirstName("Natalia");
    adminDto.setLastName("Natalia");
    adminDto.setId("admin");
    adminDto.setRole(Role.ADMIN);
    UserDto doctorDto = new UserDto();
    doctorDto.setPassword("doc");
    doctorDto.setFirstName("Paquita");
    doctorDto.setLastName("Navajas");
    doctorDto.setId("doc");
    doctorDto.setHourlyFee(123.4f);
    doctorDto.setRole(Role.DOCTOR);

    userDao.save(userDto);
    userDao.save(adminDto);
    userDao.save(doctorDto);
  }

  @SneakyThrows
  private static void initializeH2Tables(final Connection connection) {
    H2DBInitializer.initializeTables(connection);
  }

}
