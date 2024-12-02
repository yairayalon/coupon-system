package com.yairayalon.couponslogger.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.stereotype.Repository;

import com.yairayalon.couponslogger.utils.JdbcUtils;
import com.yairayalon.couponsservercommon.beans.CompanyIncome;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Repository
public class CompanyDao {
	
	// method which uses to log a new CompanyIncome object and insert it into the DB
	public void logCompanyIncome(CompanyIncome companyIncome) throws ApplicationException {
		
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "INSERTa INTO coupons_log.company_income (Company_Id, Company_Name, Action_Type, Coupon_Id, Coupon_Name, Commission, Action_Date) values (?, ?, ?, ?, ?, ?, ?)";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setLong(1, companyIncome.getCompanyId());
			preparedStatement.setString(2, companyIncome.getCompanyName());
			preparedStatement.setString(3, companyIncome.getActionType().name());
			preparedStatement.setLong(4, companyIncome.getCouponId());
			preparedStatement.setString(5, companyIncome.getCouponName());
			preparedStatement.setDouble(6, companyIncome.getCommission());
			preparedStatement.setString(7, companyIncome.getActionDate());

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, logCompanyIncome(CompanyIncome companyIncome);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}
		
	}

}