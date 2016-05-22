(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'Library', 'Person'];

    function EventDetailController($scope, $rootScope, $stateParams, entity, Event, Library, Person) {
        var vm = this;
        vm.event = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
