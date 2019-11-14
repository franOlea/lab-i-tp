package edu.palermo.lab.i.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2DBInitializer {

  public static final String USERS_CREATE_TABLE = "CREATE TABLE `users` (\n" +
      "\t`user` VARCHAR(255) NOT NULL,\n" +
      "\t`password` VARCHAR(255) NOT NULL,\n" +
      "\t`firstName` VARCHAR(255),\n" +
      "\t`lastName` VARCHAR(255),\n" +
      "\t`enabled` BOOLEAN NOT NULL,\n" +
      "\tPRIMARY KEY (`user`)\n" +
      ");";

  public static void initializeTables(final Connection connection) throws SQLException {
    try(PreparedStatement preparedStatement = connection.prepareStatement(USERS_CREATE_TABLE)) {
      preparedStatement.execute();
    }
  }

}
