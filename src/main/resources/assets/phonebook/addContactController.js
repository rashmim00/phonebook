angular.module("phoneApp").controller("AddContactController",["$http","$q", "$uibModalInstance", function($http, $q, $uibModalInstance){
	var ctrl = this;
	
	ctrl.contact = {
			id : -1,
			address : "",
			birthdate : "",
			company : "",
			email : "",
			favorite : 0,
			firstName : "",
			lastName : "",
			phones : [{"id":-1,"type":"home","number":""}],
			groups : []};
	
	ctrl.phoneTypes = [{id : "home",name : "Home"}, {id : "mobile", name : "Mobile"},{id : "work",name : "Work"}];
	
	ctrl.addGroups = [];
	
	ctrl.addPhone = function() {
		p = {
			id: -1,
			 type : "",
			 number : ""
			}
		ctrl.contact.phones.push(p);
	}
	
	ctrl.$onInit = onInit;
	
	function onInit() {
		$http.get("/api/phonebook/groups").then(function(response){
			ctrl.addGroups = response.data;
			console.log("in add grps" + JSON.stringify(ctrl.addGroups));
		}, function(response){});
	}
	
	ctrl.cancel = function() {
		$uibModalInstance.dismiss("cancel");
	}

	ctrl.save = function() {
		console.log("on add dialog " + JSON.stringify(ctrl.contact));
		$uibModalInstance.close(ctrl.contact);
	}

}]);