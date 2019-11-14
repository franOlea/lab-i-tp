package edu.palermo.lab.i;

import lombok.RequiredArgsConstructor;

import javax.swing.*;

@RequiredArgsConstructor
public abstract class ManagedPanel extends JPanel {

  protected final ScreenManager screenManager;

  protected void initialize() {
    this.removeAll();
    this.updateUI();
    this.doInitialize();
  }

  protected abstract void doInitialize();

}
