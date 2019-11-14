package edu.palermo.lab.i.user.persistence;

import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserDao {

  Optional<UserDto> getByLogin(@NonNull final String user, @NonNull final String password);
  Optional<UserDto> getById(@NonNull final String user);
  List<UserDto> getAll();
  List<UserDto> getAllEnabled();
  void save(@NonNull final UserDto userDto);

}
