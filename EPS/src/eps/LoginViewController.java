package eps;

// the imports
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author Chukwuka Odina
 */
public class LoginViewController implements Initializable {
    @FXML
    private Label info_bar; // reference the Label item 'info_bar' on the Login scene
    
    @FXML
    private TextField login_field; // reference the TextField item login_field on the Login scene
    
    @FXML
    private Button login_btn; // reference the Button item login_btn on the Login scene
    
    @FXML
    private PasswordField pword_field; // reference the PasswordField item pword_field on the Login scene
    
    private String fname; //User First Name container
    private String mname; //User Middle Name container
    private String lname; //User Last Name container
    private String sex; //User Sex container
    private String sid; //User Staff ID container
    private String passw; //User Password container
    private String rank; //User Rank container
    private String dept; //User Department container
    private String fac; //User Faculty container
    private String course; //User Course container
    private ResultSet rs; // declare a ResultSet object that holds the result of the SQL query
    private Statement loginStmt; // declare a statement object
    private String loginSQL; // declare the SQL query that will be used on the database
    
    /**
     * This method runs when the login button is clicked
     * @param event
     * @throws SQLException 
     * @throws java.lang.InterruptedException 
     * @throws java.io.IOException 
     */
    public void handleLoginButtonAction(ActionEvent event) throws SQLException, InterruptedException, IOException {
        try{
            DBCon dBCon = new DBCon(); // creates a new DBCon object
        
            setLoginStmt(dBCon.getStmt()); // initialize the loginStmt with the getStmt method from the dBCon object        
            String loginSID = getLogin_field().getText(); // get staff id from loginSID field
            String loginPass = getPword_field().getText(); // get password from password field
        
            setLoginSQL("SELECT * FROM STAFF WHERE STAFF_ID = '"+loginSID+"' AND PASSWORD = '"+loginPass+"'"); // statement to query the staff table in the database for row containing login details
            setRs(getLoginStmt().executeQuery(getLoginSQL())); // stores the query result in the rs object
        
            // get table details of the result in rs
            while(getRs().next()){
                setFname(getRs().getString(1)); // Get the first name of the user
                setMname(getRs().getString(2)); // Get the middle name of the user
                setLname(getRs().getString(3)); // Get the last name of the user
                setDept(getRs().getString(5)); // Get the department of the user
                setFac(getRs().getString(6)); // Get the faculty of the user
                setRank(getRs().getString(7)); // Get the rank of the user
                setSid(getRs().getString(8)); // Get the staff id of the user
                setPassw(getRs().getString(9)); // Get the password of the user
            }
            // compare the login details with the database details in rs
            if(loginSID.equals(getSid()) && loginPass.equals(getPassw())){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeView.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                
                HomeViewController hvc = fxmlLoader.<HomeViewController>getController();
                hvc.getLogout_btn().setText(getFname()+" "+getLname()+" (Logout)");
                Label fd_show = hvc.getFacDepShow_btn();
                if (getDept() != null) {
                    fd_show.setText(getFac() + "\n" + getDept());
                } else {
                    fd_show.setText(getFac());
                }
                
                hvc.setFname(getFname());
                hvc.setMname(getMname());
                hvc.setLname(getLname());
                hvc.setSex(getSex());
                hvc.setSid(getSid());
                hvc.setRank(getRank());
                hvc.setPassw(getPassw());
                hvc.setDept(getDept());
                hvc.setFac(getFac());
                hvc.setCourse(getCourse());
                
                Label faculty_btn = hvc.getFaculty_btn();
                Label department_btn = hvc.getDepartment_btn();
                Label program_btn = hvc.getProgram_btn();
                Label course_btn = hvc.getCourse_btn();
                Label staff_btn = hvc.getStaff_btn();
                Label student_btn = hvc.getStudent_btn();
                Label result_btn = hvc.getResult_btn();
                
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                
                // Open home scene based on staff rank
                switch(getRank()){
                    case "Admin": // Open Admin Home scene
                        System.out.println(getFac());
                        System.out.println("You are an admin");
                        try{
                            stage.setTitle("Admin Home");
                            stage.show();
                
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                        }catch(Exception e){
                        }
                    break;
                    case "Dean": // Open Dean Home scene
                        System.out.println("You are a dean");
                        try{
                            faculty_btn.setText("My Faculty");
                            stage.setTitle("Dean Home");
                            stage.show();
                
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                        }catch(Exception e){
                        }
                    break;
                    case "HOD": // Open HOD Home scene
                        System.out.println("You are an HOD");
                        try{
                            faculty_btn.disableProperty();
                            faculty_btn.setCursor(Cursor.DEFAULT);
                            faculty_btn.setOnMouseClicked(null);
                            faculty_btn.setStyle("-fx-background-color: #cccccc");
                            department_btn.setText("My Department");
                            stage.setTitle("HOD Home");
                            stage.show();
                
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                        }catch(Exception e){
                        }
                    break;
                    case "Lecturer": // Open Lecturer Home scene
                        System.out.println("You are a lecturer");
                        try{
                            faculty_btn.disableProperty();
                            faculty_btn.setCursor(Cursor.DEFAULT);
                            faculty_btn.setOnMouseClicked(null);
                            faculty_btn.setStyle("-fx-background-color: #cccccc");
                            
                            department_btn.disableProperty();
                            department_btn.setCursor(Cursor.DEFAULT);
                            department_btn.setOnMouseClicked(null);
                            department_btn.setStyle("-fx-background-color: #cccccc");
                            
                            program_btn.disableProperty();
                            program_btn.setCursor(Cursor.DEFAULT);
                            program_btn.setOnMouseClicked(null);
                            program_btn.setStyle("-fx-background-color: #cccccc");
                            
                            course_btn.disableProperty();
                            course_btn.setCursor(Cursor.DEFAULT);
                            course_btn.setOnMouseClicked(null);
                            course_btn.setStyle("-fx-background-color: #cccccc");
                            
                            staff_btn.disableProperty();
                            staff_btn.setCursor(Cursor.DEFAULT);
                            staff_btn.setOnMouseClicked(null);
                            staff_btn.setStyle("-fx-background-color: #cccccc");
                            
                            student_btn.setText("My Students");
                            
                            stage.setTitle("Lecturer Home");
                            stage.show();
                
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                        }catch(Exception e){
                        }
                    break;
                }
            }else if((loginSID.equals(getSid()) && !loginPass.equals(getPassw())) | (loginSID.equals(getSid()) && loginPass.isEmpty())){
                getInfo_bar().setText("Enter valid password!");
                getPword_field().setText("");
            }else if((!loginSID.equals(getSid()) && loginPass.equals(getPassw())) | (loginSID.isEmpty() && loginPass.equals(getPassw()))){
                getInfo_bar().setText("Enter valid Staff ID");
                getLogin_field().setText("");
            }else if((!loginSID.equals(getSid()) && !loginPass.equals(getPassw())) | (loginSID.isEmpty() && loginPass.isEmpty())){
                getInfo_bar().setText("Enter valid Staff ID and password");
                getLogin_field().setText("");
                getPword_field().setText("");
            }
        }catch (SQLException connectivityError){
            // For a datbase connection error
            getInfo_bar().setText("Network connection error. If problem persists, contact the administrator.");
            System.out.println(connectivityError);
        }
        
    }

