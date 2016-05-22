'use strict';

describe('Controller Tests', function() {

    describe('Website Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockWebsite, MockLibrary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockWebsite = jasmine.createSpy('MockWebsite');
            MockLibrary = jasmine.createSpy('MockLibrary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Website': MockWebsite,
                'Library': MockLibrary
            };
            createController = function() {
                $injector.get('$controller')("WebsiteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:websiteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
