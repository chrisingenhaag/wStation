'use strict';

angular.module('wStationApp')
    .controller('SensorStateDetailController', function ($scope, $rootScope, $stateParams, entity, SensorState) {
        $scope.sensorState = entity;
        $scope.load = function (id) {
            SensorState.get({id: id}, function(result) {
                $scope.sensorState = result;
            });
        };
        var unsubscribe = $rootScope.$on('wStationApp:sensorStateUpdate', function(event, result) {
            $scope.sensorState = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
