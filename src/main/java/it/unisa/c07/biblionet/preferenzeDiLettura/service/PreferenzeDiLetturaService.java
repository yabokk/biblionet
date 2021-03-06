package it.unisa.c07.biblionet.preferenzeDiLettura.service;

import it.unisa.c07.biblionet.model.entity.Domanda;
import it.unisa.c07.biblionet.model.entity.Genere;
import it.unisa.c07.biblionet.model.entity.utente.Esperto;
import it.unisa.c07.biblionet.model.entity.utente.Lettore;

import java.util.List;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
public interface PreferenzeDiLetturaService {

    /**
     * Implementa la funzionalità di restituire tutti i generi
     * presenti nel database.
     * @return la lista di tutti i generi presenti nel database
     */
    List<Genere> getAllGeneri();

    /**
     * Implementa la funzionalità di restituire tutti i generi
     * data una lista di nomi di generi.
     * @param generi i generi da trovare
     * @return la lista di generi contenente solamente i generi effettivamente
     * presenti nel database
     */
    List<Genere> getGeneriByName(String[] generi);

    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * ad un esperto.
     * @param generi i generi da inserire
     * @param esperto l'esperto a cui inserirli
     */
    void addGeneriEsperto(List<Genere> generi, Esperto esperto);

    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * ad un lettore.
     * @param generi i generi da inserire
     * @param lettore il lettore a cui inserirli
     */
    void addGeneriLettore(List<Genere> generi, Lettore lettore);

    /**
     * Implementa la funzionalità di generare 5 domande casuali
     * per un questionario di supporto.
     * @return la lista di domande
     */
    List<Domanda> getDomandeCasuali();

    /**
     * Implementa la funzionalità di chiamare lo script di Python
     * che effettua le predizioni.
     * @return la lista di domande
     */
    List<String> getRisposte(String r1, String r2, String r3, String r4, String r5);
}
