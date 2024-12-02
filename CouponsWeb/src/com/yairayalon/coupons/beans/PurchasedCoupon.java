package com.yairayalon.coupons.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PurchasedCoupon {

	private long customerId;
	private long couponId;
	private int quantity;

	// default constructor
	public PurchasedCoupon() {
		super();
	}
	
	// a full constructor
	public PurchasedCoupon(long customerId, long couponId, int quantity) {
		super();
		this.customerId = customerId;
		this.couponId = couponId;
		this.quantity = quantity;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	// toString - used to display purchased coupon's details
	@Override
	public String toString() {
		return "PurchasedCoupon [customerId=" + customerId + ", couponId=" + couponId + ", quantity=" + quantity + "]";
	}
	
}