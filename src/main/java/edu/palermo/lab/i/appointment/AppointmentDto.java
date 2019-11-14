package edu.palermo.lab.i.appointment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

  private String id;
  private String doctorId;
  private String patientId;
  private Long timestamp;
  private Boolean canceled;

  /**
   * Method used for ease of development with in memory implementation of persistence. To be removed on release.
   */
  public AppointmentDto copy() {
    //noinspection BoxingBoxedValue
    return new AppointmentDto(
        id, doctorId, patientId, Long.valueOf(timestamp), Boolean.valueOf(canceled));
  }

}
