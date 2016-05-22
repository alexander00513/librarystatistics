(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .factory('LibrarySearch', LibrarySearch);

    LibrarySearch.$inject = ['$resource'];

    function LibrarySearch($resource) {
        var resourceUrl =  'api/_search/libraries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
