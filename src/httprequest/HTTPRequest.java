/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Kyle
 */
public class HTTPRequest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, FileNotFoundException, ParseException {
        ParkConstants pc = new ParkConstants();
        HashMap<String, Integer> parkRatings = pc.createMap();
        ArrayList<Game> games = new ArrayList<>();
        String playerIds[] = new String[50];
        double averages[] = new double[50];
        String playerTeams[] = new String[50];
        String playerBats[] = new String[50];
        
        ArrayList<Player> players = new ArrayList<>();

        ConnectionManager manager = new ConnectionManager();
        //manager.getHitterData();
        //manager.getProbable("2019-MAR-29");

        Parser parser = new Parser();
        playerIds = parser.parseHittingLeaders(new File("JSONFiles\\top50.json"));
        averages = parser.parseAverages(new File("JSONFiles\\top50.json"));
        playerTeams = parser.parsePlayerTeams(playerIds);
        playerBats = parser.parsePlayerBatting(playerIds);
        String gameIds[] = parser.parseGameIds();
        String stadiumIds[] = parser.parseStadiumIds(playerTeams);
        HashMap<String, String> pitchers = parser.parseProbablePitchers(manager);
        manager.getPitcherData(pitchers.values().toArray(new String[0]));
        manager.getPitcherDemographic(pitchers.values().toArray(new String[0]));
        
        for(int i = 0; i < playerIds.length; i++){
            double opponentAvg = parser.parsePitcherAvg(pitchers.get(playerTeams[i]));
            String opponentThrows = parser.parsePitchingDirection(pitchers.get(playerTeams[i]));
            int parkRating = parkRatings.get(stadiumIds[i]);
            players.add(new Player(playerIds[i], averages[i], playerBats[i], playerTeams[i],
            opponentAvg, opponentThrows, parkRating));
        }
        
        
        System.out.println(players.get(0));

//        for (int i = 0; i < gameIds.length; i++) {
//            games.add(new Game(gameIds[i], homeTeamProbablePitchers[i], 
//                    awayTeamProbablePitchers[i], stadiumIds[i]));
//        }
////        
//        for(int i = 0; i < transformedIds.length; i ++){
//            System.out.println(transformedIds[i]);
//        }
    }

}
