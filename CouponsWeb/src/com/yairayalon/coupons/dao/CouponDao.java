package com.yairayalon.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.yairayalon.coupons.beans.Coupon;
import com.yairayalon.coupons.beans.PurchasedCoupon;
import com.yairayalon.coupons.enums.CouponType;
import com.yairayalon.coupons.utils.JdbcUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class CouponDao implements ICouponDao {

	// method which uses to create a new coupon object and insert it into the DB
	@Override
	public long createCoupon(Coupon coupon) throws ApplicationException {

		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet generatedKeys = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "INSERT INTO coupon (Company_Id, Coupon_Name, Start_Date, End_Date, Amount, Coupon_Type, Description, Price, Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setLong(1, coupon.getCompanyId());
			preparedStatement.setString(2, coupon.getCouponName());
			preparedStatement.setString(3, coupon.getStartDate());
			preparedStatement.setString(4, coupon.getEndDate());
			preparedStatement.setInt(5, coupon.getAmount());
			preparedStatement.setString(6, coupon.getCouponType().name());
			preparedStatement.setString(7, coupon.getDescription());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImage());

			// executing the update
			preparedStatement.executeUpdate();

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (!generatedKeys.next()) {
				throw new SQLException();
			}
			long couponId = generatedKeys.getLong(1);
			return couponId;

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, createCoupon(Coupon coupon);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement, generatedKeys);
		}
	}

	// this method removes a coupon and impacts 2 possible tables:
	// coupon (the coupon is being removed)
	// customer_coupon (if a customer has purchased earlier a coupon which is now being removed so it's also being deleted from his account)
	// (the DELETE CASCADE on MySQL keeps this synchronization of removal)
	@Override
	public void removeCoupon(long couponId) throws ApplicationException {

		// turn on connections
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "DELETE FROM coupon WHERE Coupon_Id = ?";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setLong(1, couponId);

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, removeCoupon(long couponId);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// this method updates the coupon's changeable details. the only
	// attributes that can be updated are the coupon end date and the coupon price.
	@Override
	public void updateCoupon(Coupon coupon) throws ApplicationException {

		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "UPDATE coupon SET End_Date = ?, Price = ? WHERE Coupon_Id = ?";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// asking to Replace the question marks in the statement above with the following data
			preparedStatement.setString(1, coupon.getEndDate());
			preparedStatement.setDouble(2, coupon.getPrice());
			preparedStatement.setLong(3, coupon.getCouponId());

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, updateCoupon(Coupon coupon);");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// method  which uses to get a specific coupon from the DB
	@Override
	public Coupon getCouponByCouponId(long couponId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Coupon couponByCouponId;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return null;
			}
			couponByCouponId = extractCouponFromResultSet(resultSet);
			return couponByCouponId;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getCouponByCouponId(long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all coupons from the DB
	@Override
	public Collection<Coupon> getAllCoupons() throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allCoupons;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allCoupons = extractCouponsFromResultSet(resultSet);
			return allCoupons;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllCoupons();");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all company's coupons (a specific company)
	// by the company id from the DB
	@Override
	public Collection<Coupon> getAllCompanyCouponsByCompanyId(long companyId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allCompanyCouponsByCompanyId;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Company_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, companyId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allCompanyCouponsByCompanyId = extractCouponsFromResultSet(resultSet);
			return allCompanyCouponsByCompanyId;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllCompanyCouponsByCompanyId(long companyId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all company's coupons (a specific company)
	// by the company id and by the type of the coupon from the DB
	@Override
	public Collection<Coupon> getAllCompanyCouponsByType(long companyId, CouponType couponType) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allCompanyCouponsByType;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Company_Id = ? AND Coupon_Type = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setDouble(1, companyId);
			preparedStatement.setString(2, couponType.name());

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allCompanyCouponsByType = extractCouponsFromResultSet(resultSet);
			return allCompanyCouponsByType;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllCompanyCouponsByType(long companyId, CouponType couponType);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all company's coupons (a specific company)
	// by the company id and by the limited price of the coupon from the DB
	@Override
	public Collection<Coupon> getAllCompanyCouponsByLimitedPrice(long companyId, double price) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allCompanyCouponsByLimitedPrice;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Company_Id = ? AND Price <= ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setDouble(1, companyId);
			preparedStatement.setDouble(2, price);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allCompanyCouponsByLimitedPrice = extractCouponsFromResultSet(resultSet);
			return allCompanyCouponsByLimitedPrice;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllCompanyCouponsByLimitedPrice(long companyId, double price);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all company's coupons (a specific company)
	// by the company id and by the limited end date of the coupon from the DB
	@Override
	public Collection<Coupon> getAllCompanyCouponsByLimitedEndDate(long companyId, String endDate) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allCompanyCouponsByLimitedEndDate;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Company_Id = ? AND End_Date <= ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setDouble(1, companyId);
			preparedStatement.setString(2, endDate);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allCompanyCouponsByLimitedEndDate = extractCouponsFromResultSet(resultSet);
			return allCompanyCouponsByLimitedEndDate;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllCompanyCouponsByLimitedEndDate(long companyId, String endDate);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses a customer to purchase a specific coupon
	// by the customer id from the DB
	@Override
	public void purchaseCoupon(PurchasedCoupon purchasedCoupon) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "INSERT INTO customer_coupon (Customer_Id, Coupon_Id, Quantity) VALUES (?, ?, ?)";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, purchasedCoupon.getCustomerId());
			preparedStatement.setLong(2, purchasedCoupon.getCouponId());
			preparedStatement.setInt(3, purchasedCoupon.getQuantity());

			// DB respond + information on companies
			preparedStatement.executeUpdate();

			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, purchaseCoupon(PurchasedCoupon purchasedCoupon);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	// method which uses a customer to purchase a specific coupon
	// by the customer id from the DB assuming he had already purchased it before
	public void updateCustomerCouponQuantity(PurchasedCoupon purchasedCoupon) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "UPDATE customer_coupon SET Quantity = Quantity + ? WHERE Customer_Id = ? AND Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setInt(1, purchasedCoupon.getQuantity());
			preparedStatement.setLong(2, purchasedCoupon.getCustomerId());
			preparedStatement.setLong(3, purchasedCoupon.getCouponId());

			// DB respond + information on companies
			preparedStatement.executeUpdate();

			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, updateCustomerCouponQuantity(PurchasedCoupon purchasedCoupon);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	// method which uses to update a specific coupon amount
	// in stock after it has been purchased by a customer from the DB
	public void updateCouponAmountInStock(long couponId, int quantity) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "UPDATE coupon SET Amount = Amount - ? WHERE Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setInt(1, quantity);
			preparedStatement.setLong(2, couponId);

			// DB respond + information on companies
			preparedStatement.executeUpdate();

			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, updateCouponAmountInStock(long couponId, int quantity);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// method which uses to get a collection of all customer's purchased coupons (a specific customer)
	// by the customer id from the DB
	@Override
	public Collection<Coupon> getAllPurchasedCouponsByCustomerId(long customerId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allPurchasedCouponsByCustomerId;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon JOIN customer_coupon ON coupon.Coupon_Id = customer_coupon.Coupon_Id WHERE customer_coupon.Customer_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, customerId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allPurchasedCouponsByCustomerId = extractPurchasedCouponsFromResultSet(resultSet);
			return allPurchasedCouponsByCustomerId;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllPurchasedCouponsByCustomerId(long customerId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all customer's purchased coupons (a specific customer)
	// by the customer id and by the coupon type from the DB
	@Override
	public Collection<Coupon> getAllPurchasedCouponsByType(long customerId, CouponType couponType) throws ApplicationException{

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allPurchasedCouponsByType;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon JOIN customer_coupon ON coupon.Coupon_Id = customer_coupon.Coupon_Id WHERE customer_coupon.Customer_Id = ? AND coupon.Coupon_Type = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, customerId);
			preparedStatement.setString(2, couponType.name());

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allPurchasedCouponsByType = extractPurchasedCouponsFromResultSet(resultSet);
			return allPurchasedCouponsByType;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllPurchasedCouponsByType(long customerId, CouponType couponType);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// method which uses to get a collection of all customer's purchased coupons (a specific customer)
	// by the customer id and by the coupon limited price from the DB
	@Override
	public Collection<Coupon> getAllPurchasedCouponsByLimitedPrice(long customerId, double price) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Coupon> allPurchasedCouponsByLimitedPrice;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon JOIN customer_coupon ON coupon.Coupon_Id = customer_coupon.Coupon_Id WHERE customer_coupon.Customer_Id = ? AND coupon.Price <= ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, customerId);
			preparedStatement.setDouble(2, price);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new ArrayList<Coupon>();
			}
			allPurchasedCouponsByLimitedPrice = extractPurchasedCouponsFromResultSet(resultSet);
			return allPurchasedCouponsByLimitedPrice;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getAllPurchasedCouponsByLimitedPrice(long customerId, double price);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// this method removes all expired coupons and impacts 2 possible tables:
	// coupon (the expired coupons are being removed)
	// customer_coupon (if a customer has purchased earlier any coupons that are now expired so they are also being deleted from his account)
	// (the DELETE CASCADE on MySQL keeps this synchronization of removal)
	public void removeExpiredCoupons() throws ApplicationException {

		// turn on connections
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			// getting a connection from the connection manager
			connection = JdbcUtils.getConnection();

			// creating the SQL query
			String sql = "DELETE FROM coupon WHERE CURRENT_DATE() > End_Date";

			// user id is primary key and auto incremented
			preparedStatement = connection.prepareStatement(sql);

			// executing the update
			preparedStatement.executeUpdate();

			// if there was an exception in the "try" block above, it is caught here and notifies a level above.
		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, removeExpiredCoupons();");
		} finally {
			// closing the resources
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// this method extracts the data from the DB to the Coupon object we'll create,
	// in order to store the coupon data which is requested in the get methods in it
	private Coupon extractCouponFromResultSet(ResultSet resultSet) throws ApplicationException {

		// a new coupon object is being created in order to store the resultset's
		// data in it
		Coupon coupon = new Coupon();

		// resultSet will search for this information
		try {
			coupon.setCouponId(resultSet.getLong("Coupon_Id"));
			coupon.setCompanyId(resultSet.getLong("Company_Id"));
			coupon.setCouponName(resultSet.getString("Coupon_Name"));
			coupon.setStartDate(resultSet.getString("Start_Date"));
			coupon.setEndDate(resultSet.getString("End_Date"));
			coupon.setAmount(resultSet.getInt("Amount"));
			coupon.setCouponType(CouponType.valueOf(resultSet.getString("Coupon_Type")));
			coupon.setDescription(resultSet.getString("Description"));
			coupon.setPrice(resultSet.getDouble("Price"));
			coupon.setImage(resultSet.getString("Image"));

			return coupon;

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, extractCouponFromResultSet(ResultSet resultSet);");
		}

	}
	
	// this method extracts the data from the DB to the Coupon object (which the customer has purchased)
	// we'll create, in order to store the coupon data which is requested in the get methods in it
	private Coupon extractPurchasedCouponFromResultSet(ResultSet resultSet) throws ApplicationException {

		// a new coupon object is being created in order to store the resultset's
		// data in it
		Coupon coupon = new Coupon();

		// resultSet will search for this information
		try {
			coupon.setCouponId(resultSet.getLong("Coupon_Id"));
			coupon.setCompanyId(resultSet.getLong("Company_Id"));
			coupon.setCouponName(resultSet.getString("Coupon_Name"));
			coupon.setStartDate(resultSet.getString("Start_Date"));
			coupon.setEndDate(resultSet.getString("End_Date"));
			coupon.setAmount(resultSet.getInt("Amount"));
			coupon.setCouponType(CouponType.valueOf(resultSet.getString("Coupon_Type")));
			coupon.setDescription(resultSet.getString("Description"));
			coupon.setPrice(resultSet.getDouble("Price"));
			coupon.setImage(resultSet.getString("Image"));
			coupon.setQuantity(resultSet.getInt("Quantity"));

			return coupon;

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, extractPurchasedCouponFromResultSet(ResultSet resultSet);");
		}

	}

	// this method functions like the extractCouponFromResultSet(ResultSet resultSet) method (they are related),
	// besides that it uses to extract the data from the DB to multiple Coupon objects as a collection
	private Collection<Coupon> extractCouponsFromResultSet(ResultSet resultSet) throws ApplicationException {

		// a new Collection of Coupon objects are being created in order to store the resultset's data in it
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		try {
			do {
				Coupon coupon = extractCouponFromResultSet(resultSet);
				coupons.add(coupon);
			}while(resultSet.next());
			return coupons;

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, extractCouponsFromResultSet(ResultSet resultSet);");
		}

	}
	
	// this method functions like the extractPurchasedCouponFromResultSet(ResultSet resultSet) method (they are related),
	// besides that it uses to extract the data from the DB to multiple Coupon objects as a collection
	private Collection<Coupon> extractPurchasedCouponsFromResultSet(ResultSet resultSet) throws ApplicationException {

		// a new Collection of Coupon objects are being created in order to store the resultset's data in it
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		try {
			do {
				Coupon coupon = extractPurchasedCouponFromResultSet(resultSet);
				coupons.add(coupon);
			}while(resultSet.next());
			return coupons;

		} catch (Exception e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, extractPurchasedCouponsFromResultSet(ResultSet resultSet);");
		}

	}
	
	// method which uses to get a specific coupon name by its coupon id from the DB
	public String getCouponNameByCouponId(long couponId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String couponName;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT Coupon_Name FROM coupon WHERE Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new SQLException();
			}
			couponName = resultSet.getString("Coupon_Name");
			return couponName;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getCouponNameByCouponId(long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}
	
	// method which uses to get a specific coupon price by its coupon id from the DB
	public double getPriceByCouponId(long couponId) throws ApplicationException {

		// turn on connections
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		double price;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT Price FROM coupon WHERE Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new SQLException();
			}
			price = resultSet.getDouble("Price");
			return price;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, getPriceByCouponId(long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

	}

	// this method checks if the name of the coupon which it gets as input exists
	public boolean isCouponExistByName(String couponName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Coupon_Name = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, couponName);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, isCouponExistByName(String couponName);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// this method checks if the coupon id which it gets as input exists
	public boolean isCouponExistById(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, isCouponExistById(long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// this method checks if the coupon type which it gets as input exists
	public boolean isCouponExistByType(CouponType couponType) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Coupon_Type = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setString(1, couponType.name());

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, isCouponExistByType(CouponType couponType);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	// this method checks if the amount parameter which it gets as input is negative
	public boolean isAmountNegative(int amount) {

		if (amount < 0) {
			return true;
		}
		return false;
	}

	// this method checks if the price parameter which it gets as input is negative
	public boolean isPriceNegative(double price) {

		if (price < 0) {
			return true;
		}
		return false;
	}
	
	// this method checks if the quantity parameter which it gets as input is positive
	public boolean isQuantityPositive(long quantity) {

		if (quantity > 0) {
			return true;
		}
		return false;
	}

	// this method checks if the amount of the coupon which it gets as input is in stock
	public boolean isCouponInStock(long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM coupon WHERE Coupon_Id = ? AND Amount > 0";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, isCouponInStock(long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	// this method checks if the coupon has already been purchased before by the same customer
	public boolean isCouponAlreadyPurchasedByCustomer(long customerId, long couponId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// try to connect to DB
			connection = JdbcUtils.getConnection();

			// sql syntax -->in this way we talk with our DB
			String sql = "SELECT * FROM customer_coupon WHERE Customer_Id = ? AND Coupon_Id = ?";

			// combining between syntax and our connection
			preparedStatement = connection.prepareStatement(sql);

			// we should have the same parameters that we have in the syntax
			preparedStatement.setLong(1, customerId);
			preparedStatement.setLong(2, couponId);

			// DB respond + information on companies
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return false;
			}
			return true;
			// if we have problems "catch" will tell us
		} catch (SQLException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in CouponDao, isCouponAlreadyPurchasedByCustomer(long customerId, long couponId);");
		}
		// time to turn off our connections
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

}