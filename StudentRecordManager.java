import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class StudentRecordManager {

    private static final String FILE_NAME = "students.txt";

    // Email validation pattern
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);

    static Scanner scanner = new Scanner(System.in);

    // Student class
    static class Student {
        private int id;
        private String name;
        private String email;
        private String course;
        private int age;

        public Student(int id, String name, String email,
                       String course, int age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.course = course;
            this.age = age;
        }

        @Override
        public String toString() {
            return id + "|" + name + "|" + email + "|" + course + "|" + age;
        }

        public void display() {
            System.out.println("--------------------------------");
            System.out.println("Student ID : " + id);
            System.out.println("Name       : " + name);
            System.out.println("Email      : " + email);
            System.out.println("Course     : " + course);
            System.out.println("Age        : " + age);
        }
    }

    // Add student
    public static void addStudent() {

        try {
            System.out.println("\n===== Add Student =====");

            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Student ID must be positive.");
            }

            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine();

            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Name cannot be empty.");
            }

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException(
                        "Invalid email format.");
            }

            System.out.print("Enter Course: ");
            String course = scanner.nextLine();

            if (course.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Course cannot be empty.");
            }

            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine());

            if (age <= 0 || age > 100) {
                throw new IllegalArgumentException(
                        "Age must be between 1 and 100.");
            }

            Student student = new Student(
                    id, name, email, course, age
            );

            saveStudent(student);

            System.out.println("\nStudent added successfully!");

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error: Please enter a valid number."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Error: " + e.getMessage()
            );

        } catch (IOException e) {
            System.out.println(
                    "Error while saving student data: "
                    + e.getMessage()
            );
        }
    }

    // Save student to file
    public static void saveStudent(Student student)
            throws IOException {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(FILE_NAME, true))) {

            writer.write(student.toString());
            writer.newLine();
        }
    }

    // Read students from file
    public static void readStudents() {

        System.out.println("\n===== Student Records =====");

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No student records found.");
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(FILE_NAME))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                found = true;

                String[] data = line.split("\\|");

                if (data.length == 5) {
                    Student student = new Student(
                            Integer.parseInt(data[0]),
                            data[1],
                            data[2],
                            data[3],
                            Integer.parseInt(data[4])
                    );

                    student.display();
                }
            }

            if (!found) {
                System.out.println("No student records found.");
            }

        } catch (IOException e) {
            System.out.println(
                    "Error while reading file: "
                    + e.getMessage()
            );

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error: Invalid data found in the file."
            );
        }
    }

    // Main menu
    public static void main(String[] args) {

        while (true) {

            System.out.println("\n=================================");
            System.out.println("     STUDENT RECORD MANAGER");
            System.out.println("=================================");
            System.out.println("1. Add Student");
            System.out.println("2. Read Student Records");
            System.out.println("3. Exit");
            System.out.println("=================================");

            try {
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(
                        scanner.nextLine()
                );

                switch (choice) {

                    case 1:
                        addStudent();
                        break;

                    case 2:
                        readStudents();
                        break;

                    case 3:
                        System.out.println(
                                "Thank you for using Student Record Manager!"
                        );
                        scanner.close();
                        return;

                    default:
                        throw new IllegalArgumentException(
                                "Please choose between 1 and 3."
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Error: Please enter a valid menu number."
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }
}
