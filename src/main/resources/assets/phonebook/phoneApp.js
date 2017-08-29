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
			$http.post("/api/phonebook/contacts/", addContact).then(function(response) {				
				ctrl.load();
			})
		}, function() {});
	}
	
	// add a new Group
	ctrl.addGroup = function(){
		console.log("add group clicked");
		var modalInstance = $uibModal.open({
			animation : true,
			backdrop : "static",
			templateUrl : "addGroup.html",
			controller : "AddGroupController",
			controllerAs : "ctrl",
			bindToController : true,
			size : "lg"		
			});
			modalInstance.result.then(function(addGroup) {
				$http.post("/api/phonebook/groups/", addGroup).then(function(response) {				
					ctrl.load();
				})
			}, function() {});
	}
	
	//add contact as group participant
	ctrl.addToGroup = function(contact, groupId) {
		console.log("add contact to grp clicked");
		$http.put("/api/phonebook/groups/add/"+groupId +"/"+contact.id).then(function(response) {				
			ctrl.load();
		}, function() {});
	}
	
	function filterAlreadyAdded(grp){
		var chk = true;
		if (ctrl.contact.groups != undefined) {
			ctrl.contact.groups.forEach(function(o) {
				if (grp.id == o) chk = false;
			});				
		}
		return chk;
	}
	
	ctrl.removeFromGroup = function(contact, groupId) {
		console.log("remove contact to grp clicked");
		$http.put("/api/phonebook/groups/remove/"+groupId +"/"+contact.id).then(function(response) {				
			ctrl.load();
		}, function() {});
	}
	
	
	//Show contacts of that group only or all contacts
	ctrl.filterBy = function(id) {
		console.log("filter by " + id);
		if (id == 0) {
			$http.get("/api/phonebook/contacts").then(function(response) {				
			ctrl.contacts = response.data;
			ctrl.filterGrp = "All Contacts";
			}, function() {});
		   return;
		}
		else {
		$http.get("/api/phonebook/groups/"+id).then(function(response) {				
			group = response.data;
			ctrl.filterGrp = group.name;
			ctrl.contacts = getGroupContacts(group.participants);
		}, function() {});
	  }
	}

	function getGroupContacts(participants){
		$http.get("/api/phonebook/contacts").then(function(response) {				
			all = response.data;
			ctrl.contacts = [];
			participants.forEach(function(p){
				all.forEach(function(a) {
					if (a.id == p) ctrl.contacts.push(a);
				})
			});
			
			}, function() {});		
		
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