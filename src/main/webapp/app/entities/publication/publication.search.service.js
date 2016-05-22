(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .factory('PublicationSearch', PublicationSearch);

    PublicationSearch.$inject = ['$resource'];

    function PublicationSearch($resource) {
        var resourceUrl =  'api/_search/publications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
