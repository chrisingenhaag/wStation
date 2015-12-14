'use strict';

angular.module('wStationApp')
    .controller('SensorStateController', function ($scope, $state, $modal, SensorState, ParseLinks) {
      
        $scope.sensorStates = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            SensorState.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
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
