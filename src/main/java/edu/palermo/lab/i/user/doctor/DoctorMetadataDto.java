package edu.palermo.lab.i.user.doctor;

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
public class DoctorMetadataDto {
  private String userId;
  private Float hourlyFee;

  /**
   * Method used for ease of development with in memory implementation of persistence. To be removed on release.
   */
  public DoctorMetadataDto copy() {
    //noinspection BoxingBoxedValue
    return new DoctorMetadataDto(userId, Float.valueOf(this.hourlyFee));
  }

}
