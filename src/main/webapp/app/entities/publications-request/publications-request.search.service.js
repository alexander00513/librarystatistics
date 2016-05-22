(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .factory('PublicationsRequestSearch', PublicationsRequestSearch);

    PublicationsRequestSearch.$inject = ['$resource'];

    function PublicationsRequestSearch($resource) {
        var resourceUrl =  'api/_search/publications-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
