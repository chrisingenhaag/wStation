'use strict';

angular.module('wStationApp')
	.controller('SensorStateDeleteController', function($scope, $uibModalInstance, entity, SensorState) {

        $scope.sensorState = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SensorState.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
