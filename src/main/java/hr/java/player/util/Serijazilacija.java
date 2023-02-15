package hr.java.player.util;

import hr.java.player.entiteti.Promjene;
import hr.java.player.iznimke.SerijalizacijaException;
import java.io.*;

public class Serijazilacija{
    private static final long serialVersionUID = 2711724378809456882L;
    public static final String SERIALIZATION_FILE_NAME = "dat/promjene.dat";
    public static void serijaliziraj(Promjene svePromjene){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SERIALIZATION_FILE_NAME))) {
            out.writeObject(svePromjene);
        } catch (IOException ex) {
            throw new SerijalizacijaException("Greska prilikom serijalizacije objekta u datoteku.",ex);
        }
    }
    public static Promjene deserijaliziraj(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZATION_FILE_NAME))) {
            Promjene procitanePromjene = (Promjene) in.readObject();
           return procitanePromjene;
        } catch (IOException ex) {
            throw new SerijalizacijaException("Greska prilikom cita objekta kod deserijalizacije.",ex);
        } catch (ClassNotFoundException ex) {
            throw new SerijalizacijaException("Greska prilikom castanja objekta kod deserijalizacije.",ex);
        }
    }
}
