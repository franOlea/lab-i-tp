package edu.palermo.lab.i.ui.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@AllArgsConstructor
@RequiredArgsConstructor
public class ActionAcknowledgePanel extends JPanel {

  @Getter
  private JButton acceptButton;
  @Getter
  private JButton cancelButton;
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

    acceptButton = new JButton("Aceptar");
    acceptButton.addActionListener(acceptListener);
    this.add(acceptButton);

    cancelButton = new JButton("Cancelar");
    cancelButton.addActionListener(cancelListener);
    this.add(cancelButton);
  }

}
