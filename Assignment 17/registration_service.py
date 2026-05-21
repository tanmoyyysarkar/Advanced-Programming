import re
from typing import Optional


# CUSTOM EXCEPTION 1: InvalidEmailError (Checked Exception equivalent)
# Inherits from ValueError (appropriate base for invalid data)
# This exception is raised when the email validation fails for any reason
class InvalidEmailError(ValueError):
    """Raised when the supplied email is missing or malformed."""


# CUSTOM EXCEPTION 2: UnderageError (Business Logic Exception)
# Inherits from ValueError to handle value-related validation failures
# This exception is raised when age requirement is not met
class UnderageError(ValueError):
    """Raised when the supplied age does not meet the minimum requirement."""


class RegistrationService:
    # EMAIL_PATTERN: Regex pattern that validates email format
    # Pattern breakdown:
    # ^[A-Za-z0-9._%+-]+ = starts with alphanumeric or special chars (. _ % + -)
    # @ = must have @ symbol
    # [A-Za-z0-9.-]+ = domain name with alphanumeric or hyphens/dots
    # \. = literal dot (escaped because . matches any character in regex)
    # [A-Za-z]{2,} = domain extension (at least 2 letters: .com, .org, etc.)
    # $ = end of string
    _EMAIL_PATTERN = re.compile(r"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$")

    def __init__(self, context_ready: bool = True) -> None:
        # Initialize service with a context flag
        # This flag is used for the internal assert statement
        self._context_ready = context_ready

    def register_user(self, email: Optional[str], age: int) -> bool:
        # INTERNAL ASSERTION: Verify system state before processing
        # This assert guarantees that the service is in a valid state
        # If context_ready is False, an AssertionError is raised immediately
        # This prevents invalid processing from occurring
        assert self._context_ready, "Service context is not ready"

        # VALIDATION STEP 1: Check if email is None
        # Null/None values are not allowed
        if email is None:
            raise InvalidEmailError("Email must not be null.")

        # VALIDATION STEP 2: Ensure email is actually a string type
        # Prevents unexpected type errors downstream
        if not isinstance(email, str):
            raise InvalidEmailError(
                f"Email must be a string, got {type(email).__name__}."
            )

        # VALIDATION STEP 3: Check if email is empty or whitespace-only
        # .strip() removes leading/trailing spaces before comparison
        if email.strip() == "":
            raise InvalidEmailError("Email must not be empty.")

        # VALIDATION STEP 4: Match email against the regex pattern
        # If email doesn't match the expected format, it's invalid
        if not self._EMAIL_PATTERN.match(email):
            raise InvalidEmailError(f"Email '{email}' is not a valid format.")

        # VALIDATION STEP 5: Age boundary check
        # Business rule: Users must be at least 18 years old
        if age < 18:
            raise UnderageError(
                f"User must be at least 18 years old; got {age}."
            )

        # If all validations pass, registration is approved
        return True
