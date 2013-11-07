var token = localStorage.getItem("token");

angular.module('auth', ['restangular','ui.bootstrap']).
  config(function($routeProvider, RestangularProvider) {
    $routeProvider.
      when('/', {
        controller: function($scope, $location, Restangular) {
//          $scope.isClean = function() {
//            return angular.equals(original, $scope['team']);
//          }

          $scope.destroy = function() {
            original.remove().then(function() {
              $location.path('/');
            });
          };

          $scope.save = function() {
            Restangular.all("auth/login?api_key=9c0ba686-e06c-4a2c-821b-bae2a235fd3d").post($scope.auth).then(function(response){
              console.log(response);
              localStorage.setItem("token", response.token);
              window.location = "/ws/teams.html";
            });
//            $scope.auth.post().then(function() {
//              $location.path('/teams');
//            });
          };
        }, 
        templateUrl:'login_form.html'
        }).
      otherwise({redirectTo:'/'});
      
      RestangularProvider.setDefaultHeaders({'Authorization': 'Bearer ' + token});
      //RestangularProvider.setBaseUrl('http://localhost:8080/v1');
      RestangularProvider.setBaseUrl('http://myezteam-webservices.herokuapp.com/v1');

//      RestangularProvider.setRestangularFields({
//        id: 'uuid'
//      });
      
      RestangularProvider.setRequestInterceptor(function(elem, operation, what) {
        return elem;
      });
      RestangularProvider.setResponseInterceptor(function(response, operation, what, url) {
        return response;
      });
      RestangularProvider.setErrorInterceptor(function(response) {
        if (response.status == 401) {
          if (confirm("You are not logged in, do you want to login?")) {
            window.location = '/login.html';
            return;
          }
        }
        else if  (response.status == 403) {
          alert('You are not authorized to access the requested data');
        }
        else if (response.status == 0) {
          alert('Did not get a response. Is the server running?');
        }
        else if (response.status >= 400) {
          alert('There was a server error');
        }
        return response;
      });
  });

