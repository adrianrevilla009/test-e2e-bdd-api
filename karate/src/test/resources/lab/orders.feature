Feature: Orders API (same scenarios as ../restassured)

  Background:
    * url 'http://127.0.0.1:' + karate.properties['orders.port']

  Scenario: creates an order
    Given path 'orders'
    And request { sku: 'ABC', qty: 2 }
    When method post
    Then status 201
    And match response == { id: '#number', status: 'NEW', qty: 2 }

  Scenario: fetches a created order
    # each scenario creates its own data, so scenarios stay order-independent
    Given path 'orders'
    And request { sku: 'XYZ', qty: 3 }
    When method post
    Then status 201
    * def id = response.id
    Given path 'orders', id
    When method get
    Then status 200
    And match response.qty == 3

  Scenario: rejects invalid quantity
    Given path 'orders'
    And request { sku: 'ABC', qty: 0 }
    When method post
    Then status 400

  Scenario: unknown order is 404
    Given path 'orders', 99999
    When method get
    Then status 404
