'use strict';

angular.module('wStationApp')
  .controller('GraphController', function($scope, SensorState) {

    $scope.date = new Date();
    $scope.maxDate = new Date();
    $scope.format = 'dd.MM.yyyy';
    $scope.minmaxvalues = [0, 1500];
    $scope.showValues = {
      temperature: true,
      humidity: true,
      illuminance: true,
      airpressure: true
    };

    $scope.open = function($event) {
        $scope.status.opened = true;
      };

      $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
      };

      $scope.status = {
        opened: false
      };

    $scope.goBack = function() {
      $scope.date.setDate($scope.date.getDate() - 1);
      $scope.refresh();
    };

    $scope.goForward = function() {
      if ($scope.canGoForward()) {
        $scope.date.setDate($scope.date.getDate()+1);
        $scope.refresh();
      }
    };
    $scope.canGoForward = function() {
      return $scope.date.getDate() < new Date().getDate();
    };


    $scope.refresh = function() {
        SensorState.searchBetween({
            'start': new Date($scope.date).format('yyyy-mm-dd') + '-00-00',
            'end': new Date($scope.date).format('yyyy-mm-dd') + '-23-59'
          }, function(d) {
            var edata = [];

            if ($scope.showValues.temperature) {
              var temps = {key: 'Temperatur', values: []};
              temps.values = d.map(function(d) {
                return [new Date(d.createddate).getTime(), d.temperature];
              });
              edata.push(temps);
            }
            if ($scope.showValues.humidity) {
              var humidities = { key: 'Luftfeuchtigkeit', values: []};
              humidities.values = d.map(function(d) {
                return [new Date(d.createddate).getTime(), d.humidity];
              });
              edata.push(humidities);
            }
            if ($scope.showValues.illuminance) {
              var illuminances = { key: 'Helligkeit', values: []};
              illuminances.values = d.map(function(d) {
                return [new Date(d.createddate).getTime(), d.illuminance];
              });
              edata.push(illuminances);
            }
            if ($scope.showValues.airpressure) {
              var airpressures = {key: 'Luftdruck', values: []};
              airpressures.values = d.map(function(d) {
                return [new Date(d.createddate).getTime(), d.airpressure];
              });
              edata.push(airpressures);
            }
            $scope.d3Data = edata;
          });
    };

    $scope.xAxisTickFormatFunction = function() {
                  return function(d) {
                      return d3.time.format('%H:%M')(new Date(d));
                  };
              };

    $scope.refresh();
  });
