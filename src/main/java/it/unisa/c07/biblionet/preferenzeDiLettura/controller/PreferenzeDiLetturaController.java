package it.unisa.c07.biblionet.preferenzeDiLettura.controller;

import it.unisa.c07.biblionet.model.entity.Domanda;
import it.unisa.c07.biblionet.model.entity.Genere;
import it.unisa.c07.biblionet.model.entity.utente.Esperto;
import it.unisa.c07.biblionet.model.entity.utente.HaGenere;
import it.unisa.c07.biblionet.model.entity.utente.Lettore;
import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import it.unisa.c07.biblionet.preferenzeDiLettura.service.PreferenzeDiLetturaService;
import jep.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.util.PythonInterpreter;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
@SessionAttributes("loggedUser")
@RequestMapping("/preferenze-di-lettura")
public class PreferenzeDiLetturaController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final PreferenzeDiLetturaService preferenzeDiLetturaService;

    /**
     * Implementa la funzionalità di controllare se l'utente in sessione
     * è abilitato ad inserire dei generi, se lo è riceve tutti i generi
     * presenti nel database e rimanda l'utente alla pagina di
     * inserimento dei generi, altrimenti lo rimanda alla home.
     *
     * @param model utilizzato per ottenere l'utente in sessione
     *
     * @return la view di inserimento dei generi se l'utente è
     * Esperto o Lettore, la home altrimenti,
     */
    @RequestMapping("/generi")
    public String generiLetterari(final Model model) {

            UtenteRegistrato utenteRegistrato =
                    (UtenteRegistrato) model.getAttribute("loggedUser");

            if (utenteRegistrato != null
                    && (utenteRegistrato.getTipo().equals("Esperto")
                    ||  utenteRegistrato.getTipo().equals("Lettore"))) {

                HaGenere utente = (HaGenere) utenteRegistrato;
                List<Genere> allGeneri =
                        preferenzeDiLetturaService.getAllGeneri();

                List<Genere> generiUtente = utente.getGeneri();

                if (generiUtente != null) {

                    for (Genere genere : generiUtente) {
                        allGeneri.remove(genere);
                    }
                } else {
                    generiUtente = new ArrayList<>();
                }


                model.addAttribute("generiUtente", generiUtente);
                model.addAttribute("generi", allGeneri);
                return "preferenze-lettura/modifica-generi";
            } else {
                return "index";
            }
    }

    /**
     * Implementa la funzionalità di inserire o rimuovere generi ad un esperto
     * oppure ad un lettore.
     * @param generi i generi che il lettore o l'esperto dovranno possedere
     * @param model utilizzato per prendere l'utente loggato a cui modificare
     *              i generi
     * @return la pagina home
     */
    @RequestMapping(value = "/modifica-generi", method = RequestMethod.POST)
    public String modificaGeneri(@RequestParam("genere") final String[]generi,
                                            final Model model) {

        List<Genere> toAdd = preferenzeDiLetturaService.getGeneriByName(generi);
        UtenteRegistrato utenteRegistrato =
                (UtenteRegistrato) model.getAttribute("loggedUser");

        if (utenteRegistrato != null) {
            if (utenteRegistrato.getTipo().equals("Esperto")) {

                preferenzeDiLetturaService
                        .addGeneriEsperto(toAdd, (Esperto) utenteRegistrato);

            } else if (utenteRegistrato.getTipo().equals("Lettore")) {
                preferenzeDiLetturaService
                        .addGeneriLettore(toAdd, (Lettore) utenteRegistrato);
            }
        }
        return "autenticazione/login";
    }

    /**
     * Implementa la funzionalità visualizzare un questionario di supporto.
     * @param model utilizzato per prendere l'utente loggato
     * @return la pagina del questionario
     */
    @RequestMapping(value = "/visualizza-questionario", method = RequestMethod.GET)
    public String visualizzaQuestionario(final Model model) {

        List<Domanda> listaDomande = preferenzeDiLetturaService.getDomandeCasuali();

        model.addAttribute("domanda1", listaDomande.get(0));
        model.addAttribute("domanda2", listaDomande.get(1));
        model.addAttribute("domanda3", listaDomande.get(2));
        model.addAttribute("domanda4", listaDomande.get(3));
        model.addAttribute("domanda5", listaDomande.get(4));

        return "questionario-supporto/visualizza-questionario-supporto";
    }

    /**
     * Implementa la funzionalità visualizzare un questionario di supporto.
     * @param model utilizzato per prendere l'utente loggato
     * @return la pagina del questionario
     */
    @RequestMapping(value = "/visualizza-risposta-questionario", method = RequestMethod.POST)
    public String rispostaQuestionario(final Model model,
                                       @RequestParam("options1") final String r1,
                                       @RequestParam("options2") final String r2,
                                       @RequestParam("options3") final String r3,
                                       @RequestParam("options4") final String r4,
                                       @RequestParam("options5") final String r5) {

        List<String> risposte = preferenzeDiLetturaService.getRisposte(r1, r2, r3, r4, r5);
        HashMap<String, Integer> generi = new HashMap<>();
        for(String g : risposte) {
            if(generi.containsKey(g)) {
                generi.replace(g, generi.get(g)+1);
            } else {
                generi.put(g, 1);
            }
        }
        String mainGenre = null;
        for(Map.Entry<String, Integer> h : generi.entrySet()) {
            if(mainGenre == null || h.getValue() > generi.get(mainGenre)) {
                mainGenre = h.getKey();
            }
        }
        List<String> otherGenres = new ArrayList<>();
        for(String s : risposte) {
            if(!s.equals(mainGenre) && !otherGenres.contains(s)){
                otherGenres.add(s);
            }
        }
        model.addAttribute("mainGenre", mainGenre);
        model.addAttribute("otherGenres", otherGenres);

        return "questionario-supporto/visualizza-risultati-questionario";
    }

}
