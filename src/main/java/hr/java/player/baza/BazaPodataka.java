package hr.java.player.baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
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
}
