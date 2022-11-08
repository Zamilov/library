package ru.zamilov.library.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zamilov.library.models.Book;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для работы с таблицей book в базе данных
 */
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    private static Integer id;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM book", Integer.class);
        if (id == null) id = 0;
    }

    /**
     * Возвращает список книг отсортированных по названию в обратном порядке
     */
    public List<Book> selectAllBooks() {
        return jdbcTemplate.query("SELECT * FROM book ORDER BY title DESC", new BookMapper());
    }

    /**
     * Добавляет новую книгу в таблицу book
     *
     * @param book объект книги
     */
    public Book insertBook(Book book) {
        jdbcTemplate.update("INSERT INTO book VALUES (?,?,?,?)",
                ++id, book.getTitle(), book.getAuthor(), book.getDescription());
        book.setId(id);
        return book;
    }

    /**
     * Возвращает список книг сгруппированных по автору
     */
    public Map<String, List<Book>> selectAllBooksByAuthor() {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper()).stream()
                .collect(Collectors.groupingBy(Book::getAuthor));
    }

    /**
     * Возвращает список из 10 авторов с наибольшим количеством вхождения указанного символа,
     * вместе с количеством вхождений этого символа
     *
     * @param symbol символ для поиска в названии книги
     */
    public Map<String, Long> selectAuthorsByParam(char symbol) {
        return jdbcTemplate.query("SELECT * FROM book", new BookMapper()).stream()
                .collect(
                        Collectors.groupingBy(Book::getAuthor,
                        Collectors.summingLong(book -> book.getTitle().chars()
                                .filter(ch -> ch == Character.toLowerCase(symbol) || ch == Character.toUpperCase(symbol))
                                .count())))
                .entrySet().stream()
                .filter(m -> m.getValue() > 0)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (m1, m2) -> m1, LinkedHashMap::new));
    }
}
