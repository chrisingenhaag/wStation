'use strict';

angular.module('wStationApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensorgraph', {
                parent: 'site',
                url: '/sensorgraph',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/sensorgraph/sensorgraph.html',
                        controller: 'GraphController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorgraph');
                        return $translate.refresh();
                    }]
                }
            });
    });
