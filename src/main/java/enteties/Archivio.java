package enteties;

import dao.CatalogoDao;
import dao.PrestitoDao;
import dao.UtenteDao;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
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
        Catalogo c = catalogoDao.findByIsbn(isbn);
        if (c != null) {
            List<Prestito> prestitiAssociati = prestitoDao.findPrestitiByCatalogo(c);
            if (prestitiAssociati.isEmpty()) {
                catalogoDao.remove(isbn);
                System.out.println("Elemento rimosso.");
            } else {
                System.out.println("Impossibile rimuovere: esistono prestiti associati a questo elemento.");
            }
        } else {
            System.out.println("Elemento con ISBN " + isbn + " non trovato.");
        }
    }


    // Ricerca per ISBN
    public Catalogo ricercaPerIsbn(String isbn) {
        return catalogoDao.findByIsbn(isbn);
    }

    // Ricerca per anno pubblicazione
    public List<Catalogo> ricercaPerAnno(int anno) {
        return catalogoDao.findByAnnoPubblicazione(anno);
    }

    // Ricerca per autore
    public List<Libro> ricercaPerAutore(String autore) {
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

    public void restituisciPrestito(int prestitoId, LocalDate dataRestituzione) {
        prestitoDao.effettuaRestituzione(prestitoId, dataRestituzione);
    }

    public boolean isElementoInPrestito(String isbn) {
        Catalogo elemento = catalogoDao.findByIsbn(isbn);
        if (elemento == null) return false;

        List<Prestito> prestiti = prestitoDao.findPrestitiByCatalogo(elemento);
        return prestiti.stream().anyMatch(p -> p.getDataRestituzioneEffettiva() == null);
    }

    public Prestito getPrestitoById(int id) {
        return prestitoDao.getById(id);
    }


}
