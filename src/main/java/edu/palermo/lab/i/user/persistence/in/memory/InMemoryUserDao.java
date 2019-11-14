package edu.palermo.lab.i.user.persistence.in.memory;

import edu.palermo.lab.i.user.persistence.UserDao;
import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryUserDao implements UserDao {

  private final List<UserDto> users = new LinkedList<>();

  @Override
  public Optional<UserDto> getByLogin(@NonNull final String id, @NonNull final String password) {
    return users.stream()
        .filter(user -> user.getId().equals(id) && user.getPassword().equals(password) && user.getEnabled())
        .findFirst()
        .map(UserDto::copy);
  }

  @Override
  public Optional<UserDto> getById(@NonNull final String id) {
    return users.stream()
        .filter(user -> user.getId().equals(id))
        .findFirst()
        .map(UserDto::copy);
  }

  @Override
  public List<UserDto> getAll() {
    return users.stream()
        .map(UserDto::copy)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserDto> getAllEnabled() {
    return users.stream()
        .filter(UserDto::getEnabled)
        .map(UserDto::copy)
        .collect(Collectors.toList());
  }

  @Override
  public void save(@NonNull final UserDto user) {
    Optional<UserDto> potentialExistingUser = users.stream()
        .filter(savedUser -> savedUser.getId().equals(user.getId()))
        .findFirst();
    if(potentialExistingUser.isPresent()) {
      UserDto savedUser = potentialExistingUser.get();
      savedUser.setFirstName(user.getFirstName());
      savedUser.setLastName(user.getLastName());
      savedUser.setEnabled(user.getEnabled());
      savedUser.setPassword(user.getPassword());
    } else {
      users.add(user);
    }
  }

}
