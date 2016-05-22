(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('BoroughDeleteController',BoroughDeleteController);

    BoroughDeleteController.$inject = ['$uibModalInstance', 'entity', 'Borough'];

    function BoroughDeleteController($uibModalInstance, entity, Borough) {
        var vm = this;
        vm.borough = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Borough.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
