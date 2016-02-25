import java.sql.*;
import java.util.*;

public class ParticipantActions {
	static int fails = 0;
	
	public static boolean isLeader(int partID) throws SQLException {
		String query = "select count(*) as count from leader, participant where participant.participant_id = " + partID + " AND participant.participant_id=leader.team_leader_id;";
		
		ResultSet rs = DatabaseActions.runQuery(query);
		rs.next();
		int exists = Integer.parseInt(rs.getString("count"));
		boolean leader;
		if (exists==1) {
			leader=true;
		}
		else {
			leader=false;
		}
		return leader;
		
	}
	public static void viewTeamTotal(int partID) throws SQLException {
		//Building SQL query
		String select="select team_country, sum(donation_value) as sum ";
		String from="from participant, donation, team ";
		String where="where donation.participant_ID=participant.participant_ID and participant.team_ID=team.team_id AND team.team_id= (select team_id from participant where participant.participant_ID=" + partID + ") group by team_country";
		String query = select + from + where;
		
		//Create connection and run query
		ResultSet rs= DatabaseActions.runQuery(query);
		
		String teamCountry=null;
		String donationValue=null;
		
		while (rs.next()) {
			teamCountry = rs.getString("team_country");
			donationValue = rs.getString("sum");
			
		}
		if (teamCountry==null && donationValue==null) {
			System.out.println("Error! Input values found no match");
		}
		else {
			String print = "Team Country: " + teamCountry + "\nTotal Donation Value: "+donationValue;
			System.out.println(print);
			return;
		}
	}
	
	
	public static void viewTeamMemberDonations(int partID) throws SQLException {
		String select="select team_country, donor.last_name as dl, donor.first_name as df, participant.last_name as pl, participant.first_name as pf, donation.donation_value ";
		String from = "from participant, donor, donation, team ";
		String where = "where donation.participant_ID=participant.participant_ID and participant.team_ID=team.team_id AND donation.donor_id=donor.donor_id AND team.team_id= (select team_id from participant where participant.participant_ID=" + partID + ") order by donation_value;";
		String query = select + from + where;
		
		ResultSet rs= DatabaseActions.runQuery(query);
		
		String teamCountry=null;
		List<String> donorLastName = new ArrayList<String>();
		List<String> donorFirstName=new ArrayList<String>();
		List<String> partLastName = new ArrayList<String>();
		List<String> partFirstName=new ArrayList<String>();
		List<String> donationValue=new ArrayList<String>();
		
		
		while (rs.next()) {
			teamCountry = rs.getString("team_country");			
			donorLastName.add(rs.getString("dl"));			
			donorFirstName.add(rs.getString("df"));
			partLastName.add(rs.getString("pl"));			
			partFirstName.add(rs.getString("pf"));
			donationValue.add(rs.getString("donation_value"));			
		}
		if (teamCountry==null && donorFirstName.size()==0 && donorLastName.size()==0 && donationValue.size()==0) {
			System.out.println("Error! Input values found no match");
		}
		else {
			String teamName = "Team Country: " + teamCountry + "\n";
			System.out.println(teamName);
			for (int i=0;i<donationValue.size();i++) {
				String print =  "Donor Name: "+ donorFirstName.get(i) + " " + donorLastName.get(i) + "\n   Team Member: " + partFirstName.get(i) + " " + partLastName.get(i) + "\n   Donation Value: " + donationValue.get(i);
				System.out.println(print);
			}
			return;
		}
	}
	
	
	
	public static void viewPersonalTotal(int partID) throws SQLException {
		String select="select last_name, first_name, sum(donation_value) as sum ";
		String from="from participant, donation ";
		String where="where donation.participant_ID=participant.participant_ID AND participant.participant_ID= " + partID + " group by participant.participant_ID;";
		String query = select + from + where;
		
		ResultSet rs= DatabaseActions.runQuery(query);
		
		String lastName =null;
		String firstName=null;
		String donationValue=null;
	
		while (rs.next()) {
			lastName = rs.getString("last_name");
			firstName = rs.getString("first_name");
			donationValue=rs.getString("sum");
		}
		if (lastName==null || donationValue==null || firstName==null) {
			System.out.println("Error! Input values found no match");
		}
		else {
			String print = "Participant Name: " + firstName+" " +lastName + "\nTotal Donation Value: "+donationValue;
			System.out.println(print);
			return;
		}
		
	}
	
	
	
