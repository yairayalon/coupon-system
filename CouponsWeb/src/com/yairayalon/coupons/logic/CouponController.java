package com.yairayalon.coupons.logic;

import java.text.ParseException;
import java.util.Collection;

import com.yairayalon.coupons.beans.Coupon;
import com.yairayalon.coupons.beans.PurchasedCoupon;
import com.yairayalon.coupons.dao.CompanyDao;
import com.yairayalon.coupons.dao.CouponDao;
import com.yairayalon.coupons.dao.CustomerDao;
import com.yairayalon.coupons.enums.CouponType;
import com.yairayalon.coupons.utils.DateUtils;
import com.yairayalon.coupons.utils.JerseyUtils;
import com.yairayalon.couponsservercommon.beans.CompanyIncome;
import com.yairayalon.couponsservercommon.beans.CustomerIncome;
import com.yairayalon.couponsservercommon.enums.ActionType;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

// Coupon Controller provides the logic for coupons
public class CouponController {

	private CouponDao couponDao;
	private CompanyDao companyDao;
	private CustomerDao customerDao;

	public CouponController() {
		this.couponDao = new CouponDao();
		this.companyDao = new CompanyDao();
		this.customerDao = new CustomerDao();
	}

	public void createCoupon(Coupon coupon) throws ApplicationException {
		validateCreateCoupon(coupon.getCouponName(), coupon.getStartDate(), coupon.getEndDate(), coupon.getAmount(), coupon.getPrice());
		long couponId = this.couponDao.createCoupon(coupon);
		CompanyIncome companyIncome = new CompanyIncome();
		companyIncome.setCompanyId(this.companyDao.getCompanyIdByCouponId(couponId));
		companyIncome.setCompanyName(this.companyDao.getCompanyNameByCouponId(couponId));
		companyIncome.setActionType(ActionType.COUPON_CREATION);
		companyIncome.setCouponId(couponId);
		companyIncome.setCouponName(coupon.getCouponName());
		companyIncome.setCommission(100);
		companyIncome.setActionDate(DateUtils.getCurrentDateAsString());
		String companyIncomeJson = JerseyUtils.convertObjectToJson(companyIncome);
		String webUrl = "http://localhost:8081/companies";
		JerseyUtils.sendJsonAsPostToWebResource(companyIncomeJson, webUrl);
	}

	// validation of the createCoupon(Coupon coupon) method
	private void validateCreateCoupon(String couponName,String startDate, String endDate, int amount, double price) throws ApplicationException {
		if (this.couponDao.isCouponExistByName(couponName)) {
			throw new ApplicationException(ErrorType.COUPON_NAME_ALREADY_EXISTS, "The coupon name '" + couponName + "' already exists.");
		}
		if (!DateUtils.isDateFormatValid(startDate, "yyyy/MM/dd")) {
			throw new ApplicationException(ErrorType.INVALID_DATE_FORMAT, "The coupon start date '" + startDate + "' format is invalid.");
		}
		if (!DateUtils.isDateFormatValid(endDate, "yyyy/MM/dd")) {
			throw new ApplicationException(ErrorType.INVALID_DATE_FORMAT, "The coupon end date '" + endDate + "' format is invalid.");
		}
		if (this.couponDao.isAmountNegative(amount)) {
			throw new ApplicationException(ErrorType.NEGATIVE_AMOUNT, "The coupon amount '" + amount + "' is invalid - it must not be negative.");
		}
		if (this.couponDao.isPriceNegative(price)) {
			throw new ApplicationException(ErrorType.NEGATIVE_PRICE, "The coupon price '" + price + "' is invalid - it must not be negative.");
		}
	}

	// the coupon itself is being removed in general and it's also being deleted
	// from the account of customers who've already purchased it earlier
	// (the DELETE CASCADE on MySQL keeps this synchronization of removal)
	public void removeCoupon(long couponId) throws ApplicationException {
		validateRemoveCoupon(couponId);
		this.couponDao.removeCoupon(couponId);
	}

	// validation of the removeCoupon(long couponId) method
	private void validateRemoveCoupon(long couponId) throws ApplicationException {
		if (!this.couponDao.isCouponExistById(couponId)) {
			throw new ApplicationException(ErrorType.COUPON_NOT_FOUND, "Coupon with ID " + couponId + " not found");
		}
	}

