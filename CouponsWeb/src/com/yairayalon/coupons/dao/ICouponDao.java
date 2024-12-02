package com.yairayalon.coupons.dao;

import java.util.Collection;

import com.yairayalon.coupons.beans.Coupon;
import com.yairayalon.coupons.beans.PurchasedCoupon;
import com.yairayalon.coupons.enums.CouponType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

// interface to define the operation needed for a Coupon object
public interface ICouponDao {

	public long createCoupon(Coupon coupon) throws ApplicationException;
	public void removeCoupon(long couponId) throws ApplicationException;
	public void updateCoupon(Coupon coupon) throws ApplicationException;
	public Coupon getCouponByCouponId(long couponId) throws ApplicationException;
	public Collection<Coupon> getAllCoupons() throws ApplicationException;
	public Collection<Coupon> getAllCompanyCouponsByCompanyId(long companyId) throws ApplicationException;
	public Collection<Coupon> getAllCompanyCouponsByType(long companyId, CouponType couponType) throws ApplicationException;
	public Collection<Coupon> getAllCompanyCouponsByLimitedPrice(long companyId, double price) throws ApplicationException;
	public Collection<Coupon> getAllCompanyCouponsByLimitedEndDate(long companyId, String endDate) throws ApplicationException;
	public void purchaseCoupon(PurchasedCoupon purchasedCoupon) throws ApplicationException;
	public Collection<Coupon> getAllPurchasedCouponsByCustomerId(long customerId) throws ApplicationException;
	public Collection<Coupon> getAllPurchasedCouponsByType(long customerId, CouponType couponType) throws ApplicationException;
	public Collection<Coupon> getAllPurchasedCouponsByLimitedPrice(long customerId, double price) throws ApplicationException;

}