package hr.java.player.entiteti;

public class Korisnik {
    String username;
    String password;
    RazinaOvlasti razinaOvlasti;

    public Korisnik(String username, String password, RazinaOvlasti razinaOvlasti) {
        this.username = username;
        this.password = password;
        this.razinaOvlasti = razinaOvlasti;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RazinaOvlasti getRazinaOvlasti() {
        return razinaOvlasti;
    }

    public void setRazinaOvlasti(RazinaOvlasti razinaOvlasti) {
        this.razinaOvlasti = razinaOvlasti;
    }

    @Override
    public String toString() {
        return this.username+'\n'+this.password+'\n'+this.razinaOvlasti.getRazina()+"\n";
    }
}
