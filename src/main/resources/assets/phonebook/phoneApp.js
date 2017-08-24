/******* phoneApp *********/

var app = angular.module("phoneApp",['ui.bootstrap','ngRoute']);
//app.config(function ($routeProvider) {
//    $routeProvider.when('/contacts', {
//        templateUrl: 'phonebook/index.html',
//        controller: 'MainCtrl'
//    }).otherwise({
//        redirectTo: '/contacts'
//    })
//});
app.controller("MainCtrl",["$http","$q","$uibModal", function($http, $q, $uibModal){
	var ctrl = this;

	ctrl.name = "Rashmi";
	ctrl.contacts = [];
	ctrl.filterGrp = "All Contacts";
	ctrl.groups = [];

	ctrl.isToday = function(otherDate) {
		if (otherDate == undefined || otherDate == null) return false;
		var today = new Date();
		return (today.toDateString() == (new Date(otherDate)).toDateString());
	}

	ctrl.addToGroup = function(contact) {
		console.log("add contact to group clicked");

//		var modalInstance = $uibModal.open({
//			animation : true,
//			backdrop : "static",
//			templateUrl : "addContactToGroup.html",
//			controller : "GroupController",
//			controllerAs : "ctrl",
//			bindToController : true,
//			resolve : {	}
//		});
//		modalInstance.result.then(function() {}, function() {});
	}

	ctrl.filterBy = function(id) {
		console.log("filter by " + id);
			//change filterGrp
			//change contacts list to the participants of this group id
		}

	ctrl.load = function(){
		console.log("loading");
		
		var contactsPromise = $http.get("/api/phonebook/contacts");
	
		// when they are ALL done
		$q.all([ contactsPromise]).then(function(results) {
			// check success
			var success = results.every(function(element, index, array) {
				return element.status == 200
			});

			if (success) {
				ctrl.contacts = results[0].data;
				ctrl.contacts.forEach(function(citem) {
					console.log("on load " + citem.firstName + citem.birthdate);
				});
				}
			}, function(error) {});
		group = {id:1,name:"Grammy Lovers", icon:"", participants : [1]};	
		
		ctrl.groups.push(group);
		 $('[data-toggle="tooltip"]').tooltip();
	}

	ctrl.addContact = function() {console.log("add contact clicked");}
	ctrl.makeFavorite = function(contact) {console.log("make Favorite clicked");}
	ctrl.editContact = function(contact) {console.log("edit Contact clicked");}

	ctrl.deleteContact = function(contact) {console.log("delete contact clicked");}


	ctrl.load();
	}]);