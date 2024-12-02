package com.yairayalon.coupons.listeners;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.yairayalon.coupons.threads.DailyCouponExpirationTask;

public class ThreadListener implements ServletContextListener {

	private Timer timer = null;

	public void contextInitialized(ServletContextEvent sce) {
		if (timer == null) {
			timer = new Timer();
			TimerTask timerTask = new DailyCouponExpirationTask();

			LocalDateTime localNow = LocalDateTime.now();
			LocalDateTime localNextTwoAm = localNow.withHour(2).withMinute(0).withSecond(0);
			if (localNow.compareTo(localNextTwoAm) > 0) {
				localNextTwoAm = localNextTwoAm.plusDays(1);
			}
			Duration duration = Duration.between(localNow, localNextTwoAm);
			long initialDelay = duration.getSeconds() * 1000;

			timer.schedule(timerTask, initialDelay, TimeUnit.MINUTES.toMillis(2));
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		timer.cancel();
	}

}