/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Kyle
 */
public class Parser {

    public String[] parseHittingLeaders(File file) throws FileNotFoundException, IOException, ParseException {
        String[] topHitters = new String[50];

        JSONArray hitters = getHitters(file);

        int i = 0;
        for (Object player : hitters) {
            JSONObject newPlayer = (JSONObject) player;
            String playerid = (String) newPlayer.get("player_id");
            topHitters[i] = playerid;
            i++;
        }

        return topHitters;

    }

    public double[] parseAverages(File file) throws IOException, FileNotFoundException, ParseException {
        double[] averages = new double[50];

        JSONArray hitters = getHitters(file);

        int i = 0;
        for (Object hitter : hitters) {
            JSONObject currPlayer = (JSONObject) hitter;
            averages[i] = Double.parseDouble((String) currPlayer.get("avg"));
            i++;
        }

        return averages;
    }

    public String[] parsePlayerTeams(String[] playerids) throws IOException, FileNotFoundException, ParseException {
        String playerTeams[] = new String[50];

        for (int i = 0; i < playerids.length; i++) {
            File file = new File("JSONFiles\\" + playerids[i] + ".json");
            JSONObject currentPlayer = getPlayerInfo(file);
            playerTeams[i] = (String) currentPlayer.get("team_abbrev");
        }

        return playerTeams;
    }

    public String[] parsePlayerBatting(String[] playerids) throws IOException, FileNotFoundException, ParseException {
        String playerBats[] = new String[50];

        for (int i = 0; i < playerids.length; i++) {
            File file = new File("JSONFiles\\" + playerids[i] + ".json");
            JSONObject currentPlayer = getPlayerInfo(file);
            playerBats[i] = (String) currentPlayer.get("bats");
        }

        return playerBats;
    }

    private JSONArray getHitters(File file) throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject o = (JSONObject) parser.parse(new FileReader(file));
        JSONObject p = (JSONObject) o.get("leader_hitting_repeater");
        JSONObject q = (JSONObject) p.get("leader_hitting_mux");
        JSONObject r = (JSONObject) q.get("queryResults");
        JSONArray hitters = (JSONArray) r.get("row");

        return hitters;
    }

    private JSONObject getPlayerInfo(File file) throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject o = (JSONObject) parser.parse(new FileReader(file));
        JSONObject p = (JSONObject) o.get("player_info");
        JSONObject q = (JSONObject) p.get("queryResults");
        JSONObject playerInfo = (JSONObject) q.get("row");

        return playerInfo;
    }

}
