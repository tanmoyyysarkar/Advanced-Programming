import pytest

from registration_service import InvalidEmailError, RegistrationService, UnderageError


# ===== PYTEST FIXTURES (Setup/Configuration) =====
# A fixture is like a "setup function" that provides data to your tests
# Think of it like @Before in JUnit - it runs before each test that uses it
# Fixtures with () after them are recreated fresh for each test

@pytest.fixture()
def service() -> RegistrationService:
    """
    Fixture that creates a fresh RegistrationService instance.
    This runs before each test that requests 'service' as a parameter.
    Returns a new, clean instance each time.
    """
    return RegistrationService()


@pytest.fixture()
def valid_user() -> dict:
    """
    Fixture that provides test data (valid email and age).
    This common data can be reused across multiple tests.
    Returns a dictionary with known-good values.
    """
    return {"email": "user@example.com", "age": 21}


# ===== TEST FUNCTION 1: Happy Path =====
def test_register_user_success(service: RegistrationService, valid_user: dict) -> None:
    """
    TEST: Verify successful registration with valid inputs.

    HOW IT WORKS:
    1. 'service' parameter is automatically provided by the @pytest.fixture (setUp)
    2. 'valid_user' parameter is automatically provided by its fixture (test data)
    3. We call register_user with valid email and age
    4. assert checks that the returned value is exactly True

    EXPECTED RESULT: Should return True without raising any exceptions
    """
    assert service.register_user(valid_user["email"], valid_user["age"]) is True


# ===== TEST FUNCTION 2: Multiple Invalid Emails (Parametrized) =====
# @pytest.mark.parametrize is like running the same test multiple times with different values
# Think of it like a loop that runs the test for each value in the list
@pytest.mark.parametrize(
    "email",
    [
        None,              # Test 1: Email is None
        "",                # Test 2: Email is empty string
        "   ",             # Test 3: Email is only whitespace
        "userexample.com", # Test 4: Missing @ symbol
        "user@com",        # Test 5: Missing domain extension (.com, .org, etc)
        "user@.com",       # Test 6: Missing domain name between @ and .
    ],
)
def test_register_user_invalid_email(
    service: RegistrationService, email: str
) -> None:
    """
    TEST: Verify that InvalidEmailError is raised for various invalid email formats.

    PARAMETRIZATION EXPLAINED:
    - This test function is called 6 times (once for each email value)
    - Each iteration gets a different 'email' value from the list above
    - pytest.raises is a context manager (like try-catch) that EXPECTS an exception

    HOW pytest.raises WORKS:
    - It's similar to assertThrows in JUnit
    - It expects the code inside the 'with' block to raise InvalidEmailError
    - If NO exception is raised, the test FAILS
    - If a DIFFERENT exception is raised, the test FAILS
    - If InvalidEmailError IS raised, the test PASSES

    EXPECTED RESULT: Each invalid email should trigger InvalidEmailError
    """
    with pytest.raises(InvalidEmailError):
        service.register_user(email, 21)


# ===== TEST FUNCTION 3: Age Restriction =====
def test_register_user_underage(service: RegistrationService) -> None:
    """
    TEST: Verify that UnderageError is raised when age is below 18.

    HOW IT WORKS:
    1. 'service' fixture provides a fresh RegistrationService
    2. We use a valid email but an age of 17 (below the minimum of 18)
    3. pytest.raises(UnderageError) expects an exception to be raised
    4. If UnderageError IS raised, test PASSES
    5. If no exception or different exception, test FAILS

    EXPECTED RESULT: Should raise UnderageError with age 17
    """
    with pytest.raises(UnderageError):
        service.register_user("user@example.com", 17)
