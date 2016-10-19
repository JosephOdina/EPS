package eps;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class HomeViewController implements Initializable {
    
    private String fname;
    private String mname;
    private String lname;
    private String sex;
    private String sid;
    private String passw;
    private String rank;
    private String dept;
    private String fac;
    private String course;
    
    @FXML private Label facDepShow_btn;
    @FXML private Label logout_btn;
    @FXML private Label faculty_btn;
    @FXML private Label department_btn;
    @FXML private Label program_btn;
    @FXML private Label course_btn;
    @FXML private Label staff_btn;
    @FXML private Label student_btn;
    @FXML private Label result_btn;
    
    private ObservableList<String> slist;
    
    private DBCon dbcon;
    private Connection c;
    
    private sessionmanager session = new sessionmanager();
    
    /**
     * This method runs when the logout button is clicked
     * @param event
     */
    @FXML private void logoutAction (MouseEvent event) throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Login");
            stage.show();
            
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }catch(IOException e){
        }
    }
    
    /**
     * This method runs when the staff button is clicked
     * @throws IOException 
     * @throws java.lang.InterruptedException 
     * @throws java.sql.SQLException 
     */
    public void openStaffView() throws IOException, InterruptedException, SQLException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StaffView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        
        StaffViewController stv_c = fxmlLoader.<StaffViewController>getController();
        stv_c.setSid(getSid());
        stv_c.setFac(getFac());
        stv_c.setDep(getDept());
        
        //Set rank restriction for viewing staff
        switch(getRank()){
            case "Admin":
                try{
                    stv_c.setRank("Admin");
                    stv_c.setSQL("SELECT * FROM STAFF");
                }catch (Exception e){
                    System.out.println(e);
                }
            break;
            case "Dean":
                try{
                    stv_c.setRank("Dean");
                    stv_c.setSQL("SELECT * FROM STAFF WHERE FACULTY = '"+getFac()+"'");
                    stv_c.getPassword_col().setVisible(false);
                }catch (Exception e){
                    System.out.println(e);
                }
            break;
            case "HOD":
                try{
                    stv_c.setRank("HOD");
                    stv_c.setSQL("SELECT * FROM STAFF WHERE DEPARTMENT = '"+getDept()+"'");
                    stv_c.getPassword_col().setVisible(false);
                }catch(Exception e){
                    System.out.println(e);
                }
            break;
            case "Lecturer":
                try{
                    stv_c.setRank("Lecturer");
                }catch(Exception e){
                    System.out.println(e);
                }
            break;
        }
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Staff Operations");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    /**
     * This method runs when the student button is clicked
     * @throws IOException 
     */
    public void openStudentView() throws IOException{
        String st_rank = getRank();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentView.fxml"));
            Parent root = (Parent)fxmlLoader.load();

            StudentViewController controller = fxmlLoader.<StudentViewController>getController();
            
            controller.setRank(st_rank);
            controller.setFac(getFac());
            controller.setDep(getDept());
            controller.setSid(getSid());
            
            //set rank restriction for viewing students
            switch(rank){
                case "Admin":
                    try{
                        controller.setSQL("SELECT * FROM STUDENT");
                    }catch (Exception e){
                        System.out.println(e);
                    }
                break;
                case "Dean":
                    try{
                        controller.setSQL("SELECT * FROM STUDENT WHERE FACULTY = '"+getFac()+"'");
                    }catch (Exception e){
                        System.out.println(e);
                    }
                break;
                case "HOD":
                    try{
                        controller.setSQL("SELECT * FROM STUDENT WHERE DEPARTMENT = '"+getDept()+"'");
                    }catch(Exception e){
                        System.out.println(e);
                    }
                break;
                case "Lecturer":
                    try{
                        //restrict course registration
                        controller.getRegisterCourseContextMenu().setDisable(true);
                        //restrict student creation
                        controller.getAddstudent_btn().setDisable(true);
                        controller.getAddstudent_btn().setCursor(Cursor.DEFAULT);
                        controller.getAddstudent_btn().setStyle("-fx-background-color: #cccccc");
                        controller.getAddStudentContextMenu().setDisable(true);
                        //restrict student deletion
                        controller.getDeletestudent_btn().setDisable(true);
                        controller.getDeletestudent_btn().setCursor(Cursor.DEFAULT);
                        controller.getDeletestudent_btn().setStyle("-fx-background-color: #cccccc");
                        //restrict student updating
                        controller.getUpdatestudent_btn().setDisable(true);
                        controller.getUpdatestudent_btn().setCursor(Cursor.DEFAULT);
                        controller.getUpdatestudent_btn().setStyle("-fx-background-color: #cccccc");
                        controller.setSQL("SELECT * FROM STUDENT WHERE REG_NO IN (SELECT STUDENT FROM COURSE_REG WHERE COURSE_CODE IN (SELECT COURSE_CODE FROM COURSE WHERE TEACHER = '"+getSid()+"'))");
                    }catch(Exception e){
                        System.out.println(e);
                    }
                break;
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Student Operations");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (IOException e){
            System.out.println(e);
        }
    }
    
    /**
     * This method runs when the Faculty, Department, Program and Course buttons are clicked
     * @throws IOException 
     * @throws java.sql.SQLException 
     */
    public void openFDPCView() throws IOException, SQLException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FDPCView.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            
            System.out.println(getFac());
            
            FDPCViewController controller = fxmlLoader.<FDPCViewController>getController();
            
            switch(rank){
                case "Admin":
                    controller.getFlist().addAll("Art","Education","Management and Social Sciences","Natural and Applied Sciences");
                    controller.getFacultyList().getSelectionModel().select(0);
                    controller.getDlist().addAll("Architecture and Industrial Design","Biological Sciences","Chemical Sciences","Computer Science and Mathematics","Physical Sciences");
                    controller.getDepartmentList().getSelectionModel().select(3);
                    controller.getPlist().addAll("Computer Science","Mathematics","Statistics");
                    controller.getProgramList().getSelectionModel().select(0);
                break;
                case "Dean":
                    controller.getFlist().add(getFac());
                    if(getFac()=="Art"){
                        controller.getFacultyList().getSelectionModel().select(0);
                        controller.getDlist().addAll("English and Literary Studies","History, International Studies and Diplomacy","Music");
                    }
                break;
            }

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("FDPC Operations");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (IOException e){
        }
    }
    
    /**
     * This method runs when the result button is clicked
     * @throws IOException 
     */
    public void openResultView() throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ResultView.fxml"));
            Parent root = (Parent)fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Result Operations");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (Exception e){
            System.out.println(e);
        }
        
        
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /**
     * @return the logout_btn
     */
    public Label getLogout_btn() {
        return logout_btn;
    }

    /**
     * @param logout_btn the logout_btn to set
     */
    public void setLogout_btn(Label logout_btn) {
        this.logout_btn = logout_btn;
    }
    
    /**
     * @return the faculty_btn
     */
    public Label getFaculty_btn() {
        return faculty_btn;
    }

    /**
     * @param faculty_btn the faculty_btn to set
     */
    public void setFaculty_btn(Label faculty_btn) {
        this.faculty_btn = faculty_btn;
    }

    /**
     * @return the department_btn
     */
    public Label getDepartment_btn() {
        return department_btn;
    }

    /**
     * @param department_btn the department_btn to set
     */
    public void setDepartment_btn(Label department_btn) {
        this.department_btn = department_btn;
    }

    /**
     * @return the program_btn
     */
    public Label getProgram_btn() {
        return program_btn;
    }

    /**
     * @param program_btn the program_btn to set
     */
    public void setProgram_btn(Label program_btn) {
        this.program_btn = program_btn;
    }

    /**
     * @return the course_btn
     */
    public Label getCourse_btn() {
        return course_btn;
    }

    /**
     * @param course_btn the course_btn to set
     */
    public void setCourse_btn(Label course_btn) {
        this.course_btn = course_btn;
    }

    /**
     * @return the staff_btn
     */
    public Label getStaff_btn() {
        return staff_btn;
    }

    /**
     * @param staff_btn the staff_btn to set
     */
    public void setStaff_btn(Label staff_btn) {
        this.staff_btn = staff_btn;
    }

    /**
     * @return the student_btn
     */
    public Label getStudent_btn() {
        return student_btn;
    }

    /**
     * @param student_btn the student_btn to set
     */
    public void setStudent_btn(Label student_btn) {
        this.student_btn = student_btn;
    }

    /**
     * @return the result_btn
     */
    public Label getResult_btn() {
        return result_btn;
    }

    /**
     * @param result_btn the result_btn to set
     */
    public void setResult_btn(Label result_btn) {
        this.result_btn = result_btn;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the mname
     */
    public String getMname() {
        return mname;
    }

    /**
     * @param mname the mname to set
     */
    public void setMname(String mname) {
        this.mname = mname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return the passw
     */
    public String getPassw() {
        return passw;
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
     * @return the dbcon
     */
    public DBCon getDbcon() {
        return dbcon;
    }

    /**
     * @param dbcon the dbcon to set
     */
    public void setDbcon(DBCon dbcon) {
        this.dbcon = dbcon;
    }

    /**
     * @return the slist
     */
    public ObservableList<String> getSlist() {
        return slist;
    }

    /**
     * @param slist the slist to set
     */
    public void setSlist(ObservableList<String> slist) {
        this.slist = slist;
    }

    /**
     * @return the facDepShow_btn
     */
    public Label getFacDepShow_btn() {
        return facDepShow_btn;
    }

    /**
     * @param facDepShow_btn the facDepShow_btn to set
     */
    public void setFacDepShow_btn(Label facDepShow_btn) {
        this.facDepShow_btn = facDepShow_btn;
    }

    /**
     * @return the session
     */
    public sessionmanager getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(sessionmanager session) {
        this.session = session;
    }

    /**
     * @return the c
     */
    public Connection getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(Connection c) {
        this.c = c;
    }
    
}
