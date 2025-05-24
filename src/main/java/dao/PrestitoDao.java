package dao;

import enteties.Catalogo;
import enteties.Prestito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class PrestitoDao {
    private EntityManager em;

    public PrestitoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Prestito p) {
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    public Prestito getById(int id) {
        return em.find(Prestito.class, id);
    }

    public void remove(int id) {
        Prestito p = getById(id);
        if (p != null) {
            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();
        } else {
            System.out.println("Prestito con ID " + id + " non trovato.");
        }
    }

    public List<Prestito> findPrestitiInCorsoPerUtente(String numeroTessera) {
        TypedQuery<Prestito> query = em.createQuery(
                "SELECT p FROM Prestito p WHERE p.utente.numeroTessera = :numeroTessera AND p.dataRestituzioneEffettiva IS NULL", Prestito.class);
        query.setParameter("numeroTessera", numeroTessera);
        return query.getResultList();
    }

    // Prestiti scaduti e non restituiti
    public List<Prestito> findPrestitiScadutiNonRestituiti() {
        TypedQuery<Prestito> query = em.createQuery(
                "SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < :oggi AND p.dataRestituzioneEffettiva IS NULL", Prestito.class);
        query.setParameter("oggi", LocalDate.now());
        return query.getResultList();
    }

    public void effettuaRestituzione(int prestitoId, LocalDate dataRestituzioneEffettiva) {
        em.getTransaction().begin();
        int updatedCount = em.createQuery(
                        "UPDATE Prestito p SET p.dataRestituzioneEffettiva = :dataRestituzione WHERE p.id = :id")
                .setParameter("dataRestituzione", dataRestituzioneEffettiva)
                .setParameter("id", prestitoId)
                .executeUpdate();
        em.getTransaction().commit();

        if (updatedCount == 0) {
            System.out.println("Prestito con ID " + prestitoId + " non trovato o già restituito.");
        }
    }

    public List<Prestito> findPrestitiByCatalogo(Catalogo catalogo) {
        return em.createQuery("SELECT p FROM Prestito p WHERE p.elementoPrestato = :catalogo", Prestito.class)
                .setParameter("catalogo", catalogo)
                .getResultList();
    }

}
