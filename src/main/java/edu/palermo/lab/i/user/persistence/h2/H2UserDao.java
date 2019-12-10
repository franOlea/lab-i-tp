package edu.palermo.lab.i.user.persistence.h2;

import edu.palermo.lab.i.user.Role;
import edu.palermo.lab.i.user.UserDto;
import edu.palermo.lab.i.user.persistence.UserDao;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class H2UserDao implements UserDao {

  private static final String QUERY_ALL = "select `user`, `firstName`, `lastName`, `enabled`, `password`, `role`, `hourly_fee` from `users`";
  private static final String ENABLED_TRUE = "`enabled` = 'true'";
  private static final String QUERY_ALL_ENABLED = QUERY_ALL + " where " + ENABLED_TRUE + " and `role` = ?";
  private static final String QUERY_BY_ID = QUERY_ALL + " where `user` = ?";
  private static final String QUERY_BY_LOGIN = QUERY_BY_ID + " and `password` = ? and " + ENABLED_TRUE;
  private static final String INSERT_DOCTOR = "insert into `users` " +
      "(`user`, `password`, `firstName`, `lastName`, `role`, `hourly_fee`, `enabled`) values (?, ?, ?, ?, ?, ?, 'true') " +
      "on duplicate key update `firstName` = values(`firstName`), `lastName` = values(`lastName`), " +
      "`enabled` = values(`enabled`), `role` = values(`role`), `hourly_fee` = values(`hourly_fee`)";

  private final Connection connection;
  private final H2UserMapper mapper;

  public H2UserDao(@NonNull final Connection connection, @NonNull final H2UserMapper mapper) {
    this.connection = connection;
    this.mapper = mapper;
  }

  @SneakyThrows
  @Override
  public Optional<UserDto> getByLogin(@NonNull final String user, @NonNull final String password) {
    try(PreparedStatement statement = connection.prepareStatement(QUERY_BY_LOGIN)) {
      statement.setString(1, user);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet).stream().findFirst();
    }
  }

  @SneakyThrows
  @Override
  public Optional<UserDto> getById(@NonNull final String user) {
    try(PreparedStatement statement = connection.prepareStatement(QUERY_BY_ID)) {
      statement.setString(1, user);
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet).stream().findFirst();
    }
  }

  @Override
  public List<UserDto> getAll() {
    return getList(QUERY_ALL);
  }

  @SneakyThrows
  @Override
  public List<UserDto> getAllEnabledByRole(@NonNull final Role role) {
    try(PreparedStatement statement = connection.prepareStatement(QUERY_ALL_ENABLED)) {
      statement.setString(1, role.name());
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet);
    }
  }

  @SneakyThrows
  @Override
  public void save(@NonNull final UserDto userDto) {
    validate(userDto);
    try(PreparedStatement statement = connection.prepareStatement(INSERT_DOCTOR)) {
      statement.setString(1, userDto.getId());
      statement.setString(2, userDto.getPassword());
      statement.setString(3, userDto.getFirstName());
      statement.setString(4, userDto.getLastName());
      statement.setString(5, userDto.getRole().name());
      statement.setFloat(6, userDto.getHourlyFee());
      statement.executeUpdate();
    }
  }

  private void validate(final @NonNull UserDto userDto) {
    if(userDto.getId() == null || userDto.getPassword() == null) {
      throw new IllegalArgumentException("The id must have a id and a password to save it.");
    }
  }

  @SneakyThrows
  private List<UserDto> getList(final String query) {
    try(PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      return mapper.map(resultSet);
    }
  }

}
