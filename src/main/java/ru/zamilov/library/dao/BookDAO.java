package ru.zamilov.library.dao;

import javafx.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zamilov.library.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс для работы с таблицей book в базе данных
 */
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Возвращает список книг отсортированных по названию в обратном порядке
     */
    public List<Book> selectAllBooks() {
        return jdbcTemplate.query("SELECT * FROM book ORDER BY title DESC", new BookMapper());
    }

    /**
     * Добавляет новую книгу в таблицу book
     * @param book объект книги
     */
    public Book insertBook(Book book) {
        Integer id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM book", Integer.class);
        if (id != null) ++id;
        else id = 1;
        jdbcTemplate.update("INSERT INTO book VALUES (?,?,?,?)", id, book.getTitle(), book.getAuthor(), book.getDescription());
        book.setId(id);
        return book;
    }

    /**
     * Возвращает список книг отсортированных по автору
     */
    public List<Book> selectAllBooksByAuthor() {
        return jdbcTemplate.query("SELECT * FROM book ORDER BY author", new BookMapper());
    }

    /**
     * Возвращает список из 10 авторов с наибольшим количеством вхождения указанного символа,
     * вместе с количеством вхождений этого символа
     *
     * @param symbol символ для поиска в названии книги
     */
    public List<Pair<String, Integer>> selectAuthorsByParam(char symbol) {
        List<Book> books = selectAllBooksByAuthor();
        int counter = 0;
        List<Pair<String, Integer>> authors = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            counter += books.get(i).getTitle().chars()
                    .filter(ch -> ch == Character.toLowerCase(symbol) || ch == Character.toUpperCase(symbol))
                    .count();
            if (i < books.size() - 1) {
                if (!Objects.equals(books.get(i).getAuthor(), books.get(i + 1).getAuthor())) {
                    authors.add(new Pair<>(books.get(i).getAuthor(), counter));
                    counter = 0;
                }
            } else {
                authors.add(new Pair<>(books.get(i).getAuthor(), counter));
            }
        }
        return authors.stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .filter(o -> o.getValue() > 0)
                .limit(10)
                .collect(Collectors.toList());
    }
}
