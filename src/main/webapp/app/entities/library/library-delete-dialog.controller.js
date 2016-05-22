(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('LibraryDeleteController',LibraryDeleteController);

    LibraryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Library'];

    function LibraryDeleteController($uibModalInstance, entity, Library) {
        var vm = this;
        vm.library = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Library.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
