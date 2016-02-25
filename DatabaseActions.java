import java.sql.*;


public class DatabaseActions {
	private static String dbURL = "jdbc:postgresql://localhost/postgres";
	private static String user = "postgres";
	private static String pass = "James1:12";
	public static ResultSet runQuery(String query) throws SQLException{
		
		Connection conn; 
		//Creates a SQL query and runs it
		Statement stmt = null;
		ResultSet rs = null;
		conn = DriverManager.getConnection(dbURL, user, pass);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);	
		//Returns a Result Set to parse through
		conn.close();
		return rs;		
	}
	
	public static void insertQuery(String query) throws SQLException{
		//Creating the database connection
		
		Connection conn; 
		//Creates a SQL query and runs it
		Statement stmt = null;
		ResultSet rs = null;
		conn = DriverManager.getConnection(dbURL, user, pass);
		stmt = conn.createStatement();
 
		//Returns a Result Set to parse through
		conn.close();
				
	}
	public static void total(String query) throws SQLException {
		Connection conn; 
		//Creates a SQL query and runs it
		Statement stmt = null;
		ResultSet rs = null;
		conn = DriverManager.getConnection(dbURL, user, pass);
		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i=1;i<rsmd.getColumnCount();i++) {
			String name = rsmd.getColumnName(1);
			System.out.println(name);
		}
		
		
		
		conn.close();
	}
	
}
