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
    //TODO: napraviti proper provjeru postoji li korisnik sa usernameom jer trenutno se provjerava sa LIKE
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
        if (dohvatiKorisnike(null,username,"","","",null,null).size()!=0){
            //TODO: alert umjesto sout i dodati provjeru za email koji se vec koristi
            System.out.println("Korisnik sa tim korisnickim imenom vec postoji!");
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
                Korisnik kreiranKorisnik = dohvatiKorisnike(null,username,"","","",null,null).get(0);
                List<String> stareVrijednosti = Arrays.asList("","","","","","");
                List<String> noveVrjednosti = new ArrayList<>();
                noveVrjednosti.add("username ='"+kreiranKorisnik.getUsername()+"'");
                noveVrjednosti.add("email='"+kreiranKorisnik.getEmail()+"'");
                noveVrjednosti.add("ime='"+kreiranKorisnik.getIme()+"'");
                noveVrjednosti.add("prezime='"+kreiranKorisnik.getPrezime()+"'");
                noveVrjednosti.add("password_hash="+kreiranKorisnik.getPasswordHash());
                noveVrjednosti.add("razina_ovlasti="+kreiranKorisnik.getRazinaOvlasti().getRazina());
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti,noveVrjednosti);
                Promjena<String, Korisnik> promjena = kreirajPromjenu(promjenjeniPodaci,VrstaPromjene.INSERT,kreiranKorisnik);
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

    public static void obrisiKorisnika(Long id, String username, String email, String ime, String prezime, Integer passwordHash, RazinaOvlasti razinaOvlasti)throws BazaPodatakaException{
        try(Connection veza = connectToDatabase()){
            List<Korisnik> korisniciZaBrisanje = dohvatiKorisnike(id,username,email,ime,prezime,passwordHash,razinaOvlasti);
            if (korisniciZaBrisanje.isEmpty()){
                throw new BazaPodatakaException("Korisnik koji se pokusava obrisati ne posotoji u bazi");
            }
            for (Korisnik korisnik:korisniciZaBrisanje) {
                System.out.println("id korisnika:"+korisnik.getId());
                obrisiStanicuKorisniku(korisnik.getId(),null);
                PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM korisnici WHERE id=?");
                preparedStatement.setLong(1,korisnik.getId());
                preparedStatement.executeUpdate();
                List<String> noveVrjednosti = Arrays.asList("","","","","","");
                List<String> stareVrijednosti = new ArrayList<>();
                stareVrijednosti.add("username ='"+korisnik.getUsername()+"'");
                stareVrijednosti.add("email='"+korisnik.getEmail()+"'");
                stareVrijednosti.add("ime='"+korisnik.getIme()+"'");
                stareVrijednosti.add("prezime='"+korisnik.getPrezime()+"'");
                stareVrijednosti.add("password_hash="+korisnik.getPasswordHash());
                stareVrijednosti.add("razina_ovlasti="+korisnik.getRazinaOvlasti().getRazina());
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti,noveVrjednosti);
                Promjena<String, Korisnik> promjena = kreirajPromjenu(promjenjeniPodaci,VrstaPromjene.DELETE,korisnik);
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
             idStanice = dohvatiStanice(null, "", "", "", null, "", stanica.getUrl()).get(0).getId();
             if (idStanice==null){
                 throw new BazaPodatakaException("Stanica koja se pokusava izbrisati ne postoji u bazi!");
             }
        }
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
    public static void unesiStanicu(Long korisnikId,Station stanica)throws BazaPodatakaException{
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
                    Stanica kreiranaStanica = dohvatiStanice(null,"","","",null,"",stanica.getUrl()).get(0);
                    List<String> stareVrijednosti = Arrays.asList("","","","","","");
                    List<String> noveVrjednosti = new ArrayList<>();
                    noveVrjednosti.add("naziv ='"+stanica.getName()+"'");
                    noveVrjednosti.add("zemlja='"+stanica.getCountry()+"'");
                    noveVrjednosti.add("codec='"+stanica.getCodec()+"'");
                    noveVrjednosti.add("bitrate='"+stanica.getBitrate()+"'");
                    noveVrjednosti.add("zanrovi='"+stanica.getTags()+"'");
                    noveVrjednosti.add("url="+stanica.getUrl()+"'");
                    StanicaSerializable stanicaSerializable = new StanicaSerializable(kreiranaStanica.getId(), kreiranaStanica.getStanica().getName(),kreiranaStanica.getStanica().getCountry(),kreiranaStanica.getStanica().getUrl(),kreiranaStanica.getStanica().getTags(),kreiranaStanica.getStanica().getCodec(),kreiranaStanica.getStanica().getBitrate());
                    List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti,noveVrjednosti);
                    Promjena<String, StanicaSerializable> promjena = kreirajPromjenu(promjenjeniPodaci,VrstaPromjene.INSERT,stanicaSerializable);
                    GlavnaAplikacija.dodajPromjenu(promjena);
                }else{
                    //TODO: alert
                    System.out.println("Stanica je vec u bazi");
                    throw new BazaPodatakaException("Stanica vec postoji u bazi");
                }
                if (korisnikId!=null) {
                    Long idStanice = dohvatiStanice(null, "", "", "", null, "", stanica.getUrl()).get(0).getId();
                    if (dohvatiKorisnikoveStanice(korisnikId, idStanice).isEmpty()) {
                        PreparedStatement preparedStatementKS = veza.prepareStatement("INSERT INTO korisnik_station VALUES (?,?)");
                        preparedStatementKS.setLong(1, korisnikId);
                        preparedStatementKS.setLong(2, idStanice);
                        preparedStatementKS.executeUpdate();
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
            List<Stanica> staniceZaBrisanje = dohvatiStanice(null,station.getName(),station.getCountry(),station.getCodec(),station.getBitrate(),station.getTags(),station.getUrl());
            if (staniceZaBrisanje.isEmpty()){
                throw new BazaPodatakaException("Stanica koja se pokusava obrisati ne postoji u bazi!");
            }
            for (Stanica stanica:staniceZaBrisanje) {
                Stanica obrisanaStanica = dohvatiStanice(null,"","","",null,"",station.getUrl()).get(0);
                System.out.println("id stanice:"+stanica.getId());
                obrisiStanicuKorisniku(null,stanica.getStanica());
                PreparedStatement preparedStatement = veza.prepareStatement("DELETE FROM stations WHERE id=?");
                preparedStatement.setLong(1,stanica.getId());
                preparedStatement.executeUpdate();
                List<String> stareVrijednosti = new ArrayList<>();
                List<String> noveVrjednosti = Arrays.asList("","","","","","");
                stareVrijednosti.add("naziv ='"+station.getName()+"'");
                stareVrijednosti.add("zemlja='"+station.getCountry()+"'");
                stareVrijednosti.add("codec='"+station.getCodec()+"'");
                stareVrijednosti.add("bitrate='"+station.getBitrate()+"'");
                stareVrijednosti.add("zanrovi='"+station.getTags()+"'");
                stareVrijednosti.add("url='"+station.getUrl()+"'");
                StanicaSerializable stanicaSerializable = new StanicaSerializable(obrisanaStanica.getId(), obrisanaStanica.getStanica().getName(),obrisanaStanica.getStanica().getCountry(),obrisanaStanica.getStanica().getUrl(),obrisanaStanica.getStanica().getTags(),obrisanaStanica.getStanica().getCodec(),obrisanaStanica.getStanica().getBitrate());
                List<PromjenjenPodatak<String>> promjenjeniPodaci = kreirajListPromjenjenih(stareVrijednosti,noveVrjednosti);
                Promjena<String, StanicaSerializable> promjena = kreirajPromjenu(promjenjeniPodaci,VrstaPromjene.DELETE,stanicaSerializable);
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
        Stanica trazenaStanica = dohvatiStanice(null,"","","",null,"",stanica.getUrl()).get(0);
        try(Connection veza = connectToDatabase()){
            if (dohvatiStanice(null,"","","",null,"",stanica.getUrl()).isEmpty()){
                throw new BazaPodatakaException("Stanica koja se pokusava promjeniti ne postoji u bazi!");
            }
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
