package eps;
// The java.sql imports
import java.sql.*;

/**
 *
 * @author Chukwuka Odina
 */
public class DBCon {
    private final String db_host; // declare the database host
    private final String db_user; // declare the database username
    private final String db_pass; // declare the database password
    private final Connection con; // declare a new Connection object
    private final Statement stmt; // declare a new Statement object
    private final String db_path = "192.168.43.174:3306/eps";

    public DBCon() throws SQLException{
        this.db_host = "jdbc:mysql://"+db_path+""; // the database host address
        this.db_user = "egg"; // the username with which to access the database
        this.db_pass = "egg"; // the password with which to access the database
        this.con = DriverManager.getConnection(db_host, db_user, db_pass); // a connection to the database is created using the values provided
        this.stmt = con.createStatement(); // the Statement object uses the createStatement method from the connection object
    }

    /**
     * @return the db_host
     */
    public String getDb_host() {
        return db_host;
    }

    /**
     * @return the db_user
     */
    public String getDb_user() {
        return db_user;
    }

    /**
     * @return the db_pass
     */
    public String getDb_pass() {
        return db_pass;
    }

    /**
     * @return the con
     */
    public Connection getCon() {
        return con;
    }

    /**
     * @return the stmt
     */
    public Statement getStmt() {
        return stmt;
    }

}
