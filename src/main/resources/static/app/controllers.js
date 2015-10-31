var wStationControllers = angular.module('wStation.controllers', []);

wStationControllers.controller('SensorStateListCtrl', [ '$scope',
		'SensorState', function($scope, SensorState) {

			$scope.orderProp = 'createdDate';
			$scope.orderBy = 'desc';
			$scope.page = 0;

			$scope.sensorStates = SensorState.query({
				'sort' : $scope.orderProp + ',' + $scope.orderBy,
				'page' : $scope.page
			});
			
			$scope.toggleSortBy = function() {
				$scope.orderBy = ($scope.orderBy == "desc") ? "asc" : "desc";
			}

			$scope.refresh = function() {
				$scope.sensorStates = SensorState.query({
					'sort' : $scope.orderProp + ',' + $scope.orderBy,
					'page' : $scope.page
				});
			};

			$scope.firstPage = function() {
				$scope.page = 0;
				$scope.refresh();
			};
			
			$scope.lastPage = function() {
				$scope.page = $scope.sensorStates.page.totalPages -1;
				$scope.refresh();
			};
			
			$scope.nextPage = function() {
				$scope.page++;
				$scope.refresh();
			};

			$scope.previousPage = function() {
				if ($scope.page > 0) {
					$scope.page--;
					$scope.refresh();
				}
			};
			
			$scope.remove = function(id) {
				SensorState.remove({'sensorStateId': id});
				$scope.refresh();
			}

		} ]);

wStationControllers.controller('GraphCtrl', ['$scope',
    'SensorState', function($scope, SensorState){

	$scope.date = new Date();
	$scope.maxDate = new Date();
  	$scope.format = 'dd.MM.yyyy';

	$scope.open = function($event) {
    	$scope.status.opened = true;
  	};

  	$scope.dateOptions = {
    	formatYear: 'yy',
    	startingDay: 1
  	};

  	$scope.status = {
    	opened: false
  	};

	$scope.refresh = function() {
	    SensorState.searchBetween({
	        'start': new Date($scope.date).format("yyyy-mm-dd")+'-00-00',
	        'end': new Date($scope.date).format("yyyy-mm-dd")+'-23-59'
	      }, function(data){
	        var d = data._embedded.sensorState;
	        var data = [{key: 'Temperatur', values:[]},
	        			{key: 'Luftfeuchtigkeit', values:[]},
	        			{key: 'Helligkeit', values:[]},
	        			{key: 'Luftdruck', values:[]}];
	        			
	        data[0].values = d.map(function(d) {
	        	return [new Date(d.createdDate).getTime(), d.temperature];
	        });
	        data[1].values = d.map(function(d) {
	        	return [new Date(d.createdDate).getTime(), d.humidity];
	        });
	        data[2].values = d.map(function(d) {
	        	return [new Date(d.createdDate).getTime(), d.illuminance];
	        });
	        data[3].values = d.map(function(d) {
	        	return [new Date(d.createdDate).getTime(), d.airpressure];
	        });
	        $scope.d3Data = data;
	      });
	};

	$scope.xAxisTickFormatFunction = function(){
                return function(d){
                    return d3.time.format('%H:%M')(new Date(d));
                }
            }

	$scope.refresh();    
  }]);

wStationControllers.controller('HomeCtrl', ['$scope', function($scope) {

}]);


wStationControllers.controller('NavigationCtrl', function($rootScope, $scope, $http, $location) {

  var authenticate = function(credentials, callback) {

    var headers = credentials ? {authorization : "Basic "
        + btoa(credentials.username + ":" + credentials.password)
    } : {};

    $http.get('user', {headers : headers}).success(function(data) {
      if (data.name) {
        $rootScope.authenticated = true;
      } else {
        $rootScope.authenticated = false;
      }
      callback && callback();
    }).error(function() {
      $rootScope.authenticated = false;
      callback && callback();
    });

  }

  authenticate();
  $scope.credentials = {};
  $scope.login = function() {
      authenticate($scope.credentials, function() {
        if ($rootScope.authenticated) {
          $location.path("/");
          $scope.error = false;
        } else {
          $location.path("/login");
          $scope.error = true;
        }
      });
  };

  $scope.logout = function() {
    $http.post('/logout', {}).success(function() {
      $rootScope.authenticated = false;
      $location.path("/");
    }).error(function(data) {
      $rootScope.authenticated = false;
    });
  }
  
});

