package com.yairayalon.coupons.threads;

import java.util.TimerTask;

import com.yairayalon.coupons.dao.CouponDao;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class DailyCouponExpirationTask extends TimerTask {

	private CouponDao couponDao;

	public DailyCouponExpirationTask() {
		this.couponDao = new CouponDao();
	}

	// this method will be invoked by the time scheduler
	@Override
	public void run() {
		try {
			this.couponDao.removeExpiredCoupons();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

}