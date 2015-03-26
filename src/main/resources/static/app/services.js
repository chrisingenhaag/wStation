var wStationServices = angular.module('wStation.services', [ 'ngResource' ]);

wStationServices.factory('SensorState', [ '$resource', function($resource) {
	// return $resource('http://192.168.2.11:8080/sensorState', {},{
	return $resource('/sensorState', {}, {
		query : {
			method : 'GET',
			params : {
				'sort' : 'createdDate,asc',
				'size' : '10',
				'page' : '1'
			}
		},
		searchBetween : {
			method: 'GET',
			url: '/sensorState/search/findByCreatedDateBetween',
			params : {
				'start': '',
				'end': ''
			}
		}
	});
} ]);
