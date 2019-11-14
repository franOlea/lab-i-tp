package edu.palermo.lab.i.user.ui.list;

import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.user.ui.edit.UserEdit;
import edu.palermo.lab.i.user.persistence.UserDao;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class UserAdmin extends ManagedPanel {

  private final transient UserDao userDao;

  private final transient List<UserDto> users = new LinkedList<>();

  public UserAdmin(final ScreenManager manager, final UserDao userDao) {
    super(manager);
    this.userDao = userDao;
  }

  @Override
  protected void doInitialize() {
    this.setLayout(new GridLayout(1, 2));
    users.clear();
    users.addAll(userDao.getAll());

    UserTableModel tableModel = new UserTableModel(users);

    JPanel buttons = new JPanel();
    buttons.setLayout(new GridLayout(2, 1));
    JButton createButton = new JButton("Crear");
    createButton.addActionListener(createButtonClickListener());
    JButton editButton = new JButton("Modificar");
    editButton.setEnabled(false);
    buttons.add(createButton);
    buttons.add(editButton);

    JTable table = new JTable(tableModel);
    table.getSelectionModel().addListSelectionListener(listSelectionListener(editButton));
    table.addMouseListener(listDoubleClickSelectionListener());
    JScrollPane scroll = new JScrollPane(table);

    editButton.addActionListener(editButtonClickListener(table));

    this.add(scroll);
    this.add(buttons);
  }

  private MouseAdapter listDoubleClickSelectionListener() {
    return new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent mouseEvent) {
        JTable table =(JTable) mouseEvent.getSource();
        Point point = mouseEvent.getPoint();
        int row = table.rowAtPoint(point);
        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
          openUserEditForm(row);
        }
      }
    };
  }

  private ListSelectionListener listSelectionListener(final JButton editButton) {
    return event -> editButton.setEnabled(true);
  }

  private ActionListener createButtonClickListener() {
    return event -> screenManager.switchTo(createEditForm(new UserDto()));
  }

  private ActionListener editButtonClickListener(final JTable jTable) {
    return event -> {
      int row = jTable.getSelectedRow();
      openUserEditForm(row);
    };
  }

  private void openUserEditForm(final int row) {
    UserDto userDto = users.get(row);
    screenManager.switchTo(createEditForm(userDto));
  }

  private UserEdit createEditForm(final UserDto user) {
    return new UserEdit(screenManager, userDao, user);
  }

}
