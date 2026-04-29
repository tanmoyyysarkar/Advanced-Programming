import React, { useState } from "react";

export default function CourseDashboard() {
  const [studentsMap, setStudentsMap] = useState(new Map());
  const [filterCourse, setFilterCourse] = useState("");
  const [activeFilter, setActiveFilter] = useState("");
  const [searchId, setSearchId] = useState("");
  const [foundStudent, setFoundStudent] = useState(null);

  //Dummy data
  const coursePool = ["DSA", "OS", "DBMS", "ML", "AI", "Math", "CN", "SE"];

  const firstNames = [
    "Aman",
    "Riya",
    "Karan",
    "Sneha",
    "Rahul",
    "Priya",
    "Arjun",
    "Neha",
    "Vikram",
    "Anjali",
    "Rohit",
    "Megha",
    "Varun",
    "Isha",
    "Kabir",
    "Nisha",
    "Aditya",
    "Pooja",
    "Yash",
    "Simran",
  ];

  const randomFromArray = (arr) => arr[Math.floor(Math.random() * arr.length)];

  const generateRandomCourses = () => {
    const count = Math.floor(Math.random() * 3) + 1; // 1–3 courses
    const shuffled = [...coursePool].sort(() => 0.5 - Math.random());
    return new Set(shuffled.slice(0, count));
  };

  const generateRandomGPA = () =>
    (Math.random() * (9.8 - 6.5) + 6.5).toFixed(2);

  const generateDummyStudents = () => {
    const newMap = new Map();

    for (let i = 1; i <= 1000; i++) {
      const id = i.toString().padStart(4, "0");

      newMap.set(id, {
        id,
        name: randomFromArray(firstNames) + " " + randomFromArray(firstNames),
        enrolledCourses: generateRandomCourses(),
        gpa: parseFloat(generateRandomGPA()),
      });
    }

    setStudentsMap(newMap);
  };

  // form state (with roll number)
  const [formData, setFormData] = useState({
    id: "",
    name: "",
    courses: "",
    gpa: "",
  });

  // Sorted students by GPA desc
  const sortedStudents = [...studentsMap.values()].sort(
    (a, b) => b.gpa - a.gpa,
  );

  // Unique courses using reduce + Set
  const uniqueCourses = Array.from(
    sortedStudents.reduce((acc, s) => {
      s.enrolledCourses.forEach((c) => acc.add(c));
      return acc;
    }, new Set()),
  );

  // Filter by course
  const filteredStudents = activeFilter
    ? sortedStudents.filter((s) => s.enrolledCourses.has(activeFilter))
    : sortedStudents;

  // Add student
  const handleSubmit = (e) => {
    e.preventDefault();
    const { id, name, courses, gpa } = formData;
    if (!id || !name || !courses || !gpa) return;

    const courseSet = new Set(courses.split(" ").map((c) => c.trim()));

    const newStudent = {
      id,
      name,
      enrolledCourses: courseSet,
      gpa: parseFloat(gpa),
    };

    const newMap = new Map(studentsMap);
    newMap.set(id, newStudent);
    setStudentsMap(newMap);

    setFormData({ id: "", name: "", courses: "", gpa: "" });
  };

  // Remove by ID
  const removeStudent = (id) => {
    const newMap = new Map(studentsMap);
    newMap.delete(id);
    setStudentsMap(newMap);
    setFoundStudent(null);
  };

  // Filter submit
  const handleFilterSubmit = (e) => {
    e.preventDefault();
    setActiveFilter(filterCourse.trim());
  };

  // Search by roll number
  const handleSearch = (e) => {
    e.preventDefault();
    setFoundStudent(studentsMap.get(searchId.trim()) || null);
  };

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-3xl mx-auto bg-white shadow-lg rounded-xl p-6 space-y-6">
        <h1 className="text-2xl font-bold text-slate-800">
          Course Enrollment Dashboard
        </h1>

        <button
          onClick={generateDummyStudents}
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
        >
          Generate 1000 Dummy Students
        </button>

        {/* Add Student */}
        <form onSubmit={handleSubmit} className="space-y-3">
          <h2 className="font-semibold text-slate-700">Add Student</h2>
          <div className="grid grid-cols-4 gap-3">
            <input
              className="border p-2 rounded"
              placeholder="Roll No"
              value={formData.id}
              onChange={(e) => setFormData({ ...formData, id: e.target.value })}
            />
            <input
              className="border p-2 rounded"
              placeholder="Name"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
            />
            <input
              className="border p-2 rounded"
              placeholder="Courses (space separated)"
              value={formData.courses}
              onChange={(e) =>
                setFormData({ ...formData, courses: e.target.value })
              }
            />
            <input
              className="border p-2 rounded"
              placeholder="GPA"
              value={formData.gpa}
              onChange={(e) =>
                setFormData({ ...formData, gpa: e.target.value })
              }
            />
          </div>
          <button className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700">
            Add Student
          </button>
        </form>

        {/* Unique Courses */}
        <div>
          <h2 className="font-semibold text-slate-700">All Courses</h2>
          <div className="flex flex-wrap gap-2 mt-2">
            {uniqueCourses.map((course) => (
              <span
                key={course}
                className="bg-slate-200 px-3 py-1 rounded-full text-sm"
              >
                {course}
              </span>
            ))}
          </div>
        </div>

        {/* Filter by course */}
        <form onSubmit={handleFilterSubmit} className="flex gap-2">
          <input
            className="border p-2 rounded flex-1"
            placeholder="Filter by course"
            value={filterCourse}
            onChange={(e) => setFilterCourse(e.target.value)}
          />
          <button className="bg-indigo-600 text-white px-4 rounded hover:bg-indigo-700">
            Filter
          </button>
        </form>

        {/* Students list (NO REMOVE BUTTON HERE) */}
        <div>
          <h2 className="font-semibold text-slate-700">Students</h2>
          <ul className="space-y-2 mt-2">
            {filteredStudents.map((student) => (
              <li key={student.id} className="bg-slate-100 p-3 rounded">
                <div className="font-medium">
                  {student.name} ({student.id})
                </div>
                <div className="text-sm text-slate-600">
                  GPA: {student.gpa} | Courses:{" "}
                  {[...student.enrolledCourses].join(", ")}
                </div>
              </li>
            ))}
          </ul>
        </div>

        {/* Search & Remove Section */}
        <form onSubmit={handleSearch} className="space-y-3">
          <h2 className="font-semibold text-slate-700">Search by Roll No</h2>
          <div className="flex gap-2">
            <input
              className="border p-2 rounded flex-1"
              placeholder="Enter Roll No"
              value={searchId}
              onChange={(e) => setSearchId(e.target.value)}
            />
            <button className="bg-indigo-600 text-white px-4 rounded hover:bg-indigo-700">
              Search
            </button>
          </div>

          {foundStudent && (
            <div className="bg-slate-100 p-3 rounded flex justify-between items-center">
              <div>
                <div className="font-medium">
                  {foundStudent.name} ({foundStudent.id})
                </div>
                <div className="text-sm text-slate-600">
                  GPA: {foundStudent.gpa} | Courses:{" "}
                  {[...foundStudent.enrolledCourses].join(", ")}
                </div>
              </div>
              <button
                type="button"
                onClick={() => removeStudent(foundStudent.id)}
                className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
              >
                Remove Student
              </button>
            </div>
          )}
        </form>
      </div>
    </div>
  );
}