    /**
     * @return the info_bar
     */
    public Label getInfo_bar() {
        return info_bar;
    }

    /**
     * @return the login_field
     */
    public TextField getLogin_field() {
        return login_field;
    }

    /**
     * @return the login_btn
     */
    public Button getLogin_btn() {
        return login_btn;
    }

    /**
     * @return the pword_field
     */
    public PasswordField getPword_field() {
        return pword_field;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @return the mname
     */
    public String getMname() {
        return mname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @return the passw
     */
    public String getPassw() {
        return passw;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * @param info_bar the info_bar to set
     */
    public void setInfo_bar(Label info_bar) {
        this.info_bar = info_bar;
    }

    /**
     * @param login_field the login_field to set
     */
    public void setLogin_field(TextField login_field) {
        this.login_field = login_field;
    }

    /**
     * @param login_btn the login_btn to set
     */
    public void setLogin_btn(Button login_btn) {
        this.login_btn = login_btn;
    }

    /**
     * @param pword_field the pword_field to set
     */
    public void setPword_field(PasswordField pword_field) {
        this.pword_field = pword_field;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @param mname the mname to set
     */
    public void setMname(String mname) {
        this.mname = mname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @param passw the passw to set
     */
    public void setPassw(String passw) {
        this.passw = passw;
    }

    /**
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * @return the dept
     */
    public String getDept() {
        return dept;
    }

    /**
     * @param dept the dept to set
     */
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**
     * @return the fac
     */
    public String getFac() {
        return fac;
    }

    /**
     * @param fac the fac to set
     */
    public void setFac(String fac) {
        this.fac = fac;
    }

    /**
     * @return the course
     */
    public String getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * @return the rs
     */
    public ResultSet getRs() {
        return rs;
    }

    /**
     * @param rs the rs to set
     */
    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    /**
     * @return the loginStmt
     */
    public Statement getLoginStmt() {
        return loginStmt;
    }

    /**
     * @param loginStmt the loginStmt to set
     */
    public void setLoginStmt(Statement loginStmt) {
        this.loginStmt = loginStmt;
    }

    /**
     * @return the loginSQL
     */
    public String getLoginSQL() {
        return loginSQL;
    }

    /**
     * @param loginSQL the loginSQL to set
     */
    public void setLoginSQL(String loginSQL) {
        this.loginSQL = loginSQL;
    }
    
    /**
     * 
     * @return the sex
     */
    public String getSex(){
        return sex;
    }
    
    /**
     * 
     * @param sex the sex to set
     */
    private void setSex(String sex) {
        this.sex = sex;
    }
}
