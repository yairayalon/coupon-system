package com.yairayalon.couponsservercommon.beans;

import com.yairayalon.couponsservercommon.enums.ActionType;

public class CompanyIncome {

	private long companyId;
	private String companyName;
	private ActionType actionType;
	private long couponId;
	private String couponName;
	private double commission;
	private String actionDate;

	// default constructor
	public CompanyIncome() {
		super();
	}

	// a full constructor
	public CompanyIncome(long companyId, String companyName, ActionType actionType, long couponId, String couponName,
			double commission, String actionDate) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.actionType = actionType;
		this.couponId = couponId;
		this.couponName = couponName;
		this.commission = commission;
		this.actionDate = actionDate;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	// toString - used to display company income details
	@Override
	public String toString() {
		return "CompanyIncome [companyId=" + companyId + ", companyName=" + companyName + ", actionType=" + actionType
				+ ", couponId=" + couponId + ", couponName=" + couponName + ", commission=" + commission
				+ ", actionDate=" + actionDate + "]";
	}

}