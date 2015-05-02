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
			}

		} ]);

wStationControllers.controller('GraphCtrl', ['$scope',
    'SensorState', function($scope, SensorState){

    var today = new Date();

    SensorState.searchBetween({
      'start': '2015-04-15-00-00',
      'end': '2015-04-15-23-59'
    }, function(data){
      $scope.d3Data = data._embedded.sensorState;  
    }); 

    

    $scope.title = "GraphCtrl";
//    $scope.d3Data = [
//      {name: "Greg", score:98},
//      {name: "Ari", score:96},
//      {name: "Loser", score: 48}
//    ];
    $scope.d3OnClick = function(item){
      alert(item.name);
    };
  }]);
