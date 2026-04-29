class Address:
    def __init__(self, street, city, zipCode):
        self.street = street
        self.city = city
        self.zipCode = zipCode

class Student:
    @property
    def age(self):
        return self._age

    @age.setter
    def age(self, value):
        if value <= 0:
            print("Invalid age")
        else:
            self._age = value

    def __init__(self, name, age, address):
        self.name = name
        self.age = age
        self.address = address
        self.courses = []

    def add_course(self, course):
        self.courses.append(course)

    def display(self):
        print(f"Name: {self.name} | Age: {self.age} | Address: {self.address.street} | {self.address.city}-{self.address.zipCode}\nCourses: {self.courses}")

class ScholarshipStudent(Student):
    def __init__(self, name, age, address, scholarshipAmount):
        super().__init__(name, age, address)
        self.scholarshipAmount = scholarshipAmount

    def display(self):
        super().display()
        print(f"Scholarship Amount: {self.scholarshipAmount}")

# Normal student
addr1 = Address("Main Road", "Delhi", "110001")

s1 = Student("Rahul", 20, addr1)

print("Adding courses...")
s1.add_course("Python")
s1.add_course("Java")

s1.display()

# Show mutable behavior (list persists)
print("\nAdding another course later...")
s1.add_course("DBMS")
s1.display()


# Property validation
print("\nTrying invalid age:")
s1.age = -5   # should trigger validation

print("\nUpdating valid age:")
s1.age = 21
s1.display()


# Scholarship student
addr2 = Address("Park Street", "Mumbai", "400001")

sch = ScholarshipStudent(
    "Aditi",
    19,
    addr2,
    50000
)

sch.add_course("Data Structures")
sch.add_course("Machine Learning")

print("\nScholarship Student:")
sch.display()

print("\n\nUsing polymorphism:")
students = [s1, sch]
for s in students:
    s.display()

