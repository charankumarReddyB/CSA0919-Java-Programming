import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place that hands out JDBC Connections to the MySQL database.
 *
 * NOTE ON THE DRIVER:
 * We deliberately do NOT call Class.forName("com.mysql.cj.jdbc.Driver").
 * Since JDBC 4.0 (Java 6+), DriverManager automatically discovers any
 * driver on the classpath through the "Java Service Provider" mechanism
 * (the driver jar ships a file at META-INF/services/java.sql.Driver
 * that names the driver class). As long as the MySQL Connector/J jar is
 * on the classpath when the program runs, DriverManager.getConnection()
 * finds and loads the driver by itself - no reflection code needed in
 * our own source.
 *
 * Update DB_URL / DB_USER / DB_PASSWORD to match your local MySQL setup.
 */
public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "charan";

    private DatabaseConnection() {
        // utility class, no instances
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
