'use strict';

describe('Controller Tests', function() {

    describe('PublicationsRequest Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPublicationsRequest, MockLibrary, MockPerson, MockPublication;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPublicationsRequest = jasmine.createSpy('MockPublicationsRequest');
            MockLibrary = jasmine.createSpy('MockLibrary');
            MockPerson = jasmine.createSpy('MockPerson');
            MockPublication = jasmine.createSpy('MockPublication');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PublicationsRequest': MockPublicationsRequest,
                'Library': MockLibrary,
                'Person': MockPerson,
                'Publication': MockPublication
            };
            createController = function() {
                $injector.get('$controller')("PublicationsRequestDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:publicationsRequestUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
