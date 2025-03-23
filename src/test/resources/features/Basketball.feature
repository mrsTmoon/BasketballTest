Feature: Basketball England Registration

  Scenario: Successful registration
    Given I am on the registration page
    When I fill in all required fields correctly
    And I accept all required terms
    And I click the "Confirm and Join" button
    Then I should see a success or confirmation message

  Scenario: Missing last name
    Given I am on the registration page
    When I fill in the form without a last name
    And I accept all required terms
    And I click the "Confirm and Join" button
    Then I should see an error for missing last name

  Scenario: Passwords do not match
    Given I am on the registration page
    When I fill in the form with mismatched passwords
    And I accept all required terms
    And I click the "Confirm and Join" button
    Then I should see an error for password mismatch

  Scenario: Terms and conditions not accepted
    Given I am on the registration page
    When I fill in all required fields correctly
    And I do not accept the terms
    And I click the "Confirm and Join" button
    Then I should see an error for unaccepted terms
