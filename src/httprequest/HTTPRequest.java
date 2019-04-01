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
import java.util.Collections;
import java.util.Comparator;
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
        Parser parser = new Parser();
        ParkConstants pc = new ParkConstants();
        ConnectionManager manager = new ConnectionManager();

        HashMap<String, Integer> parkRatings = pc.createMap();
        ArrayList<Game> games = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();

        String playerIds[] = new String[50];
        double averages[] = new double[50];
        String playerTeams[] = new String[50];
        String playerBats[] = new String[50];
        String playerNames[] = new String[50];
        String gameIds[];

        //manager.getHitterData();
        manager.downloadProbablePitchers("2019-APR-03");

        playerIds = parser.parseHittingLeaders(new File("JSONFiles\\top50.json"));
        averages = parser.parseAverages(new File("JSONFiles\\top50.json"));
        playerTeams = parser.parsePlayerTeams(playerIds);
        playerNames = parser.parsePlayerNames(playerIds);
        playerBats = parser.parsePlayerBatting(playerIds);
        gameIds = parser.parseGameIds();

        String stadiumIds[] = parser.parseStadiumIds(playerTeams);
        HashMap<String, String> pitchers = parser.parseProbablePitchers(manager);

        manager.downloadMLBLookupPitcherData(pitchers.values().toArray(new String[0]));
        manager.downloadPitcherDemographics(pitchers.values().toArray(new String[0]));

        for (int i = 0; i < playerIds.length; i++) {
            double opponentAvg = parser.parsePitcherAvg(pitchers.get(playerTeams[i]));
            int parkRating = 20;
            String opponentThrows = parser.parsePitchingDirection(pitchers.get(playerTeams[i]));

            if (parkRatings.containsKey(stadiumIds[i])) {
                parkRating = parkRatings.get(stadiumIds[i]);
            }

            players.add(new Player(playerNames[i], playerIds[i], averages[i], playerBats[i], playerTeams[i],
                    opponentAvg, opponentThrows, parkRating));
        }

        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return Double.compare(o2.getHitRating(), o1.getHitRating());
            }
        });

        for (Player p : players) {
            System.out.println(p);
        }
    }

}
