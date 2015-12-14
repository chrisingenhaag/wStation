'use strict';

angular.module('wStationApp')
	.controller('SensorStateDeleteController', function($scope, $modalInstance, entity, SensorState) {

        $scope.sensorState = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SensorState.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });