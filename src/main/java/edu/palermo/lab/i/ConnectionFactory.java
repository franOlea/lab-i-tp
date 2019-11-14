package edu.palermo.lab.i;

import java.sql.Connection;

public interface ConnectionFactory {
  Connection create();
}
