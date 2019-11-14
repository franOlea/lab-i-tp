package edu.palermo.lab.i.user.ui.edit;

import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.ManagedPanel;
import edu.palermo.lab.i.ScreenManager;
import edu.palermo.lab.i.user.persistence.UserDao;

import javax.swing.*;
import java.awt.*;

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
    this.setLayout(new GridLayout(7,2));

    JLabel idLabel = new JLabel("Id:");
    JTextField idField = new JTextField();
    idField.setText(userDto.getId());
    if(userDto.getId() != null && !userDto.getId().isEmpty()) {
      idField.setEnabled(false);
      create = false;
    }
    this.add(idLabel);
    this.add(idField);

    JLabel firstNameLabel = new JLabel("Nombre:");
    JTextField firstNameField = new JTextField();
    firstNameField.setText(userDto.getFirstName());
    this.add(firstNameLabel);
    this.add(firstNameField);

    JLabel lastNameLabel = new JLabel("Apellido:");
    JTextField lastNameField = new JTextField();
    lastNameField.setText(userDto.getLastName());
    this.add(lastNameLabel);
    this.add(lastNameField);

    JLabel passwordLabel = new JLabel("Contraseña:");
    JPasswordField passwordField = new JPasswordField();
    passwordField.setText(userDto.getPassword());
    this.add(passwordLabel);
    this.add(passwordField);

    JLabel roleLabel = new JLabel("Rol:");
    JComboBox<Role> roleField = new JComboBox<>(Role.values());
    roleField.setSelectedItem(userDto.getRole());
    this.add(roleLabel);
    this.add(roleField);

    JLabel enabledLabel = new JLabel("Habilitado");
    JCheckBox enabledCheckBox = new JCheckBox();
    enabledCheckBox.setSelected(userDto.getEnabled());
    this.add(enabledLabel);
    this.add(enabledCheckBox);

    JButton saveButton = new JButton("Guardar");
    JButton cancelButton = new JButton("Cancelar");
    this.add(saveButton);
    this.add(cancelButton);

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
  }

}
