package dao;

import enteties.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UtenteDao {
    private EntityManager em;

    public UtenteDao(EntityManager em) {
        this.em = em;
    }

    public void save(Utente user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public Utente getById(int id) {
        return em.find(Utente.class, id);
    }

    public void remove(int id) {
        Utente user = getById(id);
        if (user != null) {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        } else {
            System.out.println("Utente con ID " + id + " non trovato.");
        }
    }
    public Utente getByNumeroTessera(String numeroTessera) {
        try {
            return em.createQuery("SELECT u FROM Utente u WHERE u.numeroTessera = :numeroTessera", Utente.class)
                    .setParameter("numeroTessera", numeroTessera)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
