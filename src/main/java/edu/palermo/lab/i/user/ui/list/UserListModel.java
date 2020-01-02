package edu.palermo.lab.i.user.ui.list;

import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;

import javax.swing.*;
import java.util.List;

public class UserListModel extends AbstractListModel<UserDto> implements ComboBoxModel<UserDto> {

  @NonNull
  private final List<UserDto> users;

  private UserDto selectedUser;

  public UserListModel(@NonNull final List<UserDto> users) {
    this.users = users;
    this.selectedUser = users.stream().findFirst().orElse(null);
  }

  @Override
  public void setSelectedItem(@NonNull final Object selectedDoctor) {
    this.selectedUser = (UserDto) selectedDoctor;
  }

  @Override
  public Object getSelectedItem() {
    return selectedUser;
  }

  @Override
  public int getSize() {
    return users.size();
  }

  @Override
  public UserDto getElementAt(final int index) {
    return users.get(index);
  }
}
