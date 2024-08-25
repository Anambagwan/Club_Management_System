package com.peoplesclub.model;

public class Membership {
	
	private int membershipId;
    private String membershipName;
    private int validityMonths;
    
    public Membership() {
    	
    }
    // Constructor
	public Membership(int membershipId, String membershipName, int validityMonths) {
		super();
		this.membershipId = membershipId;
		this.membershipName = membershipName;
		this.validityMonths = validityMonths;
	}
	
	// Getters and Setter
	public int getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(int membershipId) {
		this.membershipId = membershipId;
	}
	public String getMembershipName() {
		return membershipName;
	}
	public void setMembershipName(String membershipName) {
		this.membershipName = membershipName;
	}
	public int getValidityMonths() {
		return validityMonths;
	}
	public void setValidityMonths(int validityMonths) {
		this.validityMonths = validityMonths;
	}
	
	public String toString() {
		return "Membership Type: "+getMembershipName();
	}
	public void displayMembershipDetails() {
		System.out.println("Membership Name: "+getMembershipName());
//		System.out.println("");
		
	}
    
    
}
