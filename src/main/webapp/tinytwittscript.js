var app = angular.module('TinyTwitt', []);
app.controller('TController',['$scope','$window', function($scope,$window) {
    
    $scope.listtwitt = [{author: 'admin',message :'Hello and Welcome to Tiny Tweet'}];
    $scope.author = '';
    $scope.login ='';
    $scope.message;
    $scope.log = false;
    $scope.userlogin ='';
	$scope.usermail ='';
	$scope.userpw ='';
	$scope.userfirstname='';
    $scope.userlastname ='';
    $scope.tnbfollowers = 0;
    $scope.listtemps = [];
    

    $scope.register = function(){
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
                if(resp.code != 503){
                    $scope.login = $scope.userlogin;
                    $scope.author = $scope.userlogin;
                    $scope.log = true;
                    console.log($scope.author);
                    console.log(" is connected");
                    $scope.$apply();
                }
			});
        }
    }

    $scope.timeline = function(nom){

        $scope.listtwitt = [];

        gapi.client.tinytwittAPI.getTimeline({
            login: nom
        }).execute(function(resp){
            console.log(resp);
            if(resp.code != 503){
                if(resp.items.length > 0){

                    for(var i = 0; i < resp.items.length; i++){
                                $scope.listtwitt.push({author: resp.items[i].nameAuthor, message: resp.items[i].message, date : resp.items[i].date});
                            }

                }else{
                    $scope.listtwitt.push({author: "admin", message: "No tweet for this user", date : "Now"});
                    console.log("No tweet for this user");
                }
                
            }else{
          
            $scope.listtwitt.push({author: "admin", message: "No user found", date : "Now"});
            }
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

    $scope.connexion = function(nom){

        gapi.client.tinytwittAPI.getUser({
            login: nom
        }).execute(function(resp){

            if(resp.login == nom){
                $scope.login = nom;
                $scope.author = $scope.login;
                $scope.log = true;
                console.log($scope.author);
                console.log(" is connected");
            }else{
                console.log(" User dont exist");
            }
            $scope.$apply();
        })
        
        
    }
    
    $scope.deconnexion = function(){
		$scope.login = '';
		$scope.author = '';
		$scope.log = false;
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
    
    $scope.twittnbfollowers = function(pseudon,nbfollowerstest){
    				var tmp = 0;
                    $scope.listtemps = [];
                     console.log("resp avant createNBfollowers");
                    gapi.client.tinytwittAPI.createNbFollowers({
                        nbFollowers: nbfollowerstest,
                        followed: pseudon
                    });

                        
                        
                       if(nbfollowerstest<=10000){
                        
                            for(var i=0;i<30;i++){
                                $scope.start = new Date().getTime();
                                gapi.client.tinytwittAPI.createTwitt({
                                    login: pseudon,
                                    message: 'test'
                                }).execute(function(respa){
                                    $scope.stop = new Date().getTime();
                                var res =  $scope.stop - $scope.start;
                                $scope.listtemps.push(res);
                                 console.log(res);
                                 tmp =  tmp + res;
                           		 $scope.tnbfollowers = tmp/i;
                                 $scope.$apply();
                                 
                                })
                                
                            }
                            
                        }else{

                            $scope.start = new Date().getTime();
                            
                                gapi.client.tinytwittAPI.createTwitt({
                                    login: pseudon,
                                    message: 'test'
                                }).execute(function(respz){
                                     $scope.stop = new Date().getTime();   
                            $scope.tnbfollowers = ($scope.stop - $scope.start);
                                      $scope.$apply();
                               })
                            
                           

                        }
                  
                        
    }

    $scope.twittgetlast10 = function(pseudal10,nbfollowerstest10) {
    	 console.log("resp avant createNBfollowers");
         gapi.client.tinytwittAPI.createNbFollowers({
             nbFollowers: nbfollowerstest10,
             followed: pseudal10
         });
         
         for (var i=0; i<15; i++) {
        	 gapi.client.tinytwittAPI.createTwitt({
                 login: pseudal10,
                 message: 'test'
             });
         }
         
         $scope.last10tweets = [];

         gapi.client.tinytwittAPI.getTimeline({
             login: pseudal10
         }).execute(function(resp){
             console.log(resp);
             if(resp.code != 503){
                 if(resp.items.length > 0){

                     for(var i = 0; i < 10; i++){
                                 $scope.last10tweets.push({author: resp.items[i].nameAuthor, message: resp.items[i].message, date : resp.items[i].date});
                             }

                 }else{
                     $scope.last10tweets.push({author: "admin", message: "No tweet for this user", date : "Now"});
                     console.log("No tweet for this user");
                 }
                 
             }else{
           
             $scope.last10tweets.push({author: "admin", message: "No user found", date : "Now"});
             }
             $scope.$apply();
         })
    }
    
    $scope.twittgetlast50 = function(pseudal50,nbfollowerstest50) {
   	 console.log("resp avant createNBfollowers");
        gapi.client.tinytwittAPI.createNbFollowers({
            nbFollowers: nbfollowerstest50,
            followed: pseudal50
        });
        
        for (var i=0; i<55; i++) {
       	 gapi.client.tinytwittAPI.createTwitt({
                login: pseudal50,
                message: 'test'
            });
        }
        
        $scope.last50tweets = [];

        gapi.client.tinytwittAPI.getTimeline({
            login: pseudal50
        }).execute(function(resp){
            console.log(resp);
            if(resp.code != 503){
                if(resp.items.length > 0){

                    for(var i = 0; i < 50; i++){
                                $scope.last50tweets.push({author: resp.items[i].nameAuthor, message: resp.items[i].message, date : resp.items[i].date});
                            }

                }else{
                    $scope.last50tweets.push({author: "admin", message: "No tweet for this user", date : "Now"});
                    console.log("No tweet for this user");
                }
                
            }else{
          
            $scope.last50tweets.push({author: "admin", message: "No user found", date : "Now"});
            }
            $scope.$apply();
        })
   }
    
    $scope.twittgetlast100 = function(pseudal100,nbfollowerstest100) {
   	 console.log("resp avant createNBfollowers");
        gapi.client.tinytwittAPI.createNbFollowers({
            nbFollowers: nbfollowerstest100,
            followed: pseudal100
        });
        
        for (var i=0; i<105; i++) {
       	 gapi.client.tinytwittAPI.createTwitt({
                login: pseudal100,
                message: 'test'
            });
        }
        
        $scope.last100tweets = [];

        gapi.client.tinytwittAPI.getTimeline({
            login: pseudal100
        }).execute(function(resp){
            console.log(resp);
            if(resp.code != 503){
                if(resp.items.length > 0){

                    for(var i = 0; i < 100; i++){
                                $scope.last100tweets.push({author: resp.items[i].nameAuthor, message: resp.items[i].message, date : resp.items[i].date});
                            }

                }else{
                    $scope.last100tweets.push({author: "admin", message: "No tweet for this user", date : "Now"});
                    console.log("No tweet for this user");
                }
                
            }else{
          
            $scope.last100tweets.push({author: "admin", message: "No user found", date : "Now"});
            }
            $scope.$apply();
        })
   }
  
   /* $scope.twittgetlastten = function(pseudal,nbfollowerstestten) {
    	gapi.client.tinytwittAPI.createNbFollowers({
            nbFollowers: nbfollowerstestten,
            followed: pseudal
        });
    	for (var i=0; i<15; i++) {
       	 gapi.client.tinytwittAPI.createTwitt({
                login: pseudal,
                message: 'test'
            });
        }
    	$scope.lasttentweets = [];
    	
    	gapi.client.tinytwittAPI.getLastTens({
    		login: pseudal
    	}).execute(function(resp){
            console.log(resp);
            if(resp.code != 503){
                if(resp.items.length > 0){

                    for(var i = 0; i < resp.items.length; i++){
                                $scope.lasttentweets.push({author: resp.items[i].nameAuthor, message: resp.items[i].message, date : resp.items[i].date});
                            }

                }else{
                    $scope.lasttentweets.push({author: "admin", message: "No tweet for this user", date : "Now"});
                    console.log("No tweet for this user");
                }
                
            }else{
          
            $scope.lasttentweets.push({author: "admin", message: "No user found", date : "Now"});
            }
            $scope.$apply();
        })
    }*/
    
    $window.init = function() {
        	    console.log("windowinit called");
          var rootApi = 'https://tinitouit.appspot.com/_ah/api/';
          console.log("connexion marche?");
	      gapi.client.load('tinytwittAPI', 'v1', function() {
	        console.log("twitter api loaded");
	        $scope.log = false;
	      }, rootApi);
	 }
}
]);
