package com.peoplesclub.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

//import com.dao.MyConnection;
import com.peoplesclub.model.Admin;
//import com.dao.MyConnection;
//import com.dao.MyConnection;
//import com.dao.NumberFormatException;
import com.peoplesclub.model.Member;
import com.peoplesclub.model.Membership;

public class MemberDAO {
	static Connection con;

	// Password Code Starts
	// Hashing password using SHA-256
	public static String hashPassword(String password) {
		try {
			// MessageDigest instance for SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] hashBytes = digest.digest(password.getBytes());

			// Convert bytes to hexadecimal format
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			return password;
			// throw new RuntimeException("Something went wrong!!", e);
		}
	}

	// Check if a password matches the hashed password
	public static boolean verifyPassword(String password, String hashedPassword) {
		String hashedInputPassword = hashPassword(password);
		return hashedInputPassword.equals(hashedPassword);
	}

	public static void main(String[] args) {
		String password = "admin";
		String hashedPassword = hashPassword(password);
		System.out.println("Admin pass: "+hashedPassword);

		System.out.println("Original Password: " + password);
		System.out.println("Hashed Password: " + hashedPassword);

		// Verify password
		String pas = "admin";
		boolean isPasswordCorrect = verifyPassword(pas, hashedPassword);
		System.out.println("Password verification: " + isPasswordCorrect);
	}
	// Password code ends

	// Register a new member method	
	// Example of registering a member and displaying the member ID
	public int registerMember(Member member)  {

		con=MyConnection.getConnection();
		PreparedStatement ps=null;	
		int memberId = 0;


		try {			
			String getSeqSQL = "SELECT member_seq.NEXTVAL FROM dual";
			ps = con.prepareStatement(getSeqSQL);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				memberId = rs.getInt(1);  // Get the next value from the sequence
			}
			ps.close();
		}
		catch(Exception e) {
			System.out.println("Something went wrong while fetching Member ID!!");
			e.printStackTrace();
		}

