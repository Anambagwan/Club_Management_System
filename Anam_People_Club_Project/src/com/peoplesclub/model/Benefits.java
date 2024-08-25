package com.peoplesclub.model;

public class Benefits {
	private int benefitId;
    private int membershipId;
    private String benefitDescription;
    
	public Benefits(int benefitId, int membershipId, String benefitDescription) {
		super();
		this.benefitId = benefitId;
		this.membershipId = membershipId;
		this.benefitDescription = benefitDescription;
	}

	public int getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(int benefitId) {
		this.benefitId = benefitId;
	}

	public int getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(int membershipId) {
		this.membershipId = membershipId;
	}

	public String getBenefitDescription() {
		return benefitDescription;
	}

	public void setBenefitDescription(String benefitDescription) {
		this.benefitDescription = benefitDescription;
	}

}
