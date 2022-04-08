package day02;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource mariaDbDataSource = new MariaDbDataSource();
        new Main().parameterizeDataSource(mariaDbDataSource);

        Flyway flyway = Flyway.configure().dataSource(mariaDbDataSource).load();
        flyway.migrate();

        BooksRepository booksRepository = new BooksRepository(mariaDbDataSource);
        booksRepository.insertBook("Fekete István", "Vuk", 3400, 10);
        booksRepository.insertBook("Fekete István", "Téli berek", 3600, 8);
        booksRepository.insertBook("Fekete Péter", "Kártyatrükkök", 1200, 2);
        booksRepository.updatePieces(1L, 30);

        System.out.println(booksRepository.findBooksByWriterPrefix("Fekete I"));
    }

    // --- private methods ----------------------------------------------------

    private void parameterizeDataSource(MariaDbDataSource mariaDbDataSource) {
        try {
            mariaDbDataSource.setUrl("jdbc:mariadb://localhost:3306/bookstore?useUnicode=true");
            mariaDbDataSource.setUser("root");
            mariaDbDataSource.setPassword("root");
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot reach database.", sqlException);
        }
    }
}