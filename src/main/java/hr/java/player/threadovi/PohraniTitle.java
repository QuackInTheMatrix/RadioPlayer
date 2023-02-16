package hr.java.player.threadovi;

import hr.java.player.gui.GlavnaAplikacija;
import hr.java.player.util.Logging;

public class PohraniTitle implements Runnable{
    String noviTitle;

    public PohraniTitle(String noviTitle) {
        this.noviTitle = noviTitle;
    }

    @Override
    public void run() {
        promjeniDohvaceniTitle();
    }
    public synchronized void promjeniDohvaceniTitle(){
        while (GlavnaAplikacija.isDohvaceniIsUsed()){
            try {
                wait();
            } catch (InterruptedException e) {
                Logging.logger.error(e.getMessage(),e);
            }
        }
        GlavnaAplikacija.setDohvaceniIsUsed(true);
        GlavnaAplikacija.setDohvaceniTitle(noviTitle);
        GlavnaAplikacija.setDohvaceniIsUsed(false);
        notifyAll();
    }
}
