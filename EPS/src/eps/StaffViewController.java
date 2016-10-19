package eps;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class StaffViewController implements Initializable {
    
    private Label deletestaff_btn;
    private Label viewstaff_btn;
    private Label addstaff_btn;
    private Label updatestaff_btn;
    
    @FXML private ResourceBundle resources;

    @FXML private URL location;
    
    @FXML private TableView<Staff> staffTable;
    
    @FXML private TableColumn<Staff, String> fname_col;
    @FXML private TableColumn<Staff, String> mname_col;
    @FXML private TableColumn<Staff, String> lname_col;
    @FXML private TableColumn<Staff, String> sex_col;
    @FXML private TableColumn<Staff, String> course_col;
    @FXML private TableColumn<Staff, String> dept_col;
    @FXML private TableColumn<Staff, String> fac_col;
    @FXML private TableColumn<Staff, String> rank_col;
    @FXML private TableColumn<Staff, String> staffid_col;
    @FXML private TableColumn<Staff, String> password_col;
    
    private String staffid;
    private String rank;
    private String fac;
    private String dep;
    private String sid;
    
    private DBCon dbcon;
    private Connection c;
    
    private ObservableList<Staff> data;
    private ObservableList<String> dlist;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        getStaffTable().setPlaceholder(new Label("Click \"View Staff\""));
        setStaffTableCellValueFactory();
        
        
    }
    
    /**
     * Set cell value factory for staff table
     */
    public void setStaffTableCellValueFactory(){
        try{
            getFname_col().setCellValueFactory(new PropertyValueFactory<>("firstName"));
            getMname_col().setCellValueFactory(new PropertyValueFactory<>("middleName"));
            getLname_col().setCellValueFactory(new PropertyValueFactory<>("lastName"));
            getSex_col().setCellValueFactory(new PropertyValueFactory<>("sex"));
            getCourse_col().setCellValueFactory(new PropertyValueFactory<>("course"));
            getDept_col().setCellValueFactory(new PropertyValueFactory<>("department"));
            getFac_col().setCellValueFactory(new PropertyValueFactory<>("faculty"));
            getRank_col().setCellValueFactory(new PropertyValueFactory<>("rank"));
            getStaffid_col().setCellValueFactory(new PropertyValueFactory<>("staffID"));
            getPassword_col().setCellValueFactory(new PropertyValueFactory<>("password"));
        
            dbcon = new DBCon();
            c = dbcon.getCon();
            
            buildData();
        }catch(IllegalStateException | SQLException e){
            System.out.println(e);
        }
    }
    
    /**
     * Build staff table data
     * @throws SQLException 
     */
    private String SQL;
    public void buildData() throws SQLException{
        setData(FXCollections.observableArrayList());
        try{
            ResultSet rs = c.createStatement().executeQuery(getSQL());
            while(rs.next()){
                ResultSet rss = c.createStatement().executeQuery("select course from lec_course where lecturer = '"+rs.getString(8)+"'");
                Staff staff = new Staff();
                staff.setFname(rs.getString(1));
                staff.setMname(rs.getString(2));
                staff.setLname(rs.getString(3));
                staff.setSex(rs.getString(4));
                List<String> courses = new ArrayList<>();
                while(rss.next()){
                    courses.add(rss.getString(1));
                    String cc = courses.toString();
                    staff.setCourse(cc.substring(1, cc.length()-1));
                }
                staff.setDepartment(rs.getString(5));
                staff.setFaculty(rs.getString(6));
                staff.setRank(rs.getString(7));
                staff.setStaffID(rs.getString(8));
                staff.setPassword(rs.getString(9));

                getData().add(staff);
                getStaffTable().setItems(getData());
            }
        }catch(Exception ex){
            System.out.println("Error building staff data.");
            System.out.println(ex);
        }
    }
    
    /**
     * Opens new staff creation scene
     * @throws InterruptedException 
     * @throws java.io.IOException 
     * @throws java.sql.SQLException 
     */
    public void addStaff() throws InterruptedException, IOException, SQLException, NullPointerException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddStaffView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        
        String r = getRank();

        AddStaffViewController astv_c = fxmlLoader.<AddStaffViewController>getController();
        
        astv_c.setUpdateSQL("INSERT INTO STAFF ("
                            + "FIRST_NAME,"
                            + "MIDDLE_NAME,"
                            + "LAST_NAME,"
                            + "SEX,"
                            + "DEPARTMENT,"
                            + "FACULTY,"
                            + "RANK,"
                            + "STAFF_ID,"
                            + "PASSWORD) VALUES (?,?,?,?,?,?,?,?,?)"
            );
        astv_c.setResponse("has been added to staff.");
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Add Staff");
        
        // Set rank restrictions for creating staff
        switch(r){
            case "Admin":
                try{
                    astv_c.setFacultySQL("SELECT FACULTY FROM FACULTY");
                    astv_c.fillFacultyCombo();
                    astv_c.setDepartmentSQL("SELECT DEPARTMENT FROM DEPARTMENT WHERE FACULTY = '");
                    astv_c.fillDepartmentCombo();
                    astv_c.getRlist().addAll("Admin","Dean","HOD","Lecturer");
                    astv_c.getRankCombo().setItems(astv_c.getRlist());
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }catch(InterruptedException | IOException | SQLException | NullPointerException e){
                    System.out.println(e);
                }
            break;
            case "Dean":
                try{
                    astv_c.setFacultySQL("SELECT FACULTY FROM STAFF WHERE STAFF_ID = '"+getSid()+"'");
                    astv_c.fillFacultyCombo();
                    astv_c.setDepartmentSQL("SELECT DEPARTMENT FROM DEPARTMENT WHERE FACULTY = '");
                    astv_c.fillDepartmentCombo();
                    astv_c.getRlist().addAll("HOD","Lecturer");
                    astv_c.getRankCombo().setItems(astv_c.getRlist());
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }catch(InterruptedException | IOException | SQLException | NullPointerException e){
                    System.out.println(e);
                }
            break;
            case "HOD":
                try{
                    astv_c.setFacultySQL("SELECT FACULTY FROM STAFF WHERE STAFF_ID = '"+getSid()+"'");
                    astv_c.fillFacultyCombo();
                    astv_c.setDepartmentSQL("SELECT DEPARTMENT FROM STAFF WHERE STAFF_ID = '"+getSid()+"' AND FACULTY = '");
                    astv_c.fillDepartmentCombo();
                    astv_c.getRlist().addAll("Lecturer");
                    astv_c.getRankCombo().setItems(astv_c.getRlist());
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }catch(InterruptedException | IOException | SQLException | NullPointerException e){
                    System.out.println(e);
                }
        }
    }
    
    /**
     * Delete selected row
     * @throws java.sql.SQLException
     */
    public void deleteRow() throws SQLException{
        Staff s = getStaffTable().getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to delete "+s.getFirstName()+" "+s.getLastName()+"?\nOperation cannot be undone!", ButtonType.YES, ButtonType.NO);
        alert.setTitle("CONFIRM DELETION");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            if(s!=null){
                c.createStatement().executeUpdate("DELETE FROM STAFF WHERE STAFF_ID = '"+s.getStaffID()+"'");
                getData().remove(s);
            }
        }
        System.out.println("Delete staff row");
    }
    
    /**
     * Update staff information
     * @throws java.io.IOException
     */
    public void updateStaff() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddStaffView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        
        String r = getRank();
        
        AddStaffViewController astv_c = fxmlLoader.<AddStaffViewController>getController();
        astv_c.setResponse("has been updated.");
        astv_c.getCreateStaffButton().setText("UPDATE");
        
        Staff s = getStaffTable().getSelectionModel().getSelectedItem();
        astv_c.setUpdateSQL("UPDATE STAFF SET "
                            + "FIRST_NAME = ?,"
                            + "MIDDLE_NAME = ?,"
                            + "LAST_NAME = ?,"
                            + "SEX = ?,"
                            + "DEPARTMENT = ?,"
                            + "FACULTY = ?,"
                            + "RANK = ?,"
                            + "STAFF_ID = ?,"
                            + "PASSWORD = ? "
                            + "WHERE STAFF_ID = '"+s.getStaffID()+"'");
        
        if(s!=null){
            astv_c.getFirstName().setText(s.getFirstName());
            astv_c.getMiddleName().setText(s.getMiddleName());
            astv_c.getLastName().setText(s.getLastName());
            astv_c.getStaffID().setText(s.getStaffID());
            astv_c.getPassword().setText(s.getPassword());
        }
        
        switch(r){
            case "Admin":
                try{
                    astv_c.setFacultySQL("SELECT * FROM FACULTY");
                    astv_c.fillFacultyCombo();
                    astv_c.setDepartmentSQL("SELECT * FROM DEPARTMENT WHERE FACULTY = '");
                    astv_c.fillDepartmentCombo();
                    astv_c.getRlist().addAll("Admin","Dean","HOD","Lecturer");
                    astv_c.getRankCombo().setItems(astv_c.getRlist());
                }catch(InterruptedException | IOException | SQLException | NullPointerException e){
                    System.out.println(e);
                }
            break;
            case "Dean":
                try{
                    astv_c.setFacultySQL("SELECT FACULTY FROM STAFF WHERE STAFF_ID = '"+getSid()+"'");
                    astv_c.fillFacultyCombo();
                    astv_c.setDepartmentSQL("SELECT DEPARTMENT FROM DEPARTMENT WHERE FACULTY = '");
                    astv_c.fillDepartmentCombo();
                    astv_c.getRlist().addAll("HOD","Lecturer");
                    astv_c.getRankCombo().setItems(astv_c.getRlist());
                }catch(InterruptedException | IOException | SQLException | NullPointerException e){
                    System.out.println(e);
                }
            break;
            case "HOD":
                try{
                    astv_c.setFacultySQL("SELECT FACULTY FROM STAFF WHERE STAFF_ID = '"+getSid()+"'");
                    astv_c.fillFacultyCombo();
                    astv_c.setDepartmentSQL("SELECT DEPARTMENT FROM STAFF WHERE STAFF_ID = '"+getSid()+"' AND FACULTY = '");
                    astv_c.fillDepartmentCombo();
                    astv_c.getRlist().addAll("Lecturer");
                    astv_c.getRankCombo().setItems(astv_c.getRlist());
                }catch(InterruptedException | IOException | SQLException | NullPointerException e){
                    System.out.println(e);
                }
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Update "+s.getFirstName()+" "+s.getLastName());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    /**
     * Assign courses to staff
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public void assignCourse() throws IOException, SQLException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CourseAssignView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        
        Staff s = getStaffTable().getSelectionModel().getSelectedItem();
        CourseAssignViewController controller = fxmlLoader.<CourseAssignViewController>getController();
        controller.setAssignSQL("INSERT INTO LEC_COURSE ("
                + "LECTURER,"
                + "COURSE) VALUES (?,?)"
        );
        
        if(s!=null){
            
            controller.getStaffIDField().setText(s.getStaffID());
            controller.getStaffIDField().setEditable(false);
            
            //Fill department combobox
            setDlist(FXCollections.observableArrayList());
            try{
                ResultSet rs = dbcon.getCon().createStatement().executeQuery("SELECT DEPARTMENT FROM DEPARTMENT");
                while(rs.next()){
                    Department department  = new Department();
                    department.setDepartmentName(rs.getString(1));
                    
                    getDlist().add(department.getDepartmentName());
                    controller.getDeptCombo().setItems(getDlist());
                }
            }catch(Exception ex){
                System.out.println("Error occured building department data");
                System.out.println(ex);
            }
        }
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Assign Course");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    /**
     * @return the resources
     */
    public ResourceBundle getResources() {
        return resources;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * @return the location
     */
    public URL getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(URL location) {
        this.location = location;
    }

    /**
     * @return the fname_col
     */
    public TableColumn<Staff, String> getFname_col() {
        return fname_col;
    }

    /**
     * @param fname_col the fname_col to set
     */
    public void setFname_col(TableColumn<Staff, String> fname_col) {
        this.fname_col = fname_col;
    }

    /**
     * @return the mname_col
     */
    public TableColumn<Staff, String> getMname_col() {
        return mname_col;
    }

    /**
     * @param mname_col the mname_col to set
     */
    public void setMname_col(TableColumn<Staff, String> mname_col) {
        this.mname_col = mname_col;
    }

    /**
     * @return the lname_col
     */
    public TableColumn<Staff, String> getLname_col() {
        return lname_col;
    }

    /**
     * @param lname_col the lname_col to set
     */
    public void setLname_col(TableColumn<Staff, String> lname_col) {
        this.lname_col = lname_col;
    }

    /**
     * @return the sex_col
     */
    public TableColumn<Staff, String> getSex_col() {
        return sex_col;
    }

    /**
     * @param sex_col the sex_col to set
     */
    public void setSex_col(TableColumn<Staff, String> sex_col) {
        this.sex_col = sex_col;
    }

    /**
     * @return the course_col
     */
    public TableColumn<Staff, String> getCourse_col() {
        return course_col;
    }

    /**
     * @param course_col the course_col to set
     */
    public void setCourse_col(TableColumn<Staff, String> course_col) {
        this.course_col = course_col;
    }

    /**
     * @return the dept_col
     */
    public TableColumn<Staff, String> getDept_col() {
        return dept_col;
    }

    /**
     * @param dept_col the dept_col to set
     */
    public void setDept_col(TableColumn<Staff, String> dept_col) {
        this.dept_col = dept_col;
    }

    /**
     * @return the fac_col
     */
    public TableColumn<Staff, String> getFac_col() {
        return fac_col;
    }

    /**
     * @param fac_col the fac_col to set
     */
    public void setFac_col(TableColumn<Staff, String> fac_col) {
        this.fac_col = fac_col;
    }

    /**
     * @return the rank_col
     */
    public TableColumn<Staff, String> getRank_col() {
        return rank_col;
    }

    /**
     * @param rank_col the rank_col to set
     */
    public void setRank_col(TableColumn<Staff, String> rank_col) {
        this.rank_col = rank_col;
    }

    /**
     * @return the staffid_col
     */
    public TableColumn<Staff, String> getStaffid_col() {
        return staffid_col;
    }

    /**
     * @param staffid_col the staffid_col to set
     */
    public void setStaffid_col(TableColumn<Staff, String> staffid_col) {
        this.staffid_col = staffid_col;
    }

    /**
     * @return the password_col
     */
    public TableColumn<Staff, String> getPassword_col() {
        return password_col;
    }

    /**
     * @param password_col the password_col to set
     */
    public void setPassword_col(TableColumn<Staff, String> password_col) {
        this.password_col = password_col;
    }

    /**
     * @return the staffTable
     */
    public TableView<Staff> getStaffTable() {
        return staffTable;
    }

    /**
     * @param staffTable the staffTable to set
     */
    public void setStaffTable(TableView<Staff> staffTable) {
        this.staffTable = staffTable;
    }

    /**
     * @return the data
     */
    public ObservableList<Staff> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ObservableList<Staff> data) {
        this.data = data;
    }

    /**
     * @return the SQL
     */
    public String getSQL() {
        return SQL;
    }

    /**
     * @param SQL the SQL to set
     */
    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    /**
     * @return the deletestaff_btn
     */
    public Label getDeletestaff_btn() {
        return deletestaff_btn;
    }

    /**
     * @param deletestaff_btn the deletestaff_btn to set
     */
    public void setDeletestaff_btn(Label deletestaff_btn) {
        this.deletestaff_btn = deletestaff_btn;
    }

    /**
     * @return the viewstaff_btn
     */
    public Label getViewstaff_btn() {
        return viewstaff_btn;
    }

    /**
     * @param viewstaff_btn the viewstaff_btn to set
     */
    public void setViewstaff_btn(Label viewstaff_btn) {
        this.viewstaff_btn = viewstaff_btn;
    }

    /**
     * @return the addstaff_btn
     */
    public Label getAddstaff_btn() {
        return addstaff_btn;
    }

    /**
     * @param addstaff_btn the addstaff_btn to set
     */
    public void setAddstaff_btn(Label addstaff_btn) {
        this.addstaff_btn = addstaff_btn;
    }

    /**
     * @return the updatestaff_btn
     */
    public Label getUpdatestaff_btn() {
        return updatestaff_btn;
    }

    /**
     * @param updatestaff_btn the updatestaff_btn to set
     */
    public void setUpdatestaff_btn(Label updatestaff_btn) {
        this.updatestaff_btn = updatestaff_btn;
    }

    /**
     * @return the staffid
     */
    public String getStaffid() {
        return staffid;
    }

    /**
     * @param staffid the staffid to set
     */
    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    /**
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param RRR the rank to set
     */
    public void setRank(String RRR) {
        this.rank = RRR;
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
     * @return the dep
     */
    public String getDep() {
        return dep;
    }

    /**
     * @param dep the dep to set
     */
    public void setDep(String dep) {
        this.dep = dep;
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
     * @return the dlist
     */
    public ObservableList<String> getDlist() {
        return dlist;
    }

    /**
     * @param dlist the dlist to set
     */
    public void setDlist(ObservableList<String> dlist) {
        this.dlist = dlist;
    }
}
