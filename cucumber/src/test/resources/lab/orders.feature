Feature: Placing orders
  As a shop customer I want to place and look up orders so that I know my purchase was accepted.

  Scenario: A valid order is accepted
    When I place an order for 2 of "ABC"
    Then the response status is 201
    And the order status is "NEW"

  Scenario: A placed order can be looked up
    Given I have placed an order for 3 of "XYZ"
    When I look the order up
    Then the response status is 200
    And the order quantity is 3

  Scenario: A zero quantity is rejected
    When I place an order for 0 of "ABC"
    Then the response status is 400

  Scenario: An unknown order is not found
    When I look up order 99999
    Then the response status is 404
