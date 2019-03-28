/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.text.DecimalFormat;
import java.util.Comparator;

/**
 *
 * @author Kyle
 */
public class Player {

    private String name;
    private String id;
    private double avg;
    private String bats;
    private String team;
    private double opponentAvg;
    private String opponentThrows;
    private int parkRating;
    private double hitRating;
    
    private DecimalFormat df = new DecimalFormat("0.000");

    public Player(String name, String id, double avg, String bats, String team, double OpponentAvg,
            String OpponentThrows, int parkRating) {
        this.name = name;
        this.id = id;
        this.avg = avg;
        this.bats = bats;
        this.team = team;
        this.opponentAvg = OpponentAvg;
        this.opponentThrows = OpponentThrows;
        this.parkRating = parkRating;

        hitRating = this.avg + this.opponentAvg - (.01 * parkRating);
        
        if(this.opponentThrows.equals(this.bats)){
            this.hitRating -= .250;
        }
    }

    public double getHitRating() {
        return this.hitRating;
    }

    @Override
    public String toString() {
        return this.name + " has a hit rating of:  " + df.format(this.hitRating);
    }

}
