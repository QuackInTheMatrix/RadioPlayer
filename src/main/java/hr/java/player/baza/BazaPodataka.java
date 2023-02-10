package hr.java.player.baza;

import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.entiteti.Korisnik;
import hr.java.player.entiteti.RazinaOvlasti;
import hr.java.player.entiteti.Stanica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    public static void promjeniKorisnika(Long id,String username, String email, String ime, String prezime, Integer passwordHash, RazinaOvlasti razinaOvlasti){
        try(Connection veza = connectToDatabase()){
            List<String> uvjeti = new ArrayList<>();
            Statement statement = veza.createStatement();
            String query = "UPDATE korisnici set ";
            if (!username.isEmpty()){
                uvjeti.add("username ='"+username+"'");
            }
            if (!email.isEmpty()){
                uvjeti.add("email='"+email+"'");
            }
            if (!ime.isEmpty()){
                uvjeti.add("ime='"+ime+"'");
            }
            if (!prezime.isEmpty()){
                uvjeti.add("prezime='"+prezime+"'");
            }
            if (passwordHash!=null){
                uvjeti.add("password_hash="+passwordHash);
            }
            if (razinaOvlasti!=null){
                uvjeti.add("razina_ovlasti="+razinaOvlasti.getRazina());
            }
            if (uvjeti.size()>0) {
                query += String.join(" , ", uvjeti)+" where id="+id;
            }
            statement.executeUpdate(query);
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom");
            ex.printStackTrace();
        }
    }
    public static List<Korisnik> dohvatiKorisnike(Long id, String username, String email, String ime, String prezime, Integer passwordHash, RazinaOvlasti razinaOvlasti){
        List<Korisnik> dohvaceniKorisnici = new ArrayList<>();
        try(Connection veza = connectToDatabase()){
            List<String> uvjeti = new ArrayList<>();
            Statement statement = veza.createStatement();
            ResultSet rs;
            String query = "SELECT * FROM korisnici";
            if (id!=null){
                uvjeti.add("id="+id);
            }
            if (!username.isEmpty()){
                uvjeti.add("username LIKE '%"+username+"%'");
            }
            if (!email.isEmpty()){
                uvjeti.add("email LIKE '%"+email+"%'");
            }
            if (!ime.isEmpty()){
                uvjeti.add("ime LIKE '%"+ime+"%'");
            }
            if (!prezime.isEmpty()){
                uvjeti.add("prezime LIKE '%"+prezime+"%'");
            }
            if (passwordHash!=null){
                uvjeti.add("password_hash="+passwordHash);
            }
            if (razinaOvlasti!=null){
                uvjeti.add("razina_ovlasti="+razinaOvlasti.getRazina());
            }
            if (uvjeti.size()>0) {
                query += " where "+String.join(" and ", uvjeti);
            }
            rs = statement.executeQuery(query);
            while (rs.next()){
                Long idEntiteta = rs.getLong("id");
                String usernameEntiteta = rs.getString("username");
                String emailEntiteta = rs.getString("email");
                String imeEntiteta = rs.getString("ime");
                String prezimeEntiteta = rs.getString("prezime");
                Integer passwordHashEntiteta = rs.getInt("password_hash");
                RazinaOvlasti razinaOvlastiEntiteta;
                switch (rs.getInt("razina_ovlasti")){
                    case 1 -> razinaOvlastiEntiteta=RazinaOvlasti.ADMIN;
                    default -> razinaOvlastiEntiteta=RazinaOvlasti.USER;
                }
                dohvaceniKorisnici.add(new Korisnik(usernameEntiteta,passwordHashEntiteta,imeEntiteta,prezimeEntiteta,emailEntiteta,razinaOvlastiEntiteta,idEntiteta));
            }
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom");
            ex.printStackTrace();
        }
        return dohvaceniKorisnici;
    }
    public static void unesiKorisnika(String username, String email, String ime, String prezime, Integer passwordHash, RazinaOvlasti razinaOvlasti){
        if (dohvatiKorisnike(null,username,"","","",null,null).size()!=0){
            //TODO: alert umjesto sout i dodati provjeru za email koji se vec koristi
            System.out.println("Korisnik sa tim korisnickim imenom vec postoji!");
        }else{
            try(Connection veza = connectToDatabase()){
                PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO korisnici (username,email,ime,prezime,password_hash,razina_ovlasti) VALUES (?,?,?,?,?,?)");
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3,ime);
                preparedStatement.setString(4,prezime);
                preparedStatement.setInt(5,passwordHash);
                preparedStatement.setInt(6,razinaOvlasti.getRazina());
                preparedStatement.executeUpdate();
            }catch (SQLException | IOException ex){
                //TODO: alert umjesto sout
                System.out.println("Greska pri radu s bazom.");
                ex.printStackTrace();
            }
        }
    }

    public static void obrisiStanicuKorisniku(Long idKorisnika, Station stanica){
        Long idStanice = dohvatiStanice(null,"","","",null,"", stanica.getUrl()).get(0).getId();
        try(Connection veza = connectToDatabase()){
            List<String> uvjeti = new ArrayList<>();
            Statement statement = veza.createStatement();
            String query = "DELETE FROM korisnik_station ";
            if (idKorisnika!=null){
                uvjeti.add("idkorisnik="+idKorisnika);
            }
            if (idStanice!=null){
                uvjeti.add("idstation="+idStanice);
            }
            if (uvjeti.size()>0) {
                query += " where "+String.join(" and ", uvjeti);
                statement.executeUpdate(query);
            }
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom");
            ex.printStackTrace();
        }
    }

    public static List<Station> dohvatiKorisnikoveStanice(Long idKorisnika, Long idStanice){
        List<Long> idStanica = new ArrayList<>();
        List<Station> dohvaceneStanice = new ArrayList<>();
        try(Connection veza = connectToDatabase()){
            List<String> uvjeti = new ArrayList<>();
            Statement statement = veza.createStatement();
            ResultSet rs;
            String query = "SELECT * FROM korisnik_station";
            if (idKorisnika!=null){
                uvjeti.add("idkorisnik="+idKorisnika);
            }
            if (idStanice!=null){
                uvjeti.add("idstation="+idStanice);
            }
            if (uvjeti.size()>0) {
                query += " where "+String.join(" and ", uvjeti);
            }
            rs = statement.executeQuery(query);
            while (rs.next()){
                idStanica.add(rs.getLong("idstation"));
            }
            for (Long id:idStanica) {
                dohvaceneStanice.add(dohvatiStanice(id,"","","",null,"","").get(0).getStanica());
            }
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom");
            ex.printStackTrace();
        }
        return dohvaceneStanice;
    }
    private static List<Stanica> dohvatiStanice(Long id, String naziv, String zemlja, String codec, Integer bitrate, String zanr, String url){
        List<Stanica> dohvaceneStanice = new ArrayList<>();
        try(Connection veza = connectToDatabase()){
            List<String> uvjeti = new ArrayList<>();
            Statement statement = veza.createStatement();
            ResultSet rs;
            String query = "SELECT * FROM stations";
            if (id!=null){
                uvjeti.add("id="+id);
            }
            if (!naziv.isEmpty()){
                uvjeti.add("naziv LIKE '%"+naziv+"%'");
            }
            if (!zemlja.isEmpty()){
                uvjeti.add("zemlja LIKE '%"+zemlja+"%'");
            }
            if (!codec.isEmpty()){
                uvjeti.add("codec LIKE '%"+codec+"%'");
            }
            if (bitrate!=null){
                uvjeti.add("bitrate="+bitrate);
            }
            if (!zanr.isEmpty()){
                uvjeti.add("zanrovi LIKE '%"+zanr+"%'");
            }
            if (!url.isEmpty()){
                uvjeti.add("url LIKE '%"+url+"%'");
            }
            if (uvjeti.size()>0) {
                query += " where "+String.join(" and ", uvjeti);
            }
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
                dohvaceneStanice.add(new Stanica(idEntiteta,novaStanica));
            }
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom");
            ex.printStackTrace();
        }
        return dohvaceneStanice;
    }
    public static void unesiStanicu(Long korisnikId,Station stanica){
        try(Connection veza = connectToDatabase()){
                if (dohvatiStanice(null,"","","",null,"", stanica.getUrl()).isEmpty()) {
                    PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO stations (naziv,zemlja,codec,bitrate,zanrovi,url) VALUES (?,?,?,?,?,?)");
                    preparedStatement.setString(1, stanica.getName());
                    preparedStatement.setString(2, stanica.getCountry());
                    preparedStatement.setString(3, stanica.getCodec());
                    preparedStatement.setInt(4, stanica.getBitrate());
                    preparedStatement.setString(5, stanica.getTags());
                    preparedStatement.setString(6, stanica.getUrl());
                    preparedStatement.executeUpdate();
                }else{
                    System.out.println("Stanica je vec u bazi");
                }
                Long idStanice = dohvatiStanice(null,"","","",null,"", stanica.getUrl()).get(0).getId();
                if (dohvatiKorisnikoveStanice(korisnikId,idStanice).isEmpty()){
                    PreparedStatement preparedStatementKS = veza.prepareStatement("INSERT INTO korisnik_station VALUES (?,?)");
                    preparedStatementKS.setLong(1,korisnikId);
                    preparedStatementKS.setLong(2,idStanice);
                    preparedStatementKS.executeUpdate();
                }else{
                    //TODO: umjesto sout ubaciti alert
                    System.out.println("Stanica je vec povezana sa korisnikom!");
                }
        }catch (SQLException | IOException ex){
            //TODO: alert umjesto sout
            System.out.println("Greska pri radu s bazom.");
            ex.printStackTrace();
        }
    }
}
