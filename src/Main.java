import java.sql.*;

public class Main {

    static String dbName = "jdslkfh";
    static String tableName = "sdfsdfsdfsdf";
    static String[] columns = new String[]{"one", "two"};

    static final String url = "jdbc:sqlite:test.db";

    private static Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
        return conn;
    }
    public static void createNewDatabase(String fileName) {

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createNewTable(String tableName, String[] columns) {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n";
        for (int i = columns.length - 1; i > 0; i--) {
            sql += columns[i] +",\n";
        } if (columns.length != 0) {
            sql += columns[0] + "\n";
        }
        sql += ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void insert(String tableName, String[] columns, String[] vals) {
        String sql = "INSERT INTO "+tableName+"(";
        for (int i = 0; i < columns.length - 1; i++) {
            sql += columns[i] + ",";
        }
        sql += columns[columns.length - 1] + ") VALUES(";
        for (int i = 0; i < columns.length - 1; i++) {
            sql += "?,";
        }
        sql += "?)";

        try (Connection conn = Main.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= vals.length; i++) {
                pstmt.setString(i, vals[i-1]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void selectAll(String tableName){
        String sql = "SELECT * FROM "+tableName;
        try (Connection conn = Main.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("one") + "\t" +
                        rs.getDouble("two"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        createNewDatabase(dbName);
        createNewTable(tableName, columns);
        insert(tableName, columns, new String[]{"1", "2"});
        selectAll(tableName);
    }
}
