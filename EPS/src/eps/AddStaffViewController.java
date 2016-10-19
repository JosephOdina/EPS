package eps;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class AddStaffViewController implements Initializable {
    
    @FXML private TextField firstName; //first name textfield from scene
    @FXML private TextField middleName; //middle name textfield from scene
    @FXML private TextField lastName; //last name textfield from scene
    @FXML private TextField staffID; //staff id textfield from scene
    @FXML private TextField password; //password textfield from scene
    @FXML private ComboBox<String> facultyCombo; //faculty combobox from scene
    @FXML private ComboBox<String> departmentCombo; //department combobox from scene
    @FXML private ComboBox<String> courseCombo; //course combobox from scene
    @FXML private ComboBox<String> rankCombo; //rank combobox from scene
    @FXML private Button createStaffButton; //create button from scene
    @FXML private RadioButton maleRadiobutton; //male radiobutton from scene
    @FXML private RadioButton femaleRadioButton; //female radiobutton from scene
    @FXML private Label info_bar; //info_bar from scene
    
    private DBCon dbcon; //DBCon object
    private Connection c; //Connection object
    private String updateSQL; //sql to add created staff to database
    private String facultySQL; //"SELECT * FROM FACULTY"
    private ResultSet getFacultyRs; //resultset for facultySQL
    private String fac_selected; //holds the selected item in faculty combobox
    private String getFIDfromFac; //"SELECT FACULTY_ID FROM FACULTY WHERE FACULTY = '"+getFac_selected()+"'"
    private int fid; //holds the faculty id gotten from getFIDfromFac
    private ResultSet getFIDRs; //resultset for getFIDfromFac
    private String departmentSQL; //"SELECT * FROM DEPARTMENT WHERE FACULTY = "+getFid()
    private ResultSet getDepartmentRs; //resultset for departmentSQL
    private String dep_selected; //holds the selected item in department combobox
    private String getCourseSQL; //"SELECT * FROM COURSE WHERE DEPARTMENT = '"+getDep_selected()+"'"
    private ResultSet getCourseRs; //resultset for getCourseSQL
    private String course_selected; //holds the selected item in course combobox
    private String rankSQL; //SELECT * FROM RANK
    private ResultSet getRankRs; //resultset for rankSQL
    private String rank_selected; //holds the selected item in rank combobox
    
    private final ObservableList<String> flist = FXCollections.observableArrayList(); //holds list of faculty names gotten from db
    private final ObservableList<String> dlist = FXCollections.observableArrayList(); //holds list of department names gotten from db
    private ObservableList<String> clist; //holds list of course names gotten from db
    private final ObservableList<String> rlist = FXCollections.observableArrayList(); //holds list of ranks gotten from db
    
    private String response;
    
    /**
     * submit input to db
     * @param event
     * @throws SQLException
     * @throws SQLTimeoutException
     * @throws InterruptedException 
     */
    public void handleCreateStaffButtonAction(ActionEvent event) throws SQLException, SQLTimeoutException, InterruptedException{
        PreparedStatement ps;
        try{
            getC().setAutoCommit(false);
            String fname = getFirstName().getText();
            String mname = getMiddleName().getText();
            String lname = getLastName().getText();
            String gender = "Male";
            if(getFemaleRadioButton().isSelected()){
                gender = "Female";
            }
            String fac = getFacultyCombo().getValue();
            String dep = getDepartmentCombo().getValue();
            String rank = getRankCombo().getValue();
            String sid = getStaffID().getText();
            String pword = getPassword().getText();
            
            ps = getC().prepareStatement(getUpdateSQL());
            ps.setString(1, fname);
            ps.setString(2, mname);
            ps.setString(3, lname);
            ps.setString(4, gender);
            ps.setString(5, dep);
            ps.setString(6, fac);
            ps.setString(7, rank);
            ps.setString(8, sid);
            ps.setString(9, pword);
            int ro = ps.executeUpdate();
            getC().commit();
            
            if(ro>0){
                getInfo_bar().setText(fname+" "+" "+lname+" "+getResponse());
                getFirstName().clear();
                getMiddleName().clear();
                getLastName().clear();
                getFacultyCombo().getSelectionModel().clearSelection();
                getDepartmentCombo().getSelectionModel().clearSelection();
                getRankCombo().getSelectionModel().clearSelection();
                getStaffID().clear();
                getPassword().clear();
            }
        }catch(SQLException sex){
            System.out.println(sex);
            getInfo_bar().setText("Error creating staff. If error persists, contact Administrator.");
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try{
            dbcon = new DBCon();
            setC(dbcon.getCon());
        }catch(IllegalStateException | SQLException | NullPointerException ex){
            //System.out.println(ex);
            Logger.getLogger(AddStaffViewController.class.getName()).log(Level.ALL, null, ex);
        }
    }
    
    /**
     * Populate Faculty ComboBox from Faculty table in db.
     * Runs on initialization
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     * @throws SQLException 
     */
    public void fillFacultyCombo() throws InterruptedException, IOException, SQLException, NullPointerException{
        try{
            setGetFacultyRs(getC().createStatement().executeQuery(getFacultySQL()));
            while(getFacultyRs.next()){
                getFlist().add(getFacultyRs.getString("FACULTY"));
            }
            getFacultyCombo().setItems(getFlist());
        }catch(Exception ex){
            System.out.println("Error building Faculty data");
            System.out.println(ex);
            Logger.getLogger(AddStaffViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Populate Department ComboBox from Department table in db
     * based on selected Faculty in Faculty ComboBox
     * @throws SQLException 
     */
    public void fillDepartmentCombo() throws SQLException{
        setFac_selected(getFacultyCombo().getSelectionModel().getSelectedItem());
        try{
            setGetDepartmentRs(getC().createStatement().executeQuery("SELECT DEPARTMENT FROM DEPARTMENT WHERE FACULTY = '"+getFac_selected()+"'"));
            while(getDepartmentRs.next()){
                getDlist().add(getDepartmentRs.getString("DEPARTMENT"));
            }
            getDepartmentCombo().setItems(getDlist());
        }catch(Exception ex){
            System.out.println("Error building Department data");
            System.out.println(ex);
        }
    }

    /**
     * @return the firstName
     */
    public TextField getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(TextField firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the middleName
     */
    public TextField getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(TextField middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the lastName
     */
    public TextField getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(TextField lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the staffID
     */
    public TextField getStaffID() {
        return staffID;
    }

    /**
     * @param staffID the staffID to set
     */
    public void setStaffID(TextField staffID) {
        this.staffID = staffID;
    }

    /**
     * @return the password
     */
    public TextField getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(TextField password) {
        this.password = password;
    }

    /**
     * @return the facultyCombo
     */
    public ComboBox<String> getFacultyCombo() {
        return facultyCombo;
    }

    /**
     * @param facultyCombo the facultyCombo to set
     */
    public void setFacultyCombo(ComboBox<String> facultyCombo) {
        this.facultyCombo = facultyCombo;
    }

    /**
     * @return the departmentCombo
     */
    public ComboBox<String> getDepartmentCombo() {
        return departmentCombo;
    }

    /**
     * @param departmentCombo the departmentCombo to set
     */
    public void setDepartmentCombo(ComboBox<String> departmentCombo) {
        this.departmentCombo = departmentCombo;
    }

    /**
     * @return the courseCombo
     */
    public ComboBox<String> getCourseCombo() {
        return courseCombo;
    }

    /**
     * @param courseCombo the courseCombo to set
     */
    public void setCourseCombo(ComboBox<String> courseCombo) {
        this.courseCombo = courseCombo;
    }

    /**
     * @return the rankCombo
     */
    public ComboBox<String> getRankCombo() {
        return rankCombo;
    }

    /**
     * @param rankCombo the rankCombo to set
     */
    public void setRankCombo(ComboBox<String> rankCombo) {
        this.rankCombo = rankCombo;
    }

    /**
     * @return the createStaffButton
     */
    public Button getCreateStaffButton() {
        return createStaffButton;
    }

    /**
     * @param createStaffButton the createStaffButton to set
     */
    public void setCreateStaffButton(Button createStaffButton) {
        this.createStaffButton = createStaffButton;
    }

    /**
     * @return the maleRadiobutton
     */
    public RadioButton getMaleRadiobutton() {
        return maleRadiobutton;
    }

    /**
     * @param maleRadiobutton the maleRadiobutton to set
     */
    public void setMaleRadiobutton(RadioButton maleRadiobutton) {
        this.maleRadiobutton = maleRadiobutton;
    }

    /**
     * @return the femaleRadioButton
     */
    public RadioButton getFemaleRadioButton() {
        return femaleRadioButton;
    }

    /**
     * @param femaleRadioButton the femaleRadioButton to set
     */
    public void setFemaleRadioButton(RadioButton femaleRadioButton) {
        this.femaleRadioButton = femaleRadioButton;
    }

    /**
     * @return the updateSQL
     */
    public String getUpdateSQL() {
        return updateSQL;
    }

    /**
     * @param updateSQL the updateSQL to set
     */
    public void setUpdateSQL(String updateSQL) {
        this.updateSQL = updateSQL;
    }

    /**
     * @return the info_bar
     */
    public Label getInfo_bar() {
        return info_bar;
    }

    /**
     * @param info_bar the info_bar to set
     */
    public void setInfo_bar(Label info_bar) {
        this.info_bar = info_bar;
    }

    /**
     * @return the flist
     */
    public ObservableList<String> getFlist() {
        return flist;
    }

//    /**
//     * @param flist the flist to set
//     */
//    public void setFlist(ObservableList<String> flist) {
//        this.flist = flist;
//    }

    /**
     * @return the dlist
     */
    public ObservableList<String> getDlist() {
        return dlist;
    }

//    /**
//     * @param dlist the dlist to set
//     */
//    public void setDlist(ObservableList<String> dlist) {
//        this.dlist = dlist;
//    }

    /**
     * @return the clist
     */
    public ObservableList<String> getClist() {
        return clist;
    }

    /**
     * @param clist the clist to set
     */
    public void setClist(ObservableList<String> clist) {
        this.clist = clist;
    }

    /**
     * @return the facultySQL
     */
    public String getFacultySQL() {
        return facultySQL;
    }

    /**
     * @param facultySQL the facultySQL to set
     */
    public void setFacultySQL(String facultySQL) {
        this.facultySQL = facultySQL;
    }

    /**
     * @return the getFacultyRs
     */
    public ResultSet getGetFacultyRs() {
        return getFacultyRs;
    }

    /**
     * @param getFacultyRs the getFacultyRs to set
     */
    public void setGetFacultyRs(ResultSet getFacultyRs) {
        this.getFacultyRs = getFacultyRs;
    }

    /**
     * @return the departmentSQL
     */
    public String getDepartmentSQL() {
        return departmentSQL;
    }

    /**
     * @param departmentSQL the departmentSQL to set
     */
    public void setDepartmentSQL(String departmentSQL) {
        this.departmentSQL = departmentSQL;
    }

    /**
     * @return the getDepartmentRs
     */
    public ResultSet getGetDepartmentRs() {
        return getDepartmentRs;
    }

    /**
     * @param getDepartmentRs the getDepartmentRs to set
     */
    public void setGetDepartmentRs(ResultSet getDepartmentRs) {
        this.getDepartmentRs = getDepartmentRs;
    }

    /**
     * @return the getFIDfromFac
     */
    public String getGetFIDfromFac() {
        return getFIDfromFac;
    }

    /**
     * @param getFIDfromFac the getFIDfromFac to set
     */
    public void setGetFIDfromFac(String getFIDfromFac) {
        this.getFIDfromFac = getFIDfromFac;
    }

    /**
     * @return the getFIDRs
     */
    public ResultSet getGetFIDRs() {
        return getFIDRs;
    }

    /**
     * @param getFIDRs the getFIDRs to set
     */
    public void setGetFIDRs(ResultSet getFIDRs) {
        this.getFIDRs = getFIDRs;
    }

    /**
     * @return the fid
     */
    public int getFid() {
        return fid;
    }

    /**
     * @param fid the fid to set
     */
    public void setFid(int fid) {
        this.fid = fid;
    }

    /**
     * @return the fac_selected
     */
    public String getFac_selected() {
        return fac_selected;
    }

    /**
     * @param fac_selected the fac_selected to set
     */
    public void setFac_selected(String fac_selected) {
        this.fac_selected = fac_selected;
    }

    /**
     * @return the dep_selected
     */
    public String getDep_selected() {
        return dep_selected;
    }

    /**
     * @param dep_selected the dep_selected to set
     */
    public void setDep_selected(String dep_selected) {
        this.dep_selected = dep_selected;
    }

    /**
     * @return the getCourseSQL
     */
    public String getGetCourseSQL() {
        return getCourseSQL;
    }

    /**
     * @param getCourseSQL the getCourseSQL to set
     */
    public void setGetCourseSQL(String getCourseSQL) {
        this.getCourseSQL = getCourseSQL;
    }

    /**
     * @return the getCourseRs
     */
    public ResultSet getGetCourseRs() {
        return getCourseRs;
    }

    /**
     * @param getCourseRs the getCourseRs to set
     */
    public void setGetCourseRs(ResultSet getCourseRs) {
        this.getCourseRs = getCourseRs;
    }

    /**
     * @return the rlist
     */
    public ObservableList<String> getRlist() {
        return rlist;
    }

//    /**
//     * @param rlist the rlist to set
//     */
//    public void setRlist(ObservableList<String> rlist) {
//        this.rlist = rlist;
//    }

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

    /**
     * @return the rankSQL
     */
    public String getRankSQL() {
        return rankSQL;
    }

    /**
     * @param rankSQL the rankSQL to set
     */
    public void setRankSQL(String rankSQL) {
        this.rankSQL = rankSQL;
    }

    /**
     * @return the getRankRs
     */
    public ResultSet getGetRankRs() {
        return getRankRs;
    }

    /**
     * @param getRankRs the getRankRs to set
     */
    public void setGetRankRs(ResultSet getRankRs) {
        this.getRankRs = getRankRs;
    }

    /**
     * @return the rank_selected
     */
    public String getRank_selected() {
        return rank_selected;
    }

    /**
     * @param rank_selected the rank_selected to set
     */
    public void setRank_selected(String rank_selected) {
        this.rank_selected = rank_selected;
    }

    /**
     * @return the course_selected
     */
    public String getCourse_selected() {
        return course_selected;
    }

    /**
     * @param course_selected the course_selected to set
     */
    public void setCourse_selected(String course_selected) {
        this.course_selected = course_selected;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }
    
}
