(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('publication', {
            parent: 'entity',
            url: '/publication?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.publication.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publication/publications.html',
                    controller: 'PublicationController',
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
                    $translatePartialLoader.addPart('publication');
                    $translatePartialLoader.addPart('publicationType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('publication-detail', {
            parent: 'entity',
            url: '/publication/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.publication.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publication/publication-detail.html',
                    controller: 'PublicationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('publication');
                    $translatePartialLoader.addPart('publicationType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Publication', function($stateParams, Publication) {
                    return Publication.get({id : $stateParams.id});
                }]
            }
        })
        .state('publication.new', {
            parent: 'publication',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publication/publication-dialog.html',
                    controller: 'PublicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                author: null,
                                isbn: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('publication', null, { reload: true });
                }, function() {
                    $state.go('publication');
                });
            }]
        })
        .state('publication.edit', {
            parent: 'publication',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publication/publication-dialog.html',
                    controller: 'PublicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Publication', function(Publication) {
                            return Publication.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('publication', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('publication.delete', {
            parent: 'publication',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publication/publication-delete-dialog.html',
                    controller: 'PublicationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Publication', function(Publication) {
                            return Publication.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('publication', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
