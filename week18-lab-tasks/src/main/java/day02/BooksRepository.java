package day02;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BooksRepository {

    // --- attributes ---------------------------------------------------------

    private JdbcTemplate jdbcTemplate;

    // --- constructors -------------------------------------------------------

    public BooksRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // --- getters and setters ------------------------------------------------

    public JdbcTemplate getJdbcTemplate() { return jdbcTemplate; }

    // --- public methods -----------------------------------------------------

    //language=SQL
    public void insertBook(String writer, String title, int price, int pieces) {
        jdbcTemplate.update(
            "insert into books (writer, title, price, pieces) values (?, ?, ?, ?)",
            writer, title, price, pieces
        );
    }

    //language=SQL
    public List<Book> findBooksByWriterPrefix(String writerPrefix) {
        return jdbcTemplate.query(
            "select * from books where writer like ?",
            (resultSet, rowNumber) -> createBookFromResult(resultSet),
            writerPrefix + "%"
        );
    }

    //language=SQL
    public void updatePieces(Long id, int pieces) {
        jdbcTemplate.update(
            "update books set pieces = pieces + ? where id = ?",
            pieces, id
        );
    }

    // --- private methods ----------------------------------------------------

    private Book createBookFromResult(ResultSet resultSet) throws SQLException {
        return new Book(
            resultSet.getLong("id"),
            resultSet.getString("writer"), resultSet.getString("title"),
            resultSet.getInt("price"), resultSet.getInt("pieces")
        );
    }
}