'use strict';

angular.module('wStationApp')
    .factory('SensorState', function ($resource, DateUtils) {
        return $resource('api/sensorStates/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createddate = DateUtils.convertDateTimeFromServer(data.createddate);
                    return data;
                }
            },
            'update': { method:'PUT' },
			'searchBetween' : {
				method : 'GET',
				url : '/api/sensorStatesBetween',
				isArray: true,
				params : {
					'start' : '',
					'end' : ''
				}
			}
        });
    });
