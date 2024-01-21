package com.heroku.java.CONTROLLER.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.MODEL.student.StudentBean;

import jakarta.servlet.http.HttpSession;

import java.sql.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;

import java.util.List;

@Controller
public class LoginStudentController {
    private final DataSource dataSource;

    @Autowired
    public LoginStudentController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/signin")
    public String signin() {
        return "student/sign-in/signin";
    }

    @PostMapping("/signin")
    public String LoginStudent(HttpSession session, @RequestParam("studentIC") String studentIC, @RequestParam("studentPassword") String studentPassword, StudentBean s, Model model) {

        try {
            // String returnPage = null;
            Connection connection = dataSource.getConnection();

            String sql = "SELECT * FROM public.student WHERE studentic=? AND studentpassword=?";
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, studentIC);
            statement.setString(2, studentPassword);

            final var resultSet = statement.executeQuery();

            System.out.println("student ic : " + studentIC);
            System.out.println("student pass : " + studentPassword);

            if (resultSet.next()) {

                // String guestICNumber = resultSet.getString("guestICNumber");
                // String teacherName = resultSet.getString("guestname");
                String username = resultSet.getString("studentIC");
                String password = resultSet.getString("studentPassword");

                System.out.println(username);
                // if they're admin
                // System.out.println("Email : " + guestEmail.equals(email) + " | " + email);
                // System.out.println("Password status : " + guestPassword.equals(password));

                if (username.equals(studentIC) && password.equals(studentPassword)) {

                    session.setAttribute("studentIC", studentIC);
                    session.setAttribute("studentPassword", studentPassword);

                    return "redirect:/dashboardStudent/" + studentIC ;
                }
            }

            connection.close();
            return "redirect:/signin?invalidUsername&Password";

        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();

            return "redirect:/signin?error";

        } catch (Exception e) {
            System.out.println("E message : " + e.getMessage());
            return "redirect:/signin?error";
        }
    }

    // @GetMapping("/dashboardStudent/{studentIC}")
    // public String dashboardStudent(StudentBean s, Model model, HttpSession session) {

    //     String studentIC = (String) session.getAttribute("studentIC");
    //     String studentPassword = (String) session.getAttribute("studentPassword");
    //     String studentname = (String) session.getAttribute("studentName");
    
    //     // Set values in the StudentBean object
    //     s.setStudentIC(studentIC);
    //     s.setStudentPassword(studentPassword);
    //     s.setStudentName(studentname);

    
    //     // Add the StudentBean object to the model
    //     model.addAttribute("student", s);
    
    //     return "student/dashboardStudent";
    // }
}
