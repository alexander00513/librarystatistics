(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('WebsiteDeleteController',WebsiteDeleteController);

    WebsiteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Website'];

    function WebsiteDeleteController($uibModalInstance, entity, Website) {
        var vm = this;
        vm.website = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Website.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
