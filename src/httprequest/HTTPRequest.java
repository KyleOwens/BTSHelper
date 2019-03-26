/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
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
        ArrayList<Game> games = new ArrayList<>();
        String playerIds[] = new String[50];
        double averages[] = new double[50];
        String playerTeams[] = new String[50];
        String playerBats[] = new String[50]; 
        


        ConnectionManager manager = new ConnectionManager();
        //manager.getHitterData();
        //manager.getProbable("2019-MAR-29");

        Parser parser = new Parser();
        playerIds = parser.parseHittingLeaders(new File("JSONFiles\\top50.json"));
        averages = parser.parseAverages(new File("JSONFiles\\top50.json"));
        playerTeams = parser.parsePlayerTeams(playerIds);
        playerBats = parser.parsePlayerBatting(playerIds);
        String gameIds[] = parser.parseGameIds();
        String homeTeamProbablePitchers[] = parser.parseProbableHomePitchers();
        String awayTeamProbablePitchers[] = parser.parseProbableAwayPitchers();
        String stadiumIds[] = parser.parseStadiumIds();

        

        for (int i = 0; i < gameIds.length; i++) {
            games.add(new Game(gameIds[i], homeTeamProbablePitchers[i], 
                    awayTeamProbablePitchers[i], stadiumIds[i]));
        }
//        
//        for(int i = 0; i < games.size(); i ++){
//            System.out.println(games.get(i));
//        }
    }

}
