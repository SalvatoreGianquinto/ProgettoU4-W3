package dao;

import enteties.Catalogo;
import enteties.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CatalogoDao {
    private EntityManager em;

    public CatalogoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Catalogo c){
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
    }

    public Catalogo findByIsbn(String isbn){
        return em.find(Catalogo.class, isbn);
    }

    public void remove(String isbn) {
        Catalogo c = findByIsbn(isbn);
        if (c != null) {
            em.getTransaction().begin();
            em.remove(c);
            em.getTransaction().commit();
        } else {
            System.out.println("Catalogo con ISBN " + isbn + " non trovato.");
        }
    }


    public List<Catalogo> findByAnnoPubblicazione(int anno) {
        TypedQuery<Catalogo> query = em.createQuery(
                "SELECT c FROM Catalogo c WHERE c.annoPubblicazione = :anno", Catalogo.class);
        query.setParameter("anno", anno);
        return query.getResultList();
    }

    public List<Libro> findByAutore(String autore) {
        return em.createQuery("SELECT l FROM Libro l WHERE l.autore = :autore", Libro.class)
                .setParameter("autore", autore)
                .getResultList();
    }

    public List<Catalogo> findByTitolo(String titolo) {
        TypedQuery<Catalogo> query = em.createQuery(
                "SELECT c FROM Catalogo c WHERE LOWER(c.titolo) LIKE LOWER(CONCAT('%', :titolo, '%'))", Catalogo.class);
        query.setParameter("titolo", titolo);
        return query.getResultList();
    }

}
