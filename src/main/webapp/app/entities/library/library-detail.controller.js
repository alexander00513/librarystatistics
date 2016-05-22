(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('LibraryDetailController', LibraryDetailController);

    LibraryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Library', 'Borough', 'Equipment', 'Publication', 'Subscription', 'PublicationsRequest', 'Event', 'Website', 'Person'];

    function LibraryDetailController($scope, $rootScope, $stateParams, entity, Library, Borough, Equipment, Publication, Subscription, PublicationsRequest, Event, Website, Person) {
        var vm = this;
        vm.library = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:libraryUpdate', function(event, result) {
            vm.library = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
