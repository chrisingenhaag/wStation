'use strict';

angular.module('wStationApp')
    .controller('SensorStateController', function ($scope, $state, SensorState, ParseLinks) {

        $scope.sensorStates = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            SensorState.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.sensorStates = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.sensorState = {
                temperature: null,
                airpressure: null,
                humidity: null,
                illuminance: null,
                createddate: null,
                id: null
            };
        };
    });
