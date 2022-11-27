import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore_bh";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";


    @Override
    public  List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setAuthor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));
                book.setPublishinYear(resultSet.getInt("publishin_year"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPrice(resultSet.getDouble("price"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Book findById(Long id) {
        Book book = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setAuthor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));
                book.setPublishinYear(resultSet.getInt("publishin_year"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPrice(resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public Book create(Book book) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO books (author, title, publishin_year, isbn, price)" +
                    "VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, book.getAuthor());
            statement.setString(2, book.getTitle());
            statement.setInt(3, book.getPublishinYear());
            statement.setString(4, book.getIsbn());
            statement.setDouble(5, book.getPrice());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findByIsbn(book.getIsbn());
    }

    @Override
    public Book update(Book book) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            PreparedStatement statement = connection.prepareStatement("UPDATE books SET author = ?, title = ?, " +
                    "publishin_year = ?, isbn = ?, price = ?)");
            statement.setString(1, book.getAuthor());
            statement.setString(2, book.getTitle());
            statement.setInt(3, book.getPublishinYear());
            statement.setString(4, book.getIsbn());
            statement.setDouble(5, book.getPrice());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findByIsbn(book.getIsbn());
    }

    @Override
    public boolean delete(Long id) {
        boolean resultOfDelete = true;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE id = ?");
            statement.setLong(1, id);
            if (statement.executeUpdate() == 0) {
                resultOfDelete = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultOfDelete;
    }

    @Override
    public Book findByIsbn(String isbn) {
        Book book = null;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE isbn = ?");
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setAuthor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));
                book.setPublishinYear(resultSet.getInt("publishin_year"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPrice(resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE author = ?");
            statement.setString(1, author);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setAuthor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));
                book.setPublishinYear(resultSet.getInt("publishin_year"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPrice(resultSet.getDouble("price"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public long countAll() {
        long count = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM books");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
