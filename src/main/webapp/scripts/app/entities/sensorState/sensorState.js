'use strict';

angular.module('wStationApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sensorState', {
                parent: 'entity',
                url: '/sensorStates',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'wStationApp.sensorState.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensorState/sensorStates.html',
                        controller: 'SensorStateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorState');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sensorState.detail', {
                parent: 'entity',
                url: '/sensorState/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'wStationApp.sensorState.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sensorState/sensorState-detail.html',
                        controller: 'SensorStateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sensorState');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SensorState', function($stateParams, SensorState) {
                        return SensorState.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sensorState.new', {
                parent: 'sensorState',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sensorState/sensorState-dialog.html',
                        controller: 'SensorStateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    temperature: null,
                                    airpressure: null,
                                    humidity: null,
                                    illuminance: null,
                                    createddate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('sensorState', null, { reload: true });
                    }, function() {
                        $state.go('sensorState');
                    })
                }]
            })
            .state('sensorState.edit', {
                parent: 'sensorState',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sensorState/sensorState-dialog.html',
                        controller: 'SensorStateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SensorState', function(SensorState) {
                                return SensorState.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sensorState', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('sensorState.delete', {
                parent: 'sensorState',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/sensorState/sensorState-delete-dialog.html',
                        controller: 'SensorStateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SensorState', function(SensorState) {
                                return SensorState.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sensorState', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
