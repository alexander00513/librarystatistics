(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('website', {
            parent: 'entity',
            url: '/website?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.website.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/website/websites.html',
                    controller: 'WebsiteController',
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
                    $translatePartialLoader.addPart('website');
                    $translatePartialLoader.addPart('websiteType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('website-detail', {
            parent: 'entity',
            url: '/website/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'librarystatisticsApp.website.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/website/website-detail.html',
                    controller: 'WebsiteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('website');
                    $translatePartialLoader.addPart('websiteType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Website', function($stateParams, Website) {
                    return Website.get({id : $stateParams.id});
                }]
            }
        })
        .state('website.new', {
            parent: 'website',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/website/website-dialog.html',
                    controller: 'WebsiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                visits: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('website', null, { reload: true });
                }, function() {
                    $state.go('website');
                });
            }]
        })
        .state('website.edit', {
            parent: 'website',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/website/website-dialog.html',
                    controller: 'WebsiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Website', function(Website) {
                            return Website.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('website', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('website.delete', {
            parent: 'website',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/website/website-delete-dialog.html',
                    controller: 'WebsiteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Website', function(Website) {
                            return Website.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('website', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