	public static void viewPersonalDonations(int partID) throws SQLException {
		String select="select participant.last_name as pl, participant.first_name as pf, donor.last_name as dl, donor.first_name as df, donation.donation_value ";
		String from = "from participant, donor, donation, team ";
		String where = "where donation.participant_ID=participant.participant_ID and participant.team_ID=team.team_id AND donation.donor_id=donor.donor_id AND participant.participant_ID=" + partID + " order by donation_value;";
		
		String query = select + from + where;
		
		ResultSet rs= DatabaseActions.runQuery(query);
		
		String teamCountry=null;
		String partLastName= null;
		String partFirstName=null;
		List<String> donorLastName = new ArrayList<String>();
		List<String> donorFirstName=new ArrayList<String>();
		List<String> donationValue=new ArrayList<String>();
		
		
		while (rs.next()) {
			partLastName = rs.getString("pl");
			partFirstName = rs.getString("pf");
			donorLastName.add(rs.getString("dl"));
			donorFirstName.add(rs.getString("df"));
			donationValue.add(rs.getString("donation_value"));			
		}
		if (teamCountry==null && donorFirstName.size()==0 && donorLastName.size()==0 && donationValue.size()==0) {
			System.out.println("Error! Input values found no match");
		}
		else {
			String partName = "Participant Name: " + partFirstName+" " +partLastName;
			System.out.println(partName);			
			for (int i=0;i<donationValue.size();i++) {
				String print =  "Donor Name: "+ donorFirstName.get(i) + " " + donorLastName.get(i) + "\n   Donation Value: " + donationValue.get(i);
				System.out.println(print);
			}
			return;
		}
		
	}
	
	
	public static void changeEmail(int partID, String teamID, String oldEmail, String newEmail) throws SQLException {
		Scanner reader = new Scanner(System.in);
		
		System.out.println("\nAre you sure you want to change your email address to " + newEmail + "?\n(Yes/No)");
		String answer = reader.next().toUpperCase();
		
		System.out.println();
		if (answer.equals("YES")) {
			String query = "update person set email=\'" + newEmail +"\' where participant_id = " + partID + " and email = \'" + oldEmail + "\';";
			DatabaseActions.insertQuery(query);
			System.out.println("\nEmail update is successful.\n");
			viewProfile(partID,teamID);
		}
		else if (answer.equals("NO")) {
			viewProfile(partID,teamID);
		}
		else {
			System.out.println("\nPlease enter a valid answer.\n");
			changeEmail(partID,teamID,oldEmail,newEmail);
		}
	
		
	}
	
	public static void changePassword(int partID, String teamID) throws SQLException {
		Scanner reader = new Scanner(System.in); 
		System.out.println("\nPlease enter your current password: ");
		String currentPass = reader.next();
		
		String searchQuery = "select count(*) as count from person where  password=\'" + currentPass + "\' AND participant_id=" +partID + ";";
		ResultSet rs = DatabaseActions.runQuery(searchQuery);
		rs.next();
		int count = Integer.parseInt(rs.getString("count"));
		
		if (count==0) {
			fails++;
			System.out.println("\nIncorrect Password.\n");
			if (fails>4) {
				System.out.println("\nYou've entered the wrong password.\n");
				viewProfile(partID, teamID);
			}
			else {
				changePassword(partID,teamID);
			}
			
		}
		else if (count==1) {
			System.out.println("Enter new password: ");
			String newPass = reader.next();
			String updatePerson = "update person set password= \'" + newPass + "\' where password=\'" + currentPass + "\' AND participant_ID = " + partID + ";";
			DatabaseActions.insertQuery(updatePerson);
//			String addNewPass = "insert into password(participant_id, password, active) values("+partID + ", \'" + newPass + "\',true);"; 
			System.out.println("\nYour password has been updated.\n\n");
			viewProfile(partID, teamID);
			
		}
		
		
		
	}
	
