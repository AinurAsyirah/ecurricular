package com.heroku.java.CONTROLLER.sidebar;

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

import com.heroku.java.CONTROLLER.activity.ActivityController.ActivityBean;
import com.heroku.java.CONTROLLER.activity.ActivityController.ClubBean;
import com.heroku.java.CONTROLLER.activity.ActivityController.SukanBean;
import com.heroku.java.MODEL.student.StudentBean;

import jakarta.servlet.http.HttpSession;

import java.sql.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;

import java.util.List;

@Controller
public class SideBarStudentController {
    private final DataSource dataSource;

    @Autowired
    public SideBarStudentController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/dashboardStudent")
    public String index1(@RequestParam(name = "success", required = false) Boolean success, HttpSession session) {
        String studentIC = (String) session.getAttribute("studentIC");
        return "student/dashboardStudent";
    }

    @GetMapping("/edit_profile")
    public String editProfile(@RequestParam(name = "success", required = false) Boolean success, HttpSession session,
            Model model) {
        String studentIC = (String) session.getAttribute("studentIC");
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM public.student where studentic=?";
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, studentIC);
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String studentName = resultSet.getString("studentName");
                String studentEmail = resultSet.getString("studentEmail");
                String studentPhone = resultSet.getString("studentPhone");
                String studentDOB = resultSet.getString("studentDOB");
                String studentGender = resultSet.getString("studentGender");
                String studentClass = resultSet.getString("studentClass");
                String studentAddress = resultSet.getString("studentAddress");
                String studentPassword = resultSet.getString("studentPassword");

                StudentBean s = new StudentBean();

                s.setStudentIC(studentIC);
                s.setStudentName(studentName);
                s.setStudentEmail(studentEmail);
                s.setStudentPhone(studentPhone);
                s.setStudentDOB(studentDOB);
                s.setStudentGender(studentGender);
                s.setStudentClass(studentClass);
                s.setStudentAddress(studentAddress);
                s.setStudentPassword(studentPassword);

