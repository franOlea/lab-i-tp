package edu.palermo.lab.i;

import edu.palermo.lab.i.user.UserDto;
import lombok.Getter;
import lombok.Setter;

public class SecurityContext {

  @Getter
  @Setter
  private UserDto currentUser;

  private static SecurityContext ourInstance = new SecurityContext();

  public static SecurityContext getInstance() {
    return ourInstance;
  }

  private SecurityContext() {
  }


}
