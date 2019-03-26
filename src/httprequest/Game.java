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
public class Game {
    private String gameid;
    private String homeProbable;
    private String awayProbable;
    private String stadiumid;

    public Game(String gameid, String homeProbable, String awayProbable, String stadiumid) {
        this.gameid = gameid;
        this.homeProbable = homeProbable;
        this.awayProbable = awayProbable;
        this.stadiumid = stadiumid;
    }

    @Override
    public String toString() {
        return "Game id: " + gameid + "  HomeProbable: " + homeProbable
                + "  AwayProbable: " + awayProbable + "  stadiumID: " + stadiumid;
    }
    
    
    
    
}
