from typing import List, Dict, Set
from collections import defaultdict
from functools import reduce

# --------------------------------------------------
# Sample Activity Logs (Input Data)
#
# Each entry is a dictionary (like a row in a table):
#   "user"     -> roll number of the student
#   "action"   -> app or website they used
#   "duration" -> how many hours they spent on it
# --------------------------------------------------
logs: List[Dict[str, object]] = [
    {"user": "CS001", "action": "YouTube",   "duration": 1.5},
    {"user": "CS002", "action": "Instagram", "duration": 2.0},
    {"user": "CS001", "action": "VSCode",    "duration": 3.0},
    {"user": "CS003", "action": "YouTube",   "duration": 0.5},
    {"user": "CS002", "action": "LeetCode",  "duration": 2.5},
    {"user": "CS001", "action": "ChatGPT",   "duration": 1.0},
    {"user": "CS004", "action": "YouTube",   "duration": 4.0},
]

# --------------------------------------------------
# 1) Total time per user using reduce()
#
# reduce(function, list, start) works like this:
#   - Starts with an empty dict {}
#   - Goes through each log one by one
#   - Calls reducer() each time to update the running total
#   - Returns the final accumulated dict
# --------------------------------------------------
def total_time_per_user(logs: List[Dict]) -> Dict[str, float]:

    # reducer() is called once per log entry.
    # acc = the running total dict built so far
    # log = the current log entry being processed
    def reducer(acc: Dict[str, float], log: Dict) -> Dict[str, float]:
        user = log["user"]
        duration = float(log["duration"])
        # .get(user, 0.0) returns existing total, or 0.0 if user is new
        acc[user] = acc.get(user, 0.0) + duration
        return acc

    # Start with an empty dict {} and apply reducer to every log
    return reduce(reducer, logs, {})

# --------------------------------------------------
# 2) Most active users (Top K) using sorted() + key
#
# sorted() returns a new sorted list without modifying the original.
# key=  tells it WHAT to sort by.
# reverse=True means highest value first (descending).
# --------------------------------------------------
def most_active_users(logs: List[Dict], k: int) -> List[str]:
    # Step 1: Get total screen time per user
    # Result looks like: {"CS001": 5.5, "CS002": 4.5, ...}
    totals = total_time_per_user(logs)

    # Step 2: Sort users by their total time, highest first
    # .items() converts dict to list of (user, time) pairs
    # lambda x: x[1]  means "use the second element (time) as the sort key"
    sorted_users = sorted(totals.items(), key=lambda x: x[1], reverse=True)

    # Step 3: Return only the top K user names (drop the time values)
    # _ is used when we don't need that variable (the time value here)
    return [user for user, _ in sorted_users[:k]]

# --------------------------------------------------
# 3) Unique actions using set comprehension
#
# {expression for item in list} is a set comprehension.
# It works like a list comprehension but builds a set,
# which automatically removes duplicate values.
# --------------------------------------------------
def unique_actions(logs: List[Dict]) -> Set[str]:
    return {log["action"] for log in logs}

# --------------------------------------------------
# MAIN DRIVER
#
# "if __name__ == '__main__'" means:
#   Run this block ONLY when this file is executed directly.
#   If another file imports this file, this block is skipped.
# --------------------------------------------------
if __name__ == "__main__":

    print("===== Activity Log Analyzer =====\n")

    totals = total_time_per_user(logs)
    print("Total Screen Time Per User:")
    for user, time in totals.items():
        print(f"  {user} -> {time:.2f} hrs")

    print("\nTop 3 Most Active Users:")
    print(most_active_users(logs, 3))

    print("\nUnique Actions Performed:")
    print(unique_actions(logs))

# --------------------------------------------------
# Complexity Analysis
# --------------------------------------------------
"""
Let:
  N = number of log entries
  U = number of unique users
  A = number of unique actions

Time Complexity:
  1) total_time_per_user  -> O(N)           one pass through all logs
  2) most_active_users    -> O(N + U log U)  O(N) to sum + O(U log U) to sort
  3) unique_actions       -> O(N)           one pass through all logs

  Overall dominant: O(U log U)

Space Complexity:
  totals dict        -> O(U)   one entry per unique user
  sorted_users list  -> O(U)   copy of totals as list of (user, time) pairs
  unique actions set -> O(A)   one entry per unique action

  Total auxiliary space: O(U + A)
"""
