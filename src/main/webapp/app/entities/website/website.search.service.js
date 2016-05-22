(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .factory('WebsiteSearch', WebsiteSearch);

    WebsiteSearch.$inject = ['$resource'];

    function WebsiteSearch($resource) {
        var resourceUrl =  'api/_search/websites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
