import java.sql.*;
import java.sql.SQLException;
import java.util.Scanner;


public class AdminActions {
	public static void notRegistered() throws SQLException {
		String query = "select first_name, last_name from participant where participant_id NOT IN (select participant.participant_id from participant, person where person.participant_id=participant.participant_id);";
		
		ResultSet rs = DatabaseActions.runQuery(query);
		System.out.println("\nParticipants not yet registered:\n");
		while (rs.next()) {
			System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));			
		}
	}
	
	
	public static void customSQL() throws SQLException {
		System.out.println("\nPlease enter your desired search query:\n");
		Scanner reader = new Scanner(System.in); 		
		String query = reader.nextLine();
		DatabaseActions.total(query);
	}
	
	
	

	public static void adminPrompt() throws SQLException {
		System.out.println("\nWelcome Administrator.\n\n");
		System.out.println(" [1] View all participants\n [2] ");
		customSQL();
	}
}
