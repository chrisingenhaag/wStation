'use strict';

angular.module('wStationApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensorsettings', {
                parent: 'site',
                url: '/sensorsettings',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/sensorsettings/sensorsettings.html',
                        controller: 'SensorSettingsController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorgraph');
                        $translatePartialLoader.addPart('sensorState');
                        return $translate.refresh();
                    }]
                }
            });
    });
