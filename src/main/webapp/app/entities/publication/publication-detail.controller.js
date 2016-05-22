(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PublicationDetailController', PublicationDetailController);

    PublicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Publication', 'Library'];

    function PublicationDetailController($scope, $rootScope, $stateParams, entity, Publication, Library) {
        var vm = this;
        vm.publication = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:publicationUpdate', function(event, result) {
            vm.publication = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