	public static void editProfile(int partID, String teamID) throws SQLException {
		Scanner reader = new Scanner(System.in); 
		System.out.println("\nEdit Profile:\n [1] Change Password\n [2] Change Email Address\n\n [3] Return to options");
		int option = reader.nextInt();
		
		if (option==1) {
			changePassword(partID,teamID);
			viewProfile(partID,teamID);
		}
		else if (option ==2) {
			String oldEmail = LogInActions.getEmailByPartID(partID);
			System.out.println("Your current email address: " + oldEmail);
			System.out.println("Enter new email: ");
			String newEmail = reader.next().trim();
			changeEmail(partID,teamID,oldEmail,newEmail);
			viewProfile(partID,teamID);
		}
		else if (option==3) {
			System.out.println("\nPlease enter a valid number.\n");
			editProfile(partID, teamID);
		}
		
		
		
	}
	
	
	
	public static void viewProfile(int partID, String teamID) throws SQLException {
		Scanner reader = new Scanner(System.in); 
		
		System.out.println("\n" + LogInActions.getFirstLastNameByPartID(partID) + "\'s Profile\n User ID: " + LogInActions.getUserIDByPartID(partID));
		System.out.println(" Email Address: "+ LogInActions.getEmailByPartID(partID) + "\n\n [1] Return to options\n [2] Edit Profile\n");
		int option = reader.nextInt();
		
		if (option ==1) {
			participantPrompt(partID, teamID);
		}
		else if (option==2) {
			editProfile(partID, teamID);
		}
		else {
			viewProfile(partID,teamID);
		}
	}
	
	public static void participantPrompt(int partID, String teamID) throws SQLException {
		Scanner reader = new Scanner(System.in); 
		
		System.out.println("\nWelcome " + LogInActions.getFirstLastNameByPartID(partID)+ ".\n\nPlease select an action:\n [1] View/Edit Profile\n\nTeam Searches:\n [2] View Team " +LogInActions.getTeamNameByTeamID(teamID) + "\'s Total Collected");
		System.out.println("\nPersonal Searches\n [3] View Personal Total Collected\n [4] View All Personal Donations\n\n [5] Logout");
		int option = reader.nextInt(); 
		if (option == 1) {
			
			viewProfile(partID,teamID);
			
			
		}
		else if (option ==2) {
			viewTeamTotal(partID);
			participantPrompt(partID, teamID);
		}
		else if (option == 3) {
			viewPersonalTotal(partID);
			participantPrompt(partID, teamID);
		}
		else if (option == 4) {
			viewPersonalDonations(partID);
			participantPrompt(partID, teamID);
		}
		else if (option == 5) {
			LogInActions.firstPrompt();
		}
		else {
			System.out.println("\nYou've entered an incorrect option. Try again.\n");
			participantPrompt(partID, teamID);
		}


	}
	
	public static void leaderPrompt(int partID, String teamID) throws SQLException {
		System.out.println("\nWelcome " + LogInActions.getFirstLastNameByPartID(partID)+ ".\nYou are the leader of Team " + LogInActions.getTeamNameByTeamID(teamID) );
		System.out.println("\nPlease select an action:\n [1] View/Edit Profile\n\nTeam Searches:\n [2] View Team " +LogInActions.getTeamNameByTeamID(teamID) + "\'s Total Collected\n [3] View All Team "+LogInActions.getTeamNameByTeamID(teamID) + "\'s Donations");
		System.out.println("\nPersonal Searches\n [4] View Personal Total Collected\n [5] View All Personal Donations\n\n [6] Logout");
		Scanner reader = new Scanner(System.in); 
		int option=reader.nextInt();
		

		if (option == 1) {
			
			viewProfile(partID,teamID);
			
			
		}
		else if (option ==2) {
			viewTeamTotal(partID);
			leaderPrompt(partID, teamID);
		}
		else if (option == 4) {
			viewPersonalTotal(partID);
			leaderPrompt(partID, teamID);
		}
		else if (option == 5) {
			viewPersonalDonations(partID);
			leaderPrompt(partID, teamID);
		}
		else if (option == 3) {
			viewTeamMemberDonations(partID);
			leaderPrompt(partID, teamID);
		}
		else if (option == 6) {
			LogInActions.firstPrompt();
		}
		else {
			System.out.println("\nYou've entered an incorrect option. Try again.\n");
			leaderPrompt(partID, teamID);
		}
	}
		
		

}
	
	

