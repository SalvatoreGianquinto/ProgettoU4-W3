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
            System.out.println("11 - Restituisci prestito");
            System.out.println("0 - Esci");

            String scelta = scanner.nextLine().trim();

            switch (scelta) {
                case "1":
                    System.out.print("Libro o rivista? ");
                    String tipo = scanner.nextLine().trim().toLowerCase();

                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine().trim();

                    if (archivio.ricercaPerIsbn(isbn) != null) {
                        System.out.println("ISBN già esistente. Elemento non aggiunto.");
                        break;
                    }

                    System.out.print("Titolo: ");
                    String titolo = scanner.nextLine().trim();
                    if (titolo.isEmpty()) {
                        System.out.println("Titolo non può essere vuoto.");
                        break;
                    }

                    int anno, pagine;
                    try {
                        System.out.print("Anno: ");
                        anno = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Numero pagine: ");
                        pagine = Integer.parseInt(scanner.nextLine().trim());

                        if (anno <= 0 || pagine <= 0) {
                            System.out.println("Anno e pagine devono essere maggiori di zero.");
                            break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Anno o numero di pagine non validi.");
                        break;
                    }

                    if (tipo.equals("libro")) {
                        System.out.print("Autore: ");
                        String autoreLibro = scanner.nextLine().trim();
                        System.out.print("Genere: ");
                        String genere = scanner.nextLine().trim();

                        if (autoreLibro.isEmpty() || genere.isEmpty()) {
                            System.out.println("Autore e genere non possono essere vuoti.");
                            break;
                        }

                        archivio.aggiungiElementoCatalogo(new Libro(isbn, titolo, anno, pagine, autoreLibro, genere));
                        System.out.println("Libro aggiunto.");
                    } else if (tipo.equals("rivista")) {
                        try {
                            System.out.print("Periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
                            Periodicita periodicita = Periodicita.valueOf(scanner.nextLine().trim().toUpperCase());
                            archivio.aggiungiElementoCatalogo(new Rivista(isbn, titolo, anno, pagine, periodicita));
                            System.out.println("Rivista aggiunta.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Periodicità non valida.");
                        }
                    } else {
                        System.out.println("Tipo non riconosciuto. Inserire 'libro' o 'rivista'.");
                    }
                    break;


                case "2":
                    System.out.print("ISBN: ");
                    String isbnRicerca = scanner.nextLine().trim();
                    if (isbnRicerca.isEmpty()) {
                        System.out.println("ISBN non può essere vuoto.");
                        break;
                    }
                    Catalogo trovato = archivio.ricercaPerIsbn(isbnRicerca);
                    System.out.println(trovato != null ? trovato : "Elemento non trovato.");
                    break;


                case "3":
                    System.out.print("ISBN da rimuovere: ");
                    String isbnDel = scanner.nextLine().trim();
                    if (!isbnDel.isEmpty()) {
                        archivio.rimuoviElementoCatalogo(isbnDel);
                        System.out.println("Elemento rimosso (se presente).");
                    } else {
                        System.out.println("ISBN non valido.");
                    }
                    break;

                case "4":
                    System.out.print("Anno: ");
                    try {
                        int annoRicerca = Integer.parseInt(scanner.nextLine().trim());
                        List<Catalogo> perAnno = archivio.ricercaPerAnno(annoRicerca);
                        if (perAnno.isEmpty()) System.out.println("Nessun risultato.");
                        else perAnno.forEach(System.out::println);
                    } catch (NumberFormatException e) {
                        System.out.println("Anno non valido.");
                    }

                    break;

                case "5":
                    System.out.print("Autore: ");
                    String autore = scanner.nextLine().trim();
                    if (autore.isEmpty()) {
                        System.out.println("Autore non può essere vuoto.");
                        break;
                    }
                    List<Libro> perAutore = archivio.ricercaPerAutore(autore);
                    if (perAutore.isEmpty()) {
                        System.out.println("Nessun libro trovato per l'autore indicato.");
                    } else {
                        perAutore.forEach(System.out::println);
                    }
                    break;
                case "6":
                    System.out.print("Titolo (o parte): ");
                    String titoloLike = scanner.nextLine().trim();
                    if (titoloLike.isEmpty()) {
                        System.out.println("Titolo non può essere vuoto.");
                        break;
                    }
                    List<Catalogo> perTitolo = archivio.ricercaPerTitolo(titoloLike);
                    if (perTitolo.isEmpty()) {
                        System.out.println("Nessun elemento trovato per il titolo indicato.");
                    } else {
                        perTitolo.forEach(System.out::println);
                    }
                    break;

                case "7":
                    System.out.print("Numero tessera: ");
                    String tessera = scanner.nextLine().trim();
                    if (tessera.isEmpty()) {
                        System.out.println("Numero tessera non valido.");
                        break;
                    }
                    List<Prestito> prestiti = archivio.prestitiInCorsoPerUtente(tessera);
                    System.out.println(prestiti.isEmpty() ? "Nessun prestito in corso." : "");
                    prestiti.forEach(System.out::println);
                    break;


                case "8":
                    // Prestiti scaduti non restituiti
                    List<Prestito> scaduti = archivio.prestitiScadutiNonRestituiti();
                    if (scaduti.isEmpty()) {
                        System.out.println("Nessun prestito scaduto trovato.");
                    } else {
                        for (Prestito p : scaduti) {
                            System.out.println(p);
                        }
                    }
                    break;


                case "9":
                    System.out.print("Numero tessera: ");
                    String nTessera = scanner.nextLine().trim();

                    if (nTessera.isEmpty()) {
                        System.out.println("Numero tessera non può essere vuoto.");
                        break;
                    }

                    if (archivio.getUtenteByNumeroTessera(nTessera) != null) {
                        System.out.println("Utente con questo numero di tessera già esistente.");
                        break;
                    }

                    System.out.print("Nome: ");
                    String nome = scanner.nextLine().trim();
                    System.out.print("Cognome: ");
                    String cognome = scanner.nextLine().trim();

                    if (nome.isEmpty() || cognome.isEmpty()) {
                        System.out.println("Nome e cognome non possono essere vuoti.");
                        break;
                    }

                    System.out.print("Data di nascita (YYYY-MM-DD): ");

                    try {
                        LocalDate dataNascita = LocalDate.parse(scanner.nextLine().trim());
                        archivio.aggiungiUtente(new Utente(nome, cognome, dataNascita, nTessera));
                        System.out.println("Utente aggiunto.");
                    } catch (Exception e) {
                        System.out.println("Formato data non valido.");
                    }
                    break;

                case "10":
                    System.out.print("Numero tessera utente: ");
                    String tess = scanner.nextLine().trim();
                    System.out.print("ISBN del libro/rivista: ");
                    String isbnPrestito = scanner.nextLine().trim();

                    Utente utentePrestito = archivio.getUtenteByNumeroTessera(tess);
                    Catalogo elementoPrestato = archivio.ricercaPerIsbn(isbnPrestito);

                    if (utentePrestito == null || elementoPrestato == null) {
                        System.out.println("Utente o elemento non trovato.");
                        break;
                    }

                    if (archivio.isElementoInPrestito(isbnPrestito)) {
                        System.out.println("Elemento attualmente in prestito. Impossibile prestarlo di nuovo.");
                        break;
                    }

                    System.out.print("Inserisci data inizio prestito (YYYY-MM-DD): ");
                    LocalDate dataInizioPrestito;

                    try {
                        dataInizioPrestito = LocalDate.parse(scanner.nextLine().trim());
                        if (dataInizioPrestito.isAfter(LocalDate.now())) {
                            System.out.println("La data di inizio prestito non può essere nel futuro.");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Data non valida. Uso la data odierna.");
                        dataInizioPrestito = LocalDate.now();
                    }

                    LocalDate dataRestituzionePrevista = dataInizioPrestito.plusDays(30);
                    archivio.aggiungiPrestito(new Prestito(utentePrestito, elementoPrestato, dataInizioPrestito, dataRestituzionePrevista));
                    System.out.println("Prestito aggiunto.");
                    break;



                case "11":
                    System.out.print("ID prestito da restituire: ");
                    try {
                        int idPrestito = Integer.parseInt(scanner.nextLine().trim());
                        Prestito p = archivio.getPrestitoById(idPrestito);

                        if (p == null) {
                            System.out.println("Prestito non trovato.");
                            break;
                        }

                        if (p.getDataRestituzioneEffettiva() != null) {
                            System.out.println("Prestito già restituito il " + p.getDataRestituzioneEffettiva() + ".");
                            break;
                        }

                        archivio.restituisciPrestito(idPrestito, LocalDate.now());
                        System.out.println("Restituzione registrata.");
                    } catch (NumberFormatException e) {
                        System.out.println("ID prestito non valido.");
                    }
                    break;


                case "0":
                    System.out.println("Uscito");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Scelta non valida.");
                    break;
            }
        }
    }
}