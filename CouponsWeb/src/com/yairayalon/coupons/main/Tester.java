package com.yairayalon.coupons.main;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.yairayalon.coupons.api.LoginApi;
import com.yairayalon.coupons.beans.*;
import com.yairayalon.coupons.logic.*;
import com.yairayalon.coupons.enums.ClientType;
import com.yairayalon.coupons.enums.CouponType;
import com.yairayalon.coupons.logic.CompanyController;
import com.yairayalon.coupons.threads.DailyCouponExpirationTask;
import com.yairayalon.coupons.utils.LoginUtils;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class Tester {

	public static void main(String[] args) throws ApplicationException, ParseException {

//		// Creating a task
//		TimerTask timerTask = new DailyCouponExpirationTask();
//
//		// Creating a timer
//		Timer timer = new Timer();
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.HOUR_OF_DAY, 2);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MILLISECOND, 0);
//
//		// Activating the DailyCouponExpirationTask once a day
//		timer.schedule(timerTask, calendar.getTime(), TimeUnit.DAYS.toMillis(1));
//
//
//
//		// login as an admin
//		UserLoginDetails userLoginDetails1 = new UserLoginDetails("admin", "1234", ClientType.ADMIN);
//		System.out.println(LoginUtils.login(userLoginDetails1));
//
//
//
//		CompanyController companyController = new CompanyController();
//
//		// creating a company
//		Company company1 = new Company("Facebook", "1234", "facebook@gmail.com");
//		companyController.createCompany(company1);
//
//		// removing a company
//		companyController.removeCompany(14);
//
//		// updating a company
//		Company company2 = new Company(8, "2345", "fanta@gmail.com");
//		companyController.updateCompany(company2);
//
//		// getting a company
//		Company company3 = companyController.getCompany(8);
//		System.out.println(company3);
//
//		 //getting all companies
//		Collection<Company> allCompanies = companyController.getAllCompanies();
//		System.out.println(allCompanies);
//
//		// login as a company
//		UserLoginDetails userLoginDetails2 = new UserLoginDetails("Facebook", "1234", ClientType.COMPANY);
//		System.out.println(LoginUtils.login(userLoginDetails2));
//
//		//getting a companyId by its name
//		long companyId = companyController.getCompanyIdByCompanyName("Facebook");
//		System.out.println(companyId);
//
//
//
//		CustomerController customerController = new CustomerController();
//
//		// creating a customer
//		Customer customer1 = new Customer("Dan", "1234");
//		customerController.createCustomer(customer1);
//
//		// removing a customer
//		customerController.removeCustomer(100);
//
//		// updating a customer
//		Customer customer2 = new Customer(12, "1010");
//		customerController.updateCustomer(customer2);
//
//		// getting a customer
//		Customer customer3 = customerController.getCustomer(7);
//		System.out.println(customer3);
//
//		// getting all customers
//		Collection<Customer> allCustomers = customerController.getAllCustomers();
//		System.out.println(allCustomers);
//
//		// login as a customer
//		UserLoginDetails userLoginDetails3 = new UserLoginDetails("Dan", "1234", ClientType.CUSTOMER);
//		System.out.println(LoginUtils.login(userLoginDetails3));
//
//		//getting a customerId by its name
//		long customerId = customerController.getCustomerIdByCustomerName("Dan");
//		System.out.println(customerId);
//
//
//
//
//		CouponController couponController = new CouponController();
//
//		// creating a coupon
//		Coupon coupon1 = new Coupon(8, "SuperMan", "1990/01/01" , "2017/12/25", 20, CouponType.MOVIES, "This is an action movie", 150, "image");
//		couponController.createCoupon(coupon1);
//
//		// removing a coupon
//		couponController.removeCoupon(30);
//
//		// updating a coupon
//		Coupon coupon2 = new Coupon(9, "2012/12/12", 500);
//		couponController.updateCoupon(coupon2);
//
//		// getting a coupon by coupon id
//		Coupon couponByCouponId = couponController.getCouponByCouponId(9);
//		System.out.println(couponByCouponId);
//
//		// getting all coupons
//		Collection<Coupon> allCoupons = couponController.getAllCoupons();
//		System.out.println(allCoupons);
//
//		// getting all of company's coupons by company id
//		Collection<Coupon> allCompanyCouponsByCompanyId = couponController.getAllCompanyCouponsByCompanyId(8);
//		System.out.println(allCompanyCouponsByCompanyId);
//
//		// getting all of company's coupons by company id and by coupon type
//		Collection<Coupon> allCompanyCouponsByType = couponController.getAllCompanyCouponsByType(8, CouponType.MOVIES);
//		System.out.println(allCompanyCouponsByType);
//
//		// getting all of company's coupons by company id and by coupon's limited price
//		Collection<Coupon> allCompanyCouponsByLimitedPrice = couponController.getAllCompanyCouponsByLimitedPrice(8, 300);
//		System.out.println(allCompanyCouponsByLimitedPrice);
//
//		// getting all of company's coupons by company id and by coupon's end date
//		Collection<Coupon> allCompanyCouponsByLimitedEndDate = couponController.getAllCompanyCouponsByLimitedEndDate(8, "2017/12/30");
//		System.out.println(allCompanyCouponsByLimitedEndDate);
//
//		// purchasing a coupon
//		PurchasedCoupon purchasedCoupon = new PurchasedCoupon(12, 8);
//		couponController.purchaseCoupon(purchasedCoupon);
//
//		// getting all of customer's purchased coupons by customer id
//		Collection<Coupon> allPurchasedCouponsByCustomerId = couponController.getAllPurchasedCouponsByCustomerId(12);
//		System.out.println(allPurchasedCouponsByCustomerId);
//
//		// getting all of customer's purchased coupons by customer id and by coupon type
//		Collection<Coupon> allPurchasedCouponsByType = couponController.getAllPurchasedCouponsByType(12, CouponType.MOVIES);
//		System.out.println(allPurchasedCouponsByType);
//
//		// getting all of customer's purchased coupons by customer id and by coupon's limited price
//		Collection<Coupon> allPurchasedCouponsByLimitedPrice = couponController.getAllPurchasedCouponsByLimitedPrice(12, 300);
//		System.out.println(allPurchasedCouponsByLimitedPrice);

	}

}