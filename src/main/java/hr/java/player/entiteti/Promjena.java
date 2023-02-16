package hr.java.player.entiteti;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Promjena<T,E extends Entitet> implements Serializable {
    Korisnik vrsiteljPromjena;
    List<PromjenjenPodatak<T>> promjeniPodataci;
    LocalDateTime vrijemePromjene;
    VrstaPromjene vrstaPromjene;
    E objektPromjene;

    public Promjena(Korisnik vrsiteljPromjena, List<PromjenjenPodatak<T>> promjeniPodataci, LocalDateTime vrijemePromjene, VrstaPromjene vrstaPromjene, E objektPromjene) {
        this.vrsiteljPromjena = vrsiteljPromjena;
        this.promjeniPodataci = promjeniPodataci;
        this.vrijemePromjene = vrijemePromjene;
        this.vrstaPromjene = vrstaPromjene;
        this.objektPromjene = objektPromjene;
    }

    public String getTipPromjenjenog(){
        if (objektPromjene instanceof Korisnik){
            return "Korisnik";
        }else if(objektPromjene instanceof StanicaSerializable stanicaSerializable){
            if (stanicaSerializable.getId()==null){
                return "Korisnik_Stanica";
            }
            return "Stanica";
        }
        return "Nepoznata vrsta objekta, pogreska!";
    }

    public Korisnik getVrsiteljPromjena() {
        return vrsiteljPromjena;
    }

    public void setVrsiteljPromjena(Korisnik vrsiteljPromjena) {
        this.vrsiteljPromjena = vrsiteljPromjena;
    }

    public List<PromjenjenPodatak<T>> getPromjeniPodataci() {
        return promjeniPodataci;
    }

    public void setPromjeniPodataci(List<PromjenjenPodatak<T>> promjeniPodataci) {
        this.promjeniPodataci = promjeniPodataci;
    }
    public LocalDateTime getVrijemePromjene() {
        return vrijemePromjene;
    }

    public void setVrijemePromjene(LocalDateTime vrijemePromjene) {
        this.vrijemePromjene = vrijemePromjene;
    }

    public VrstaPromjene getVrstaPromjene() {
        return vrstaPromjene;
    }

    public void setVrstaPromjene(VrstaPromjene vrstaPromjene) {
        this.vrstaPromjene = vrstaPromjene;
    }

    public E getObjektPromjene() {
        return objektPromjene;
    }

    public void setObjektPromjene(E objektPromjene) {
        this.objektPromjene = objektPromjene;
    }
}
