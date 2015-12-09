var wStationServices = angular.module('wStationApp', [ 'ngResource' ]);

wStationServices
		.factory(
				'SensorState',
				[
						'$resource',
						function($resource) {
							return $resource(
									'/sensorState/:sensorStateId',
									{
										sensorStateId: '@sensorStateId'
									},
									{
										// return $resource('/sensorState', {},
										// {
										query : {
											method : 'GET',
											params : {
												'sort' : 'createdDate,asc',
												'size' : '10',
												'page' : '1'
											}
										},
										remove : {
											method : 'DELETE',
										},
										searchBetween : {
											method : 'GET',
											url : '/sensorState/search/findByCreatedDateBetween',
											params : {
												'start' : '',
												'end' : ''
											}
										}
									});
						} ]);