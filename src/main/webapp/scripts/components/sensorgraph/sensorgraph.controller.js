'use strict';

angular.module('wStationApp')
	.controller('GraphController', function($scope, SensorState){

		$scope.date = new Date();
		$scope.maxDate = new Date();
	  	$scope.format = 'dd.MM.yyyy';
		$scope.minmaxvalues = [0,1500];

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
			$scope.date.setDate($scope.date.getDate()-1);
			$scope.refresh();
		};

		$scope.goForward = function() {
			if($scope.canGoForward()) {
				$scope.date.setDate($scope.date.getDate()+1);
				$scope.refresh();
			}
		};
		$scope.canGoForward = function() {
			return $scope.date.getDate() < new Date().getDate();
		};


		$scope.refresh = function() {
		    SensorState.searchBetween({
		        'start': new Date($scope.date).format("yyyy-mm-dd")+'-00-00',
		        'end': new Date($scope.date).format("yyyy-mm-dd")+'-23-59'
		      }, function(data){
		        var d = data;
		        var data = [{key: 'Temperatur', values:[]},
		        			{key: 'Luftfeuchtigkeit', values:[]},
		        			{key: 'Helligkeit', values:[]},
		        			{key: 'Luftdruck', values:[]}];

		        data[0].values = d.map(function(d) {
		        	return [new Date(d.createddate).getTime(), d.temperature];
		        });
		        data[1].values = d.map(function(d) {
		        	return [new Date(d.createddate).getTime(), d.humidity];
		        });
		        data[2].values = d.map(function(d) {
		        	return [new Date(d.createddate).getTime(), d.illuminance];
		        });
		        data[3].values = d.map(function(d) {
		        	return [new Date(d.createddate).getTime(), d.airpressure];
		        });
		        $scope.d3Data = data;

		      });
		};

		$scope.xAxisTickFormatFunction = function(){
	                return function(d){
	                    return d3.time.format('%H:%M')(new Date(d));
	                };
	            };

		$scope.refresh();
	});
