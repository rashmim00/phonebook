/******* phoneApp *********/

angular.module("phoneApp",[]).controller("MainCtrl",[function(){
	var ctrl = this;

	ctrl.name = "Rashmi";
	ctrl.contacts = [];
	ctrl.groups = [];

	ctrl.isToday = function(otherDate) {
		var today = new Date();
		return (today.toDateString() == otherDate.toDateString());
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

		group = {id:1,name:"Grammy"};
		ctrl.groups.push(group);
	}

	ctrl.addContact = function() {console.log("add contact clicked");}
	ctrl.makeFavorite = function(contact) {console.log("make Favorite clicked");}
	ctrl.editContact = function(contact) {console.log("edit Contact clicked");}
	ctrl.addToGroup = function(contact) {console.log("add contact to group clicked");}
	ctrl.deleteContact = function(contact) {console.log("delete contact clicked");}

	ctrl.load();
	}]);