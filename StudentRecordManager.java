import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class StudentRecordManager {

    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "students.txt";

    static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    static class Student {
        int id;
        String name;
        String email;
        String course;
        int age;

        Student(int id, String name, String email,
                String course, int age) {

            this.id = id;
            this.name = name;
            this.email = email;
            this.course = course;
            this.age = age;
        }

        String getData() {
            return id + "|" + name + "|" + email
                    + "|" + course + "|" + age;
        }
    }

    static void addStudent() {

        try {
            System.out.println("\n--- Add Student ---");

            System.out.print("Enter ID: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            if (name.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Name cannot be empty.");
            }

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            if (!Pattern.matches(EMAIL_REGEX, email)) {
                throw new IllegalArgumentException(
                        "Invalid email format.");
            }

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Age: ");
            int age = Integer.parseInt(sc.nextLine());

            if (age <= 0) {
                throw new IllegalArgumentException(
                        "Age must be greater than 0.");
            }

            Student student =
                    new Student(id, name, email, course, age);

            saveStudent(student);

            System.out.println(
                    "Student added successfully!");

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid input! Please enter numbers correctly.");

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Error: " + e.getMessage());

        } catch (IOException e) {

            System.out.println(
                    "File error: " + e.getMessage());
        }
    }

    static void saveStudent(Student student)
            throws IOException {

        FileWriter fw =
                new FileWriter(FILE_NAME, true);

        BufferedWriter bw =
                new BufferedWriter(fw);

        bw.write(student.getData());
        bw.newLine();

        bw.close();
    }

    static void readStudents() {

        try {

            File file = new File(FILE_NAME);

            if (!file.exists()) {
                System.out.println(
                        "No student records found.");
                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            System.out.println(
                    "\n--- Student Records ---");

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|");

                System.out.println(
                        "ID     : " + data[0]);
                System.out.println(
                        "Name   : " + data[1]);
                System.out.println(
                        "Email  : " + data[2]);
                System.out.println(
                        "Course : " + data[3]);
                System.out.println(
                        "Age    : " + data[4]);

                System.out.println("----------------------");
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading file: "
                    + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Invalid student data in file.");
        }
    }

    public static void main(String[] args) {

        while (true) {

            System.out.println(
                    "\n===== STUDENT RECORD MANAGER =====");

            System.out.println("1. Add Student");
            System.out.println("2. Read Student Data");
            System.out.println("3. Exit");

            try {

                System.out.print("Enter choice: ");

                int choice =
                        Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        addStudent();
                        break;

                    case 2:
                        readStudents();
                        break;

                    case 3:
                        System.out.println(
                                "Program closed.");
                        return;

                    default:
                        System.out.println(
                                "Invalid choice!");
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }
}
