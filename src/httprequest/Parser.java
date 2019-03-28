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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    public String[] parsePlayerNames(String[] playerids) {
        try {
            String playerTeams[] = new String[50];
            for (int i = 0; i < playerids.length; i++) {
                File file = new File("JSONFiles\\" + playerids[i] + ".json");
                JSONObject currentPlayer = getPlayerInfo(file);
                playerTeams[i] = (String) currentPlayer.get("name_display_first_last");
            }

            return playerTeams;
        } catch (Exception e) {

        }

        return null;
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

    public String[] parseGameIds() throws IOException, FileNotFoundException, ParseException {
        JSONArray games = getGameData();
        String[] gameIds = new String[games.size()];

        int i = 0;
        for (Object o : games) {
            JSONObject game = (JSONObject) o;
            gameIds[i] = Objects.toString(game.get("GameID"), null);
            i++;
        }

        return gameIds;
    }

    public String parseName(File file) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject o = (JSONObject) parser.parse(new FileReader(file));
            return (String) o.get("Name");

        } catch (Exception e) {

        }
        return null;
    }

    public String parseNewId(String name) {
        try {
            File file = new File("JSONFiles\\" + name + ".json");
            JSONObject player = (JSONObject) getSearchedPlayerInfo(file, name);
            String compareName = (String) player.get("name_display_first_last");
            if (compareName.toLowerCase().equals(name.toLowerCase())) {
                return (String) player.get("player_id");
            } else {
                System.out.println("fart");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public HashMap<String, String> parseProbablePitchers(ConnectionManager manager) {
        try {
            JSONArray games = getGameData();
            HashMap<String, String> probablePitchers = new HashMap<>();

            for (Object o : games) {
                JSONObject game = (JSONObject) o;
                manager.getOddPitcherIds(Objects.toString(game.get("AwayTeamProbablePitcherID"), null));
                manager.getOddPitcherIds(Objects.toString(game.get("HomeTeamProbablePitcherID"), null));
                String awayPitcher = manager.transformId(Objects.toString(game.get("AwayTeamProbablePitcherID"), null), this);
                String homePitcher = manager.transformId(Objects.toString(game.get("HomeTeamProbablePitcherID"), null), this);
                String awayTeam = (String) game.get("AwayTeam");
                String homeTeam = (String) game.get("HomeTeam");

                probablePitchers.put(homeTeam, awayPitcher);
                probablePitchers.put(awayTeam, homePitcher);
            }
            return probablePitchers;
        } catch (Exception e) {

        }
        return null;
    }

    public String[] parseStadiumIds(String playerTeams[]) {
        try {
            JSONArray games = getGameData();
            String homeTeam;
            String awayTeam;
            String[] stadiumIds = new String[playerTeams.length];

            for (int x = 0; x < playerTeams.length; x++) {
                for (Object o : games) {
                    JSONObject game = (JSONObject) o;
                    homeTeam = (String) game.get("HomeTeam");
                    awayTeam = (String) game.get("AwayTeam");

                    if (playerTeams[x].equals(homeTeam) || playerTeams[x].equals(awayTeam)) {
                        stadiumIds[x] = Objects.toString(game.get("StadiumID"), null);
                    }

                }
            }

            return stadiumIds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double parsePitcherAvg(String id) throws FileNotFoundException, IOException, ParseException {
        try {
            File file = new File("JSONFiles\\" + id + ".json");
            JSONParser parser = new JSONParser();
            JSONObject o = (JSONObject) parser.parse(new FileReader(file));
            JSONObject p = (JSONObject) o.get("sport_pitching_tm");
            JSONObject q = (JSONObject) p.get("queryResults");
            JSONObject r = (JSONObject) q.get("row");

            String playerAverage = (String) r.get("avg");
            double avg = Double.parseDouble(playerAverage);
            return avg;
        } catch (Exception e) {
            if(id != null){
                File file = new File("JSONFiles\\" + id + ".json");
                JSONParser parser = new JSONParser();
                JSONObject o = (JSONObject) parser.parse(new FileReader(file));
                JSONObject p = (JSONObject) o.get("sport_pitching_tm");
                JSONObject q = (JSONObject) p.get("queryResults");
                JSONArray r = (JSONArray) q.get("row");

                double avgAverage = 0;
                double teams = 0;
                for (Object obj : r) {
                    JSONObject jObj = (JSONObject) obj;
                    avgAverage += Double.parseDouble((String) jObj.get("avg"));
                    teams++;
                }

                return avgAverage / teams;
            } else {
                return 0;
            }

        }
    }

    public String parsePitchingDirection(String id) {
        try {
            File file = new File("JSONFiles\\" + id + "-demographic.json");
            JSONObject playerInfo = getPlayerInfo(file);
            String direction = (String) playerInfo.get("throws");
            return direction;
        } catch (Exception e) {

        }

        return null;
    }

    private JSONArray getGameData() throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray games = (JSONArray) parser.parse(new FileReader(new File("JSONFiles\\probables.json")));

        return games;
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

    private JSONObject getSearchedPlayerInfo(File file, String name) throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject o = (JSONObject) parser.parse(new FileReader(file));
        JSONObject p = (JSONObject) o.get("search_player_all");
        JSONObject q = (JSONObject) p.get("queryResults");
        try {
            JSONObject playerInfo = (JSONObject) q.get("row");
            return playerInfo;
        } catch (Exception e) {
            JSONArray players = (JSONArray) q.get("row");
            for (Object obj : players) {
                JSONObject curr = (JSONObject) obj;
                String compare = (String) curr.get("name_display_first_last");
                if (compare.toLowerCase().equals(name.toLowerCase())) {
                    return curr;
                }
            }
        }

        return null;
    }

}
