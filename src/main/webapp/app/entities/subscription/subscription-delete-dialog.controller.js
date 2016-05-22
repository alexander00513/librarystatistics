(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('SubscriptionDeleteController',SubscriptionDeleteController);

    SubscriptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Subscription'];

    function SubscriptionDeleteController($uibModalInstance, entity, Subscription) {
        var vm = this;
        vm.subscription = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Subscription.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