	public void updateCoupon(Coupon coupon) throws ApplicationException {
		validateUpdateCoupon(coupon.getCouponId(), coupon.getEndDate(), coupon.getPrice());
		this.couponDao.updateCoupon(coupon);
		CompanyIncome companyIncome = new CompanyIncome();
		companyIncome.setCompanyId(this.companyDao.getCompanyIdByCouponId(coupon.getCouponId()));
		companyIncome.setCompanyName(this.companyDao.getCompanyNameByCouponId(coupon.getCouponId()));
		companyIncome.setActionType(ActionType.COUPON_UPDATE);
		companyIncome.setCouponId(coupon.getCouponId());
		companyIncome.setCouponName(this.couponDao.getCouponNameByCouponId(coupon.getCouponId()));
		companyIncome.setCommission(10);
		companyIncome.setActionDate(DateUtils.getCurrentDateAsString());
		String companyIncomeJson = JerseyUtils.convertObjectToJson(companyIncome);
		String webUrl = "http://localhost:8081/companies";
		JerseyUtils.sendJsonAsPostToWebResource(companyIncomeJson, webUrl);
	}

	// validation of the updateCoupon(Coupon coupon) method
	private void validateUpdateCoupon(long couponId, String endDate, double price) throws ApplicationException {
		if (!this.couponDao.isCouponExistById(couponId)) {
			throw new ApplicationException(ErrorType.COUPON_NOT_FOUND, "Coupon with ID " + couponId + " not found");
		}
		if (!DateUtils.isDateFormatValid(endDate, "yyyy/MM/dd")) {
			throw new ApplicationException(ErrorType.INVALID_DATE_FORMAT, "The coupon end date '" + endDate + "' format is invalid.");
		}
		if (this.couponDao.isPriceNegative(price)) {
			throw new ApplicationException(ErrorType.NEGATIVE_PRICE, "The coupon price '" + price + "' is invalid - it must not be negative.");
		}
	}

	public Coupon getCouponByCouponId(long couponId) throws ApplicationException {
		Coupon couponByCouponId = this.couponDao.getCouponByCouponId(couponId);
		return couponByCouponId;
	}

	public Collection<Coupon> getAllCoupons() throws ApplicationException {
		Collection<Coupon> allCoupons = this.couponDao.getAllCoupons();
		return allCoupons;
	}

	public Collection<Coupon> getAllCompanyCouponsByCompanyId(long companyId) throws ApplicationException {
		Collection<Coupon> allCompanyCouponsByCompanyId = this.couponDao.getAllCompanyCouponsByCompanyId(companyId);
		return allCompanyCouponsByCompanyId;
	}

	public Collection<Coupon> getAllCompanyCouponsByType(long companyId, CouponType couponType) throws ApplicationException {
		Collection<Coupon> allCompanyCouponsByType = this.couponDao.getAllCompanyCouponsByType(companyId, couponType);
		return allCompanyCouponsByType;
	}

	public Collection<Coupon> getAllCompanyCouponsByLimitedPrice(long companyId, double price) throws ApplicationException {
		validateGetAllCompanyCouponsByLimitedPrice(price);
		Collection<Coupon> allCompanyCouponsByLimitedPrice = this.couponDao.getAllCompanyCouponsByLimitedPrice(companyId, price);
		return allCompanyCouponsByLimitedPrice;
	}

	// validation of the getAllCompanyCouponsByLimitedPrice(double price) method
	private void validateGetAllCompanyCouponsByLimitedPrice(double price) throws ApplicationException {
		if (this.couponDao.isPriceNegative(price)) {
			throw new ApplicationException(ErrorType.NEGATIVE_PRICE, "The coupon price '" + price + "' is invalid - it must not be negative.");
		}
	}

	public Collection<Coupon> getAllCompanyCouponsByLimitedEndDate(long companyId, String endDate) throws ApplicationException {
		validateGetAllCompanyCouponsByLimitedEndDate(endDate);
		Collection<Coupon> allCompanyCouponsByLimitedEndDate = this.couponDao.getAllCompanyCouponsByLimitedEndDate(companyId, endDate);
		return allCompanyCouponsByLimitedEndDate;
	}

	// validation of the getAllCompanyCouponsByLimitedEndDate(String endDate) method
	private void validateGetAllCompanyCouponsByLimitedEndDate(String endDate) throws ApplicationException {
		if (!DateUtils.isDateFormatValid(endDate, "yyyy/MM/dd")) {
			throw new ApplicationException(ErrorType.INVALID_DATE_FORMAT, "The coupon end date '" + endDate + "' format is invalid.");
		}
	}

