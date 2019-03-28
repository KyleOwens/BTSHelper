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

    private HttpURLConnection con;

    public ConnectionManager() {
    }

    public void getHitterData() throws MalformedURLException, IOException {
        System.out.println("Pulling top hitters...");
        URL url = new URL("http://lookup-service-prod.mlb.com/json/named.leader_hitting_repeater.bam?sport_code='mlb'&results=50&game_type='R'&season='2018'&sort_column='avg'");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        download(con, "top50.json");

    }

    public void getSchedule() throws MalformedURLException, IOException {
        URL url = new URL("http://statsapi.mlb.com/api/v1/schedule/games/?sportId=1&date=03/29/2019");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        download(con, "schedule.json");
    }

    public void getProbable(String date) throws MalformedURLException, IOException {
        System.out.println("Pulling current probable pitchers...");
        URL url = new URL("https://api.fantasydata.net/v3/mlb/scores/json/GamesByDate/" + date);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.addRequestProperty("Ocp-Apim-Subscription-Key", "f413a3ca812c4ef1b07e6ec2fe2b017e");
        download(con, "probables.json");
    }

    public void getStadiums() throws MalformedURLException, IOException {
        System.out.println("Pulling current Stadium info...");
        URL url = new URL("https://api.fantasydata.net/v3/mlb/scores/JSON/Stadiums");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.addRequestProperty("Ocp-Apim-Subscription-Key", "f413a3ca812c4ef1b07e6ec2fe2b017e");
        download(con, "stadiuminfo.json");
    }

    public void getPlayerData(String[] playerids) throws MalformedURLException, ProtocolException, IOException {
        System.out.println("Pulling all player data...");
        for (int i = 0; i < playerids.length; i++) {
            URL url = new URL("http://lookup-service-prod.mlb.com/json/named.player_info.bam?sport_code='mlb'&player_id='" + playerids[i] + "'");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            download(con, playerids[i] + ".json");
        }

    }

    public void getOddPitcherIds(String id) {
        try {
            URL url = new URL("https://api.fantasydata.net/v3/mlb/stats/JSON/PlayerSeasonStatsByPlayer/2018/"+ id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.addRequestProperty("Ocp-Apim-Subscription-Key", "f413a3ca812c4ef1b07e6ec2fe2b017e");
            download(con, id+".json");
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void getPitcherData(String[] playerids) {
        System.out.println("Pulling all pitcher data...");
        try {
            for (int i = 0; i < playerids.length; i++) {
                URL url = new URL("http://lookup-service-prod.mlb.com/json/named.sport_pitching_tm.bam?league_list_id='mlb'&game_type='R'&season='2018'&player_id='" + playerids[i] + "'");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoOutput(true);
                download(con, playerids[i] + ".json");
            }
        } catch (Exception e) {

        }

    }

    public void getPitcherDemographic(String[] playerids) {
        System.out.println("Pulling all pitcher demographics...");
        try {
            for (int i = 0; i < playerids.length; i++) {
                URL url = new URL("http://lookup-service-prod.mlb.com/json/named.player_info.bam?sport_code='mlb'&player_id='" + playerids[i] + "'");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoOutput(true);
                download(con, playerids[i] + "-demographic.json");
            }
        } catch (Exception e) {

        }

    }

    public String transformId(String playerids, Parser parser) {
        try {
            String name = parser.parseName(new File("JSONFiles\\" + playerids + ".json"));
            return getNewId(name, parser);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private String getNewId(String name, Parser parser) {
        try {
            URL url = new URL("http://lookup-service-prod.mlb.com/json/named.search_player_all.bam?sport_code='mlb'&active_sw='Y'&name_part='" + name.toLowerCase().substring(name.indexOf(" ")) + "%25'");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            download(con, name + ".json");
            return parser.parseNewId(name);

        } catch (Exception e) {

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
