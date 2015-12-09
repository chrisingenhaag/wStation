'use strict';

angular.module('wStationApp').controller('SensorStateDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SensorState',
        function($scope, $stateParams, $modalInstance, entity, SensorState) {

        $scope.sensorState = entity;
        $scope.load = function(id) {
            SensorState.get({id : id}, function(result) {
                $scope.sensorState = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('wStationApp:sensorStateUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sensorState.id != null) {
                SensorState.update($scope.sensorState, onSaveSuccess, onSaveError);
            } else {
                SensorState.save($scope.sensorState, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
