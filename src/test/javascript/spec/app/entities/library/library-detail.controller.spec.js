'use strict';

describe('Controller Tests', function() {

    describe('Library Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLibrary, MockBorough, MockEquipment, MockPublication, MockSubscription, MockPublicationsRequest, MockEvent, MockWebsite, MockPerson;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLibrary = jasmine.createSpy('MockLibrary');
            MockBorough = jasmine.createSpy('MockBorough');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockPublication = jasmine.createSpy('MockPublication');
            MockSubscription = jasmine.createSpy('MockSubscription');
            MockPublicationsRequest = jasmine.createSpy('MockPublicationsRequest');
            MockEvent = jasmine.createSpy('MockEvent');
            MockWebsite = jasmine.createSpy('MockWebsite');
            MockPerson = jasmine.createSpy('MockPerson');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Library': MockLibrary,
                'Borough': MockBorough,
                'Equipment': MockEquipment,
                'Publication': MockPublication,
                'Subscription': MockSubscription,
                'PublicationsRequest': MockPublicationsRequest,
                'Event': MockEvent,
                'Website': MockWebsite,
                'Person': MockPerson
            };
            createController = function() {
                $injector.get('$controller')("LibraryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:libraryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
