package com.yairayalon.couponsservercommon.beans;

import com.yairayalon.couponsservercommon.enums.ActionType;

public class CustomerIncome {
	
	private long customerId;
	private String customerName;
	private ActionType actionType;
	private long couponId;
	private String couponName;
	private int quantity;
	private double pricePerCoupon;
	private double income;
	private String actionDate;
	
	// default constructor
	public CustomerIncome() {
		super();
	}

	// a full constructor
	public CustomerIncome(long customerId, String customerName, ActionType actionType, long couponId, String couponName,
			int quantity, double pricePerCoupon, double income, String actionDate) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.actionType = actionType;
		this.couponId = couponId;
		this.couponName = couponName;
		this.quantity = quantity;
		this.pricePerCoupon = pricePerCoupon;
		this.income = income;
		this.actionDate = actionDate;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPricePerCoupon() {
		return pricePerCoupon;
	}

	public void setPricePerCoupon(double pricePerCoupon) {
		this.pricePerCoupon = pricePerCoupon;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}
	
	// toString - used to display customer income details
	@Override
	public String toString() {
		return "CustomerIncome [customerId=" + customerId + ", customerName=" + customerName + ", actionType="
				+ actionType + ", couponId=" + couponId + ", couponName=" + couponName + ", quantity=" + quantity
				+ ", pricePerCoupon=" + pricePerCoupon + ", income=" + income + ", actionDate=" + actionDate + "]";
	}

}