                model.addAttribute("s", s);

                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "student/profile/edit_profile";
    }


    @GetMapping("/registration")
    public String registration(@RequestParam(name = "success", required = false) Boolean success, HttpSession session, Model model) {
        String studentIC = (String) session.getAttribute("studentIC");
        // int activityid = (int) session.getAttribute("activityID");
        System.out.println("guestICNumber: " + studentIC);

        List <SukanBean> sukan= new ArrayList<SukanBean>();
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT a.activityid, a.activityname, s.sportinformation, s.sportquota " +
            "FROM activity a JOIN sport s ON a.activityid = s.activityid";
            final var statement = connection.prepareStatement(sql);
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                SukanBean s = new SukanBean();
                ActivityBean activity = new ActivityBean();
                activity.setActivityID(resultSet.getInt("activityID"));
               activity.setActivityName(resultSet.getString("activityName"));

               s.setActivity(activity);
               s.setInfoSukan(resultSet.getString("sportInformation"));
               s.setQuotaSukan(resultSet.getInt("sportQuota"));

               sukan.add(s);
                model.addAttribute("s", s);

                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List <ClubBean> club= new ArrayList<ClubBean>();
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT a.activityid, a.activityname, c.clubinformation, c.clubquota " +
            "FROM activity a JOIN club c ON a.activityid = c.activityid";
            final var statement = connection.prepareStatement(sql);
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ClubBean c = new ClubBean();
                ActivityBean activity = new ActivityBean();
                activity.setActivityID(resultSet.getInt("activityID"));
               activity.setActivityName(resultSet.getString("activityName"));

               c.setActivity(activity);
               c.setInfoClub(resultSet.getString("clubInformation"));
               c.setQuotaClub(resultSet.getInt("clubQuota"));

               club.add(c);

                connection.close();
                model.addAttribute("c", c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List <UnitBean> unit= new ArrayList<UnitBean>();
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT a.activityid, a.activityname, u.uniforminformation, u.uniformquota " +
            "FROM activity a JOIN uniform u ON a.activityid = u.activityid";
            final var statement = connection.prepareStatement(sql);
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UnitBean u = new UnitBean();
                ActivityBean activity = new ActivityBean();
                activity.setActivityID(resultSet.getInt("activityID"));
               activity.setActivityName(resultSet.getString("activityName"));

               u.setActivity(activity);
               u.setInfoUnit(resultSet.getString("uniformInformation"));
               u.setQuotaUnit(resultSet.getInt("uniformQuota"));

               unit.add(u);

                connection.close();
                model.addAttribute("u", u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "student/registration/registration";
    }

    @GetMapping("/semakan")
    public String semakan() {
        return "semakan";
    }

    //---------------------------BEANS------------------------------//
    interface Bean {
        int getActivityID();
        void setActivityID(int activityID);
    }
    
    //---------------------------SUKAN BEAN------------------------------//
    public class SukanBean implements Bean  {
	
        private String namaSukan;
        private String infoSukan;
        private int quotaSukan;
        private int activityID;

        public SukanBean() {
        }
        
        @Override
        public int getActivityID() {
            return activityID;
        }
        @Override
        public void setActivityID(int activityID) {
            this.activityID = activityID;
        }
    
        private ActivityBean activity;
    
        // Setter method for ActivityBean
        public void setActivity(ActivityBean activity) {
            this.activity = activity;
        }
    
        // Getter method for ActivityBean
        public ActivityBean getActivity() {
            return activity;
        }
          
        public String getNamaSukan() {
            return namaSukan;
        }
    
        public void setNamaSukan(String namaSukan) {
            this.namaSukan = namaSukan;
        }
    
        public String getInfoSukan() {
            return infoSukan;
        }
    
        public void setInfoSukan(String infoSukan) {
            this.infoSukan = infoSukan;
        }
    
        public int getQuotaSukan() {
            return quotaSukan;
        }
    
        public void setQuotaSukan(int quotaSukan) {
            this.quotaSukan = quotaSukan;
        }
    

    }

    //---------------------------UNIT BEAN------------------------------//
    public class UnitBean {
	
        private String namaUnit;
        private String infoUnit;
        private int quotaUnit;
        private int activityID;
        
        public int getActivityID() {
            return activityID;
        }
    
        public void setActivityID(int activityID) {
            this.activityID = activityID;
        }
    
        private ActivityBean activity;

        // Setter method for ActivityBean
        public void setActivity(ActivityBean activity) {
            this.activity = activity;
        }
    
        // Getter method for ActivityBean
        public ActivityBean getActivity() {
            return activity;
        }
          
        public String getNamaUnit() {
            return namaUnit;
        }
    
        public void setNamaUnit(String namaUnit) {
            this.namaUnit = namaUnit;
        }
    
        public String getInfoUnit() {
            return infoUnit;
        }
    
        public void setInfoUnit(String infoUnit) {
            this.infoUnit = infoUnit;
        }
    
        public int getQuotaUnit() {
            return quotaUnit;
        }
    
        public void setQuotaUnit(int quotaUnit) {
            this.quotaUnit = quotaUnit;
        }
    }

    //---------------------------CLUB BEAN------------------------------//
public class ClubBean {
	
	private String namaClub;
	private String infoClub;
	private int quotaClub;
	private int activityID;
	
	public int getActivityID() {
		return activityID;
	}

	public void setActivityID(int activityID) {
		this.activityID = activityID;
	}

	private ActivityBean activity;
	
	/*
	 * public int getActivityID() { // This getter is not necessary if it's already
	 * defined in ActivityBean return super.getActivityID(); // This calls the
	 * inherited getter from ActivityBean }
	 * 
	 * public void setActivityID(int activityID) { // This setter is not necessary
	 * if it's already defined in ActivityBean super.setActivityID(activityID); //
	 * This calls the inherited setter from ActivityBean }
	 */

	// Setter method for ActivityBean
    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    // Getter method for ActivityBean
    public ActivityBean getActivity() {
        return activity;
    }
	  
	public String getNamaClub() {
		return namaClub;
	}

	public void setNamaClub(String namaClub) {
		this.namaClub = namaClub;
	}

	public String getInfoClub() {
		return infoClub;
	}

	public void setInfoClub(String infoClub) {
		this.infoClub = infoClub;
	}

	public int getQuotaClub() {
		return quotaClub;
	}

	public void setQuotaClub(int quotaClub) {
		this.quotaClub = quotaClub;
	}
}

    

    //---------------------------ACTIVITY BEAN------------------------------//
    public class ActivityBean implements Bean{
	
        private String activityName;
        private String TeacherID;
        private  int activityID;

        public ActivityBean() {
        }
        
        @Override
        public int getActivityID() {
            return activityID;
        }
    
        public Integer getMaxActivityID() {
            int maxActivityID = 0;
		try (Connection con = dataSource.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT MAX(ACTIVITYID) FROM ACTIVITY")) {

			if (rs.next()) {
				maxActivityID = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// Handle exceptions as needed
		}
		return maxActivityID;
        }

        @Override
        public void setActivityID(int activityID) {
            this.activityID = activityID;
        }
    
        public String getTeacherID() {
            return TeacherID;
        }
    
        public void setTeacherID(String teacherID) {
            TeacherID = teacherID;
        }
    
        public String getActivityName() {
            return activityName;
        }
    
        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }
    
    }

}
