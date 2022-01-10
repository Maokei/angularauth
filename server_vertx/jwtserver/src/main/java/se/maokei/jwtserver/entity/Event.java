package se.maokei.jwtserver.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Event {
  private String _id;
  private String name;
  private String description;
  private Date date;
}
