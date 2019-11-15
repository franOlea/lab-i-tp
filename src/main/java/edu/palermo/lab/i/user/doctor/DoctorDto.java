package edu.palermo.lab.i.user.doctor;

import edu.palermo.lab.i.user.UserDto;
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
public class DoctorDto {
  private UserDto userDto;
  private Float hourlyFee;

  /**
   * Method used for ease of development with in memory implementation of persistence. To be removed on release.
   */
  public DoctorDto copy() {
    //noinspection BoxingBoxedValue
    return new DoctorDto(userDto.copy(), Float.valueOf(this.hourlyFee));
  }

}
