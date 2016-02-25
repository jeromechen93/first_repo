import java.sql.*;
import java.util.*;



public class LogInActions {
	String activeCheck = " AND person.active=true;";
	static int fails = 0;
	public static String firstUpper(String s) {
		String one = String.valueOf(s.charAt(0)).toUpperCase();
		s = one+s.substring(1);
		return s;
	}
	
	public static String getTeamByCode(int code) {
		String teamID = null;
		if (code==123456) {
			return "JAP";
		}
		else if (code==456789) {
			return "NIC";
		}
		else if (code==789123) {
			return "CHI";
		}
		else if (code==456123) {
			return "PHI";
		}
		else if (code==789456) {
			return "MEX";
		}
		else if (code==123789) {
			return "UGA";
		}
		else if (code==123123) {
			return "GUA";
		}
		return teamID;
	}
	
	public static String getLastNameByUserID(String userID) throws SQLException {
		String query = "select last_name from participant, person where person_id = \'" +userID+"\' AND participant.participant_id=person.participant_id;";
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String lastName = rs.getString("last_name");
		return lastName;
	}
	
	public static String getFirstNameByUserID(String userID) throws SQLException {
		String query = "select first_name from participant, person where person_id = \'" +userID+"\' AND participant.participant_id=person.participant_id;";
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String firstName = rs.getString("first_name");
		return firstName;
	}
	
	public static int getPartIDByUserID(String userID) throws SQLException {
		String query = "select participant_id from person where person_id = \'" +userID+"\';";
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		int partID = rs.getInt("participant_id");
		return partID;
	}
	
	public static String getUserIDByPartID(int partID) throws SQLException {
		String query = "select person_id from person where participant_id = " +partID+";";
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String userID = rs.getString("person_id");
		return userID;
	}
	
	public static String getEmailByPartID(int partID) throws SQLException {
		String query = "select email from person where participant_id = " +partID+";";
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String userID = rs.getString("email");
		return userID;
	}
	
	public static String getTeamIDByUserID(String userID) throws SQLException {
		String query = "select team_id from person, participant where person_id = \'" +userID+"\' AND participant.participant_id=person.participant_id;";
		
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String partID = rs.getString("team_id");
		return partID;
	}
	public static String getTeamNameByTeamID(String teamID) throws SQLException {
		String query = "select team_country from team where team_id = \'" +teamID+"\' ;";
		
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String teamName = rs.getString("team_country");
		return teamName;
	}
	
	public static String getPartIDByNameAndTeam(String lastName, String firstName, String teamID) throws SQLException {
		String query = "select participant_id from participant where last_name = \'" +lastName+"\' AND first_name= \'" + firstName +"\' AND team_id = \'" + teamID + "\';";
		
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String partID = rs.getString("participant_id");
		return partID;
	}
	
	public static String getFirstLastNameByPartID(int partID) throws SQLException {
		String query = "select first_name, last_name from participant where participant_id = " +partID+";";
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		String firstName = rs.getString("first_name");
		String lastName = rs.getString("last_name");
		return firstName + " " + lastName;
	}
	
	public static boolean participantExists(String lastName, String firstName, String teamID) throws SQLException {
		boolean exists;

		String query = "select count(*) as count from participant where last_name = \'" + lastName + "\' AND first_name = \'" + firstName + "\' AND team_id = \'" + teamID + "\';";
		
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		int count = Integer.parseInt(rs.getString("count"));
		int id=1;
		if (count == 1) {
			String querytwo ="select count(*) as count from person where participant_id = " + getPartIDByNameAndTeam(lastName,firstName,teamID) + ";";
			ResultSet rstwo = DatabaseActions.runQuery(querytwo);
			rstwo.next();
			id = Integer.parseInt(rstwo.getString("count"));
		}
		if (count==1 && id==0) {
			exists=true;
		}
		else {
			exists=false;
		}
		return exists;
	}
	
	public static boolean userExists(String userID) throws SQLException {
		String query = "select count(*) as count from person where person_ID = \'" + userID + "\';";
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		boolean exists;
		int count = Integer.parseInt(rs.getString("count"));
		if (count==0) {
			exists= false;
		}
		else {
			exists=true;
		}
		return exists;
		
	}
	
