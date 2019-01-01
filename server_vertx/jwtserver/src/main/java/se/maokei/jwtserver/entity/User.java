package se.maokei.jwtserver.entity;

public class User {
  private String password;
  private String email;

  public User() {

  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return email + " " + password;
  }
}
