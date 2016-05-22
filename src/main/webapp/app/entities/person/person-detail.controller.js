(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PersonDetailController', PersonDetailController);

    PersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Person', 'Library', 'Event'];

    function PersonDetailController($scope, $rootScope, $stateParams, entity, Person, Library, Event) {
        var vm = this;
        vm.person = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:personUpdate', function(event, result) {
            vm.person = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
