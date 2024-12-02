package com.yairayalon.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.yairayalon.coupons.beans.Company;
import com.yairayalon.coupons.utils.JdbcUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class CompanyDao implements ICompanyDao {

	// method which uses to create a new company object and insert it into the DB
	@Override
	public void createCompany(Company company) throws ApplicationException {

		// turn on connections
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "INSERT INTO company (Company_Name, Password, Email) VALUES (?, ?, ?)";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setString(1, company.getCompanyName());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setString(3, company.getEmail());

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, createCompany(Company company);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}
		
	}

	// this method removes a company and impacts 3 possible tables:
	// company (the company is being removed)
	// coupon (if the company had any coupons so its coupons are also being deleted)
	// customer_coupon (if a customer has purchased earlier any coupons that are now being deleted so they are also being deleted from his account)
	// (the DELETE CASCADE on MySQL keeps this synchronization of removal)
	@Override
	public void removeCompany(long companyId) throws ApplicationException {

		// turn on connections
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "DELETE FROM company WHERE Company_Id = ?";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setLong(1, companyId);

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, removeCompany(long companyId);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// this method updates the company's changeable details. the only
	// attributes that can be updated are the company password and the company email.
	@Override
	public void updateCompany(Company company) throws ApplicationException {

		// turn on connections
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "UPDATE company SET Password = ?, Email = ? WHERE Company_Id = ?";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setString(1, company.getPassword());
			preparedStatement.setString(2, company.getEmail());
			preparedStatement.setLong(3, company.getCompanyId());

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, updateCompany(Company company);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// method which uses to get a specific company from the DB
	@Override
	public Company getCompany(long companyId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Company company;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM company WHERE Company_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, companyId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			company = extractCompanyFromResultSet(resultSet);
			return company;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, getCompany(long companyId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}

	// method which uses to get a collection of all companies from the DB
	@Override
	public Collection<Company> getAllCompanies() throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Company> allCompanies;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM company";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Company>();
			}
			allCompanies = extractAllCompaniesFromResultSet(resultSet);
			return allCompanies;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, getAllCompanies();");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
	}

	// this method will look for the company name and password combination
	// in the DB.
	// if the name and the password match, it will return true, otherwise - false
	@Override
	public boolean login(String companyName, String password) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM company WHERE Company_Name = ? AND Password = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, companyName);
			preparedStatement.setString(2, password);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, login(String companyName, String password);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
	}

	// this method extracts the data from the DB to the Company object we'll create,
	// in order to store the company data which is requested in the get methods in it
	private Company extractCompanyFromResultSet(ResultSet resultSet) throws ApplicationException {

		// a new Company object is being created in order to store the resultset's
		// data in it
		Company company = new Company();

		// resultSet will search for this information
		try {
			company.setCompanyId(resultSet.getLong("Company_Id"));
			company.setCompanyName(resultSet.getString("Company_Name"));
			company.setPassword(resultSet.getString("Password"));
			company.setEmail(resultSet.getString("Email"));

			return company;

			// if we have problems "catch" will tell us
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, extractCompanyFromResultSet(ResultSet resultSet);");
		}

	}

	// this method functions like the extractCompanyFromResultSet(ResultSet resultSet) method (they are related),
	// besides that it uses to extract the data from the DB to multiple Company objects as a collection
	private Collection<Company> extractAllCompaniesFromResultSet(ResultSet resultSet) throws ApplicationException {

		// a new Collection of Company objects are being created in order to store the resultset's data in it
		Collection<Company> allCompanies = new ArrayList<Company>();
		try {
			do {
				Company company = extractCompanyFromResultSet(resultSet);
				allCompanies.add(company);
			}while(resultSet.next());
			return allCompanies;

			// if we have problems "catch" will tell us
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, extractAllCompaniesFromResultSet(ResultSet resultSet);");
		}

	}
	
	// method which uses to get a specific company id by its name and by its password from the DB
	public long getCompanyIdByCompanyNameAndPassword(String companyName, String password) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		long companyId;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT Company_Id FROM company WHERE Company_Name = ? AND Password = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, companyName);
			preparedStatement.setString(2, password);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return -1;
			}
			companyId = resultSet.getLong("Company_Id");
			return companyId;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, getCompanyIdByCompanyNameAndPassword(String companyName, String password);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}
	
	// method which uses to get a specific company id by one of its coupon id from the DB
	public long getCompanyIdByCouponId(long couponId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		long companyId;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT Company_Id FROM coupon WHERE Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new SQLException();
			}
			companyId = resultSet.getLong("Company_Id");
			return companyId;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, getCompanyIdByCouponId(long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}
	
	// method which uses to get a specific company name by one of its coupon id from the DB
	public String getCompanyNameByCouponId(long couponId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String companyName;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT Company_Name FROM company JOIN coupon ON company.Company_Id = coupon.Company_Id WHERE coupon.Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new SQLException();
			}
			companyName = resultSet.getString("Company_Name");
			return companyName;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, getCompanyNameByCouponId(long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}

	// this method checks if the name of the company which it gets as input exists
	public boolean isCompanyExistByName(String companyName) throws ApplicationException {
		
		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM company WHERE Company_Name = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, companyName);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, isCompanyExistByName(String companyName);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
	}

	// this method checks if the company id which it gets as input exists
	public boolean isCompanyExistById(long companyId) throws ApplicationException {
		
		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM company WHERE Company_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, companyId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CompanyDao, isCompanyExistById(long companyId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
	}

}