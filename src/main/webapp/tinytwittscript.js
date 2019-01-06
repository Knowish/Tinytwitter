var app = angular.module('TinyTwitt', []);
app.controller('TController',['$scope','$window', function($scope,$window) {
    
    $scope.listtwitt = [{author: 'admin',message :'Bienvenu sur tiny twitt FDP'}];
    $scope.author = '';
    $scope.message;
    $scope.log = false;
    $scope.userlogin ='';
	$scope.usermail ='';
	$scope.userpw ='';
	$scope.userfirstname='';
    $scope.userlastname ='';
    $scope.erreurLog=false;
    

    $scope.inscription = function(){
        console.log("inscription passe? avant if v3");
		if($scope.log == false){
            console.log("inscription passe? apres if v3");
            console.log(gapi.client.tinytwittAPI);
                 gapi.client.tinytwittAPI.createUser({
				login: $scope.userlogin,
				email: $scope.usermail,
				pw: $scope.userpw,
				firstname: $scope.userfirstname,
				lastname: $scope.userlastname
			}).execute(function(resp){
				console.log(resp);
				$scope.author = $scope.userlogin;
				$scope.log = true;
				console.log($scope.author);
				console.log(" is connected");
			});
        }
    }

    $scope.timeline = function(nom){

        $scope.listtwitt = [];

        gapi.client.tinytwittAPI.getTimeline({
            login: nom
        }).execute(function(resp){
            console.log(resp);
            if(resp != null){
                if(resp.items.length > 0){

                    for(var i = 0; i < resp.items.length; i++){
                                $scope.listtwitt.push({author: resp.items[i].nameAuthor, message: resp.items[i].message, date : resp.items[i].date});
                            }

                }else{
                    $scope.listtwitt.push({author: "admin", message: "No tweet for this user", date : "Now"});
                    console.log("No tweet for this user");
                }
                
            }
          
            $scope.listtwitt.push({author: "admin", message: "No user found", date : "Now"});
            $scope.$apply();
        })
    }

    $scope.follow = function(nom){
        if($scope.log == true){
            gapi.client.tinytwittAPI.followUser({
                follower: $scope.author,
                followed: nom
            }).execute(function(resp){

                console.log(resp);
                $scope.$apply();
            })
        }
    }

    $scope.connection = function(nom){

        gapi.client.tinytwittAPI.getUser({
            login: nom
        }).execute(function(resp){

            if(resp.login == nom){
                $scope.login = nom;
                $scope.author = $scope.login;
                /*$scope.listTwitt();*/
                $scope.log = true;
                $scope.erreurLog=false;
                console.log($scope.author);
                console.log(" is connected");
            }else{
                console.log(" User dont exist");
            }
        })
        
        
    }
    
    $scope.deconnexion = function(){
		$scope.login = '';
		$scope.author = '';
		$scope.log = false;
        $scope.erreurLog=false;
		$scope.listtwitt = [{author: 'admin',message :'Bienvenu sur tiny twitt'}];
		console.log(" is deconnected");
	}



    $scope.postTwitt = function(){
        if($scope.log == true){
            gapi.client.tinytwittAPI.createTwitt({
                login: $scope.author,
                message: $scope.message
            }).execute(function(resp){
                console.log(resp);
                $scope.listtwitt.push({
						author: $scope.author,
						message: $scope.message
                    });
                    $scope.$apply();
            });
        }
    }
    

    $window.init = function() {
        	    console.log("windowinit called");
          var rootApi = 'https://tinytwitter.appspot.com/_ah/api/';
          console.log("connexion marche?");
	      gapi.client.load('tinytwittAPI', 'v1', function() {
	        console.log("twitter api loaded");
	        $scope.log = false;
	      }, rootApi);
	 }
}
]);
