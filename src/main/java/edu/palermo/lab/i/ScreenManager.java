package edu.palermo.lab.i;

import lombok.NonNull;

public interface ScreenManager {
  void initialize();
  void switchTo(@NonNull ManagedPanel panel);
  void switchToDefault();
  void goBack();
}
