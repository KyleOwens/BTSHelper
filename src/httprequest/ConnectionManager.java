/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 * @author Kyle
 */
public class ConnectionManager {

    public ConnectionManager() {
    }

    public void downloadTopHitterData(String year) {
        System.out.println("Downloading top hitters...");
        String apiUrl = "http://lookup-service-prod.mlb.com/json/named.leader_hitting_repeater.bam?sport_code="
                + "'mlb'&results=50&game_type='R'&season='" + year + "'&sort_column='avg'";
        HttpURLConnection connection = prepareConnection(apiUrl, false);
        download(connection, "top50.json");

    }

    public void downloadProbablePitchers(String date) {
        System.out.println("Downloading current probable pitchers for " + date + "...");
        String apiUrl = "https://api.fantasydata.net/v3/mlb/scores/json/GamesByDate/" + date;
        HttpURLConnection connection = prepareConnection(apiUrl, true);
        download(connection, "probables.json");
    }

    public void downloadMLBLookupPlayerData(String[] playerids) throws MalformedURLException, ProtocolException, IOException {
        System.out.println("Downloading player team info...");
        for (int i = 0; i < playerids.length; i++) {
            String apiUrl = "http://lookup-service-prod.mlb.com/json/named.player_info.bam?sport_code="
                    + "'mlb'&player_id='" + playerids[i] + "'";
            HttpURLConnection connection = prepareConnection(apiUrl, false);
            download(connection, playerids[i] + ".json");
        }

    }

    public void downloadFantasyApiPitcherInfo(String id) {
        System.out.println("Downloading pitcher data from fantasy api...");
        try {
            String apiUrl = "https://api.fantasydata.net/v3/mlb/stats/JSON/PlayerSeasonStatsByPlayer/2018/" + id;
            HttpURLConnection connection = prepareConnection(apiUrl, true);
            download(connection, id + ".json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadMLBLookupPitcherData(String[] playerids) {
        System.out.println("Downloading pitcher data from MLB Lookup...");
        try {
            for (int i = 0; i < playerids.length; i++) {
                URL url = new URL("http://lookup-service-prod.mlb.com/json/named.sport_pitching_tm.bam?league_list_id='mlb'&game_type='R'&season='2018'&player_id='" + playerids[i] + "'");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoOutput(true);
                download(con, playerids[i] + ".json");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadPitcherDemographics(String[] playerids) {
        System.out.println("Downloading pitcher demographics...");
        try {
            for (int i = 0; i < playerids.length; i++) {
                String apiUrl = "http://lookup-service-prod.mlb.com/json/named.player_info.bam?sport_code='mlb'&player_id='" + playerids[i] + "'";
                HttpURLConnection connection = prepareConnection(apiUrl, false);
                download(connection, playerids[i] + "-demographic.json");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String transformId(String playerids, Parser parser) {
        try {
            String name = parser.parseName(new File("JSONFiles\\" + playerids + ".json"));
            return downloadMLBLookupIdByName(name, parser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String downloadMLBLookupIdByName(String name, Parser parser) {
        try {
            String apiUrl = "http://lookup-service-prod.mlb.com/json/named.search_player_all.bam?sport_code='mlb'&active_sw='Y'&name_part='" + name.toLowerCase().substring(name.indexOf(" ")) + "%25'";
            HttpURLConnection connection = prepareConnection(apiUrl, false);
            download(connection, name + ".json");
            return parser.parseNewId(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void downloadStadiumInfo() throws MalformedURLException, IOException {
        System.out.println("Pulling current Stadium info...");
        String apiUrl = "https://api.fantasydata.net/v3/mlb/scores/JSON/Stadiums";
        HttpURLConnection connection = prepareConnection(apiUrl, true);
        download(connection, "stadiuminfo.json");
    }

    private HttpURLConnection prepareConnection(String apiUrl, Boolean fantasyApi) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            if (fantasyApi) {
                connection.addRequestProperty("Ocp-Apim-Subscription-Key", "f413a3ca812c4ef1b07e6ec2fe2b017e");
            }
            return connection;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void download(HttpURLConnection con, String fileName) {
        try {
            InputStream content = (InputStream) con.getInputStream();

            byte[] buffer = new byte[4096];
            int i = - 1;

            File file = new File("JSONFiles\\" + fileName);

            OutputStream output = new FileOutputStream(file);
            while ((i = content.read(buffer)) != -1) {
                output.write(buffer, 0, i);
            }
            output.close();
            content.close();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
