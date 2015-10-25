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
	$scope.display = 'temperature';

	$scope.refresh = function() {
	    SensorState.searchBetween({
	        'start': $scope.date.format("yyyy-mm-dd")+'-00-00',
	        'end': $scope.date.format("yyyy-mm-dd")+'-23-59'
	      }, function(data){
	        $scope.d3Data = data._embedded.sensorState;  
	      });
	};

	$scope.refresh();

	$scope.displayValues = [{"value":"temperature", "description": "Temperatur"},
	                        {"value":"humidity", "description": "Luftfeuchtigkeit"},
	                        {"value":"illuminance", "description": "Helligkeit"},
	                        {"value":"airpressure", "description": "Druck"}];

    $scope.d3OnClick = function(item){
      alert(item.name);
    };
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

