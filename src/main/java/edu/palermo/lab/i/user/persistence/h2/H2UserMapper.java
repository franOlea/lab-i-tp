package edu.palermo.lab.i.user.persistence.h2;

import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class H2UserMapper {

  @SneakyThrows
  List<UserDto> map(@NonNull final ResultSet resultSet) {
    List<UserDto> doctors = new LinkedList<>();
    while(resultSet.next()) {
      UserDto user = new UserDto();
      user.setId(resultSet.getString("user"));
      user.setFirstName(resultSet.getString("firstName"));
      user.setLastName(resultSet.getString("lastName"));
      user.setEnabled(resultSet.getBoolean("enabled"));
      user.setPassword(resultSet.getString("password"));
      user.setHourlyFee(resultSet.getFloat("hourly_fee"));
      user.setRole(Role.valueOf(resultSet.getString("role")));
      doctors.add(user);
    }
    return doctors;
  }
}
