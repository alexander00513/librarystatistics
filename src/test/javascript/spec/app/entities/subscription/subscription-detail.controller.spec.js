'use strict';

describe('Controller Tests', function() {

    describe('Subscription Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSubscription, MockLibrary, MockPerson, MockPublication;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSubscription = jasmine.createSpy('MockSubscription');
            MockLibrary = jasmine.createSpy('MockLibrary');
            MockPerson = jasmine.createSpy('MockPerson');
            MockPublication = jasmine.createSpy('MockPublication');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Subscription': MockSubscription,
                'Library': MockLibrary,
                'Person': MockPerson,
                'Publication': MockPublication
            };
            createController = function() {
                $injector.get('$controller')("SubscriptionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:subscriptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
