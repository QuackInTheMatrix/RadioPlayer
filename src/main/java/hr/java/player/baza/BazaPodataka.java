package hr.java.player.baza;

import de.sfuhrm.radiobrowser4j.Station;
import hr.java.player.entiteti.*;
import hr.java.player.gui.GlavnaAplikacija;
import hr.java.player.iznimke.BazaPodatakaException;
import hr.java.player.util.Logging;
import javafx.scene.control.Alert;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BazaPodataka{
    private static final String DATABASE_FILE = "dat/database.properties";
    private static <T> List<PromjenjenPodatak<T>> kreirajListPromjenjenih(List<T> stari, List<T> novi){
        List<PromjenjenPodatak<T>> promjenjenPodatakList = new ArrayList<>();
        for (int i=0;i<stari.size();i++){
            promjenjenPodatakList.add(new PromjenjenPodatak<>(stari.get(i),novi.get(i)));
        }
        return promjenjenPodatakList;
    }
    private static <T,E extends Entitet> Promjena<T,E> kreirajPromjenu(List<PromjenjenPodatak<T>> promjenjeniPodaci, VrstaPromjene vrstaPromjene, E objektPromjene){
        Korisnik vrsitelj = GlavnaAplikacija.getKorisnik();
        if (vrsitelj==null){
            vrsitelj = new Korisnik("Registracija",null,"","","",RazinaOvlasti.USER,null);
        }
        LocalDateTime vrijemePromjene = LocalDateTime.now();
        Promjena<T,E> promjena = new Promjena<>(vrsitelj,promjenjeniPodaci, vrijemePromjene,vrstaPromjene,objektPromjene);
        return promjena;
    }
    private static Connection connectToDatabase() throws SQLException, IOException {
        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_FILE));
        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");
        return DriverManager.getConnection(urlBazePodataka, korisnickoIme,lozinka);
    }
    public static boolean usernameExists(String username){
        try(Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM korisnici WHERE username=?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean stanicaExists(String url){
        try(Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM stations WHERE url=?");
            preparedStatement.setString(1, url);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void promjeniKorisnika(Long id,String username, String email, String ime, String prezime, Integer passwordHash, RazinaOvlasti razinaOvlasti)throws BazaPodatakaException{
        try(Connection veza = connectToDatabase()){
            if (dohvatiKorisnike(id,"","","","",null,null).isEmpty()){
                throw new BazaPodatakaException("Korisnik koji se pokusava promjeniti ne postoji u bazi");
            }
            List<String> uvjeti = new ArrayList<>();
            List<String> stari = new ArrayList<>();
            Korisnik mjenjanKorisnik = dohvatiKorisnike(id,"","","","",null,null).get(0);
            Statement statement = veza.createStatement();
            String query = "UPDATE korisnici set ";
            if (!username.isEmpty()){
                uvjeti.add("username ='"+username+"'");
                stari.add("username ='"+mjenjanKorisnik.getUsername()+"'");
            }
            if (!email.isEmpty()){
                uvjeti.add("email='"+email+"'");
                stari.add("email='"+mjenjanKorisnik.getEmail()+"'");
            }
            if (!ime.isEmpty()){
                uvjeti.add("ime='"+ime+"'");
                stari.add("ime='"+mjenjanKorisnik.getIme()+"'");
            }
            if (!prezime.isEmpty()){
                uvjeti.add("prezime='"+prezime+"'");
                stari.add("prezime='"+mjenjanKorisnik.getPrezime()+"'");
            }
            if (passwordHash!=null){
                uvjeti.add("password_hash="+passwordHash);
                stari.add("password_hash="+mjenjanKorisnik.getPasswordHash());
            }
            if (razinaOvlasti!=null){
                uvjeti.add("razina_ovlasti="+razinaOvlasti.getRazina());
                stari.add("razina_ovlasti="+mjenjanKorisnik.getRazinaOvlasti().getRazina());
            }
            if (uvjeti.size()>0) {
                query += String.join(" , ", uvjeti)+" where id="+id;
                statement.executeUpdate(query);
                List<PromjenjenPodatak<String>> promjenjeniPodataci = kreirajListPromjenjenih(stari,uvjeti);
                Promjena<String,Korisnik> promjena = kreirajPromjenu(promjenjeniPodataci,VrstaPromjene.UPDATE,mjenjanKorisnik);
                GlavnaAplikacija.dodajPromjenu(promjena);
            }
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
    }
    public static Korisnik dohvatiKorisnika(String username){
        try(Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM KORISNICI where username=?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
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
            return new Korisnik(usernameEntiteta,passwordHashEntiteta,imeEntiteta,prezimeEntiteta,emailEntiteta,razinaOvlastiEntiteta,idEntiteta);
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
            return null;
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
        return dohvaceniKorisnici;
    }
    public static void unesiKorisnika(String username, String email, String ime, String prezime, Integer passwordHash, RazinaOvlasti razinaOvlasti)throws BazaPodatakaException{
        if (usernameExists(username)){
            throw  new BazaPodatakaException("Korisnik sa tim korisnickim imenom vec postoji u bazi!");
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
                List<String> stareVrijednosti = Arrays.asList("","","","","","");
                List<String> noveVrjednosti = new ArrayList<>();
                noveVrjednosti.add("username ='"+username+"'");
                noveVrjednosti.add("email='"+email+"'");
                noveVrjednosti.add("ime='"+ime+"'");
                noveVrjednosti.add("prezime='"+prezime+"'");
                noveVrjednosti.add("password_hash="+passwordHash);
                noveVrjednosti.add("razina_ovlasti="+razinaOvlasti.getRazina());
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti,noveVrjednosti);
                Promjena<String, Korisnik> promjena = kreirajPromjenu(promjenjeniPodaci,VrstaPromjene.INSERT,dohvatiKorisnika(username));
                GlavnaAplikacija.dodajPromjenu(promjena);
            }catch (SQLException | IOException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greska");
                alert.setHeaderText("Dogodila se greska.");
                alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
                alert.showAndWait();
                Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
            }
        }
    }

    public static void obrisiKorisnika(Long id)throws BazaPodatakaException{
        try(Connection veza = connectToDatabase()){
            if (dohvatiKorisnike(id,"","","","",null,null).isEmpty()){
                throw new BazaPodatakaException("Korisnik koji se pokusava obrisati ne posotoji u bazi");
            }else {
                Korisnik korisnik = dohvatiKorisnike(id,"","","","",null,null).get(0);
                obrisiStanicuKorisniku(id, null);
                PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM korisnici WHERE id=?");
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                List<String> noveVrjednosti = Arrays.asList("", "", "", "", "", "");
                List<String> stareVrijednosti = new ArrayList<>();
                stareVrijednosti.add("username ='" + korisnik.getUsername() + "'");
                stareVrijednosti.add("email='" + korisnik.getEmail() + "'");
                stareVrijednosti.add("ime='" + korisnik.getIme() + "'");
                stareVrijednosti.add("prezime='" + korisnik.getPrezime() + "'");
                stareVrijednosti.add("password_hash=" + korisnik.getPasswordHash());
                stareVrijednosti.add("razina_ovlasti=" + korisnik.getRazinaOvlasti().getRazina());
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti, noveVrjednosti);
                Promjena<String, Korisnik> promjena = kreirajPromjenu(promjenjeniPodaci, VrstaPromjene.DELETE, korisnik);
                GlavnaAplikacija.dodajPromjenu(promjena);
            }
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
    }

    public static void obrisiStanicuKorisniku(Long idKorisnika, Station stanica)throws BazaPodatakaException{
        Long idStanice=null;
        if (stanica!=null) {
             idStanice = dohvatiStanicu(stanica.getUrl()).getId();
             if (idStanice==null){
                 throw new BazaPodatakaException("Stanica koja se pokusava izbrisati ne postoji u bazi!");
             }
        }
        try(Connection veza = connectToDatabase()){
            List<String> uvjeti = new ArrayList<>();
            List<String> noveVrjednosti = Arrays.asList("","");
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
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(uvjeti, noveVrjednosti);
                Promjena<String, StanicaSerializable> promjena = kreirajPromjenu(promjenjeniPodaci, VrstaPromjene.DELETE, new StanicaSerializable(null,"","","","","",null));
                GlavnaAplikacija.dodajPromjenu(promjena);
            }
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
        return dohvaceneStanice;
    }
    public static Stanica dohvatiStanicu(String url){
        try(Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza.prepareStatement("SELECT * FROM stations where url=?");
            preparedStatement.setString(1, url);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Long idEntiteta = rs.getLong("id");
            String nazivEntiteta = rs.getString("naziv");
            String zemljaEntiteta = rs.getString("zemlja");
            String codecEntiteta = rs.getString("codec");
            String zanroviEntiteta = rs.getString("zanrovi");
            Integer bitrateEntieteta = rs.getInt("bitrate");
            String urlEntiteta = rs.getString("url");
            return new Stanica.Builder(idEntiteta,urlEntiteta)
                    .withNaziv(nazivEntiteta)
                    .withZemlja(zemljaEntiteta)
                    .withCodec(codecEntiteta)
                    .withTags(zanroviEntiteta)
                    .withBitrate(bitrateEntieteta)
                    .build();
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
            return null;
        }
    }
    public static List<Stanica> dohvatiStanice(Long id, String naziv, String zemlja, String codec, Integer bitrate, String zanr, String url){
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
                uvjeti.add("url ='"+url+"'");
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
                dohvaceneStanice.add(new Stanica.Builder(idEntiteta,urlEntiteta)
                        .withNaziv(nazivEntiteta)
                        .withZemlja(zemljaEntiteta)
                        .withCodec(codecEntiteta)
                        .withBitrate(bitrateEntiteta)
                        .withTags(zanroviEntiteta)
                        .build());
            }
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
        return dohvaceneStanice;
    }
    public static void unesiStanicu(Long korisnikId,Station stanica){
        try(Connection veza = connectToDatabase()){
                if (!stanicaExists(stanica.getUrl())) {
                    PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO stations (naziv,zemlja,codec,bitrate,zanrovi,url) VALUES (?,?,?,?,?,?)");
                    preparedStatement.setString(1, stanica.getName());
                    preparedStatement.setString(2, stanica.getCountry());
                    preparedStatement.setString(3, stanica.getCodec());
                    preparedStatement.setInt(4, stanica.getBitrate());
                    preparedStatement.setString(5, stanica.getTags());
                    preparedStatement.setString(6, stanica.getUrl());
                    preparedStatement.executeUpdate();
                    List<String> stareVrijednosti = Arrays.asList("","","","","","");
                    List<String> noveVrjednosti = new ArrayList<>();
                    noveVrjednosti.add("naziv ='"+stanica.getName()+"'");
                    noveVrjednosti.add("zemlja='"+stanica.getCountry()+"'");
                    noveVrjednosti.add("codec='"+stanica.getCodec()+"'");
                    noveVrjednosti.add("bitrate='"+stanica.getBitrate()+"'");
                    noveVrjednosti.add("zanrovi='"+stanica.getTags()+"'");
                    noveVrjednosti.add("url="+stanica.getUrl()+"'");
                    List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti,noveVrjednosti);
                    Stanica kreiranaStanica = dohvatiStanicu(stanica.getUrl());
                    StanicaSerializable stanicaSerializable = new StanicaSerializable(kreiranaStanica.getId(), kreiranaStanica.getStanica().getName(),kreiranaStanica.getStanica().getCountry(),kreiranaStanica.getStanica().getUrl(),kreiranaStanica.getStanica().getTags(),kreiranaStanica.getStanica().getCodec(),kreiranaStanica.getStanica().getBitrate());
                    Promjena<String, StanicaSerializable> promjena = kreirajPromjenu(promjenjeniPodaci,VrstaPromjene.INSERT,stanicaSerializable);
                    GlavnaAplikacija.dodajPromjenu(promjena);
                }
                if (korisnikId!=null) {
                    Long idStanice = dohvatiStanicu(stanica.getUrl()).getId();
                    if (dohvatiKorisnikoveStanice(korisnikId, idStanice).isEmpty()) {
                        PreparedStatement preparedStatementKS = veza.prepareStatement("INSERT INTO korisnik_station VALUES (?,?)");
                        preparedStatementKS.setLong(1, korisnikId);
                        preparedStatementKS.setLong(2, idStanice);
                        preparedStatementKS.executeUpdate();
                        List<String> stareVrjednosti = Arrays.asList("","");
                        List<String> noveVrjednosti = Arrays.asList("idkorisnik="+korisnikId,"idstation="+idStanice);
                        List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrjednosti, noveVrjednosti);
                        Promjena<String, StanicaSerializable> promjena = kreirajPromjenu(promjenjeniPodaci, VrstaPromjene.INSERT, new StanicaSerializable(null,"","","","","",null));
                        GlavnaAplikacija.dodajPromjenu(promjena);
                    }
                }
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
    }
    public static void obrisiStanicu(Station station)throws BazaPodatakaException{
        try(Connection veza = connectToDatabase()){
            if (!stanicaExists(station.getUrl())){
                throw new BazaPodatakaException("Stanica koja se pokusava obrisati ne postoji u bazi!");
            }else {
                Stanica stanica = dohvatiStanicu(station.getUrl());
                obrisiStanicuKorisniku(null, stanica.getStanica());
                PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM stations WHERE id=?");
                preparedStatement.setLong(1, stanica.getId());
                preparedStatement.executeUpdate();
                List<String> stareVrijednosti = new ArrayList<>();
                List<String> noveVrjednosti = Arrays.asList("", "", "", "", "", "");
                stareVrijednosti.add("naziv ='" + station.getName() + "'");
                stareVrijednosti.add("zemlja='" + station.getCountry() + "'");
                stareVrijednosti.add("codec='" + station.getCodec() + "'");
                stareVrijednosti.add("bitrate='" + station.getBitrate() + "'");
                stareVrijednosti.add("zanrovi='" + station.getTags() + "'");
                stareVrijednosti.add("url='" + station.getUrl() + "'");
                StanicaSerializable stanicaSerializable = new StanicaSerializable(stanica.getId(), stanica.getStanica().getName(), stanica.getStanica().getCountry(), stanica.getStanica().getUrl(), stanica.getStanica().getTags(), stanica.getStanica().getCodec(), stanica.getStanica().getBitrate());
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti, noveVrjednosti);
                Promjena<String, StanicaSerializable> promjena = kreirajPromjenu(promjenjeniPodaci, VrstaPromjene.DELETE, stanicaSerializable);
                GlavnaAplikacija.dodajPromjenu(promjena);
            }
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
    }
    public static void promjeniStanicu(Station stanica, String naziv, String zemlja, String codec, Integer bitrate, String zanr, String url)throws BazaPodatakaException{
        try(Connection veza = connectToDatabase()){
            if (!stanicaExists(stanica.getUrl())){
                throw new BazaPodatakaException("Stanica koja se pokusava promjeniti ne postoji u bazi!");
            }
            Stanica trazenaStanica = dohvatiStanicu(stanica.getUrl());
            List<String> uvjeti = new ArrayList<>();
            List<String> stareVrijednosti = new ArrayList<>();
            Statement statement = veza.createStatement();
            String query = "UPDATE STATIONS ";
            if (!naziv.isEmpty()){
                uvjeti.add("naziv ='"+naziv+"'");
                stareVrijednosti.add("naziv ='"+trazenaStanica.getStanica().getName()+"'");
            }
            if (!zemlja.isEmpty()){
                uvjeti.add("zemlja='"+zemlja+"'");
                stareVrijednosti.add("zemlja='"+trazenaStanica.getStanica().getCountry()+"'");
            }
            if (!codec.isEmpty()){
                uvjeti.add("codec='"+codec+"'");
                stareVrijednosti.add("codec='"+trazenaStanica.getStanica().getCodec()+"'");
            }
            if (bitrate!=null){
                uvjeti.add("bitrate="+bitrate);
                stareVrijednosti.add("bitrate="+trazenaStanica.getStanica().getBitrate());
            }
            if (!zanr.isEmpty()){
                uvjeti.add("zanrovi='"+zanr+"'");
                stareVrijednosti.add("zanrovi='"+trazenaStanica.getStanica().getTags()+"'");
            }
            if (!url.isEmpty()){
                uvjeti.add("url='"+url+"'");
                stareVrijednosti.add("url='"+trazenaStanica.getStanica().getUrl()+"'");
            }
            if (uvjeti.size()>0) {
                query += " set "+String.join(", ", uvjeti)+ " where id="+trazenaStanica.getId();
                statement.executeUpdate(query);
                List<String> noveVrjednosti = uvjeti;
                StanicaSerializable stanicaSerializable = new StanicaSerializable(trazenaStanica.getId(), trazenaStanica.getStanica().getName(),trazenaStanica.getStanica().getCountry(),trazenaStanica.getStanica().getUrl(),trazenaStanica.getStanica().getTags(),trazenaStanica.getStanica().getCodec(),trazenaStanica.getStanica().getBitrate());
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti,noveVrjednosti);
                Promjena<String, StanicaSerializable> promjena = kreirajPromjenu(promjenjeniPodaci,VrstaPromjene.UPDATE,stanicaSerializable);
                GlavnaAplikacija.dodajPromjenu(promjena);
            }
        }catch (SQLException | IOException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greska");
            alert.setHeaderText("Dogodila se greska.");
            alert.setContentText("Greska pri citanju properties file-a i/ili spajanja na bazu.");
            alert.showAndWait();
            Logging.logger.error("Greska pri citanju properties file-a i/ili spajanja na bazu.",ex);
        }
    }
}