	public void purchaseCoupon(PurchasedCoupon purchasedCoupon) throws ApplicationException, ParseException {
		validatePurchaseCoupon(purchasedCoupon.getCustomerId(), purchasedCoupon.getCouponId(), purchasedCoupon.getQuantity());
		if (!this.couponDao.isCouponAlreadyPurchasedByCustomer(purchasedCoupon.getCustomerId(), purchasedCoupon.getCouponId())) {
			this.couponDao.purchaseCoupon(purchasedCoupon);
		}
		else {
			this.couponDao.updateCustomerCouponQuantity(purchasedCoupon);
		}
		this.couponDao.updateCouponAmountInStock(purchasedCoupon.getCouponId(), purchasedCoupon.getQuantity());
		CustomerIncome customerIncome = new CustomerIncome();
		customerIncome.setCustomerId(purchasedCoupon.getCustomerId());
		customerIncome.setCustomerName(this.customerDao.getCustomerNameByCustomerId(purchasedCoupon.getCustomerId()));
		customerIncome.setActionType(ActionType.COUPON_PURCHASE);
		customerIncome.setCouponId(purchasedCoupon.getCouponId());
		customerIncome.setCouponName(this.couponDao.getCouponNameByCouponId(purchasedCoupon.getCouponId()));
		customerIncome.setQuantity(purchasedCoupon.getQuantity());
		customerIncome.setPricePerCoupon(this.couponDao.getPriceByCouponId(purchasedCoupon.getCouponId()));
		customerIncome.setIncome(purchasedCoupon.getQuantity() * customerIncome.getPricePerCoupon());
		customerIncome.setActionDate(DateUtils.getCurrentDateAsString());
		String customerIncomeJson = JerseyUtils.convertObjectToJson(customerIncome);
		String webUrl = "http://localhost:8081/customers";
		JerseyUtils.sendJsonAsPostToWebResource(customerIncomeJson, webUrl);
	}

	// validation of the purchaseCoupon(long customerId, long couponId) method
	private void validatePurchaseCoupon(long customerId, long couponId, long quantity) throws ApplicationException, ParseException {
		if (!this.customerDao.isCustomerExistById(customerId)) {
			throw new ApplicationException(ErrorType.CUSTOMER_NOT_FOUND, "Customer with ID " + customerId + " not found");
		}
		if (!this.couponDao.isCouponExistById(couponId)) {
			throw new ApplicationException(ErrorType.COUPON_NOT_FOUND, "Coupon with ID " + couponId + " not found");
		}
		if (!this.couponDao.isCouponInStock(couponId)) {
			throw new ApplicationException(ErrorType.COUPON_OUT_OF_STOCK, "Coupon with ID " + couponId + " is out of stock");
		}
		Coupon coupon = this.couponDao.getCouponByCouponId(couponId);
		if (DateUtils.isCouponExpired(coupon.getEndDate())) {
			throw new ApplicationException(ErrorType.COUPON_EXPIRED, "Coupon with ID " + couponId + " expired");
		}
		if (!this.couponDao.isQuantityPositive(quantity)) {
			throw new ApplicationException(ErrorType.NOT_POSITIVE_QUANTITY, "The number of coupons '" + quantity + "' to purchase is invalid - it must be positive.");
		}
	}

	public Collection<Coupon> getAllPurchasedCouponsByCustomerId(long customerId) throws ApplicationException {
		Collection<Coupon> allPurchasedCouponsByCustomerId = this.couponDao.getAllPurchasedCouponsByCustomerId(customerId);
		return allPurchasedCouponsByCustomerId;
	}

	public Collection<Coupon> getAllPurchasedCouponsByType(long customerId, CouponType couponType) throws ApplicationException {
		Collection<Coupon> allPurchasedCouponsByType = this.couponDao.getAllPurchasedCouponsByType(customerId, couponType);
		return allPurchasedCouponsByType;
	}

	public Collection<Coupon> getAllPurchasedCouponsByLimitedPrice(long customerId, double price) throws ApplicationException {
		validateGetAllPurchasedCouponsByLimitedPrice(price);
		Collection<Coupon> allPurchasedCouponsByLimitedPrice = this.couponDao.getAllPurchasedCouponsByLimitedPrice(customerId, price);
		return allPurchasedCouponsByLimitedPrice;
	}

	// validation of the getAllPurchasedCouponsByLimitedPrice(double price) method
	private void validateGetAllPurchasedCouponsByLimitedPrice(double price) throws ApplicationException {
		if (this.couponDao.isPriceNegative(price)) {
			throw new ApplicationException(ErrorType.NEGATIVE_PRICE, "The coupon price '" + price + "' is invalid - it must not be negative.");
		}
	}

}