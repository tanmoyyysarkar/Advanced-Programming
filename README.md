# Assignment 14
---
Create a scenario where objects are "dead" but still have a reference count higher than zero, then force the Garbage Collector to clean them up. Do in python only.

Implementation Steps:

1. Create a Node class with a name and a link attribute.
2. Create a Cycle: Instantiate Node A and Node B.
3. Set A.link = B and B.link = A.
4. Check References: Use sys.getrefcount() to show that both objects have multiple references.
5. The "Deletion": Use del A and del B.
6. The Investigation: Use the gc module to show that these objects still exist in memory because of the cycle, even though you can no longer access them from your code.
7. The Cleanup: Call gc.collect() and print the number of "unreachable" objects collected.
