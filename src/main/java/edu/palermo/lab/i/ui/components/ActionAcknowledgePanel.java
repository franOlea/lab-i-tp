package edu.palermo.lab.i.ui.components;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public class ActionAcknowledgePanel extends JPanel {

  @Getter
  private JButton acceptButton;
  @Getter
  private JButton cancelButton;
  @Setter
  @NonNull
  private String acceptLabel = "Aceptar";
  @NonNull
  private final ActionListener acceptListener;
  @NonNull
  private String cancelLabel = "Cancelar";
  @NonNull
  private final ActionListener cancelListener;

  public void initialize() {
    this.setLayout(new GridLayout(1,2));

    acceptButton = new JButton(acceptLabel);
    acceptButton.addActionListener(acceptListener);
    this.add(acceptButton);

    cancelButton = new JButton(cancelLabel);
    cancelButton.addActionListener(cancelListener);
    this.add(cancelButton);
  }

}
