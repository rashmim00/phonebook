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
	
	//open add contact modal dialog
	ctrl.addContact = function() {
		console.log("add contact clicked");
		var modalInstance = $uibModal.open({
		animation : true,
		backdrop : "static",
		templateUrl : "addContact.html",
		controller : "AddContactController",
		controllerAs : "ctrl",
		bindToController : true,
		size : "lg"		
		});
		modalInstance.result.then(function(addContact) {
			console.log("on add " + JSON.stringify(addContact));
			$http.post("/api/phonebook/contacts/", addContact).then(function(response) {				
				ctrl.load();
			})
		}, function() {});
	}
	
	//Add a contact to group. Allows you to select a group and add there
	ctrl.addToGroup = function(contact) {
		console.log("add contact to group clicked");

		var modalInstance = $uibModal.open({
			animation : true,
			backdrop : "static",
			templateUrl : "addContactToGroup.html",
			controller : "AddContactToGroupController",
			controllerAs : "ctrl",
			bindToController : true,
			size : "lg",
			resolve : {
				contact : function() {
					return contact;
					}
				}
			});
		modalInstance.result.then(function() {}, function() {});
	}

	//Show contacts of that group only or all contacts
	ctrl.filterBy = function(id) {
		console.log("filter by " + id);
			//change filterGrp
			//change contacts list to the participants of this group id
		}

	//Initialize and load all contacts and groups
	ctrl.load = function(){
		console.log("loading");
		
		var contactsPromise = $http.get("/api/phonebook/contacts");
		var groupsPromise = $http.get("/api/phonebook/groups");
	
		// when they are ALL done
		$q.all([ contactsPromise, groupsPromise]).then(function(results) {
			// check success
			var success = results.every(function(element, index, array) {
				return element.status == 200
			});

			if (success) {
				ctrl.contacts = results[0].data;
				ctrl.contacts.forEach(function(citem) {
					console.log("on load " + JSON.stringify(citem));
				});
				ctrl.groups = results[1].data;
				}
			}, function(error) {});	
		
		
		 $('[data-toggle="tooltip"]').tooltip();
	}
	
	//Quickly make a contact your favorite
	ctrl.makeFavorite = function(contact) {
		console.log("make Favorite clicked");
		contact.favorite = (contact.favorite != 0) ? 0 : 1;
		$http.put("/api/phonebook/contacts/" + contact.id, contact).then(function(response) {
		console.log("put response status" + response.status);	
		//refresh contact
		ctrl.contacts.forEach(function(c, index) {
			if (c.id == contact.id) {
				ctrl.contacts[index] = contact;
				return;
			}
		})	
			
		}, function(response){});
	}
	
	//edit a contact	
	ctrl.editContact = function(contact) {
		console.log("edit Contact clicked");
		$http.put("/api/phonebook/contacts/" + contact.id, contact).then(function(response) {
			console.log("put response status" + response.status);	
			//refresh contact
			ctrl.contacts.forEach(function(c, index) {
				if (c.id == contact.id) {
					ctrl.contacts[index] = contact;
					return;
				}
			})	
				
			}, function(response){});
	}

	//delete contact
	ctrl.deleteContact = function(contact) {
		console.log("delete contact clicked");
		$http.delete("/api/phonebook/contacts/" + contact.id).then(function(response) {
			// check for delete success
			var deleted = (response.status === 204);
			ctrl.load();
			
		}, function(response){});
	}


	ctrl.load();
	}]);