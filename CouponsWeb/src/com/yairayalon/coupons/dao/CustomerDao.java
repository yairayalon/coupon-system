package com.yairayalon.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.yairayalon.coupons.beans.Customer;
import com.yairayalon.coupons.utils.JdbcUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class CustomerDao implements ICustomerDao {

	// method which uses to create a new customer object and insert it into the DB
	@Override
	public void createCustomer(Customer customer) throws ApplicationException {

		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "INSERT INTO customer (Customer_Name, Password) VALUES (?, ?)";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setString(1, customer.getCustomerName());
			preparedStatement.setString(2, customer.getPassword());

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, createCustomer(Customer customer);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);

		}

	}

	// this method removes a customer and impacts 2 possible tables:
	// customer (the customer is being removed)
	// customer_coupon (if a customer has purchased earlier any coupons so his coupons purchase history is being deleted with himself)
	// (the DELETE CASCADE on MySQL keeps this synchronization of removal)
	@Override
	public void removeCustomer(long customerId) throws ApplicationException {

		// turn on connections
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "DELETE FROM customer WHERE Customer_Id = ?";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setLong(1, customerId);

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, removeCustomer(long customerId);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// this method updates the customer's changeable details.
	// the only attribute that can be updated is the customer password.
	@Override
	public void updateCustomer(Customer customer) throws ApplicationException {

		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "UPDATE customer SET Password = ? WHERE Customer_Id = ?";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setString(1, customer.getPassword());
			preparedStatement.setLong(2, customer.getCustomerId());

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, updateCustomer(Customer customer);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// method  which uses to get a specific customer from the DB
	@Override
	public Customer getCustomer(long customerId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM customer WHERE Customer_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, customerId);

			// DB respond + information on customers
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			customer = extractCustomerFromResultSet(resultSet);
			return customer;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, getCustomer(long customerId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all customers from the DB
	@Override
	public Collection<Customer> getAllCustomers() throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Customer> allCustomers;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM customer";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// DB respond + information on customers
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Customer>();
			}
			allCustomers = extractAllCustomersFromResultSet(resultSet);
			return allCustomers;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, getAllCustomers();");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// this method will look for the customer name and password combination
	// in the DB.
	// if the name and the password match, it will return true, otherwise - false
	@Override
	public boolean login(String customerName, String password) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM customer WHERE Customer_Name = ? AND Password = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, customerName);
			preparedStatement.setString(2, password);

			// DB respond + information on customers
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, login(String customerName, String password);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// this method extracts the data from the DB to the Customer object we'll create,
	// in order to store the customer data which is requested in the get methods in it
	private Customer extractCustomerFromResultSet(ResultSet resultSet) throws ApplicationException {

		// a new Customer object is being created in order to store the resultset's
		// data in it
		Customer customer = new Customer();

		// resultSet will search for this information
		try {
			customer.setCustomerId(resultSet.getLong("Customer_Id"));
			customer.setCustomerName(resultSet.getString("Customer_Name"));
			customer.setPassword(resultSet.getString("Password"));

			return customer;

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, extractCustomerFromResultSet(ResultSet resultSet);");
		}

	}

	private Collection<Customer> extractAllCustomersFromResultSet(ResultSet resultSet) throws ApplicationException {

		// this method functions like the extractCustomerFromResultSet(ResultSet resultSet) method (they are related),
		// besides that it uses to extract the data from the DB to multiple Customer objects as a collection
		Collection<Customer> allCustomers = new ArrayList<Customer>();
		try {
			do {
				Customer customer = extractCustomerFromResultSet(resultSet);
				allCustomers.add(customer);
			}while(resultSet.next());
			return allCustomers;

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, extractAllCustomersFromResultSet(ResultSet resultSet);");
		}

	}
	
	// method which uses to get a specific customer id (based on its name and password) from the DB
	public long getCustomerIdByCustomerNameAndPassword(String customerName, String password) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		long customerId;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT Customer_Id FROM customer WHERE Customer_Name = ? AND Password = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, customerName);
			preparedStatement.setString(2, password);

			// DB respond + information on customers
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return -1;
			}
			customerId = resultSet.getLong("Customer_Id");
			return customerId;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, getCustomerIdByCustomerNameAndPassword(String customerName, String password);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}
	
	// method which uses to get a specific customer name by its customer id from the DB
	public String getCustomerNameByCustomerId(long customerId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String customerName;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT Customer_Name FROM customer WHERE Customer_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, customerId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new SQLException();
			}
			customerName = resultSet.getString("Customer_Name");
			return customerName;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, getCustomerNameByCustomerId(long customerId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}

	// this method checks if the name of the customer which it gets as input exists
	public boolean isCustomerExistByName(String customerName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM customer WHERE Customer_Name = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, customerName);

			// DB respond + information on customers
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, isCustomerExistByName(String customerName);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// this method checks if the customer id which it gets as input exists
	public boolean isCustomerExistById(long customerId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM customer WHERE Customer_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, customerId);

			// DB respond + information on customers
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CustomerDao, isCustomerExistById(long customerId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

}