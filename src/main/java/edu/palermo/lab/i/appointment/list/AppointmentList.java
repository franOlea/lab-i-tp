package edu.palermo.lab.i.appointment.list;

import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.SecurityContext;
import edu.palermo.lab.i.appointment.AppointmentDto;
import edu.palermo.lab.i.appointment.persistence.AppointmentDao;
import edu.palermo.lab.i.appointment.ui.AppointmentCreator;
import edu.palermo.lab.i.user.persistence.UserDao;
import lombok.NonNull;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class AppointmentList extends ManagedPanel {

  @NonNull
  private final transient AppointmentDao appointmentDao;
  @NonNull
  private final transient UserDao userDao;

  private final transient List<AppointmentDto> appointments = new LinkedList<>();

  public AppointmentList(final ScreenManager screenManager,
                          final AppointmentDao appointmentDao,
                          final UserDao userDao) {
    super(screenManager);
    this.appointmentDao = appointmentDao;
    this.userDao = userDao;
  }

  @Override
  protected void doInitialize() {
    this.setLayout(new GridLayout(1, 2));
    appointments.clear();
    String currentUserId = SecurityContext.getInstance().getCurrentUser().getId();
    appointments.addAll(appointmentDao.getPatientsAppointments(currentUserId));

    AppointmentTableModel tableModel = new AppointmentTableModel(appointments, userDao);

    JPanel buttons = new JPanel();
    buttons.setLayout(new GridLayout(2, 1));
    JButton createButton = new JButton("Crear");
    createButton.addActionListener(createButtonClickListener());
    JButton cancelButton = new JButton("Cancelar");
    cancelButton.setEnabled(false);
    buttons.add(createButton);
    buttons.add(cancelButton);

    JTable table = new JTable(tableModel);
    table.getSelectionModel().addListSelectionListener(listSelectionListener(cancelButton));

    JScrollPane scroll = new JScrollPane(table);
    cancelButton.addActionListener(cancelButtonClickListener(table));

    this.add(scroll);
    this.add(buttons);
  }

  private ActionListener cancelButtonClickListener(final JTable table) {
    return event -> {
      int result = JOptionPane.showConfirmDialog(this,
          "Esta seguro de que quiere cancelar el turno?", "Cancelar turno",
          JOptionPane.OK_CANCEL_OPTION);
      if(result == JOptionPane.OK_OPTION) {
        int row = table.getSelectedRow();
        AppointmentDto appointment = appointments.remove(row);
        appointment.setCanceled(true);
        table.repaint();
      }
    };
  }

  private ListSelectionListener listSelectionListener(final JButton cancelButton) {
    return event -> cancelButton.setEnabled(true);
  }

  private ActionListener createButtonClickListener() {
    return event -> screenManager
        .switchTo(new AppointmentCreator(screenManager, appointmentDao, userDao));
  }


}
