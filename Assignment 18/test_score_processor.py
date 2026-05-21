import pytest

from score_processor import ScoreProcessor


# ===== PYTEST FIXTURE (Custom Setup) =====
@pytest.fixture()
def processor() -> ScoreProcessor:
    """
    Fixture that creates a fresh ScoreProcessor instance before each test.
    Similar to @Before in JUnit or setUp() method.
    Returns a new instance so each test has a clean slate.
    """
    return ScoreProcessor()


# ===== TEST FUNCTION 1: Happy Path (Successful File Read) =====
def test_process_score_file_success(processor: ScoreProcessor, tmp_path) -> None:
    """
    TEST: Verify successful score processing with valid file.

    PARAMETERS EXPLAINED:
    - 'processor' fixture: Provides a fresh ScoreProcessor instance
    - 'tmp_path' fixture: A pytest built-in fixture that creates a temporary directory
      (automatically cleaned up after test finishes)

    HOW THE TEST WORKS:
    1. tmp_path is a temporary folder that exists only during this test
    2. We create a file named "score.txt" in that folder
    3. write_text("7") writes the number 7 into the file
    4. We call process_score_file with the path to our temporary file
    5. We assert that the result is 70 (which is 7 * 10)

    EXPECTED RESULT: Should return 70 without raising any exceptions
    """
    # Create a temporary file named "score.txt" in tmp_path
    score_file = tmp_path / "score.txt"

    # Write the string "7" to the file with UTF-8 encoding
    score_file.write_text("7", encoding="utf-8")

    # Call the method with the path to our test file
    # Assert that it returns 70 (7 multiplied by 10)
    assert processor.process_score_file(str(score_file)) == 70


# ===== TEST FUNCTION 2: Error Path (Missing File) =====
def test_process_score_file_missing_file(processor: ScoreProcessor, tmp_path) -> None:
    """
    TEST: Verify that FileNotFoundError is raised when file doesn't exist.

    HOW pytest.raises WORKS (Like assertThrows in JUnit):
    - pytest.raises(FileNotFoundError) is a context manager
    - It expects the code inside the 'with' block to raise FileNotFoundError
    - If NO exception is raised: TEST FAILS
    - If WRONG exception is raised: TEST FAILS
    - If FileNotFoundError IS raised: TEST PASSES

    CONTEXT MANAGER SYNTAX:
    'with pytest.raises(SomeException):' means "the code inside should raise SomeException"

    EXPECTED RESULT: Should raise FileNotFoundError when accessing missing file
    """
    # Create a path to a file that doesn't exist (missing.txt)
    # We don't actually create the file, just define its path
    missing_file = tmp_path / "missing.txt"

    # Wrap the call in pytest.raises - we EXPECT FileNotFoundError
    with pytest.raises(FileNotFoundError):
        # This will try to open a non-existent file
        # process_score_file will catch it and re-raise it
        # pytest.raises catches it and the test PASSES because we expected it
        processor.process_score_file(str(missing_file))


# ===== TEST FUNCTION 3: Error Path (Invalid Data) =====
def test_process_score_file_invalid_data(processor: ScoreProcessor, tmp_path) -> None:
    """
    TEST: Verify that ValueError is raised when file contains non-numeric data.

    SCENARIO:
    - File exists and is readable
    - But it contains text that can't be converted to a number

    HOW IT TRIGGERS:
    1. File is successfully opened
    2. We read "not-a-number" from the file
    3. int("not-a-number") fails and raises ValueError
    4. process_score_file catches ValueError and re-raises it
    5. pytest.raises catches it and test PASSES

    EXPECTED RESULT: Should raise ValueError when int() can't parse the data
    """
    # Create a temporary file named "score.txt"
    score_file = tmp_path / "score.txt"

    # Write invalid data (text instead of numbers) to the file
    score_file.write_text("not-a-number", encoding="utf-8")

    # Wrap the call in pytest.raises - we EXPECT ValueError
    with pytest.raises(ValueError):
        # This will open the file successfully, but fail when trying
        # to convert "not-a-number" to an integer
        # process_score_file catches it and re-raises it
        # pytest.raises catches it and test PASSES
        processor.process_score_file(str(score_file))
