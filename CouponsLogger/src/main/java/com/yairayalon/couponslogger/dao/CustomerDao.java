package com.yairayalon.couponslogger.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.stereotype.Repository;

import com.yairayalon.couponslogger.utils.JdbcUtils;
import com.yairayalon.couponsservercommon.beans.CustomerIncome;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Repository
public class CustomerDao {
	
	// method which uses to log a new CustomerIncome object and insert it into the DB
	public void logCustomerIncome(CustomerIncome customerIncome) throws ApplicationException {
		
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "INSERT INTO coupons_log.customer_income (Customer_Id, Customer_Name, Action_Type, Coupon_Id, Coupon_Name, Quantity, Price_Per_Coupon, Income, Action_Date) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setLong(1, customerIncome.getCustomerId());
			preparedStatement.setString(2, customerIncome.getCustomerName());
			preparedStatement.setString(3, customerIncome.getActionType().name());
			preparedStatement.setLong(4, customerIncome.getCouponId());
			preparedStatement.setString(5, customerIncome.getCouponName());
			preparedStatement.setInt(6, customerIncome.getQuantity());
			preparedStatement.setDouble(7, customerIncome.getPricePerCoupon());
			preparedStatement.setDouble(8, customerIncome.getIncome());
			preparedStatement.setString(9, customerIncome.getActionDate());

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, logCustomerIncome(CustomerIncome customerIncome);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}
		
	}

}