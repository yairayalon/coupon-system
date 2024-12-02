(function() {

	var app = angular.module('myApp', ['ngRoute', 'ngSanitize']);
	app.config(function($routeProvider){

		// the routeProvider changes the html content of a page according to the url address trigger
		$routeProvider
		.when('/', {
			templateUrl : 'login/login.html'
		})
		.when('/register-home', {
			templateUrl : 'register/register-home.html'
		})
		.when('/register-companies', {
			templateUrl : 'register/register-company.html'
		})
		.when('/register-customers', {
			templateUrl : 'register/register-customer.html'
		})
		.when('/admin-home', {
			templateUrl : 'admin/admin-home.html'
		})
		.when('/admin-companies', {
			templateUrl : 'admin/admin-companies.html'
		})
		.when('/admin-customers', {
			templateUrl : 'admin/admin-customers.html'
		})
		.when('/admin-coupons', {
			templateUrl : 'admin/admin-coupons.html'
		})
		.when('/companies-home', {
			templateUrl : 'company/company-home.html'
		})
		.when('/companies-coupons', {
			templateUrl : 'company/company-coupons.html'
		})
		.when('/companies-details', {
			templateUrl : 'company/company-details.html'
		})
		.when('/customers-home', {
			templateUrl : 'customer/customer-home.html'
		})
		.when('/customers-purchase-coupons', {
			templateUrl : 'customer/customer-purchase-coupons.html'
		})
		.when('/customers-my-coupons', {
			templateUrl : 'customer/customer-my-coupons.html'
		})
		.when('/customers-details', {
			templateUrl : 'customer/customer-details.html'
		})
	})

	app.controller('LoginController', ['$http', '$scope', '$location', function($http, $scope, $location){

		// this function is used to check if the login details are correct, and it changes the url address in accordance
		// of the client type. it also sets the client id in the sessionStorage right after a successful login
		$scope.login = function (){
			var user = JSON.stringify($scope.user);
			$http.post('/CouponsWeb/rest/login', user)
			.then(function onSuccess(response){
				sessionStorage.setItem('clientId', response.data.clientId);
				if ($scope.user.clientTypeId==1){
					$location.path('/admin-home');
				} 
				else if($scope.user.clientTypeId==2){
					$location.path('/companies-home');
				}
				else{
					$location.path('/customers-home');
				}
			}, function onFailiure(){
				$scope.loginStatus = 'failure';
				$scope.errorMessage = 'Invalid username or password. Please try again.'
			})
		}
		
		// this function is used to remove the client id from sessionStorage (at the same time, the
		// client is also being routed to the login page using the html "href" attribute)
		$scope.logout = function (){
			$http.delete('/CouponsWeb/rest/login')
			.then(function onSuccess(response){
				sessionStorage.removeItem('clientId');
			}, function onFailiure(response){
				alert(response.data.errorMessage);
			})
		}

	}]);
	
	app.controller('RegisterController', ['$http', '$scope', '$location', 'ErrorService', function($http, $scope, $location, ErrorService){

		// this function is used to create a new company if the registration is successful
		$scope.registerCompany = function (){
			var company = JSON.stringify($scope.company);
			$http.post('/CouponsWeb/rest/register/company', company)
			.then(function onSuccess(response){
				$scope.registerCompanyStatus = 'success';
			}, function onFailiure(response){
				$scope.registerCompanyStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}
		
		// this function is used to create a new customer if the registration is successful
		$scope.registerCustomer = function (){
			var customer = JSON.stringify($scope.customer);
			$http.post('/CouponsWeb/rest/register/customer', customer)
			.then(function onSuccess(response){
				$scope.registerCustomerStatus = 'success';
			}, function onFailiure(response){
				$scope.registerCustomerStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

	}]);
	
	// the purpose of this service is to control the authorization
	// in order to prevent unwanted behavior from the client side
	app.service('AuthorizationService', ['$location', function($location){
		this.getClientId = function() {
			var clientId = sessionStorage.getItem('clientId');
			return clientId;
		}
		this.removeUnauthorized = function(clientId) {
			if (clientId == null) {
				$location.path('/');
				alert('Invalid cookie or unauthorized use with cookie');
			}
		}
	}]);
	
	// the purpose of this service is to supply the error message from within the response
	// being returned as a result of the http request that was sent to the server
	app.service('ErrorService', function(){
		this.getErrorMessage = function(response) {
			var errorMessage = response.data.errorMessage;
			return errorMessage;
		}
	});
	
	// the purpose of this filter is to filter the data from the http response only when the data
	// is being returned as an array, if it's not an array so the filter won't be activated
    app.filter('toArray', function () {
    	  return function (obj, addKey) {
    	    if (!angular.isObject(obj)) return obj;
    	    if ( addKey === false ) {
    	      return Object.keys(obj).map(function(key) {
    	        return obj[key];
    	      });
    	    } else {
    	      return Object.keys(obj).map(function (key) {
    	        var value = obj[key];
    	        return angular.isObject(value) ?
    	          Object.defineProperty(value, '$key', { enumerable: false, value: key}) :
    	          { $key: key, $value: value };
    	      });
    	    }
    	  };
    	});

	app.controller('CompanyController', ['$http', '$scope', '$location', 'AuthorizationService', 'ErrorService', function($http, $scope, $location, AuthorizationService, ErrorService){

		var clientId = AuthorizationService.getClientId();
		AuthorizationService.removeUnauthorized(clientId);
		
		// this function is used to prepare the company's create modal
		$scope.setCompanyDetailsForCreate = function(){
			$scope.createCompanyStatus = '';
			$scope.toCreateCompany = {};
		}
		
		// this function is used to prepare the company's remove modal
		$scope.setCompanyDetailsForRemove = function(companyName, companyId){
			$scope.removeCompanyStatus = '';
			$scope.toRemoveCompanyName = companyName;
			$scope.toRemoveCompany = {companyId:companyId};
		}
		
		// this function is used to prepare the company's update modal
		$scope.setCompanyDetailsForUpdate = function(companyName, companyId){
			$scope.updateCompanyStatus = '';
			$scope.toUpdateCompanyName = companyName;
			$scope.toUpdateCompany = {companyId:companyId};
		}

		// this function is used to create a company
		$scope.createCompany = function (){
			var jsonCreateCompany = JSON.stringify($scope.toCreateCompany);
			$http.post('/CouponsWeb/rest/companies', jsonCreateCompany)
			.then(function onSuccess(response){
				$scope.getAllCompanies();
				$scope.createCompanyStatus = 'success';
			}, function onFailiure(response){
				$scope.createCompanyStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to remove a company
		$scope.removeCompany = function(){
			$http.delete('/CouponsWeb/rest/companies/'+$scope.toRemoveCompany.companyId)
			.then(function onSuccess(){
				$scope.getAllCompanies();
				$scope.removeCompanyStatus = 'success';
			}, function onFailiure(response){
				$scope.removeCompanyStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to update a company
		$scope.updateCompany = function(){
			if (clientId != 0) {
				$scope.toUpdateCompany.companyId = clientId;
			}
			var jsonUpdateCompany = JSON.stringify($scope.toUpdateCompany);
			$http.put('/CouponsWeb/rest/companies', jsonUpdateCompany)
			.then(function onSuccess(){
				if ($location.path().indexOf('admin-') > -1) {
					$scope.getAllCompanies();
				}
				else if ($location.path().indexOf('companies-') > -1) {
					$scope.getCompany();
				}
				$scope.updateCompanyStatus = 'success';
			}, function onFailiure(response){
				$scope.updateCompanyStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}
		
		// this function is used to get a company
		$scope.getCompany = function(){
			if (clientId != 0) {
				$scope.companyIdToGet = clientId;
			}
			$http.get('/CouponsWeb/rest/companies/'+$scope.companyIdToGet)
			.then(function onSuccess(response){
				$scope.companyFound = true;
				if(!response.data){
					$scope.companyFound = false;
					$scope.companyNotFoundMsg = "<h2>Company not Found</h2>";
				}
				else {
					$scope.company = response.data;
				}
			}, function onFailiure(response){
				if(response.data.internalErrorCode == 675 || response.status == 401){
					$location.path('/');
				}
				if(response.status != 401){
					alert(response.data.errorMessage);
				}
			})
		}

		// this function is used to get all of the companies
		$scope.getAllCompanies = function(){
			$http.get('/CouponsWeb/rest/companies')
			.then(function onSuccess(response){
				$scope.allCompaniesFound = true;
				if(!response.data){
					$scope.allCompaniesFound = false;
					$scope.allCompaniesNotFoundMsg = "<h2>No Companies Found</h2>";
				}
				else if (!Array.isArray(response.data.company)){
					$scope.allCompanies = response.data;
				}
				else{
					$scope.allCompanies = response.data.company;
				}
			}, function onFailiure(response){
				if(response.data.internalErrorCode == 675 || response.status == 401){
					$location.path('/');
				}
				if(response.status != 401){
					alert(response.data.errorMessage);
				}
			})
		}

		// this function performs an additional layer of authorization
		// checking, and also gets some preliminary data
		$scope.initController = function (){
			if (clientId == 0) {
				if ($location.path().indexOf('admin-') == -1) {
					$location.path('/');
					alert('Invalid cookie or unauthorized use with cookie');
				}
				else {
					$scope.getAllCompanies();
				}
			}
			else if ($location.path().indexOf('admin-') > -1) {
				$location.path('/');
				alert('Invalid cookie or unauthorized use with cookie');
			}
			else {
				$scope.getCompany();
			}
		}();

	}]);

	app.controller('CustomerController', ['$http', '$scope', '$location', 'AuthorizationService', 'ErrorService', function($http, $scope, $location, AuthorizationService, ErrorService){
		
		var clientId = AuthorizationService.getClientId();
		AuthorizationService.removeUnauthorized(clientId);
		
		// this function is used to prepare the customer's create modal
		$scope.setCustomerDetailsForCreate = function(){
			$scope.createCustomerStatus = '';
			$scope.toCreateCustomer = {};
		}
		
		// this function is used to prepare the customer's remove modal
		$scope.setCustomerDetailsForRemove = function(customerName, customerId){
			$scope.removeCustomerStatus = '';
			$scope.toRemoveCustomerName = customerName;
			$scope.toRemoveCustomer = {customerId:customerId};
		}
		
		// this function is used to prepare the customer's update modal
		$scope.setCustomerDetailsForUpdate = function(customerName, customerId){
			$scope.updateCustomerStatus = '';
			$scope.toUpdateCustomerName = customerName;
			$scope.toUpdateCustomer = {customerId:customerId};
		}
		
		// this function is used to create a customer
		$scope.createCustomer = function (){
			var jsonCreateCustomer = JSON.stringify($scope.toCreateCustomer);
			$http.post('/CouponsWeb/rest/customers', jsonCreateCustomer)
			.then(function onSuccess(response){
				$scope.getAllCustomers();
				$scope.createCustomerStatus = 'success';
			}, function onFailiure(response){
				$scope.createCustomerStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to remove a customer
		$scope.removeCustomer = function(){
			$http.delete('/CouponsWeb/rest/customers/'+$scope.toRemoveCustomer.customerId)
			.then(function onSuccess(){
				$scope.getAllCustomers();
				$scope.removeCustomerStatus = 'success';
			}, function onFailiure(response){
				$scope.removeCustomerStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to update a customer
		$scope.updateCustomer = function(){
			if (clientId != 0) {
				$scope.toUpdateCustomer.customerId = clientId;
			}
			var jsonUpdateCustomer = JSON.stringify($scope.toUpdateCustomer);
			$http.put('/CouponsWeb/rest/customers', jsonUpdateCustomer)
			.then(function onSuccess(){
				if ($location.path().indexOf('admin-') > -1) {
					$scope.getAllCustomers();
				}
				else if ($location.path().indexOf('customers-') > -1) {
					$scope.getCustomer();
				}
				$scope.updateCustomerStatus = 'success';
			}, function onFailiure(response){
				$scope.updateCustomerStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to get a customer
		$scope.getCustomer = function(){
			if (clientId != 0) {
				$scope.customerIdToGet = clientId;
			}
			$http.get('/CouponsWeb/rest/customers/'+$scope.customerIdToGet)
			.then(function onSuccess(response){
				$scope.customerFound = true;
				if(!response.data){
					$scope.customerFound = false;
					$scope.customerNotFoundMsg = "<h2>Customer not Found</h2>";
				}
				else {
					$scope.customer = response.data;
				}
			}, function onFailiure(response){
				if(response.data.internalErrorCode == 675 || response.status == 401){
					$location.path('/');
				}
				if(response.status != 401){
					alert(response.data.errorMessage);
				}
			})
		}

		// this function is used to get all of the customers
		$scope.getAllCustomers = function(){
			$http.get('/CouponsWeb/rest/customers')
			.then(function onSuccess(response){
				$scope.allCustomersFound = true;
				if(!response.data){
					$scope.allCustomersFound = false;
					$scope.allCustomersNotFoundMsg = "<h2>No Customers Found</h2>";
				}
				else if (!Array.isArray(response.data.customer)){
					$scope.allCustomers = response.data;
				}
				else{
					$scope.allCustomers = response.data.customer;
				}
			}, function onFailiure(response){
				if(response.data.internalErrorCode == 675 || response.status == 401){
					$location.path('/');
				}
				if(response.status != 401){
					alert(response.data.errorMessage);
				}
			})
		}

		// this function performs an additional layer of authorization
		// checking, and also gets some preliminary data
		$scope.initController = function (){
			if (clientId == 0) {
				if ($location.path().indexOf('admin-') == -1) {
					$location.path('/');
					alert('Invalid cookie or unauthorized use with cookie');
				}
				else {
					$scope.getAllCustomers();
				}
			}
			else if ($location.path().indexOf('admin-') > -1) {
				$location.path('/');
				alert('Invalid cookie or unauthorized use with cookie');
			}
			else {
				$scope.getCustomer();
			}
		}();

	}]);

	app.controller('CouponController', ['$http', '$scope', '$location', 'AuthorizationService', 'ErrorService', function($http, $scope, $location, AuthorizationService, ErrorService){
		
		var clientId = AuthorizationService.getClientId();
		AuthorizationService.removeUnauthorized(clientId);
		
		$scope.couponTypes = ["RESTAURANTS", "ELECTRICITY", "FOOD", "HEALTH", "SPORTS", "CAMPING", "TRAVELLING", "LEISURE", "MOVIES"];
		
		// this function is used to prepare the coupon's create modal
		$scope.setCouponDetailsForCreate = function(){
			$scope.createCouponStatus = '';
			$scope.toCreateCoupon = {companyId:clientId};
		}
		
		// this function is used to prepare the coupon's remove modal
		$scope.setCouponDetailsForRemove = function(couponName, couponId){
			$scope.removeCouponStatus = '';
			$scope.toRemoveCouponName = couponName;
			$scope.toRemoveCoupon = {couponId:couponId};
		}
		
		// this function is used to prepare the coupon's update modal
		$scope.setCouponDetailsForUpdate = function(couponName, couponId){
			$scope.updateCouponStatus = '';
			$scope.toUpdateCouponName = couponName;
			$scope.toUpdateCoupon = {couponId:couponId};
		}
		
		// this function is used to prepare the coupon's purchase modal
		$scope.setCouponDetailsForPurchase = function(couponName, couponId, quantity, amount){
			$scope.purchaseCouponStatus = '';
			$scope.toPurchaseCouponName = couponName;
			$scope.toPurchaseCouponMaxQuantity = amount;
			$scope.toPurchaseCoupon = {customerId:clientId, couponId:couponId, quantity:quantity};
		}

		// this function is used to create a coupon
		$scope.createCoupon = function (){
			var isoStartDate = new Date($scope.toCreateCoupon.startDate + "UTC");
			$scope.toCreateCoupon.startDate = isoStartDate.toISOString().substring(0, 10).replace(/-/g, '/');
			var isoEndDate = new Date($scope.toCreateCoupon.endDate + "UTC");
			$scope.toCreateCoupon.endDate = isoEndDate.toISOString().substring(0, 10).replace(/-/g, '/');

			var jsonCreateCoupon = JSON.stringify($scope.toCreateCoupon);
			$http.post('/CouponsWeb/rest/coupons', jsonCreateCoupon)
			.then(function onSuccess(response){
				$scope.getAllCompanyCouponsByCompanyId();
				$scope.createCouponStatus = 'success';
			}, function onFailiure(response){
				$scope.createCouponStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to remove a coupon
		$scope.removeCoupon = function(){
			$http.delete('/CouponsWeb/rest/coupons/'+$scope.toRemoveCoupon.couponId)
			.then(function onSuccess(){
				if ($location.path().indexOf('admin-') > -1) {
					$scope.getAllCoupons();
				}
				else if ($location.path().indexOf('companies-') > -1) {
					$scope.getAllCompanyCouponsByCompanyId();
				}
				$scope.removeCouponStatus = 'success';
			}, function onFailiure(response){
				$scope.removeCouponStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to update a coupon
		$scope.updateCoupon = function(){
			var isoEndDate = new Date($scope.toUpdateCoupon.endDate + "UTC");
			$scope.toUpdateCoupon.endDate = isoEndDate.toISOString().substring(0, 10).replace(/-/g, '/');

			var jsonUpdateCoupon = JSON.stringify($scope.toUpdateCoupon);
			$http.put('/CouponsWeb/rest/coupons', jsonUpdateCoupon)
			.then(function onSuccess(){
				if ($location.path().indexOf('admin-') > -1) {
					$scope.getAllCoupons();
				}
				else if ($location.path().indexOf('companies-') > -1) {
					$scope.getAllCompanyCouponsByCompanyId();
				}
				$scope.updateCouponStatus = 'success';
			}, function onFailiure(response){
				$scope.updateCouponStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to get a coupon
		$scope.getCouponByCouponId = function(){
			$http.get('/CouponsWeb/rest/coupons/'+$scope.couponIdToGet)
			.then(function onSuccess(response){
				$scope.couponByCouponIdFound = true;
				if(!response.data){
					$scope.couponByCouponIdFound = false;
					$scope.couponByCouponIdNotFoundMsg = "<h2>Coupon not Found</h2>";
				}
				else {
					$scope.coupon = response.data;
				}
			}, function onFailiure(response){
				alert(response.data.errorMessage);
			})
		}

		// this function is used to get all of the coupons
		$scope.getAllCoupons = function(){
			$http.get('/CouponsWeb/rest/coupons')
			.then(function onSuccess(response){
				$scope.getAllCouponsData(response);
			}, function onFailiure(response){
				if(response.data.internalErrorCode == 675 || response.status == 401){
					$location.path('/');
				}
				if(response.status != 401){
					alert(response.data.errorMessage);
				}
			})
		}

		// this function is used to get all of the company coupons by the company id
		$scope.getAllCompanyCouponsByCompanyId = function(){
			$http.get('/CouponsWeb/rest/coupons/allCompanyCouponsByCompanyId?companyId='+clientId)
			.then(function onSuccess(response){
				$scope.getCompanyCouponsData(response);
			}, function onFailiure(response){
				if(response.data.internalErrorCode == 675 || response.status == 401){
					$location.path('/');
				}
				if(response.status != 401){
					alert(response.data.errorMessage);
				}
			})
		}

		// this function is used to get all of the company coupons by the coupon type
		$scope.getAllCompanyCouponsByType = function(){
			$http.get('/CouponsWeb/rest/coupons/allCompanyCouponsByType?companyId='+clientId+'&couponType='+$scope.couponTypeToGet)
			.then(function onSuccess(response){
				$scope.getCompanyCouponsData(response);
			}, function onFailiure(response){
				alert(response.data.errorMessage);
			})
		}

		// this function is used to get all of the company coupons by limited price
		$scope.getAllCompanyCouponsByLimitedPrice = function(){
			$http.get('/CouponsWeb/rest/coupons/allCompanyCouponsByLimitedPrice?companyId='+clientId+'&price='+$scope.couponsLimitedPriceToGet)
			.then(function onSuccess(response){
				$scope.getCompanyCouponsData(response);
			}, function onFailiure(response){
				alert(response.data.errorMessage);
			})
		}

		// this function is used to get all of the company coupons by limited end date
		$scope.getAllCompanyCouponsByLimitedEndDate = function(){
			var isoLimitedEndDate = new Date($scope.couponsLimitedEndDateToGet + "UTC");
			$scope.couponsLimitedEndDateToGet = isoLimitedEndDate.toISOString().substring(0, 10).replace(/-/g, '/');

			$http.get('/CouponsWeb/rest/coupons/allCompanyCouponsByLimitedEndDate?companyId='+clientId+'&endDate='+$scope.couponsLimitedEndDateToGet)
			.then(function onSuccess(response){
				$scope.getCompanyCouponsData(response);
			}, function onFailiure(response){
				alert(response.data.errorMessage);
			})
		}

		// this function is used to purchase a coupon
		$scope.purchaseCoupon = function(){
			var jsonPurchaseCoupon = JSON.stringify($scope.toPurchaseCoupon);
			$http.post('/CouponsWeb/rest/coupons/couponPurchase', jsonPurchaseCoupon)
			.then(function onSuccess(){
				$scope.getAllCoupons();
				$scope.purchaseCouponStatus = 'success';
			}, function onFailiure(response){
				$scope.purchaseCouponStatus = 'failure';
				$scope.errorMessage = ErrorService.getErrorMessage(response);
			})
		}

		// this function is used to get all of the customer coupons by the customer id
		$scope.getAllPurchasedCouponsByCustomerId = function(){
			$http.get('/CouponsWeb/rest/coupons/allPurchasedCouponsByCustomerId?customerId='+clientId)
			.then(function onSuccess(response){
				$scope.getCustomerCouponsData(response);
			}, function onFailiure(response){
				if(response.data.internalErrorCode == 675 || response.status == 401){
					$location.path('/');
				}
				if(response.status != 401){
					alert(response.data.errorMessage);
				}
			})
		}

		// this function is used to get all of the customer coupons by the coupon type
		$scope.getAllPurchasedCouponsByType = function(){
			$http.get('/CouponsWeb/rest/coupons/allPurchasedCouponsByType?customerId='+clientId+'&couponType='+$scope.couponTypeToGet)
			.then(function onSuccess(response){
				$scope.getCustomerCouponsData(response);
			}, function onFailiure(response){
				alert(response.data.errorMessage);
			})
		}

		// this function is used to get all of the customer coupons by limited price
		$scope.getAllPurchasedCouponsByLimitedPrice = function(){
			$http.get('/CouponsWeb/rest/coupons/allPurchasedCouponsByLimitedPrice?customerId='+clientId+'&price='+$scope.couponsLimitedPriceToGet)
			.then(function onSuccess(response){
				$scope.getCustomerCouponsData(response);
			}, function onFailiure(response){
				alert(response.data.errorMessage);
			})
		}
		
		$scope.getAllCouponsData = function(response){
			$scope.allCouponsFound = true;
			if(!response.data){
				$scope.allCouponsFound = false;
				$scope.allCouponsNotFoundMsg = "<h2>No Coupons Found</h2>";
			}
			else if (!Array.isArray(response.data.coupon)){
				$scope.allCoupons = response.data;
			}
			else{
				$scope.allCoupons = response.data.coupon;
			}
		}
		
		$scope.getCompanyCouponsData = function(response){
			$scope.couponsFound = true;
			if(!response.data){
				$scope.couponsFound = false;
				$scope.couponsNotFoundMsg = "<h2>No Owned Matching Coupons Found</h2>";
			}
			else if (!Array.isArray(response.data.coupon)){
				$scope.companyCoupons = response.data;
			}
			else {
				$scope.companyCoupons = response.data.coupon;
			}
		}
		
		$scope.getCustomerCouponsData = function(response){
			$scope.customerCouponsFound = true;
			if(!response.data){
				$scope.customerCouponsFound = false;
				$scope.customerCouponsNotFoundMsg = "<h2>No Owned Matching Coupons Found</h2>";
			}
			else if (!Array.isArray(response.data.coupon)){
				$scope.customerCoupons = response.data;
			}
			else {
				$scope.customerCoupons = response.data.coupon;
			}
		}
		
		// this function is used to clear the coupons' filters inputs fields
		// once another input is being selected (focused)
		$scope.clearFiltersInputs = function (){
			$scope.couponTypeToGet = null;
			$scope.couponsLimitedPriceToGet = null;
			$scope.couponsLimitedEndDateToGet = null;
		}

		// this function performs an additional layer of authorization
		// checking, and also gets some preliminary data
		$scope.initController = function (){
			if (clientId == 0) {
				if ($location.path().indexOf('admin-') == -1) {
					$location.path('/');
					alert('Invalid cookie or unauthorized use with cookie');
				}
				else {
					$scope.getAllCoupons();
				}
			}
			else if ($location.path().indexOf('admin-') > -1) {
				$location.path('/');
				alert('Invalid cookie or unauthorized use with cookie');
			}
			else if ($location.path().indexOf('companies-') > -1) {
				$scope.getAllCompanyCouponsByCompanyId();
			}
			else if ($location.path().indexOf('customers-') > -1) {
				$scope.getAllPurchasedCouponsByCustomerId();
				$scope.getAllCoupons();
			}
		}();

	}]);

})();