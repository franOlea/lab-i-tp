package edu.palermo.lab.i;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import javax.swing.*;

@RequiredArgsConstructor
public class OneBackScreenManager implements ScreenManager {

  private final JFrame frame;
  private ManagedPanel currentPanel;
  private ManagedPanel previousPanel;

  @Override
  public void switchTo(@NonNull final ManagedPanel panel) {
    doSwitch(panel);
    previousPanel = currentPanel;
    currentPanel = panel;
    currentPanel.initialize();
  }

  @Override
  public void goBack() {
    if(previousPanel == null) {
      throw new IllegalStateException("There is no previous panel.");
    }
    ManagedPanel temp = currentPanel;
    doSwitch(previousPanel);
    currentPanel = previousPanel;
    previousPanel = temp;
    currentPanel.initialize();
  }

  private void doSwitch(@NonNull final ManagedPanel panel) {
    frame.getContentPane().removeAll();
    frame.getContentPane().add(panel);
    frame.getContentPane().validate();
    frame.getContentPane().repaint();
  }

}
