package edu.palermo.lab.i.h2;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class H2DBInitializer {

  public static final String USERS_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `users` (\n" +
      "\t`user` VARCHAR(255) NOT NULL,\n" +
      "\t`password` VARCHAR(255) NOT NULL,\n" +
      "\t`firstName` VARCHAR(255),\n" +
      "\t`lastName` VARCHAR(255),\n" +
      "\t`enabled` BOOLEAN NOT NULL,\n" +
      "\t`role` VARCHAR(255) NOT NULL,\n" +
      "\t`hourly_fee` FLOAT NOT NULL,\n" +
      "\tPRIMARY KEY (`user`)\n" +
      ");";

  public static final String APPOINTMENT_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `appointments` (\n" +
      "\t`id` BIGINT AUTO_INCREMENT NOT NULL,\n" +
      "\t`doctor` VARCHAR(255) NOT NULL,\n" +
      "\t`patient` VARCHAR(255) NOT NULL,\n" +
      "\t`timestamp` BIGINT NOT NULL,\n" +
      "\t`canceled` BOOLEAN NOT NULL,\n" +
      "\tPRIMARY KEY (`id`)\n" +
      ");";

  @SneakyThrows
  public static void initializeTables(final Connection connection) {
    createTable(connection, USERS_CREATE_TABLE);
    createTable(connection, APPOINTMENT_CREATE_TABLE);
  }

  @SneakyThrows
  private static void createTable(final Connection connection, final String statement) {
    try(PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
      preparedStatement.execute();
    }
  }

}
