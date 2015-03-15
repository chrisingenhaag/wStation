var wStationControllers = angular.module('wStation.controllers', []);

wStationControllers.controller('SensorStateListCtrl', [ '$scope',
		'SensorState', function($scope, SensorState) {

			$scope.orderProp = 'createdDate';
			$scope.orderBy = 'desc';
			$scope.page = 1;

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
				if ($scope.page > 1) {
					$scope.page--;
					$scope.refresh();
				}
			};

		} ]);