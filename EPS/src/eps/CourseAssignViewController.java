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
public class CourseAssignViewController implements Initializable {
    
    @FXML private TextField staffIDField;
    @FXML private ComboBox<String> deptCombo;
    @FXML private Label goBtn;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course,String> courseCol;
    @FXML private TableColumn<Course,String> codeCol;
    @FXML private TableColumn<Course,String> levelCol;
    @FXML private Label submitBtn;
    
    private ObservableList<Course> data;
    
    private DBCon dbcon;
    private Connection c;
    
    private String assignSQL;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getCourseTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getCourseTable().setPlaceholder(new Label("Hold down 'CTRL' to select multiple items."));
    }
    
    public void setCourseAssignTableCellValueFactory() throws Exception {
        try {
            getCourseCol().setCellValueFactory(new PropertyValueFactory<>("courseName"));
            getCodeCol().setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            getLevelCol().setCellValueFactory(new PropertyValueFactory<>("level"));

            setDbcon(new DBCon());
            setC(getDbcon().getCon());

            buildData();
        } catch (IllegalStateException | SQLException e) {
            System.out.println(e);
        }
    }
    
    public void buildData() throws SQLException{
        setData(FXCollections.observableArrayList());
        String dsel = getDeptCombo().getSelectionModel().getSelectedItem();
        try{
            ResultSet rs = getC().createStatement().executeQuery("SELECT * FROM COURSE WHERE DEPARTMENT = '"+dsel+"'");
            while(rs.next()){
                Course course = new Course();
                course.setCourseName(rs.getString(1));
                course.setCourseCode(rs.getString(2));
                course.setLevel(rs.getString(4));
                
                getData().add(course);
                getCourseTable().setItems(getData());
            }
        }catch(SQLException | NumberFormatException ex){
            System.out.println("Error building course registration data");
            System.out.println(ex);
        }
    }
    
    private int ro; //row count
    public int getRo(){
        return ro;
    }
    public void setRo(int ro){
        this.ro = ro;
    }
    public void assign() throws SQLException{
        String sid = getStaffIDField().getText();
        ObservableList<Course> selectedItems = getCourseTable().getSelectionModel().getSelectedItems();
        
        ArrayList<String> codes = new ArrayList<>();
        for(Course row:selectedItems){
            codes.add(row.getCourseCode());
        }
        
        ListIterator<String> code_it = codes.listIterator();
        
        while(code_it.hasNext()){
            String SQL = "INSERT INTO LEC_COURSE (`LECTURER`,`COURSE`)VALUES("
                    + "'"+sid+"',"
                    + "'"+code_it.next()+"')";
            
            setRo(getC().createStatement().executeUpdate(SQL));
        }
        
        if(getRo()>0){
                Alert alert = new Alert(Alert.AlertType.NONE, "Course Assignment Succeessful.", ButtonType.CLOSE);
                alert.setTitle("");
                alert.showAndWait();
                if(alert.getResult() == ButtonType.CLOSE){
                    alert.close();
                }
                Stage cass = (Stage) getSubmitBtn().getScene().getWindow();
                cass.close();
            }
    }
    
    /**
     * @return the staffIDField
     */
    public TextField getStaffIDField() {
        return staffIDField;
    }

    /**
     * @param staffIDField the staffIDField to set
     */
    public void setStaffIDField(TextField staffIDField) {
        this.staffIDField = staffIDField;
    }

    /**
     * @return the deptCombo
     */
    public ComboBox<String> getDeptCombo() {
        return deptCombo;
    }

    /**
     * @param deptCombo the deptCombo to set
     */
    public void setDeptCombo(ComboBox<String> deptCombo) {
        this.deptCombo = deptCombo;
    }

    /**
     * @return the goBtn
     */
    public Label getGoBtn() {
        return goBtn;
    }

    /**
     * @param goBtn the goBtn to set
     */
    public void setGoBtn(Label goBtn) {
        this.goBtn = goBtn;
    }

    /**
     * @return the courseTable
     */
    public TableView<Course> getCourseTable() {
        return courseTable;
    }

    /**
     * @param courseAssignTable the courseTable to set
     */
    public void setCourseTable(TableView<Course> courseAssignTable) {
        this.courseTable = courseAssignTable;
    }

    /**
     * @return the courseCol
     */
    public TableColumn<Course,String> getCourseCol() {
        return courseCol;
    }

    /**
     * @param courseCol the courseCol to set
     */
    public void setCourseCol(TableColumn<Course,String> courseCol) {
        this.courseCol = courseCol;
    }

    /**
     * @return the codeCol
     */
    public TableColumn<Course,String> getCodeCol() {
        return codeCol;
    }

    /**
     * @param codeCol the codeCol to set
     */
    public void setCodeCol(TableColumn<Course,String> codeCol) {
        this.codeCol = codeCol;
    }

    /**
     * @return the levelCol
     */
    public TableColumn<Course,String> getLevelCol() {
        return levelCol;
    }

    /**
     * @param levelCol the levelCol to set
     */
    public void setLevelCol(TableColumn<Course,String> levelCol) {
        this.levelCol = levelCol;
    }

    /**
     * @return the submitBtn
     */
    public Label getSubmitBtn() {
        return submitBtn;
    }

    /**
     * @param submitBtn the submitBtn to set
     */
    public void setSubmitBtn(Label submitBtn) {
        this.submitBtn = submitBtn;
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
     * @return the assignSQL
     */
    public String getAssignSQL() {
        return assignSQL;
    }

    /**
     * @param assignSQL the assignSQL to set
     */
    public void setAssignSQL(String assignSQL) {
        this.assignSQL = assignSQL;
    }
    
}