	public static void insertUserPass(String lastName, String firstName, String teamID) throws SQLException {
		Scanner reader = new Scanner(System.in); 
		String partIDQuery = "select participant_id from participant where last_name = \'" + lastName + "\' AND first_name = \'" + firstName + "\' AND team_id = \'" + teamID + "\';";
		ResultSet partIDRS = DatabaseActions.runQuery(partIDQuery);
		partIDRS.next();
		int partID = partIDRS.getInt("participant_id");
		
		System.out.println("Enter desired user ID:");
		String userID = reader.next().trim();
		
		if (userExists(userID)) {
			System.out.println("This user ID is already in use. Please enter a different user ID.\n");
			insertUserPass( lastName,  firstName,  teamID);
		}
		else {
			System.out.println("Enter desired password:");
			String password = reader.next().trim();
			System.out.println("Enter email address:");
			String email = reader.next().trim();
			//String insertPasswordQuery = "insert into password(participant_id, password, active) values("+partID + ", \'" + password + "\',true);"; 
			//DatabaseActions.insertQuery(insertPasswordQuery);
			
			//String passIDQuery = "select password_id from password where password = \'" + password + "\' AND participant_id = " + partID + " AND active=true;";
			//time to get the password id and insert a the person
			//ResultSet passIDRS = DatabaseActions.runQuery(passIDQuery);
			//passIDRS.next();
			//String passID = passIDRS.getString("password_id");
			String insertPersonQuery = "insert into person values(\'" + userID + "\', " + partID + ", \'" + password + "\', \'" + email + "\', true);";
			DatabaseActions.insertQuery(insertPersonQuery);
			
			System.out.println("Thank you for registering. Please log in.\n");
			logIn();
		}
	}
	
	
	
	public static void register() throws SQLException {
		Scanner reader = new Scanner(System.in); 
		System.out.println("Please enter your team code:");
		int code = reader.nextInt();
		String teamID = getTeamByCode(code);
		
		if (teamID==null) {
			if (fails<4) {
				System.out.println("You entered an incorrect team code. Try again.\n");
				fails++;
				register();
			}
			else {
				System.out.println("\nYou've entered an incorrect team code multiple times.\n\n");
				firstPrompt();
			}
			
		}
		else {
			String query = "select team_country from team where team_id= \'" + teamID + "\';";
			ResultSet rs = DatabaseActions.runQuery(query);
			rs.next();
			String teamName = rs.getString("team_country");
			System.out.println("\nYou are registering for the " + teamName + " team.\nPlease enter your name to run a check.\n");
			System.out.println("First Name: ");
			String firstName = firstUpper(reader.next().trim());
			System.out.println("Last Name: ");
			String lastName = firstUpper(reader.next().trim());
			
			if (participantExists(lastName,firstName,teamID)) {
				insertUserPass(lastName,firstName,teamID);
			
			}
			else if (!participantExists(lastName,firstName,teamID)) {
				System.out.println("\nFailed registration.\nYou either already registered for your team or you are not apart of this team.\n");
				firstPrompt();
			}
		}
	
	}
	
	
	public static void logIn() throws SQLException, NumberFormatException {
		String activeCheck = " AND person.active=true;";
		Scanner reader = new Scanner(System.in); 
		
		System.out.println("\n\nPlease enter your login credentials\nUser ID: ");
		String userID = reader.next().trim(); 
		System.out.println("Password: ");
		String password = reader.next().trim(); 
		
		String query = "select count(*) as count from person where person_id = \'" + userID + "\' AND password = \'" + password + "\'"+activeCheck; 
		
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		int exists = Integer.parseInt(rs.getString("count"));
		
		if (userID.equals("admin") && password.equals("abc12345") ) {
			AdminActions.adminPrompt();
		}
		
		if (exists==1) {			
			boolean leader = ParticipantActions.isLeader(getPartIDByUserID(userID));
			int partID = getPartIDByUserID(userID);
			String teamID = getTeamIDByUserID(userID);
			
			if (leader==true) {
				ParticipantActions.leaderPrompt(partID, teamID);
			}
			else if (leader==false) {
				ParticipantActions.participantPrompt(partID, teamID);
			}
			
		}
		else {
			System.out.println("\nFailed to log in.\n [1] Try again\n [2] Register");
			int option = Integer.parseInt(reader.next().trim()); 
			if (option==1) {
				logIn();
			}
			else {
				firstPrompt();
			}
		}
		
	}
	
	
	public static void firstPrompt() throws SQLException {
		Scanner reader = new Scanner(System.in);  
		int option=0;
		try{
			System.out.println("Welcome to the Missions Reporting Interface!\n\nPlease select an option:\n [1] Log in\n [2] Register ");
				option = Integer.parseInt(reader.next().trim()); 
				if (option!=1 && option!=2) {
					System.out.println("Sorry, your input was not valid. Try again.\n");			
					firstPrompt();
				}			
				else if (option==1) {
					logIn();				
				}
				else if (option==2) {
					register();
				}
		}
		catch (NumberFormatException e) {
			System.out.println("\n\nPlease enter a correct option.\n\n");
			firstPrompt();
		}
	}

	public static void main(String[] args) throws SQLException {
		firstPrompt();
	}
}

