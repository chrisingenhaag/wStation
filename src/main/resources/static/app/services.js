var wStationServices = angular.module('wStation.services', [ 'ngResource' ]);

wStationServices
		.factory(
				'SensorState',
				[
						'$resource',
						function($resource) {
							return $resource(
									'http://192.168.2.11:8080/sensorState/:sensorStateId',
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
											url : 'http://192.168.2.11:8080/sensorState/search/findByCreatedDateBetween',
											params : {
												'start' : '',
												'end' : ''
											}
										}
									});
						} ]);

wStationServices.factory('d3Service', [ '$document', '$q', '$rootScope',
		function($document, $q, $rootScope) {
			var d = $q.defer();
			function onScriptLoad() {
				// Load client in the browser
				$rootScope.$apply(function() {
					d.resolve(window.d3);
				});
			}
			// Create a script tag with d3 as the source
			// and call our onScriptLoad callback when it
			// has been loaded
			var scriptTag = $document[0].createElement('script');
			scriptTag.type = 'text/javascript';
			scriptTag.async = true;
			scriptTag.src = '../bower_components/d3/d3.js';
			scriptTag.onreadystatechange = function() {
				if (this.readyState == 'complete')
					onScriptLoad();
			}
			scriptTag.onload = onScriptLoad;

			var s = $document[0].getElementsByTagName('body')[0];
			s.appendChild(scriptTag);

			return {
				d3 : function() {
					return d.promise;
				}
			};
		} ]);
