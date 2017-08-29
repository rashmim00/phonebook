angular.module("phoneApp").controller("AddContactController",["$http","$q", "$uibModalInstance", function($http, $q, $uibModalInstance){
	var ctrl = this;
	
	ctrl.contact = {};
	
	ctrl.cancel = function() {
		$uibModalInstance.dismiss("cancel");
	}

	ctrl.save = function() {
		console.log("on add dialog " + JSON.stringify(ctrl.contact));
		$uibModalInstance.close(ctrl.contact);
	}

}]);