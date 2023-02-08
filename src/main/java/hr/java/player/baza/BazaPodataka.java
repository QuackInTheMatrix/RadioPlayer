package hr.java.player.baza;

import de.sfuhrm.radiobrowser4j.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class BazaPodataka{
    private static final String DATABASE_FILE = "dat/database.properties";
    private static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);

    private static Connection connectToDatabase() throws SQLException, IOException {
        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_FILE));
        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");
        return DriverManager.getConnection(urlBazePodataka, korisnickoIme,lozinka);
    }
    public static List<Station> dohvatiStranice(Long id, String naziv, String zemlja, String codec, Integer bitrate, String zanr, String url){
        List<Station> dohvaceneStanice = new ArrayList<>();
        try(Connection veza = connectToDatabase()){
            List<String> uvjeti = new ArrayList<>();
            Statement statement = veza.createStatement();
            ResultSet rs;
            String query = "SELECT * FROM stanice";
            if (id!=null){
                uvjeti.add("id="+id);
            }
            if (!naziv.isEmpty()){
                uvjeti.add("naziv LIKE %"+naziv+"%");
            }
            if (!zemlja.isEmpty()){
                uvjeti.add("zemlja LIKE %"+zemlja+"%");
            }
            if (!codec.isEmpty()){
                uvjeti.add("codec LIKE %"+codec+"%");
            }
            if (bitrate!=null){
                uvjeti.add("bitrate="+bitrate);
            }
            if (!zanr.isEmpty()){
                uvjeti.add("zanrovi LIKE %"+zanr+"%");
            }
            if (url!=null){
                uvjeti.add("url LIKE %"+url+"%");
            }
            query+=String.join(" and ",uvjeti);
            rs = statement.executeQuery(query);
            while (rs.next()){
                Long idEntiteta = rs.getLong("id");
                String nazivEntiteta = rs.getString("naziv");
                String zemljaEntiteta = rs.getString("zemlja");
                String codecEntiteta = rs.getString("codec");
                Integer bitrateEntiteta = rs.getInt("bitrate");
                String zanroviEntiteta = rs.getString("zanrovi");
                String urlEntiteta = rs.getString("url");
                Station novaStanica = new Station();
                novaStanica.setName(nazivEntiteta);
                novaStanica.setCountry(zemljaEntiteta);
                novaStanica.setCodec(codecEntiteta);
                novaStanica.setBitrate(bitrateEntiteta);
                novaStanica.setTags(zanroviEntiteta);
                novaStanica.setUrl(urlEntiteta);
                dohvaceneStanice.add(novaStanica);
            }
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom");
            ex.printStackTrace();
        }
        return dohvaceneStanice;
    }
    public static void unesiStanicu(Station stanica){
        try(Connection veza = connectToDatabase()){
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO stations (naziv,zemlja,codec,bitrate,zanrovi,url) VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1,stanica.getName());
            preparedStatement.setString(2,stanica.getCountry());
            preparedStatement.setString(3,stanica.getCodec());
            preparedStatement.setInt(4,stanica.getBitrate());
            preparedStatement.setString(5,stanica.getTags());
            preparedStatement.setString(6, stanica.getUrl());
            preparedStatement.executeUpdate();
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom.");
            ex.printStackTrace();
        }
    }
}
