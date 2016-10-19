package eps;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class StudentViewController implements Initializable {
    
    @FXML private Label deletestudent_btn;
    @FXML private Label viewstudent_btn;
    @FXML private Label addstudent_btn;
    @FXML private Label updatestudent_btn;
    
    @FXML private ResourceBundle resources;
    @FXML private URL location;
    
    @FXML private TableView<Student> studentsTable;
    
    @FXML private TableColumn<Student, String> fname_col;
    @FXML private TableColumn<Student, String> mname_col;
    @FXML private TableColumn<Student, String> lname_col;
    @FXML private TableColumn<Student, String> regno_col;
    @FXML private TableColumn<Student, String> sex_col;
    @FXML private TableColumn<Student, String> level_col;
    @FXML private TableColumn<Student, String> fac_col;
    @FXML private TableColumn<Student, String> dept_col;
    @FXML private TableColumn<Student, String> program_col;
    
    @FXML private MenuItem registerCourseContextMenu;
    @FXML private MenuItem addStudentContextMenu;
    
    private String regno;
    private String rank;
    private String fac;
    private String dep;
    private String sid;
    
    private DBCon dbcon;
    private Connection c;
    
    private ObservableList<Student> data;
    
    private ObservableList<String> se_list;
    private ObservableList<String> lv_list;
    private ObservableList<Integer> sm_list;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getStudentsTable().setPlaceholder(new Label("Click \"View Students\""));
        setStudentsTableCellValueFactory();
    }    
    
    /**
     * Set cell value factory for student table
     */
    public void setStudentsTableCellValueFactory(){
        try{
            getFname_col().setCellValueFactory(new PropertyValueFactory<>("firstName"));
            getMname_col().setCellValueFactory(new PropertyValueFactory<>("middleName"));
            getLname_col().setCellValueFactory(new PropertyValueFactory<>("lastName"));
            getRegno_col().setCellValueFactory(new PropertyValueFactory<>("regNumber"));
            getSex_col().setCellValueFactory(new PropertyValueFactory<>("sex"));
            getLevel_col().setCellValueFactory(new PropertyValueFactory<>("level"));
            getFac_col().setCellValueFactory(new PropertyValueFactory<>("faculty"));
            getDept_col().setCellValueFactory(new PropertyValueFactory<>("department"));
            getProgram_col().setCellValueFactory(new PropertyValueFactory<>("program"));
            
            dbcon = new DBCon();
            setC(dbcon.getCon());
            
            buildData();
        }catch(IllegalStateException | SQLException e){
            System.out.println(e);
        }
    }
    
    /**
     * Build student table data
     */
    private String SQL;
    public void buildData() throws SQLException{
        setData(FXCollections.observableArrayList());
        try{
            ResultSet rs = c.createStatement().executeQuery(getSQL());
            while(rs.next()){
                Student student = new Student();
                student.setFirstName(rs.getString(1));
                student.setMiddleName(rs.getString(2));
                student.setLastName(rs.getString(3));
                student.setRegNumber(rs.getString(4));
                student.setSex(rs.getString(5));
                student.setLevel(rs.getString(6));
                student.setFaculty(rs.getString(8));
                student.setDepartment(rs.getString(7));
                student.setProgram(rs.getString(9));
                
                getData().add(student);
                getStudentsTable().setItems(getData());
            }
        }catch(Exception ex){
            System.out.println("Error building student data");
            System.out.println(ex);
        }
    }
    
    /**
     * Open new student creation scene
     * @throws java.io.IOException
     */
    public void addStudent() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddStudentView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        
        AddStudentViewController astdv_c = fxmlLoader.<AddStudentViewController>getController();
        astdv_c.setUpdateSQL("INSERT INTO STUDENT ("
                + "FIRST_NAME,"
                + "MIDDLE_NAME,"
                + "LAST_NAME,"
                + "REG_NO,"
                + "SEX,"
                + "LEVEL,"
                + "DEPARTMENT,"
                + "FACULTY,"
                + "PROGRAM,"
                + "PICTURE) VALUES (?,?,?,?,?,?,?,?,?,?)"
        );
        astdv_c.setResponse("has been added to students.");
        String r = getRank();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Add Student");
        
        System.out.println("Student rank = "+r);
        
        // Set rank restrictions for creating student
        switch(r){
            case "Admin":
                try{
                    
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }catch(Exception e){
                    
                }
            break;
            case "Dean":
                try{
                    
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }catch(Exception e){
                    
                }
            break;
            case "HOD":
                try{
                    
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }catch(Exception e){
                    
                }
            break;
        }
    }
    
    /**
     * Delete Selected row
     * @throws java.sql.SQLException
     */
    public void deleteRow() throws SQLException{
        Student s = getStudentsTable().getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to delete "+s.getFirstName()+" "+s.getLastName()+"?\nOperation cannot be Undone.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("CONFIRM DELETION");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES){
            if(s!=null){
                getC().createStatement().executeUpdate("DELETE FROM STUDENT WHERE REG_NO = '"+s.getRegNumber()+"'");
                getC().createStatement().executeUpdate("DELETE FROM COURSE_REG WHERE STUDENT = '"+s.getRegNumber()+"'");
                getData().remove(s);
            }
        }
        System.out.println("Delete student row");
    }
    
    /**
     * Update student information
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public void updateStudent() throws IOException, SQLException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddStudentView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        
        AddStudentViewController astdv_c = fxmlLoader.<AddStudentViewController>getController();
        astdv_c.setResponse("has been updated.");
        astdv_c.getCreateStudentButton().setText("UPDATE");
        
        Student s = getStudentsTable().getSelectionModel().getSelectedItem();
        astdv_c.setUpdateSQL("UPDATE STUDENT SET "
                + "FIRST_NAME = ?, "
                + "MIDDLE_NAME = ?, "
                + "LAST_NAME = ?, "
                + "REG_NO = ?,"
                + "SEX = ?,"
                + "LEVEL = ?,"
                + "DEPARTMENT = ?,"
                + "FACULTY = ?,"
                + "PROGRAM = ?,"
                + "PICTURE = ? "
                + "WHERE REG_NO = '"+s.getRegNumber()+"'");
        
        if(s!=null){
            astdv_c.getFirstName().setText(s.getFirstName());
            astdv_c.getMiddleName().setText(s.getMiddleName());
            astdv_c.getLastName().setText(s.getLastName());
            astdv_c.getRegNumber().setText(s.getRegNumber());
            astdv_c.getFacultyCombo().setValue(s.getFaculty());
            astdv_c.getDepartmentCombo().setValue(s.getDepartment());
            astdv_c.getLevelCombo().setValue(s.getLevel());
            
            ResultSet rs = getC().createStatement().executeQuery("SELECT PICTURE FROM STUDENT WHERE REG_NO = '"+s.getRegNumber()+"'");
            if(rs.next()){
                //get blobfile from db into byte array
                Blob blob = rs.getBlob(1);
                byte[] imgbyte = blob.getBytes(1, (int)blob.length());
                
                //create a tempfile
                File tempFile = File.createTempFile("photo","jpg",null);
                //make the tempfile a fileoutputstream
                FileOutputStream file = new FileOutputStream(tempFile);
                //write bytearray int the tempfile
                file.write(imgbyte);
                
                //set the tempfile as readable file in astdv_c
                astdv_c.setFile(tempFile);
                
                //convert the bytearray into an image
                Image image = new Image(new ByteArrayInputStream(imgbyte));
                //display the image on the imageview in astdv_c
                astdv_c.getImageView().setImage(image);
                astdv_c.getImageView().setFitHeight(200);                
                astdv_c.getImageView().setPreserveRatio(true);
                //hide overflow
                astdv_c.getImagePane().setClip(new Rectangle(220,200));
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
     * Open course registration scene
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public void registerCourse() throws IOException, SQLException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CourseRegView.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        Student s = getStudentsTable().getSelectionModel().getSelectedItem();
        
        if(s!=null){
            CourseRegViewController controller = fxmlLoader.<CourseRegViewController>getController();
            controller.getRegnoField().setText(s.getRegNumber()); //regnumber
            controller.getRegnoField().setEditable(false);
            controller.setDepartment(s.getDepartment()); //department
            
            //Fill session combobox
            setSe_list(FXCollections.observableArrayList());
            try{
                ResultSet rs = dbcon.getCon().createStatement().executeQuery("SELECT SESSION FROM SESSION");
                while(rs.next()){
                    Session session = new Session();
                    session.setSession(rs.getString(1));
                    
                    getSe_list().add(session.getSession());
                    controller.getSessionCombo().setItems(getSe_list());
                }
            }catch(Exception ex){
                System.out.println("Error occured building session data");
                System.out.println(ex);
            }
            
            //Fill level combobox
            setLv_list(FXCollections.observableArrayList());
            try{
                ResultSet rs = dbcon.getCon().createStatement().executeQuery("SELECT LEVEL FROM LEVEL");
                while(rs.next()){
                    Level_ level = new Level_();
                    level.setLevel(rs.getString(1));
                    
                    getLv_list().add(level.getLevel());
                    controller.getLevelCombo().setItems(getLv_list());
                }
            }catch(Exception ex){
                System.out.println("Error occured building level data");
                System.out.println(ex);
            }
            
            //Fill semester combobox
            setSm_list(FXCollections.observableArrayList());
            try{
                ResultSet rs = dbcon.getCon().createStatement().executeQuery("SELECT SEMESTER FROM SEMESTER");
                while(rs.next()){
                    Semester semester = new Semester();
                    semester.setSemester(rs.getInt(1));
                    
                    getSm_list().add(semester.getSemester());
                    controller.getSemesterCombo().setItems(getSm_list());
                }
            }catch(Exception ex){
                System.out.println("Error occured building semester data");
                System.out.println(ex);
            }
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Register Course");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }
    
    /**
     * @return the deletestudent_btn
     */
    public Label getDeletestudent_btn() {
        return deletestudent_btn;
    }

    /**
     * @param deletestudent_btn the deletestudent_btn to set
     */
    public void setDeletestudent_btn(Label deletestudent_btn) {
        this.deletestudent_btn = deletestudent_btn;
    }

    /**
     * @return the viewstudent_btn
     */
    public Label getViewstudent_btn() {
        return viewstudent_btn;
    }

    /**
     * @param viewstudent_btn the viewstudent_btn to set
     */
    public void setViewstudent_btn(Label viewstudent_btn) {
        this.viewstudent_btn = viewstudent_btn;
    }

    /**
     * @return the addstudent_btn
     */
    public Label getAddstudent_btn() {
        return addstudent_btn;
    }

    /**
     * @param addstudent_btn the addstudent_btn to set
     */
    public void setAddstudent_btn(Label addstudent_btn) {
        this.addstudent_btn = addstudent_btn;
    }

    /**
     * @return the updatestudent_btn
     */
    public Label getUpdatestudent_btn() {
        return updatestudent_btn;
    }

    /**
     * @param updatestudent_btn the updatestudent_btn to set
     */
    public void setUpdatestudent_btn(Label updatestudent_btn) {
        this.updatestudent_btn = updatestudent_btn;
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
     * @return the studentTable
     */
    public TableView<Student> getStudentsTable() {
        return studentsTable;
    }

    /**
     * @param studentTable the studentTable to set
     */
    public void setStudentsTable(TableView<Student> studentTable) {
        this.studentsTable = studentTable;
    }

    /**
     * @return the fname_col
     */
    public TableColumn<Student, String> getFname_col() {
        return fname_col;
    }

    /**
     * @param fname_col the fname_col to set
     */
    public void setFname_col(TableColumn<Student, String> fname_col) {
        this.fname_col = fname_col;
    }

    /**
     * @return the mname_col
     */
    public TableColumn<Student, String> getMname_col() {
        return mname_col;
    }

    /**
     * @param mname_col the mname_col to set
     */
    public void setMname_col(TableColumn<Student, String> mname_col) {
        this.mname_col = mname_col;
    }

    /**
     * @return the lname_col
     */
    public TableColumn<Student, String> getLname_col() {
        return lname_col;
    }

    /**
     * @param lname_col the lname_col to set
     */
    public void setLname_col(TableColumn<Student, String> lname_col) {
        this.lname_col = lname_col;
    }

    /**
     * @return the regno_col
     */
    public TableColumn<Student, String> getRegno_col() {
        return regno_col;
    }

    /**
     * @param regno_col the regno_col to set
     */
    public void setRegno_col(TableColumn<Student, String> regno_col) {
        this.regno_col = regno_col;
    }

    /**
     * @return the sex_col
     */
    public TableColumn<Student, String> getSex_col() {
        return sex_col;
    }

    /**
     * @param sex_col the sex_col to set
     */
    public void setSex_col(TableColumn<Student, String> sex_col) {
        this.sex_col = sex_col;
    }

    /**
     * @return the level_col
     */
    public TableColumn<Student, String> getLevel_col() {
        return level_col;
    }

    /**
     * @param level_col the level_col to set
     */
    public void setLevel_col(TableColumn<Student, String> level_col) {
        this.level_col = level_col;
    }

    /**
     * @return the fac_col
     */
    public TableColumn<Student, String> getFac_col() {
        return fac_col;
    }

    /**
     * @param fac_col the fac_col to set
     */
    public void setFac_col(TableColumn<Student, String> fac_col) {
        this.fac_col = fac_col;
    }

    /**
     * @return the dept_col
     */
    public TableColumn<Student, String> getDept_col() {
        return dept_col;
    }

    /**
     * @param dept_col the dept_col to set
     */
    public void setDept_col(TableColumn<Student, String> dept_col) {
        this.dept_col = dept_col;
    }

    /**
     * @return the program_col
     */
    public TableColumn<Student, String> getProgram_col() {
        return program_col;
    }

    /**
     * @param program_col the program_col to set
     */
    public void setProgram_col(TableColumn<Student, String> program_col) {
        this.program_col = program_col;
    }

    /**
     * @return the regno
     */
    public String getRegno() {
        return regno;
    }

    /**
     * @param regno the regno to set
     */
    public void setRegno(String regno) {
        this.regno = regno;
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
     * @return the data
     */
    public ObservableList<Student> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ObservableList<Student> data) {
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
     * @return the registerCourseContextMenu
     */
    public MenuItem getRegisterCourseContextMenu() {
        return registerCourseContextMenu;
    }

    /**
     * @param registerCourseContextMenu the registerCourseContextMenu to set
     */
    public void setRegisterCourseContextMenu(MenuItem registerCourseContextMenu) {
        this.registerCourseContextMenu = registerCourseContextMenu;
    }

    /**
     * @return the se_list
     */
    public ObservableList<String> getSe_list() {
        return se_list;
    }

    /**
     * @param se_list the se_list to set
     */
    public void setSe_list(ObservableList<String> se_list) {
        this.se_list = se_list;
    }

    /**
     * @return the lv_list
     */
    public ObservableList<String> getLv_list() {
        return lv_list;
    }

    /**
     * @param lv_list the lv_list to set
     */
    public void setLv_list(ObservableList<String> lv_list) {
        this.lv_list = lv_list;
    }

    /**
     * @return the sm_list
     */
    public ObservableList<Integer> getSm_list() {
        return sm_list;
    }

    /**
     * @param sm_list the sm_list to set
     */
    public void setSm_list(ObservableList<Integer> sm_list) {
        this.sm_list = sm_list;
    }

    /**
     * @return the addStudentContextMenu
     */
    public MenuItem getAddStudentContextMenu() {
        return addStudentContextMenu;
    }

    /**
     * @param addStudentContextMenu the addStudentContextMenu to set
     */
    public void setAddStudentContextMenu(MenuItem addStudentContextMenu) {
        this.addStudentContextMenu = addStudentContextMenu;
    }
    
}
