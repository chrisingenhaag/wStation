var wStationApp = angular.module("wStation", [ 'ngRoute', 'wStation.services',
		'wStation.controllers','wStation.directives','ui.date','nvd3ChartDirectives' ]);

wStationApp.config([ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
	$routeProvider.when('/', {
		templateUrl : './partials/home.html',
		controller : 'HomeCtrl'
	}).when('/list', {
		templateUrl : './partials/sensorStateList.html',
		controller : 'SensorStateListCtrl',
		name: 'Werteliste'
	}).when('/graph', {
		templateUrl : './partials/sensorStateGraphs.html',
		controller : 'GraphCtrl',
		name: 'Graphische Darstellung'
	}).when('/login', {
		templateUrl : './partials/login.html',
		controller : 'NavigationCtrl',
		name: 'Login'
	}).otherwise({
		redirectTo : '/'
	});

  $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';


} ]);

wStationApp.run(function($rootScope, $location, $route) {
	$rootScope.$location = $location;
	$rootScope.$route = $route;
	$rootScope.keys = Object.keys;
});

wStationApp.factory('routeNavigation', function($route, $location) {
	var routes = [];
	angular.forEach($route.routes, function(route, path) {
		if (route.name) {
			routes.push({
				path : path,
				name : route.name
			});
		}
	});

	return {
		routes : routes,
		activeRoute : function(route) {
			return route.path === $location.path();
		}
	};
});
