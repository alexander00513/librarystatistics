(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('publications-request', {
            parent: 'entity',
            url: '/publications-request?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.publicationsRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publications-request/publications-requests.html',
                    controller: 'PublicationsRequestController',
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
                    $translatePartialLoader.addPart('publicationsRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('publications-request-detail', {
            parent: 'entity',
            url: '/publications-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.publicationsRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publications-request/publications-request-detail.html',
                    controller: 'PublicationsRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('publicationsRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PublicationsRequest', function($stateParams, PublicationsRequest) {
                    return PublicationsRequest.get({id : $stateParams.id});
                }]
            }
        })
        .state('publications-request.new', {
            parent: 'publications-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publications-request/publications-request-dialog.html',
                    controller: 'PublicationsRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fromDate: null,
                                toDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('publications-request', null, { reload: true });
                }, function() {
                    $state.go('publications-request');
                });
            }]
        })
        .state('publications-request.edit', {
            parent: 'publications-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publications-request/publications-request-dialog.html',
                    controller: 'PublicationsRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PublicationsRequest', function(PublicationsRequest) {
                            return PublicationsRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('publications-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('publications-request.delete', {
            parent: 'publications-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publications-request/publications-request-delete-dialog.html',
                    controller: 'PublicationsRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PublicationsRequest', function(PublicationsRequest) {
                            return PublicationsRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('publications-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
