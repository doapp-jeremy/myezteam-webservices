var token = localStorage.getItem("token");

angular.module('team', ['restangular','ui.bootstrap']).
  config(function($routeProvider, RestangularProvider) {
    $routeProvider.
    when('/', {
      controller: function($scope, Restangular) {
        $scope['teams'] = Restangular.all('teams?api_key=9c0ba686-e06c-4a2c-821b-bae2a235fd3d').getList()
      }, 
      templateUrl:'list.html'
    }).
    otherwise({redirectTo:'/'});
      
      RestangularProvider.setDefaultHeaders({'Authorization': 'Bearer ' + token});
      //RestangularProvider.setBaseUrl('http://localhost:8080/v1');
      RestangularProvider.setBaseUrl('http://myezteam-webservices.herokuapp.com/v1');
      
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

