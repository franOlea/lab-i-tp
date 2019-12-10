package edu.palermo.lab.i.user.ui.edit;

import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.user.persistence.UserDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserEdit extends ManagedPanel {

  private final transient UserDao userDao;
  private transient UserDto userDto;
  private transient boolean create = true;

  public UserEdit(final ScreenManager manager, final UserDao userDao, final UserDto userDto) {
    super(manager);
    this.userDao = userDao;
    this.userDto = userDto;
  }

  @Override
  protected void doInitialize() {
    this.setLayout(new GridLayout(8,2));

    JLabel idLabel = new JLabel("Id:");
    JTextField idField = new JTextField();
    idField.setText(userDto.getId());
    if(userDto.getId() != null && !userDto.getId().isEmpty()) {
      idField.setEnabled(false);
      create = false;
    }

    JLabel firstNameLabel = new JLabel("Nombre:");
    JTextField firstNameField = new JTextField();
    firstNameField.setText(userDto.getFirstName());

    JLabel lastNameLabel = new JLabel("Apellido:");
    JTextField lastNameField = new JTextField();
    lastNameField.setText(userDto.getLastName());

    JLabel passwordLabel = new JLabel("Contraseña:");
    JPasswordField passwordField = new JPasswordField();
    passwordField.setText(userDto.getPassword());

    JLabel doctorFeeLabel = new JLabel("Honorarios por hora:");
    JTextField doctorFeeField = new JTextField();
    if(userDto.getRole().equals(Role.DOCTOR)) {
      doctorFeeField.setText(userDto.getHourlyFee().toString());
      doctorFeeField.setEditable(true);
      doctorFeeField.setVisible(true);
    } else {
      doctorFeeField.setEditable(false);
      doctorFeeField.setVisible(false);
    }
    doctorFeeField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent event) {
        String value = doctorFeeField.getText();
        doctorFeeField.setText(value.replace("[^0-9]", ""));
      }
    });

    JLabel roleLabel = new JLabel("Rol:");
    JComboBox<Role> roleField = new JComboBox<>(Role.values());
    roleField.setSelectedItem(userDto.getRole());
    roleField.addItemListener(itemListener -> {
      Role selectedRole = (Role) itemListener.getItem();
      if(selectedRole == Role.DOCTOR) {
        if(create) {
          doctorFeeField.setText("0");
        } else {
          Float hourlyFee = userDto.getHourlyFee();
          doctorFeeField.setText(hourlyFee == null ? "0" : hourlyFee.toString());
        }
        doctorFeeField.setEditable(true);
        doctorFeeField.setVisible(true);
      } else {
        doctorFeeField.setEditable(false);
        doctorFeeField.setVisible(false);
      }
    });

    JLabel enabledLabel = new JLabel("Habilitado");
    JCheckBox enabledCheckBox = new JCheckBox();
    enabledCheckBox.setSelected(userDto.getEnabled());

    JButton saveButton = new JButton("Guardar");
    JButton cancelButton = new JButton("Cancelar");

    saveButton.addActionListener(action -> {
      if(idField.getText().isEmpty() || passwordField.getPassword().length == 0) {
        JOptionPane.showMessageDialog(this,
            "El usuario debe tener id y contraseña para poder guardarse.", "Error!",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      if(!enabledCheckBox.isSelected()) {
        int choiceResult = JOptionPane.showConfirmDialog(this,
            "Esta a punto de deshabilitar a un usuario, el usuario no podra volver a acceder al sistema.", "Atención",
            JOptionPane.OK_CANCEL_OPTION);
        if(choiceResult == JOptionPane.CANCEL_OPTION) {
          return;
        }
      }
      userDto.setId(idField.getText());
      userDto.setFirstName(firstNameField.getText());
      userDto.setLastName(lastNameField.getText());
      userDto.setPassword(new String(passwordField.getPassword()));
      userDto.setRole((Role) roleField.getSelectedItem());
      userDto.setEnabled(enabledCheckBox.isSelected());
      if(userDto.getRole().equals(Role.DOCTOR)) {
        if(doctorFeeField.getText().isEmpty()) {
          doctorFeeField.setText("0");
        }
        userDto.setHourlyFee(Float.valueOf(doctorFeeField.getText()));
      }

      if(create && userDao.getById(userDto.getId()).isPresent()) {
        JOptionPane.showMessageDialog(this,
            "Ya existe un usuario con ese id, por favor elija otro.", "Error!",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      userDao.save(userDto);
      screenManager.goBack();
    });
    cancelButton.addActionListener(event -> screenManager.goBack());

    this.add(idLabel);
    this.add(idField);
    this.add(firstNameLabel);
    this.add(firstNameField);
    this.add(lastNameLabel);
    this.add(lastNameField);
    this.add(passwordLabel);
    this.add(passwordField);
    this.add(roleLabel);
    this.add(roleField);
    this.add(enabledLabel);
    this.add(enabledCheckBox);
    this.add(doctorFeeLabel);
    this.add(doctorFeeField);
    this.add(saveButton);
    this.add(cancelButton);
  }

}
