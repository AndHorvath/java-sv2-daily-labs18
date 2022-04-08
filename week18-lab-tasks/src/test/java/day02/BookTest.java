package day02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    Book book;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Author A", "Title T", 1000, 10);
    }

    @Test
    void getIdTest() {
        assertEquals(1L, book.getId());
    }

    @Test
    void getWriterTest() {
        assertEquals("Author A", book.getWriter());
    }

    @Test
    void getTitleTest() {
        assertEquals("Title T", book.getTitle());
    }

    @Test
    void getPriceTest() {
        assertEquals(1000,  book.getPrice());
    }

    @Test
    void getPiecesTest() {
        assertEquals(10, book.getPieces());
    }

    @Test
    void testToStringTest() {
        assertEquals(
            "Book{id=1, writer='Author A', title='Title T', price=1000, pieces=10}",
            book.toString()
        );
    }
}