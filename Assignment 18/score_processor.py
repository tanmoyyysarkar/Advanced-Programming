class ScoreProcessor:
    """
    A utility class that reads integer scores from files and performs calculations.
    This demonstrates proper exception handling and resource management.
    """

    def process_score_file(self, file_path: str) -> int:
        """
        Read an integer score from a file, multiply it by 10, and return the result.

        EXCEPTION HANDLING STRUCTURE:
        - try: Attempt to open and read file
        - except FileNotFoundError: Handle missing file
        - except ValueError: Handle non-numeric data
        - else: Code that runs ONLY if no exceptions occur
        - finally: Cleanup code that ALWAYS runs (like a destructor)

        Args:
            file_path: Path to the file containing the score

        Returns:
            The score multiplied by 10

        Raises:
            FileNotFoundError: If the file doesn't exist
            ValueError: If the file contains non-numeric data
        """
        try:
            # TRY BLOCK: Attempt normal file operation
            # 'with' statement automatically closes the file when done (resource management)
            # 'encoding="utf-8"' specifies how to read text characters
            with open(file_path, "r", encoding="utf-8") as file_handle:
                # Read entire file content and remove whitespace from start/end
                raw_value = file_handle.read().strip()

                # Convert string to integer
                # If the string contains letters/symbols, int() raises ValueError
                score = int(raw_value)

        except FileNotFoundError as exc:
            # EXCEPT BLOCK 1: File doesn't exist
            # This specific exception is caught separately so we can handle it differently
            # from other types of errors
            print(f"Error: file not found -> {file_path}")
            # Re-raise the exception so calling code knows something went wrong
            raise exc

        except ValueError as exc:
            # EXCEPT BLOCK 2: File exists but contains invalid data
            # ValueError is raised when int() can't convert the string to a number
            # (e.g., file contains "abc" or "12.34" instead of "5")
            print(f"Error: invalid integer data -> '{raw_value}'")
            # Re-raise the exception so calling code knows something went wrong
            raise exc

        else:
            # ELSE BLOCK: Runs ONLY if NO exceptions occurred in try block
            # This is like "success" path - we only reach here if file read succeeded
            print("Data processed successfully")
            # Perform the calculation and return result
            return score * 10

        finally:
            # FINALLY BLOCK: ALWAYS runs, regardless of exceptions
            # Use this for cleanup (closing files, releasing resources, etc.)
            # Note: The 'with' statement already closed the file, but this demonstrates
            # the finally concept - code that must run no matter what
            print("File cleanup completed")
