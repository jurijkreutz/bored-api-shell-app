package at.stadtwerke.itacademy.bored.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    private String activity;

    private String type;

    private int participants;

    private double price;

    private String link;

    private double accessibility;

}
