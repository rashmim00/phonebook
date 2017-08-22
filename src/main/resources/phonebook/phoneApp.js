/******* phoneApp *********/

angular.module("phoneApp",['ui.bootstrap']).controller("MainCtrl",["$uibModal", function($uibModal){
	var ctrl = this;

	ctrl.name = "Rashmi";
	ctrl.contacts = [];
	ctrl.filterGrp = "All Contacts";
	ctrl.groups = [];

	ctrl.isToday = function(otherDate) {
		var today = new Date();
		return (today.toDateString() == otherDate.toDateString());
	}

	ctrl.addToGroup = function(contact) {
		console.log("add contact to group clicked");

		var modalInstance = $uibModal.open({
			animation : true,
			backdrop : "static",
			templateUrl : "addContactToGroup.html",
			controller : "GroupController",
			controllerAs : "ctrl",
			bindToController : true,
			resolve : {	}
		});
		modalInstance.result.then(function() {}, function() {});
	}

	ctrl.filterBy = function(id) {
		console.log("filter by " + id);
			//change filterGrp
			//change contacts list to the participants of this group id
		}

	ctrl.load = function(){
		console.log("loading");
		contact = {
					id:1,
					address:"12345 West Niles Dr, Los Angeles, CA",
					birthdate: new Date(),
					company: "",
					email:"alicia.keyes@gmail.com",
					favorite:1,
					firstName:"Alicia",
					groups: [1],
					lastName:"Keyes",
					phones : [{type:"home", number:"213-234-3456"}, {type:"Mobile", number:"213-245-6789"}]
			};
		ctrl.contacts.push(contact);

		contact1 = {
					id:2,
					address:"12345 West Niles Dr, Los Angeles, CA",
					birthdate: '',
					company: "Mixer Records Ltd",
					email:"alicia.keyes@gmail.com",
					favorite:0,
					firstName:"Alicia",
					lastName:"Keyes",
					phones : [{type:"home", number:"213-234-3456"}, {type:"Mobile", number:"213-245-6789"}]
			};
		ctrl.contacts.push(contact1);
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