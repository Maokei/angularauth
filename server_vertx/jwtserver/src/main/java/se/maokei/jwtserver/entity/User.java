package se.maokei.jwtserver.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
  private String password;
  private String email;

  @Override
  public String toString() {
    return email + " " + password;
  }
}
