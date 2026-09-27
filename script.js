// Student Record Manager

let students = [];


// Load saved data when page opens

window.onload = function () {

    const savedData = localStorage.getItem("students");

    if (savedData) {

        try {
            students = JSON.parse(savedData);
        }

        catch (error) {
            students = [];
            showMessage(
                "Unable to read saved student data.",
                "error"
            );
        }
    }

    displayStudents();
};


// Email Regex

const emailRegex =
    /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;


// Add Student

document
    .getElementById("studentForm")
    .addEventListener("submit", function (event) {

        event.preventDefault();

        try {

            const id =
                document.getElementById("studentId")
                    .value.trim();

            const name =
                document.getElementById("studentName")
                    .value.trim();

            const email =
                document.getElementById("email")
                    .value.trim();

            const course =
                document.getElementById("course")
                    .value.trim();

            const age =
                document.getElementById("age")
                    .value.trim();


            // Invalid input checking

            if (id === "" ||
                name === "" ||
                email === "" ||
                course === "" ||
                age === "") {

                throw new Error(
                    "All fields are required."
                );
            }


            if (isNaN(id) || Number(id) <= 0) {

                throw new Error(
                    "Student ID must be a valid positive number."
                );
            }


            if (!emailRegex.test(email)) {

                throw new Error(
                    "Please enter a valid email address."
                );
            }


            if (isNaN(age) ||
                Number(age) <= 0 ||
                Number(age) > 100) {

                throw new Error(
                    "Age must be between 1 and 100."
                );
            }


            // Check duplicate ID

            const duplicate =
                students.some(
                    student => student.id === Number(id)
                );

            if (duplicate) {

                throw new Error(
                    "Student ID already exists."
                );
            }


            // Create student object

            const student = {

                id: Number(id),

                name: name,

                email: email,

                course: course,

                age: Number(age)
            };


            // Add student

            students.push(student);


            // Save in browser

            saveToLocalStorage();


            // Update table

            displayStudents();


            // Success message

            showMessage(
                "Student added successfully!",
                "success"
            );


            // Clear form

            document
                .getElementById("studentForm")
                .reset();

        }

        catch (error) {

            showMessage(
                error.message,
                "error"
            );
        }
    });


// Save to Local Storage

function saveToLocalStorage() {

    localStorage.setItem(
        "students",
        JSON.stringify(students)
    );
}


// Display Students

function displayStudents(searchText = "") {

    const table =
        document.getElementById("studentTable");

    const emptyMessage =
        document.getElementById("emptyMessage");

    table.innerHTML = "";


    const filteredStudents =
        students.filter(student => {

            const search =
                searchText.toLowerCase();

            return (

                student.id.toString()
                    .includes(search)

                ||

                student.name.toLowerCase()
                    .includes(search)

                ||

                student.email.toLowerCase()
                    .includes(search)

                ||

                student.course.toLowerCase()
                    .includes(search)
            );
        });


    if (filteredStudents.length === 0) {

        emptyMessage.style.display = "block";

    }

    else {

        emptyMessage.style.display = "none";
    }


    filteredStudents.forEach(student => {

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>${student.id}</td>

            <td>${escapeHTML(student.name)}</td>

            <td>${escapeHTML(student.email)}</td>

            <td>${escapeHTML(student.course)}</td>

            <td>${student.age}</td>

            <td>
                <button
                    class="delete-btn"
                    onclick="deleteStudent(${student.id})">
                    Delete
                </button>
            </td>

        `;


        table.appendChild(row);
    });


    updateStatistics(filteredStudents);
}


// Delete Student

function deleteStudent(id) {

    const confirmDelete =
        confirm(
            "Are you sure you want to delete this student?"
        );


    if (!confirmDelete) {
        return;
    }


    students =
        students.filter(
            student => student.id !== id
        );


    saveToLocalStorage();

    displayStudents();

    showMessage(
        "Student deleted successfully.",
        "success"
    );
}


// Search

document
    .getElementById("searchInput")
    .addEventListener("input", function () {

        displayStudents(this.value);

    });


// Save data to JSON file

function exportData() {

    if (students.length === 0) {

        showMessage(
            "There are no student records to save.",
            "error"
        );

        return;
    }


    const data =
        JSON.stringify(students, null, 4);


    const blob =
        new Blob(
            [data],
            { type: "application/json" }
        );


    const url =
        URL.createObjectURL(blob);


    const link =
        document.createElement("a");


    link.href = url;

    link.download =
        "student-records.json";


    link.click();


    URL.revokeObjectURL(url);


    showMessage(
        "Student data saved to file.",
        "success"
    );
}


// Read student data from JSON file

document
    .getElementById("fileInput")
    .addEventListener("change", function (event) {

        const file =
            event.target.files[0];


        if (!file) {
            return;
        }


        const reader =
            new FileReader();


        reader.onload = function (e) {

            try {

                const importedData =
                    JSON.parse(e.target.result);


                if (!Array.isArray(importedData)) {

                    throw new Error(
                        "Invalid file format."
                    );
                }


                students = importedData;


                saveToLocalStorage();

                displayStudents();


                showMessage(
                    "Student data loaded successfully.",
                    "success"
                );

            }

            catch (error) {

                showMessage(
                    "Invalid student data file.",
                    "error"
                );
            }
        };


        reader.readAsText(file);

    });


// Display message

function showMessage(message, type) {

    const messageBox =
        document.getElementById("message");


    messageBox.textContent =
        message;


    messageBox.className = type;


    setTimeout(function () {

        messageBox.textContent = "";

        messageBox.className = "";

    }, 3000);
}


// Statistics

function updateStatistics(data) {

    document.getElementById(
        "totalStudents"
    ).textContent = students.length;


    const courses =
        new Set(
            students.map(
                student => student.course.toLowerCase()
            )
        );


    document.getElementById(
        "totalCourses"
    ).textContent = courses.size;


    document.getElementById(
        "totalEmails"
    ).textContent = students.filter(
        student => emailRegex.test(student.email)
    ).length;
}


// Prevent HTML injection

function escapeHTML(value) {

    return value
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
