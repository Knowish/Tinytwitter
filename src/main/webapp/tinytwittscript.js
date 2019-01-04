/**
 * 
 */

var app = angular.module('TinyTwitt', []);
app.controller('TController',['$scope','$window', function($scope,$window) {
    
    $scope.author = '';
    $scope.log = false;
    $scope.userlogin ='';
	$scope.usermail ='';
	$scope.userpw ='';
	$scope.userfirstname='';
    $scope.userlastname ='';
    

    $scope.inscription = function(){
		if($scope.log == false){
			gapi.client.TinyTwittAPI.createUser({
				login: $scope.userlogin,
				email: $scope.usermail,
				pw: $scope.usermdp,
				firstname: $scope.userprenom,
				lastname: $scope.usernom
			}).execute(function(resp){
				console.log(resp);
				$scope.author = $scope.userlogin;
				$scope.log = true;
				console.log($scope.author);
				console.log(" is connected");
			});
		}	
	}

    $window.init = function() {
	      console.log("windowinit called");
	      var rootApi = 'https://tiny-twitt.appspot.com/_ah/api/';
	      gapi.client.load('TinyTwittAPI', 'v1', function() {
	        console.log("twitt api loaded");
	        $scope.log = false;
	      }, rootApi);
	 }
}
]);
