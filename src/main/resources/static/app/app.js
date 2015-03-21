var wStationApp = angular.module("wStation", [ 'ngRoute', 'wStation.services',
		'wStation.controllers' ]);

wStationApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/list', {
		templateUrl : './partials/sensorStateList.html',
		controller : 'SensorStateListCtrl',
		name: 'Sensorhistorie'
	}).otherwise({
		redirectTo : '/list'
	});
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
	console.log($route.routes);
	return {
		routes : routes,
		activeRoute : function(route) {
			return route.path === $location.path();
		}
	};
});

wStationApp.directive('navigation', function(routeNavigation) {
	return {
		restrict : "E",
		replace : true,
		templateUrl : "./directives/navigation-directive.tpl.html",
		controller : function($scope) {
			$scope.routes = routeNavigation.routes;
			$scope.activeRoute = routeNavigation.activeRoute;
		}
	};
});
