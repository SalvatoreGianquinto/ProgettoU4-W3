package enteties;

import dao.CatalogoDao;
import dao.PrestitoDao;
import dao.UtenteDao;
import jakarta.persistence.EntityManager;

import java.util.List;

public class Archivio {

    private EntityManager em;
    private CatalogoDao catalogoDao;
    private PrestitoDao prestitoDao;
    private UtenteDao utenteDao;


    public Archivio(EntityManager em) {
        this.em = em;
        this.catalogoDao = new CatalogoDao(em);
        this.prestitoDao = new PrestitoDao(em) ;
        this.utenteDao = new UtenteDao(em);
    }

    public void aggiungiElementoCatalogo(Catalogo c){
        catalogoDao.save(c);
    }

    public void rimuoviElementoCatalogo(String isbn) {
        Catalogo c = catalogoDao.getByIsbn(isbn);
        if (c != null) {
            catalogoDao.remove(isbn);
        } else {
            System.out.println("Elemento con ISBN " + isbn + " non trovato.");
        }
    }

    // Ricerca per ISBN
    public Catalogo ricercaPerIsbn(String isbn) {
        return catalogoDao.getByIsbn(isbn);
    }

    // Ricerca per anno pubblicazione
    public List<Catalogo> ricercaPerAnno(int anno) {
        return catalogoDao.findByAnnoPubblicazione(anno);
    }

    // Ricerca per autore
    public List<Catalogo> ricercaPerAutore(String autore) {
        return catalogoDao.findByAutore(autore);
    }

    // Ricerca per titolo o parte di esso
    public List<Catalogo> ricercaPerTitolo(String titolo) {
        return catalogoDao.findByTitolo(titolo);
    }

    // Ricerca prestiti in corso per numero tessera utente
    public List<Prestito> prestitiInCorsoPerUtente(String numeroTessera) {
        return prestitoDao.findPrestitiInCorsoPerUtente(numeroTessera);
    }

    // Ricerca prestiti scaduti e non restituiti
    public List<Prestito> prestitiScadutiNonRestituiti() {
        return prestitoDao.findPrestitiScadutiNonRestituiti();
    }

    public Utente getUtenteByNumeroTessera(String numeroTessera) {
        return utenteDao.getByNumeroTessera(numeroTessera);
    }

    public void aggiungiUtente(Utente utente) {
        utenteDao.save(utente);
    }

    public void aggiungiPrestito(Prestito prestito) {
        prestitoDao.save(prestito);
    }


}
