import java.util.*;
import java.util.stream.*;

/*
 * ASSIGNMENT 6: Student Performance Analyzer
 *
 * Purpose: Analyze student performance using appropriate Java collections
 * and functional programming with Streams.
 *
 * Requirements:
 * 1. Store students using ArrayList, HashMap, HashSet
 * 2. Use Streams for aggregation and filtering
 * 3. Sort by average score (descending) using Comparator
 * 4. Handle missing scores with getOrDefault()
 * 5. Ensure type safety with Generics
 */

// -------------------------------------------------------
// Student class: stores data for one student
// -------------------------------------------------------
class Student {
    private int id;
    private String name;
    private List<String> courses;        // ArrayList: keeps course order, allows duplicates
    private Map<String, Integer> scores; // HashMap: fast O(1) lookup of score by course name

    public Student(int id, String name, List<String> courses, Map<String, Integer> scores) {
        this.id = id;
        this.name = name;
        // Defensive copy: make our own copy so outside changes don't affect this student
        this.courses = new ArrayList<>(courses);
        this.scores  = new HashMap<>(scores);
    }

    // Getters: provide read access to private fields
    public int getId()                      { return id; }
    public String getName()                 { return name; }
    public List<String> getCourses()        { return courses; }
    public Map<String, Integer> getScores() { return scores; }

    /*
     * Calculates this student's average score across all their enrolled courses.
     *
     * How it works step by step:
     *   1. courses.stream()          -> create a stream from the list of course names
     *   2. .mapToInt(...)            -> for each course name, look up the score
     *                                   getOrDefault(course, 0) returns 0 if score is missing
     *   3. .average()               -> compute the average of all those scores
     *   4. .orElse(0.0)             -> if the course list is empty, return 0.0
     *
     * Time Complexity: O(C) where C = number of courses this student is enrolled in
     */
    public double getAverageScore() {
        return courses.stream()
                .mapToInt(course -> scores.getOrDefault(course, 0))
                .average()
                .orElse(0.0);
    }

    // Used by System.out.println() to print a readable description of a student
    @Override
    public String toString() {
        return id + " - " + name + "  Avg: " + getAverageScore();
    }
}

// -------------------------------------------------------
// StudentAnalyzer class: contains the analysis methods
// All methods are static so we can call them without creating an object
// -------------------------------------------------------
public class StudentAnalyzer {

    /*
     * METHOD 1: getTopNStudents
     * Returns the top N students ranked by average score (highest first).
     *
     * How it works step by step:
     *   1. .stream()                         -> turn the list into a stream for processing
     *   2. .sorted(Comparator...)            -> sort students by average score
     *        comparingDouble(getAverageScore) -> use average score as the sort key
     *        .reversed()                     -> flip to descending (highest first)
     *   3. .limit(n)                         -> keep only the first N students
     *   4. .collect(Collectors.toList())     -> gather results back into a List
     *
     * Time Complexity: O(S log S) — sorting S students using merge sort
     * Space Complexity: O(N) — storing the result list of N students
     */
    public static List<Student> getTopNStudents(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /*
     * METHOD 2: getAverageScorePerCourse
     * For each course, computes the average score across all students.
     *
     * How it works step by step:
     *   Step 1: Collect all unique course names (e.g. "Math", "DSA", "OS", "DBMS")
     *   Step 2: For each course, go through all students and average their scores
     *           getOrDefault(course, 0) handles students missing that course -> treated as 0
     *
     * Time Complexity: O(U * S) — for each of U courses, scan all S students
     * Space Complexity: O(U) — result map has one entry per unique course
     */
    public static Map<String, Double> getAverageScorePerCourse(List<Student> students) {

        // Step 1: Get the set of all unique courses offered by any student
        Set<String> allCourses = getAllUniqueCourses(students);

        // Step 2: For each course, calculate the average score across all students
        // Collectors.toMap(keyMapper, valueMapper) builds a Map from a stream
        return allCourses.stream()
                .collect(Collectors.toMap(
                        course -> course,   // key   = the course name
                        course -> students.stream()
                                .mapToInt(s -> s.getScores().getOrDefault(course, 0))
                                .average()
                                .orElse(0.0)  // value = average score for that course
                ));
    }

    /*
     * METHOD 3: getAllUniqueCourses
     * Returns a set of all course names across all students (no duplicates).
     *
     * How it works step by step:
     *   1. .stream()                  -> stream over the list of students
     *   2. .flatMap(s -> courses...)  -> each student has a List of courses;
     *                                    flatMap flattens all those lists into one stream
     *                                    e.g. [[Math,DSA],[Math,DBMS]] -> [Math,DSA,Math,DBMS]
     *   3. .collect(HashSet::new)     -> collect into a HashSet, which removes duplicates
     *                                    result: {Math, DSA, DBMS}
     *
     * Time Complexity: O(S * C) — visiting every course of every student
     * Space Complexity: O(U) — HashSet holds only unique course names
     */
    public static Set<String> getAllUniqueCourses(List<Student> students) {
        return students.stream()
                .flatMap(s -> s.getCourses().stream())
                .collect(Collectors.toCollection(HashSet::new));
    }
}

// -------------------------------------------------------
// Main class: entry point of the program
// Run with: java Main
// -------------------------------------------------------
class Main {
    public static void main(String[] args) {

        // ArrayList to hold all students (ordered, resizable)
        List<Student> students = new ArrayList<>();

        // --- Test Data: 3 students, each with their courses and scores ---

        // Student 1: Aman — has scores for all 3 enrolled courses
        students.add(new Student(
                1, "Aman",
                Arrays.asList("Math", "DSA", "OS"),
                Map.of("Math", 90, "DSA", 95, "OS", 80)
        ));

        // Student 2: Riya — enrolled in different courses, all scores present
        students.add(new Student(
                2, "Riya",
                Arrays.asList("Math", "DSA", "DBMS"),
                Map.of("Math", 85, "DSA", 92, "DBMS", 88)
        ));

        // Student 3: Karan — enrolled in DBMS but NO score for it
        // getOrDefault() in getAverageScore() will treat missing DBMS score as 0
        students.add(new Student(
                3, "Karan",
                Arrays.asList("Math", "OS", "DBMS"),
                Map.of("Math", 70, "OS", 75)
        ));

        // --- Output 1: Top 2 students by average score ---
        System.out.println("Top Students:");
        System.out.println("Complexity: O(S log S) for sorting, where S = " + students.size());
        StudentAnalyzer.getTopNStudents(students, 2)
                .forEach(System.out::println);

        // --- Output 2: Average score per course ---
        System.out.println("\nCourse Averages:");
        System.out.println("Complexity: O(U * S) where U = unique courses, S = " + students.size());
        StudentAnalyzer.getAverageScorePerCourse(students)
                .forEach((course, avg) -> System.out.println(course + " -> " + avg));

        // --- Output 3: All unique courses across all students ---
        System.out.println("\nUnique Courses:");
        System.out.println("Complexity: O(S * C) where S = students, C = avg courses per student");
        System.out.println(StudentAnalyzer.getAllUniqueCourses(students));
    }
}
