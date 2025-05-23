import enteties.*;
import enumerations.Periodicita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");
        EntityManager em = emf.createEntityManager();
        Archivio archivio = new Archivio(em);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Benvenuto in Biblioteca");

        while (true) {
            System.out.println("Scegli un'opzione:");
            System.out.println("1 - Aggiungi elemento");
            System.out.println("2 - Ricerca elemento per ISBN");
            System.out.println("3 - Rimuovi elemento");
            System.out.println("4 - Ricerca elemento per anno");
            System.out.println("5 - Ricerca elemento per autore");
            System.out.println("6 - Ricerca per titolo o parte di esso");
            System.out.println("7 - Prestiti in corso per numero tessera");
            System.out.println("8 - Prestiti scaduti e non restituiti");
            System.out.println("9 - Aggiungi utente");
            System.out.println("10 - Aggiungi prestito");
            System.out.println("0 - Esci");

            String scelta = scanner.nextLine().trim();

            switch (scelta) {
                case "1":
                    // Aggiungi elemento
                    System.out.print("Libro o rivista? ");
                    String tipo = scanner.nextLine().trim().toLowerCase();
                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine().trim();
                    System.out.print("Titolo: ");
                    String titolo = scanner.nextLine().trim();
                    System.out.print("Anno: ");
                    int anno = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Numero pagine: ");
                    int pagine = Integer.parseInt(scanner.nextLine().trim());

                    if (tipo.equals("libro")) {
                        System.out.print("Autore: ");
                        String autore = scanner.nextLine().trim();
                        System.out.print("Genere: ");
                        String genere = scanner.nextLine().trim();
                        Libro libro = new Libro(isbn, titolo, anno, pagine, autore, genere);
                        archivio.aggiungiElementoCatalogo(libro);
                        System.out.println("Libro aggiunto.");
                    } else if (tipo.equals("rivista")) {
                        System.out.print("Periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
                        Periodicita periodicita = Periodicita.valueOf(scanner.nextLine().trim().toUpperCase());
                        Rivista rivista = new Rivista(isbn, titolo, anno, pagine, periodicita);
                        archivio.aggiungiElementoCatalogo(rivista);
                        System.out.println("Rivista aggiunta.");
                    } else {
                        System.out.println("Tipo non valido.");
                    }
                    break;

                case "2":
                    // Ricerca per ISBN
                    System.out.print("ISBN: ");
                    String isbnRicerca = scanner.nextLine().trim();
                    Catalogo trovato = archivio.ricercaPerIsbn(isbnRicerca);
                    if (trovato != null) {
                        System.out.println(trovato);
                    } else {
                        System.out.println("Elemento non trovato.");
                    }
                    break;

                case "3":
                    // Rimuovi elemento
                    System.out.print("ISBN da rimuovere: ");
                    String isbnDel = scanner.nextLine().trim();
                    archivio.rimuoviElementoCatalogo(isbnDel);
                    System.out.println("Rimosso se presente.");
                    break;

                case "4":
                    // Ricerca per anno
                    System.out.print("Anno: ");
                    int annoRicerca = Integer.parseInt(scanner.nextLine().trim());
                    List<Catalogo> perAnno = archivio.ricercaPerAnno(annoRicerca);
                    for (Catalogo c : perAnno) {
                        System.out.println(c);
                    }
                    break;

                case "5":
                    // Ricerca per autore
                    System.out.print("Autore: ");
                    String autore = scanner.nextLine().trim();
                    List<Catalogo> perAutore = archivio.ricercaPerAutore(autore);
                    for (Catalogo c : perAutore) {
                        System.out.println(c);
                    }
                    break;

                case "6":
                    // Ricerca per titolo
                    System.out.print("Titolo (o parte): ");
                    String titoloLike = scanner.nextLine().trim();
                    List<Catalogo> perTitolo = archivio.ricercaPerTitolo(titoloLike);
                    for (Catalogo c : perTitolo) {
                        System.out.println(c);
                    }
                    break;

                case "7":
                    // Prestiti in corso per tessera
                    System.out.print("Numero tessera: ");
                    String tessera = scanner.nextLine().trim();
                    List<Prestito> prestiti = archivio.prestitiInCorsoPerUtente(tessera);
                    for (Prestito p : prestiti) {
                        System.out.println(p);
                    }
                    break;

                case "8":
                    // Prestiti scaduti non restituiti
                    List<Prestito> scaduti = archivio.prestitiScadutiNonRestituiti();
                    for (Prestito p : scaduti) {
                        System.out.println(p);
                    }
                    break;

                case "9":
                    // Aggiungi utente
                    System.out.print("Numero tessera: ");
                    String nTessera = scanner.nextLine().trim();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine().trim();
                    System.out.print("Cognome: ");
                    String cognome = scanner.nextLine().trim();
                    System.out.print("Data di nascita (YYYY-MM-DD): ");
                    LocalDate dataNascita = LocalDate.parse(scanner.nextLine().trim());
                    Utente utente = new Utente(nome, cognome, dataNascita, nTessera);
                    archivio.aggiungiUtente(utente);
                    System.out.println("Utente aggiunto.");
                    break;

                case "10":
                    // Aggiungi prestito
                    System.out.print("Numero tessera utente: ");
                    String tess = scanner.nextLine().trim();

                    System.out.print("ISBN del libro/rivista: ");
                    String isbnPrestito = scanner.nextLine().trim();

                    Utente utentePrestito = archivio.getUtenteByNumeroTessera(tess);
                    Catalogo elementoPrestato = archivio.ricercaPerIsbn(isbnPrestito);

                    if (utentePrestito != null && elementoPrestato != null) {
                        System.out.print("Inserisci data inizio prestito (YYYY-MM-DD): ");
                        String dataInizioStr = scanner.nextLine().trim();
                        LocalDate dataInizioPrestito;

                        try {
                            dataInizioPrestito = LocalDate.parse(dataInizioStr);
                        } catch (Exception e) {
                            System.out.println("Formato data non valido. Usata la data odierna.");
                            dataInizioPrestito = LocalDate.now();
                        }

                        LocalDate dataRestituzionePrevista = dataInizioPrestito.plusDays(30);

                        Prestito nuovoPrestito = new Prestito(utentePrestito, elementoPrestato, dataInizioPrestito, dataRestituzionePrevista);
                        archivio.aggiungiPrestito(nuovoPrestito);
                        System.out.println("Prestito aggiunto con successo.");
                    } else {
                        System.out.println("Utente o elemento non trovato.");
                    }
                    break;

                case "0":
                    System.out.println("Uscito");
                    break;

                default:
                    System.out.println("Scelta non valida.");
                    break;
            }
        }
    }
}