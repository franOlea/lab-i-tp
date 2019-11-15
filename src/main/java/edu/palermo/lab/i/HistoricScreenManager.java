package edu.palermo.lab.i;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.Deque;

@RequiredArgsConstructor
public class HistoricScreenManager implements ScreenManager {

  private static final int MAX_SIZE = 10;

  private final JFrame frame;
  private Deque<ManagedPanel> previousPanels = new ArrayDeque<>(MAX_SIZE);

  public void switchTo(@NonNull final ManagedPanel panel) {
    if(previousPanels.size() >= MAX_SIZE) {
      previousPanels.removeFirst();
    }
    previousPanels.addLast(panel);
    doSwitch(panel);
  }

  public void goBack() {
    if(previousPanels.isEmpty()) {
      throw new IllegalStateException("There is no previous panel.");
    }
    previousPanels.removeLast();
    ManagedPanel previousPanel = previousPanels.peekLast();
    doSwitch(previousPanel);
  }

  private void doSwitch(@NonNull final ManagedPanel panel) {
    frame.getContentPane().removeAll();
    frame.getContentPane().add(panel);
    frame.getContentPane().validate();
    frame.getContentPane().repaint();
    panel.initialize();
  }

}
