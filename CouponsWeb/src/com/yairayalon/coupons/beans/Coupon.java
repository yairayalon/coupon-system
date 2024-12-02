package com.yairayalon.coupons.beans;

import javax.xml.bind.annotation.XmlRootElement;

import com.yairayalon.coupons.enums.CouponType;

@XmlRootElement
public class Coupon {

	private long couponId;
	private long companyId;
	private String couponName;
	private String startDate;
	private String endDate;
	private int amount;
	private CouponType couponType;
	private String description;
	private double price;
	private String image;
	private int quantity;

	// default constructor
	public Coupon() {
		super();
	}

	// a constructor which uses only part of the variables (ideal for creating a coupon)
	public Coupon(long companyId, String couponName, String startDate, String endDate, int amount, CouponType couponType,
			String description, double price, String image) {
		super();
		this.companyId = companyId;
		this.couponName = couponName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.couponType = couponType;
		this.description = description;
		this.price = price;
		this.image = image;
	}

	// a constructor which uses only part of the variables (ideal for updating a coupon)
	public Coupon(long couponId, String endDate, double price) {
		super();
		this.couponId = couponId;
		this.endDate = endDate;
		this.price = price;
	}

	// a full constructor
	public Coupon(long couponId, long companyId, String couponName, String startDate, String endDate, int amount,
			CouponType couponType, String description, double price, String image) {
		super();
		this.couponId = couponId;
		this.companyId = companyId;
		this.couponName = couponName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.couponType = couponType;
		this.description = description;
		this.price = price;
		this.image = image;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CouponType getCouponType() {
		return couponType;
	}

	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "Coupon [couponId=" + couponId + ", companyId=" + companyId + ", couponName=" + couponName
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", amount=" + amount + ", couponType="
				+ couponType + ", description=" + description + ", price=" + price + ", image=" + image + ", quantity="
				+ quantity + "]";
	}

//	// toString - used to display coupon's details
//	@Override
//	public String toString() {
//		return "Coupon [couponId=" + couponId + ", companyId=" + companyId + ", couponName=" + couponName
//				+ ", startDate=" + startDate + ", endDate=" + endDate + ", amount=" + amount + ", couponType="
//				+ couponType + ", description=" + description + ", price=" + price + ", image=" + image + "]";
//	}

}