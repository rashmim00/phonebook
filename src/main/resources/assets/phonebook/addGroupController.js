angular.module("phoneApp").controller("AddGroupController",["$http","$q", "$uibModalInstance", function($http, $q, $uibModalInstance){
	var ctrl = this;
	
	ctrl.group = {
			id : -1,
			name : "",
			icon : "",
			participants : []};
	
	ctrl.allContacts = [];
		
	ctrl.$onInit = onInit;
	
	function onInit() {
		$http.get("/api/phonebook/contacts").then(function(response){
			ctrl.allContacts = response.data;			
		}, function(response){});
	}
	
	ctrl.cancel = function() {
		$uibModalInstance.dismiss("cancel");
	}

	ctrl.save = function() {
		console.log("on add dialog " + JSON.stringify(ctrl.group));
		$uibModalInstance.close(ctrl.group);
	}

}]);