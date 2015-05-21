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
