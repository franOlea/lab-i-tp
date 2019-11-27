package edu.palermo.lab.i;

import lombok.NonNull;

public interface ScreenManager {
  void switchTo(@NonNull ManagedPanel panel);
  void switchToDefault();
  void goBack();
}
