Feature: Payflex Login

  Scenario Outline: Successful login
    Given I open the Payflex login page
    When I enter valid credentials
    #When I login via the API
    Then I should be logged in and see my dashboard
    And I select edit for profile details
    Then I update my address city "<city>" and postal "<postal>"
    Then I validate the success message

    Examples:
      | city         | postal |
      | Cape Town    | 8001   |
      | Johannesburg | 2000   |
      | Durban       | 4001   |