(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('SubscriptionDetailController', SubscriptionDetailController);

    SubscriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Subscription', 'Library', 'Person', 'Publication'];

    function SubscriptionDetailController($scope, $rootScope, $stateParams, entity, Subscription, Library, Person, Publication) {
        var vm = this;
        vm.subscription = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:subscriptionUpdate', function(event, result) {
            vm.subscription = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
