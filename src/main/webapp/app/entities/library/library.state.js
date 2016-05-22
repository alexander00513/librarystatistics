(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('library', {
            parent: 'entity',
            url: '/library?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.library.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/library/libraries.html',
                    controller: 'LibraryController',
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
                    $translatePartialLoader.addPart('library');
                    $translatePartialLoader.addPart('libraryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('library-detail', {
            parent: 'entity',
            url: '/library/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.library.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/library/library-detail.html',
                    controller: 'LibraryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('library');
                    $translatePartialLoader.addPart('libraryType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Library', function($stateParams, Library) {
                    return Library.get({id : $stateParams.id});
                }]
            }
        })
        .state('library.new', {
            parent: 'library',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/library/library-dialog.html',
                    controller: 'LibraryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                type: null,
                                internetAccess: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('library', null, { reload: true });
                }, function() {
                    $state.go('library');
                });
            }]
        })
        .state('library.edit', {
            parent: 'library',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/library/library-dialog.html',
                    controller: 'LibraryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Library', function(Library) {
                            return Library.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('library', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('library.delete', {
            parent: 'library',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/library/library-delete-dialog.html',
                    controller: 'LibraryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Library', function(Library) {
                            return Library.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('library', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
