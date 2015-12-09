var wStationDirectives = angular.module('wStation.directives',[ 'wStation.services' ]);

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