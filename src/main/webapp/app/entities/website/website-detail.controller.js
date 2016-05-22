(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('WebsiteDetailController', WebsiteDetailController);

    WebsiteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Website', 'Library'];

    function WebsiteDetailController($scope, $rootScope, $stateParams, entity, Website, Library) {
        var vm = this;
        vm.website = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:websiteUpdate', function(event, result) {
            vm.website = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
