package eps;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class CourseRegViewController implements Initializable {
    
    @FXML private TextField RegnoField;

    @FXML private Label GoButton;
    
    @FXML private ComboBox<String> SessionCombo;
    @FXML private ComboBox<String> LevelCombo;
    @FXML private ComboBox<Integer> SemesterCombo;
    
    private String department;

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> CourseCol;
    @FXML private TableColumn<Course, String> CodeCol;
    @FXML private TableColumn<Course, Integer> CreditCol;

    @FXML private Label SubmitButton;
    
    private ObservableList<Course> data;

    private DBCon dbcon;
    private Connection c;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        getCourseTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getCourseTable().setPlaceholder(new Label("Hold down 'CTRL' to select multiple items."));
    }
    
    /**
     * Registration queries
     * @throws java.sql.SQLException
     */
    private int ro; //row count
    public int getRo(){
        return ro;
    }
    public void setRo(int ro){
        this.ro = ro;
    }
    public void register() throws SQLException{
        String reg_no = getRegnoField().getText();
        String session = getSessionCombo().getSelectionModel().getSelectedItem();
        String level_ = getLevelCombo().getSelectionModel().getSelectedItem();
        int semester = getSemesterCombo().getSelectionModel().getSelectedItem();
        
        ObservableList<Course> selectedItems = getCourseTable().getSelectionModel().getSelectedItems();
        
        ArrayList<String> courses = new ArrayList<>();
        ArrayList<String> codes = new ArrayList<>();
        ArrayList<Integer> credits = new ArrayList<>();
        for(Course row:selectedItems){
            courses.add(row.getCourseName());
            codes.add(row.getCourseCode());
            credits.add(row.getCreditUnit());
        }
        
        ListIterator<String> course_it = courses.listIterator();
        ListIterator<String> code_it = codes.listIterator();
        ListIterator<Integer> cred_it = credits.listIterator();
        
        
        while(course_it.hasNext()){

            String SQL = "INSERT INTO `course_reg`(`STUDENT`, `LEVEL`, `COURSE`, `COURSE_CODE`, `CREDIT_UNIT`, `SESSION`, `SEMESTER`) VALUES ("
                    + "'"+reg_no+"',"
                    + "'"+level_+"',"
                    + "'"+course_it.next()+"',"
                    + "'"+code_it.next()+"',"
                    + ""+cred_it.next()+","
                    + "'"+session+"',"
                    + ""+semester+")";
            
            setRo(getC().createStatement().executeUpdate(SQL));
        }
        if(getRo()>0){
                Alert alert = new Alert(Alert.AlertType.NONE, "Course Registration Succeessful.", ButtonType.CLOSE);
                alert.setTitle("");
                alert.showAndWait();
                if(alert.getResult() == ButtonType.CLOSE){
                    alert.close();
                }
                Stage creg = (Stage) getSubmitButton().getScene().getWindow();
                creg.close();
            }
    }
    
    /**
     * Set cell value factory for course registration table
     * @throws java.lang.Exception
     */
    public void setCregTableCellValueFactory() throws Exception{
        try{
            getCourseCol().setCellValueFactory(new PropertyValueFactory<>("CourseName"));
            getCodeCol().setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
            getCreditCol().setCellValueFactory(new PropertyValueFactory<>("CreditUnit"));
            
            dbcon = new DBCon();
            setC(dbcon.getCon());
            
            buildData();
        }catch(IllegalStateException | SQLException e){
            System.out.println(e);
        }
    }
    
    /**
     * Build Student table data
     * @throws java.sql.SQLException
     */
    public void buildData() throws SQLException{
        setData(FXCollections.observableArrayList());
        String lsel = getLevelCombo().getSelectionModel().getSelectedItem();
        int ssel = getSemesterCombo().getSelectionModel().getSelectedItem();
        
        try{
            ResultSet rs = getC().createStatement().executeQuery("SELECT * FROM COURSE WHERE LEVEL = '"+lsel+"' AND SEMESTER = "+ssel+"");
            while(rs.next()){
                Course course = new Course();
                course.setCourseName(rs.getString(1));
                course.setCourseCode(rs.getString(2));
                course.setCreditUnit(rs.getInt(3));
                
                getData().add(course);
                getCourseTable().setItems(getData());
            }
        }catch(SQLException | NumberFormatException ex){
            System.out.println("Error building course registration data");
            System.out.println(ex);
        }
    }

    /**
     * @return the RegnoField
     */
    public TextField getRegnoField() {
        return RegnoField;
    }

    /**
     * @param RegnoField the RegnoField to set
     */
    public void setRegnoField(TextField RegnoField) {
        this.RegnoField = RegnoField;
    }

    /**
     * @return the GoButton
     */
    public Label getGoButton() {
        return GoButton;
    }

    /**
     * @param GoButton the GoButton to set
     */
    public void setGoButton(Label GoButton) {
        this.GoButton = GoButton;
    }

    /**
     * @return the SessionCombo
     */
    public ComboBox<String> getSessionCombo() {
        return SessionCombo;
    }

    /**
     * @param SessionCombo the SessionCombo to set
     */
    public void setSessionCombo(ComboBox<String> SessionCombo) {
        this.SessionCombo = SessionCombo;
    }

    /**
     * @return the LevelCombo
     */
    public ComboBox<String> getLevelCombo() {
        return LevelCombo;
    }

    /**
     * @param LevelCombo the LevelCombo to set
     */
    public void setLevelCombo(ComboBox<String> LevelCombo) {
        this.LevelCombo = LevelCombo;
    }

    /**
     * @return the SemesterCombo
     */
    public ComboBox<Integer> getSemesterCombo() {
        return SemesterCombo;
    }

    /**
     * @param SemesterCombo the SemesterCombo to set
     */
    public void setSemesterCombo(ComboBox<Integer> SemesterCombo) {
        this.SemesterCombo = SemesterCombo;
    }

    /**
     * @return the CRegTable
     */
    public TableView<Course> getCourseTable(){
        return courseTable;
    }

    /**
     * @param ctable the courseTable to set
     */
    public void setCourseTable(TableView<Course> ctable) {
        this.courseTable = ctable;
    }

    /**
     * @return the CourseCol
     */
    public TableColumn<Course, String> getCourseCol() {
        return CourseCol;
    }

    /**
     * @param CourseCol the CourseCol to set
     */
    public void setCourseCol(TableColumn<Course, String> CourseCol) {
        this.CourseCol = CourseCol;
    }

    /**
     * @return the CodeCol
     */
    public TableColumn<Course, String> getCodeCol() {
        return CodeCol;
    }

    /**
     * @param CodeCol the CodeCol to set
     */
    public void setCodeCol(TableColumn<Course, String> CodeCol) {
        this.CodeCol = CodeCol;
    }
    
    /**
     * 
     * @return the CreditCol
     */
    public TableColumn<Course, Integer> getCreditCol(){
        return CreditCol;
    }
    
    /**
     * 
     * @param cr_col the CreditCol to set
     */
    public void setCredit_col(TableColumn<Course, Integer> cr_col){
        this.CreditCol = cr_col;
    }

    /**
     * @return the SubmitButton
     */
    public Label getSubmitButton() {
        return SubmitButton;
    }

    /**
     * @param SubmitButton the SubmitButton to set
     */
    public void setSubmitButton(Label SubmitButton) {
        this.SubmitButton = SubmitButton;
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
    public ObservableList<Course> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ObservableList<Course> data) {
        this.data = data;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
}
