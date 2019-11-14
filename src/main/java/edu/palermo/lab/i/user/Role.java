package edu.palermo.lab.i.user;

public enum Role {
  ADMIN("Administrador"),
  DOCTOR("Doctor"),
  USER("Paciente");

  private final String display;

  Role(String s) {
    display = s;
  }

  @Override
  public String toString() {
    return display;
  }

}