		int i=0;
		try {
			ps=con.prepareStatement("INSERT INTO members VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)");
			LocalDate today = LocalDate.now();
			LocalDate expiryDate = today.plusMonths(getMembershipMonths(member.getMembershipId()));
			//			System.out.println("LocalDate: "+expiryDate);

			// Convert LocalDate to java.sql.Date
			Date sqlToday = Date.valueOf(today);
			Date sqlExpiryDate = Date.valueOf(expiryDate);

			ps.setInt(1, (memberId-1));
			ps.setString(2, member.getFirstName());
			ps.setString(3, member.getLastName());
			ps.setString(4,member.getEmail());
			ps.setString(5, member.getPhone());
			ps.setInt(6, member.getMembershipId());
			ps.setDate(7, sqlToday);
			ps.setDate(8, sqlExpiryDate);
			ps.setString(9, member.getPassword());
			i=ps.executeUpdate();
			if(i > 0) {
				System.out.println("Congratulations!!");
				System.out.println("Member registered successfully with ID: " + (memberId-1));
				System.out.println("Please remember this ID for logging into your account.");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
			System.out.println("Exception: "+e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
			System.out.println(e);
		}
		return i;

	}


	public boolean login(int memberId, String password) {

		boolean b=false;
		PreparedStatement ps=null;
		con=MyConnection.getConnection();
		try {
			ps=con.prepareStatement("select * from members where member_id = ? and password = ?");
			ps.setInt(1,memberId);
			ps.setString(2,hashPassword(password));
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				b=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return b;
		//		return false;
	}

	public boolean adminLogin(String adminUsername, String adminPassword) {
//		System.out.println("In admin loginnnn");

		List<Admin> ls=null;
		//		String str=;
		con=MyConnection.getConnection();
//		//		System.out.println("Conn: "+con);
//		try {
//			Statement s=con.createStatement();
//			ResultSet rs=s.executeQuery("select * from admin");
////			System.out.println(rs);
//
//			ls=new LinkedList<Admin>();
//			while(rs.next()){
//				Admin reg=new Admin();
//				reg.setAdminId(rs.getInt(1));
//				reg.setUsername(rs.getString(2));
//				reg.setPassword(rs.getString(3));
//				System.out.println(reg);
//
//				ls.add(reg);				
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//		return ls;	

//		for (Admin l : ls) {
//			System.out.println("Admin ID: "+l.getAdminId());
//			System.out.println("Admin Username: "+l.getUsername());
//			System.out.println("Admin Password: "+l.getPassword());
//		}
//


		boolean b=false;
		PreparedStatement ps=null;
		con=MyConnection.getConnection();
		try {
			ps=con.prepareStatement("select * from admin where username = ? and password = ?");
			ps.setString(1,adminUsername);
			//			System.out.println("hhhhhhhhhh:"+hashPassword(adminPassword));
			ps.setString(2,hashPassword(adminPassword));
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				b=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return b;
	}

	public Map<String, Object> viewMembershipDetails(int memberId) {
		Member mem = new Member();
		Membership membership = new Membership();

		boolean b=false;
		PreparedStatement ps=null;
		//		Registration reg=new Registration();
		//		Member mem = new Member();
		List<Member> ls=null;
		con=MyConnection.getConnection();
		try {
			String sql = "select m.member_id, m.first_name, m.last_name, "
					+ "m.email, m.phone, m.registration_date, m.expiry_date, "
					+ "s.membership_id, s.membership_name, s.validity_months "
					+ "from members m join membership_types s "
					+ "on m.membership_id = s.membership_id where m.member_id = ?";
			ps=con.prepareStatement(sql);
			ps.setInt(1,memberId);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){

				mem.setMemberId(rs.getInt(1));
				mem.setFirstName(rs.getString(2));
				mem.setLastName(rs.getString(3));
				mem.setEmail(rs.getString(4));
				mem.setPhone(rs.getString(5));
				mem.setRegistrationDate(rs.getDate(6));
				mem.setExpiryDate(rs.getDate(7));	
				mem.setMembershipId(rs.getInt(8));
				membership.setMembershipId(rs.getInt(8));
				membership.setMembershipName(rs.getString(9));
				membership.setValidityMonths(10);




			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> result = new HashMap<>();
		result.put("member", mem);
		result.put("membership", membership);

		return result;  // Return the map

	}

	public int getMembershipMonths(int membershipId) {
		PreparedStatement ps=null;
		int month = 0;
		con=MyConnection.getConnection();
		try {
			ps=con.prepareStatement("select VALIDITY_MONTHS from membership_types where MEMBERSHIP_ID = ?");
			ps.setInt(1,membershipId);
			//			ps.setString(2,hashPassword(password));
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				month = rs.getInt(1);
				//				System.out.println("Month is: "+month);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return month;
	}
	public static long calculateRemainingMonths(LocalDate expiryDate) {
		LocalDate today = LocalDate.now(); // Get the current date

		// If the expiry date is after today, calculate the remaining months
		if (expiryDate.isAfter(today)) {
			return ChronoUnit.MONTHS.between(today, expiryDate);
		} else {
			// If the expiry date has already passed, return 0
			return 0;
		}
	}

	public int updateMembership(int memberId, int newMembershipId) {
		// TODO Auto-generated method stub
		return 0;
		
		

	}

	public int modifyMembershipScheme(int id, String modifiedSchemeName, int new_mon_update, String newBenefits) {
		// TODO Auto-generated method stub
		System.out.println("In update function");
		con=MyConnection.getConnection();
		PreparedStatement ps=null;
		int i=0;		
		try {
			ps=con.prepareStatement("update membership_types set membership_name = ?, validity_months = ?, benefits = ? where membership_id = ?");			
			ps.setString(1,modifiedSchemeName);
			ps.setInt(2,new_mon_update);
			ps.setString(3,newBenefits);
			ps.setInt(4,id);
			System.out.println("Prepared statemetn: "+ps);
			
			i=ps.executeUpdate();
			System.out.println("I calue: "+i);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return i;

	}

	public void removeMembershipScheme(int schemeIdToRemove) {
		// TODO Auto-generated method stub

	}

	public int addMembershipScheme(String newSchemeName, int new_month, String benefits) {
		con=MyConnection.getConnection();
		PreparedStatement ps=null;	
		int membershipId = 0;


		try {			
//			String getSeqSQL = ;
			ps = con.prepareStatement("SELECT membership_seq.NEXTVAL FROM dual");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				membershipId = rs.getInt(1);  // Get the next value from the sequence
				System.out.println("membershippppppppppdi: "+membershipId);
			}
			ps.close();
		}
		catch(Exception e) {
			System.out.println("Something went wrong while fetching Membership ID!!");
			
//			e.printStackTrace();
		}

		int i=0;
		try {
			ps=con.prepareStatement("INSERT INTO membership_types VALUES (?, ?, ?, ?)");
//			LocalDate today = LocalDate.now();
//			LocalDate expiryDate = today.plusMonths(getMembershipMonths(member.getMembershipId()));
			//			System.out.println("LocalDate: "+expiryDate);

//			// Convert LocalDate to java.sql.Date
//			Date sqlToday = Date.valueOf(today);
//			Date sqlExpiryDate = Date.valueOf(expiryDate);

			ps.setInt(1, (membershipId - 1));
			ps.setString(2, newSchemeName);
			ps.setInt(3, new_month);
			ps.setString(4,benefits);
			
			i=ps.executeUpdate();
			if(i > 0) {
				System.out.println("Congratulations!!");
				System.out.println("Membership Scheme registered successfully with ID: " + (membershipId-1));
//				System.out.println("Please remember this ID for logging into your account.");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
			System.out.println("Exception: "+e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
			System.out.println(e);
		}
		return i;


	}

	public void viewAllMembers() {
		Member mem = new Member();
		Membership membership = new Membership();

		//		boolean b=false;
		PreparedStatement ps=null;
		List<Member> ls=null;
		con=MyConnection.getConnection();
		try {
			String sql = "select m.member_id, m.first_name, m.last_name, "
					+ "m.email, m.phone, m.registration_date, m.expiry_date, "
					+ "s.membership_id, s.membership_name, s.validity_months "
					+ "from members m join membership_types s "
					+ "on m.membership_id = s.membership_id";
			ps=con.prepareStatement(sql);
			//			ps.setInt(1,memberId);
			System.out.printf("%-10s %-15s %-15s %-30s %-15s %-15s %-15s\n", 
	                "Member ID", "First Name", "Last Name", "Email", "Phone", "Registration Date", "Expiry Date", "Membership Name");
	            System.out.println("-----------------------------------------------------------------------------------------------------------");

			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){

				mem.setMemberId(rs.getInt(1));
				mem.setFirstName(rs.getString(2));
				mem.setLastName(rs.getString(3));
				mem.setEmail(rs.getString(4));
				mem.setPhone(rs.getString(5));
				mem.setRegistrationDate(rs.getDate(6));
				mem.setExpiryDate(rs.getDate(7));	
				mem.setMembershipId(rs.getInt(8));
				membership.setMembershipId(rs.getInt(8));
				membership.setMembershipName(rs.getString(9));
				membership.setValidityMonths(10);
				System.out.println("-----------------------------------------------------------------------------------------------------------");
				
				System.out.printf("%-10d %-15s %-15s %-30s %-15s %-15s %-15s %-15s\n", 
						mem.getMemberId(), mem.getFirstName(), mem.getLastName(), mem.getEmail(), mem.getPhone(), mem.getRegistrationDate(), mem.getExpiryDate(), membership.getMembershipName());

			}
			System.out.println("-----------------------------------------------------------------------------------------------------------");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//		return result;  // Return the map

	}

	




}

