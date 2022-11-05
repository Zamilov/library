package ru.zamilov.library.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.zamilov.library.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс маппинга данных полученных из БД в объект Book
 */
public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setDescription(rs.getString("description"));

        return book;
    }
}
