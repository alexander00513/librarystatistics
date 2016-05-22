(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('borough', {
            parent: 'entity',
            url: '/borough?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.borough.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/borough/boroughs.html',
                    controller: 'BoroughController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('borough');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('borough-detail', {
            parent: 'entity',
            url: '/borough/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.borough.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/borough/borough-detail.html',
                    controller: 'BoroughDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('borough');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Borough', function($stateParams, Borough) {
                    return Borough.get({id : $stateParams.id});
                }]
            }
        })
        .state('borough.new', {
            parent: 'borough',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/borough/borough-dialog.html',
                    controller: 'BoroughDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('borough', null, { reload: true });
                }, function() {
                    $state.go('borough');
                });
            }]
        })
        .state('borough.edit', {
            parent: 'borough',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/borough/borough-dialog.html',
                    controller: 'BoroughDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Borough', function(Borough) {
                            return Borough.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('borough', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('borough.delete', {
            parent: 'borough',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/borough/borough-delete-dialog.html',
                    controller: 'BoroughDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Borough', function(Borough) {
                            return Borough.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('borough', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
