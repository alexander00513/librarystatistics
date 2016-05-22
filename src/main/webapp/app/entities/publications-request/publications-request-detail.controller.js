(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PublicationsRequestDetailController', PublicationsRequestDetailController);

    PublicationsRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PublicationsRequest', 'Library', 'Person', 'Publication'];

    function PublicationsRequestDetailController($scope, $rootScope, $stateParams, entity, PublicationsRequest, Library, Person, Publication) {
        var vm = this;
        vm.publicationsRequest = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:publicationsRequestUpdate', function(event, result) {
            vm.publicationsRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
