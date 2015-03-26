var wStationControllers = angular.module('wStation.controllers', []);

wStationControllers.controller('SensorStateListCtrl', [ '$scope',
		'SensorState', function($scope, SensorState) {

			$scope.orderProp = 'createdDate';
			$scope.orderBy = 'desc';
			$scope.page = 0;

			SensorState.searchBetween({

			});

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

		} ]);

wStationControllers.controller('SensorStateGraphCtrl', [ '$scope',
		'SensorState', function($scope, SensorState) {

			var startDate = new Date();
			startDate.setHours(0);
			startDate.setMinutes(0);
			startDate.setSeconds(0);
			startDate.setMilliseconds(0);
			var endDate = new Date();
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
			endDate.setMilliseconds(0);

			var dateFunc = function(date) {
				var r = "";
				r += date.getFullYear() + "-";
				r += (date.getMonth() + 1) + "-";
				r += date.getDate() + "-";
				r += date.getHours() + "-";
				r += date.getMinutes();

				return r;
			}

			$scope.sensorStates = SensorState.searchBetween({
				'start' : dateFunc(startDate),
				'end' : dateFunc(endDate)
			});

		} ])
