package day02;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BooksRepositoryTest {

    BooksRepository booksRepository;
    MariaDbDataSource mariaDbDataSource;
    Flyway flyway;

    @BeforeEach
    void setUp() {

        mariaDbDataSource = new MariaDbDataSource();
        parameterizeDataSource(mariaDbDataSource);

        flyway = Flyway.configure().dataSource(mariaDbDataSource).load();
        flyway.clean();
        flyway.migrate();

        booksRepository = new BooksRepository(mariaDbDataSource);
    }

    @Test
    void getJdbcTemplateTest() {
        assertNotNull(booksRepository.getJdbcTemplate());
    }

    @Test
    void insertBookTest() {
        booksRepository.insertBook("Author", "Title", 1000, 10);
        assertEquals(1, booksRepository.findBooksByWriterPrefix("Author").size());
        assertEquals(
            "Book{id=1, writer='Author', title='Title', price=1000, pieces=10}",
            booksRepository.findBooksByWriterPrefix("Author").get(0).toString()
        );
    }

    @Test
    void findBooksByWriterPrefixTest() {
        booksRepository.insertBook("Writer A", "Title WA", 5000, 50);
        booksRepository.insertBook("Author A", "Title AA", 1000, 10);
        booksRepository.insertBook("Author B", "Title AB", 2000, 20);
        booksRepository.insertBook("Writer B", "Title WB", 5000, 50);
        booksRepository.insertBook("Author C", "Title AC", 3000, 30);

        List<Book> booksOfAuthors = booksRepository.findBooksByWriterPrefix("Author");
        booksOfAuthors = booksOfAuthors.stream()
                .sorted(Comparator.comparingLong(Book::getId))
                .toList();

        assertEquals(3, booksOfAuthors.size());
        assertEquals(
            "Book{id=2, writer='Author A', title='Title AA', price=1000, pieces=10}",
            booksOfAuthors.get(0).toString()
        );
        assertEquals(
            "Book{id=3, writer='Author B', title='Title AB', price=2000, pieces=20}",
            booksOfAuthors.get(1).toString()
        );
        assertEquals(
            "Book{id=5, writer='Author C', title='Title AC', price=3000, pieces=30}",
            booksOfAuthors.get(2).toString()
        );
    }

    @Test
    void updatePiecesTest() {
        booksRepository.insertBook("Author A", "Title AA", 1000, 10);
        booksRepository.insertBook("Author B", "Title AB", 2000, 20);

        booksRepository.updatePieces(1L, 20);
        booksRepository.updatePieces(2L, 10);

        List<Book> booksOfAuthors = booksRepository.findBooksByWriterPrefix("Author");
        booksOfAuthors = booksOfAuthors.stream()
            .sorted(Comparator.comparingLong(Book::getId))
            .toList();

        assertEquals(
            "Book{id=1, writer='Author A', title='Title AA', price=1000, pieces=30}",
            booksOfAuthors.get(0).toString()
        );
        assertEquals(
            "Book{id=2, writer='Author B', title='Title AB', price=2000, pieces=30}",
            booksOfAuthors.get(1).toString()
        );
    }

    // --- private methods ----------------------------------------------------

    private void parameterizeDataSource(MariaDbDataSource mariaDbDataSource) {
        try {
            mariaDbDataSource.setUrl("jdbc:mariadb://localhost:3306/bookstore_test?useUnicode=true");
            mariaDbDataSource.setUser("root");
            mariaDbDataSource.setPassword("root");
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot reach database.", sqlException);
        }
    }
}