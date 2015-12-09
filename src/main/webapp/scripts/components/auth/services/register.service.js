'use strict';

angular.module('wStationApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


