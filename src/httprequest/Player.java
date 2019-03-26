/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

/**
 *
 * @author Kyle
 */
public class Player {

    private String id;
    private double avg;
    private String bats;
    private String team;
    private double opponentAvg;
    private String opponentThrows;
    private int parkRating;

    public Player(String id, double avg, String bats, String team, double OpponentAvg,
            String OpponentThrows, int parkRating) {
        this.id = id;
        this.avg = avg;
        this.bats = bats;
        this.team = team;
        this.opponentAvg = OpponentAvg;
        this.opponentThrows = OpponentThrows;
        this.parkRating = parkRating;
    }

    @Override
    public String toString() {
        return id + " bats " + bats + " and plays a pitcher that throws " + opponentThrows + ".  "
                + "This player bats " + avg + " and the pitcher throws an average of " + opponentAvg
                + ".  Finally, this player plays in a park with rating: " + parkRating;
    }
    
    

}
