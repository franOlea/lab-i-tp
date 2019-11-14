package edu.palermo.lab.i.user;

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
public class UserDto {
  private String id;
  private String password;
  private String firstName;
  private String lastName;
  private Role role = Role.USER;
  private Boolean enabled = true;

  /**
   * Method used for ease of development with in memory implementation of persistence. To be removed on release.
   */
  public UserDto copy() {
    //noinspection BoxingBoxedValue
    return new UserDto(
        this.getId(),
        this.getPassword(),
        this.getFirstName(),
        this.getLastName(),
        this.role,
        Boolean.valueOf(this.getEnabled()));
  }
}
