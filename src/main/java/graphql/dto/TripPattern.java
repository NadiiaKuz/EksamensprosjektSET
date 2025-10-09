package graphql.dto;

import java.util.List;

public class TripPattern {

    public String expectedStartTime;
    public String expectedEndTime;
    public int duration;
    public List<Leg> legs;

}
