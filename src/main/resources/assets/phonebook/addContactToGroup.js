angular.module("phoneApp").controller("AddContactToGroupController",["$http","$q", "$uibModalInstance", function($http, $q, $uibModalInstance){
	var ctrl = this;
	
	ctrl.contact = {};
	ctrl.$onInit = onInit;
	
	function onInit() {
	}
	
	ctrl.cancel = function() {
		$uibModalInstance.dismiss("cancel");
	}

	ctrl.save = function() {
		$uibModalInstance.close(ctrl.contact);
	}

}]);