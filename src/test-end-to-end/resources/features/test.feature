Feature: End 2 end test example

  Scenario: nothing
    Given the api returns hello
    When I make a request for /test
    Then the request says hello

