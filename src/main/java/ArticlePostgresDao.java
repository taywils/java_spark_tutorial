import java.sql.*;
import java.util.ArrayList;

public class ArticlePostgresDao<T extends Article> implements ArticleDbService<T> {
    // PostgreSQL connection to the database
    private Connection conn;
    // A raw SQL query used without parameters
    private Statement stmt;

    public ArticlePostgresDao() {
        // The account names setup from the command line interface
        String user = "postgres";
        String passwd = "postgres";
        String dbName = "sparkledb";
        // DB connection on localhost via JDBC
        String uri = "jdbc:postgresql://localhost/" + dbName;

        // Standard SQL CREATE TABLE query
        // The primary key is not auto incremented
        String createTableQuery =
                "CREATE TABLE IF NOT EXISTS article(" +
                        "id         INT         PRIMARY KEY NOT NULL," +
                        "title      VARCHAR(64) NOT NULL," +
                        "content    VARCHAR(512)NOT NULL," +
                        "summary    VARCHAR(64) NOT NULL," +
                        "deleted    BOOLEAN     DEFAULT FALSE," +
                        "createdAt  DATE        NOT NULL" +
                        ");"
                ;

        // Create the article table within sparkledb and close resources if an exception is thrown
        try {
            conn = DriverManager.getConnection(uri, user, passwd);
            stmt = conn.createStatement();
            stmt.execute(createTableQuery);

            System.out.println("Connecting to PostgreSQL database");
        } catch(Exception e) {
            System.out.println(e.getMessage());

            try {
                if(null != stmt) {
                    stmt.close();
                }
                if(null != conn) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    @Override
    public Boolean create(T entity) {
        try {
            String insertQuery = "INSERT INTO article(id, title, content, summary, createdAt) VALUES(?, ?, ?, ?, ?);";

            // Prepared statements allow us to avoid SQL injection attacks
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);

            // JDBC binds every prepared statement argument to a Java Class such as Integer and or String
            pstmt.setInt(1, entity.getId());
            pstmt.setString(2, entity.getTitle());
            pstmt.setString(3, entity.getContent());
            pstmt.setString(4, entity.getSummary());

            java.sql.Date sqlNow = new Date(new java.util.Date().getTime());
            pstmt.setDate(5, sqlNow);

            pstmt.executeUpdate();

            // Unless closed prepared statement connections will linger
            // Not very important for a trivial app but it will burn you in a professional large codebase
            pstmt.close();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            try {
                if(null != stmt) {
                    stmt.close();
                }
                if(null != conn) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T readOne(int id) {
        try {
            String selectQuery = "SELECT * FROM article where id = ?";

            PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            pstmt.setInt(1, id);

            pstmt.executeQuery();

            // A ResultSet is Class which represents a table returned by a SQL query
            ResultSet resultSet = pstmt.getResultSet();

            if(resultSet.next()) {
                Article entity = new Article(
                        // You must know both the column name and the type to extract the row
                        resultSet.getString("title"),
                        resultSet.getString("summary"),
                        resultSet.getString("content"),
                        resultSet.getInt("id"),
                        resultSet.getDate("createdat"),
                        resultSet.getBoolean("deleted")
                );

                pstmt.close();

                return (T) entity;
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());

            try {
                if(null != stmt) {
                    stmt.close();
                }
                if(null != conn) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked") //Tells the compiler to ignore unchecked type casts
    public ArrayList<T> readAll() {
        // Type cast the generic T into an Article
        ArrayList<Article> results = (ArrayList<Article>) new ArrayList<T>();

        try {
            String query = "SELECT * FROM article;";

            stmt.execute(query);
            ResultSet resultSet = stmt.getResultSet();

            while(resultSet.next()) {
                Article entity = new Article(
                        resultSet.getString("title"),
                        resultSet.getString("summary"),
                        resultSet.getString("content"),
                        resultSet.getInt("id"),
                        resultSet.getDate("createdat"),
                        resultSet.getBoolean("deleted")
                );

                results.add(entity);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());

            try {
                if(null != stmt) {
                    stmt.close();
                }
                if(null != conn) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        // The interface ArticleDbService relies upon the generic type T so we cast it back
        return (ArrayList<T>) results;
    }

    @Override
    public Boolean update(int id, String title, String summary, String content) {
        try {
            String updateQuery =
                "UPDATE article SET title = ?, summary = ?, content = ?" +
                        "WHERE id = ?;"
                ;

            PreparedStatement pstmt = conn.prepareStatement(updateQuery);

            pstmt.setString(1, title);
            pstmt.setString(2, summary);
            pstmt.setString(3, content);
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
        } catch(Exception e) {
            System.out.println(e.getMessage());

            try {
                if(null != stmt) {
                    stmt.close();
                }
                if(null != conn) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public Boolean delete(int id) {
        try {
            String deleteQuery = "DELETE FROM article WHERE id = ?";

            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            try {
                if(null != stmt) {
                    stmt.close();
                }
                if(null != conn) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        return true;
    }
}
