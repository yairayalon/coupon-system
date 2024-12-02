package com.yairayalon.coupons.api;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import com.yairayalon.coupons.beans.Coupon;
import com.yairayalon.coupons.beans.PurchasedCoupon;
import com.yairayalon.coupons.enums.ClientType;
import com.yairayalon.coupons.enums.CouponType;
import com.yairayalon.coupons.logic.CouponController;
import com.yairayalon.coupons.utils.CookieUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Path("/coupons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class CouponApi {
	
	private CouponController couponController;

	public CouponApi() {
		this.couponController = new CouponController();
	}
	
	@POST
	public void createCoupon(Coupon coupon) throws ApplicationException {
		this.couponController.createCoupon(coupon);
	}

	@DELETE
	@Path("/{couponId}")
	public void reomoveCoupon(@PathParam("couponId") long couponId) throws ApplicationException {
		this.couponController.removeCoupon(couponId);
	}

	@PUT
	public void updateCoupon(Coupon coupon) throws ApplicationException {
		this.couponController.updateCoupon(coupon);
	}

	@GET
	@Path("/{couponId}")
	public Coupon getCouponByCouponId(@PathParam("couponId") long couponId) throws ApplicationException {
		Coupon couponByCouponId = this.couponController.getCouponByCouponId(couponId);
		return couponByCouponId;
	}

	@GET
	public Collection<Coupon> getAllCoupons() throws ApplicationException {
		Collection<Coupon> allCoupons = this.couponController.getAllCoupons();
		return allCoupons;
	}

	@GET
	@Path("/allCompanyCouponsByCompanyId")
	public Collection<Coupon> getAllCompanyCouponsByCompanyId(@CookieParam("couponsSession") Cookie cookie, @QueryParam("companyId") long companyId) throws ApplicationException {
		Map<String, ClientType> token = CookieUtils.createSessionToken(cookie);
		if (token.containsValue(ClientType.COMPANY) && token.containsKey(String.valueOf(companyId))) {
			Collection<Coupon> allCompanyCouponsByCompanyId = this.couponController.getAllCompanyCouponsByCompanyId(companyId);
			return allCompanyCouponsByCompanyId;
		}
		else {
			throw new ApplicationException(ErrorType.INVALID_COOKIE, "Invalid cookie or unauthorized use with cookie");
		}
	}

	@GET
	@Path("/allCompanyCouponsByType")
	public Collection<Coupon> getAllCompanyCouponsByType(@QueryParam("companyId") long companyId, @QueryParam("couponType") CouponType couponType)
																 throws ApplicationException {
		Collection<Coupon> allCompanyCouponsByType = this.couponController.getAllCompanyCouponsByType(companyId, couponType);
		return allCompanyCouponsByType;
	}

	@GET
	@Path("/allCompanyCouponsByLimitedPrice")
	public Collection<Coupon> getAllCompanyCouponsByLimitedPrice(@QueryParam("companyId") long companyId, @QueryParam("price") double price)
														 				 throws ApplicationException {
		Collection<Coupon> allCompanyCouponsByLimitedPrice = this.couponController.getAllCompanyCouponsByLimitedPrice(companyId, price);
		return allCompanyCouponsByLimitedPrice;
	}

	@GET
	@Path("/allCompanyCouponsByLimitedEndDate")
	public Collection<Coupon> getAllCompanyCouponsByLimitedEndDate(@QueryParam("companyId") long companyId, @QueryParam("endDate") String endDate)
														 				   throws ApplicationException {
		Collection<Coupon> allCompanyCouponsByLimitedEndDate = this.couponController.getAllCompanyCouponsByLimitedEndDate(companyId, endDate);
		return allCompanyCouponsByLimitedEndDate;
	}

	@POST
	@Path("/couponPurchase")
	public void purchaseCoupon(PurchasedCoupon purchasedCoupon) throws ApplicationException, ParseException {
		this.couponController.purchaseCoupon(purchasedCoupon);
	}

	@GET
	@Path("/allPurchasedCouponsByCustomerId")
	public Collection<Coupon> getAllPurchasedCouponsByCustomerId(@CookieParam("couponsSession") Cookie cookie,
																 @QueryParam("customerId") long customerId)
																	throws ApplicationException {
		Map<String, ClientType> token = CookieUtils.createSessionToken(cookie);
		if (token.containsValue(ClientType.CUSTOMER) && token.containsKey(String.valueOf(customerId))) {
			Collection<Coupon> allPurchasedCouponsByCustomerId = this.couponController.getAllPurchasedCouponsByCustomerId(customerId);
			return allPurchasedCouponsByCustomerId;
		}
		else {
			throw new ApplicationException(ErrorType.INVALID_COOKIE, "Invalid cookie or unauthorized use with cookie");
		}
	}

	@GET
	@Path("/allPurchasedCouponsByType")
	public Collection<Coupon> getAllPurchasedCouponsByType(@QueryParam("customerId") long customerId, @QueryParam("couponType") CouponType couponType)
																   throws ApplicationException {
		Collection<Coupon> allPurchasedCouponsByType = this.couponController.getAllPurchasedCouponsByType(customerId, couponType);
		return allPurchasedCouponsByType;
	}

	@GET
	@Path("/allPurchasedCouponsByLimitedPrice")
	public Collection<Coupon> getAllPurchasedCouponsByLimitedPrice(@QueryParam("customerId") long customerId, @QueryParam("price") double price)
														   				   throws ApplicationException {
		Collection<Coupon> allPurchasedCouponsByLimitedPrice = this.couponController.getAllPurchasedCouponsByLimitedPrice(customerId, price);
		return allPurchasedCouponsByLimitedPrice;
	}

}