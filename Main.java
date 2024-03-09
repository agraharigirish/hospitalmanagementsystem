import hospitalmanagementsystem.Doctors;
import hospitalmanagementsystem.Patients;

import java.sql.*;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";

    private static final String userName = "root";

    private static final String password = "root";

    public static void main(String[] args) throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Patients patients = new Patients(connection, scanner);
            Doctors doctors = new Doctors(connection);
            while (true) {
                System.out.println("Hospital Management System : ");
                System.out.println("1. Add Patients to Hospitals ");
                System.out.println("2. View Patients ");
                System.out.println("3. View Doctors");
                System.out.println("4. View Appointments");
                System.out.println("5. Exit ");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        //Add patients
                        patients.addPatients(connection, scanner);
                        break;
                    case 2:
                        //Veiw Particular Patients
                        System.out.println("Enter the patient id : ");
                        int patientId = scanner.nextInt();
                        boolean b = patients.getPatientsById(patientId);
                        System.out.println(b);
                        if (b) {
                            System.out.println("Patiets shows Successfully");
                        } else {
                            System.out.println("Patients doesn't Exists");
                        }
                        break;
                    case 3:
                        //view  doctors
                        doctors.viewDoctors();
                        break;
                    case 4:
                        //Book Appointments
                        System.out.println("Enter the doctorsId :");
                        int dId = scanner.nextInt();
                        System.out.println("Enter the patientsId :");
                        int pId = scanner.nextInt();
                        System.out.println("Appointment Date (yyyy-mm-dd)");
                        String appointmentDate = scanner.next();
                        if (doctors.getDoctorsById(dId) && patients.getPatientsById(pId)) {
                            if (checkAvailability(dId, appointmentDate,connection)) {
                                String quuery = "INSERT INTO appoinments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                                PreparedStatement preparedStatement = connection.prepareStatement(quuery);
                                preparedStatement.setInt(1, pId);
                                preparedStatement.setInt(2, dId);
                                preparedStatement.setString(3, appointmentDate);
                                int rowsAffected = preparedStatement.executeUpdate();
                                if(rowsAffected>0){
                                    System.out.println("Appointment is Fixed");
                                }else {
                                    System.out.println("Failed to found the Appointmnet");
                                }
                            } else {
                                System.out.println("Doctor Not Avaialable At this Date");
                            }
                        } else {
                            System.out.println("Either Doctors or Patients doesn't exixts");
                        }
                        break;
                    default:
                        System.out.println("sorry for inconience");

                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            sqlException.printStackTrace();
        }
    }

    private static boolean checkAvailability(int dId, String appointmentDate,Connection connection) {
          String query = "SELECT COUNT(*) FROM appoinments WHERE doctor_Id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,dId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


















