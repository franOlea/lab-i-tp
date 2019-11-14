package edu.palermo.lab.i.h2;

import edu.palermo.lab.i.ConnectionFactory;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionFactory implements ConnectionFactory {

	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_BASE_URL = "jdbc:h2:mem:;MODE=MySQL";
	private static final String DB_USERNAME = "sa";
	private static final String DB_PASS = "";

	public Connection create() {
    try {
      return doCreate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Connection doCreate() throws Exception {
    Class.forName(DB_DRIVER);
    Connection connection = DriverManager.getConnection(DB_BASE_URL, DB_USERNAME, DB_PASS);
    connection.setAutoCommit(false);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> closeConnection(connection)));
    return connection;
  }

  @SneakyThrows
  private void closeConnection(final Connection connection) {
    connection.close();
  }

}
