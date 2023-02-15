package hr.java.player.entiteti;

import java.io.Serializable;
import java.util.List;

public class Promjene implements Serializable {
    List<Promjena<String,Entitet>> list;

    public Promjene(List<Promjena<String, Entitet>> list) {
        this.list = list;
    }

    public void addPromjena(Promjena<String,Entitet> nova){
        list.add(nova);
    }

    public List<Promjena<String, Entitet>> getList() {
        return list;
    }

    public void setList(List<Promjena<String, Entitet>> list) {
        this.list = list;
    }
}
