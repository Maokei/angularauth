package se.maokei.jwtserver_spring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class Event {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;

    public Event(String name, String description, Date date){
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public Event() {}
}
