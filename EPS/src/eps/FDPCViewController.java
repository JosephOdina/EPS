package eps;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class FDPCViewController implements Initializable {
    
    @FXML private Label facultyLabel;
    @FXML private AnchorPane facultyPane;
    @FXML private ListView<String> facultyList;
    
    @FXML private Label departmentLabel;
    @FXML private AnchorPane departmentPane;
    @FXML private ListView<String> departmentList;
    
    @FXML private Label programLabel;
    @FXML private AnchorPane programPane;
    @FXML private ListView<String> programList;
    
    @FXML private Label courseLabel;
    @FXML private AnchorPane coursePane;
    @FXML private ListView<String> courseList;
    
    private ObservableList<String> flist;
    private ObservableList<String> dlist;
    private ObservableList<String> plist;
    private ObservableList<String> clist;
    private String fsql;
    
    private DBCon dbcon;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildFacultyData();
        buildDepartmentData();
        buildProgramData();
    }
    
    /**
     * build faculty data
     */
    public void buildFacultyData(){
        setFlist(FXCollections.observableArrayList());
        getFacultyList().setItems(getFlist());
    }
    
    /**
     * build department data from faculty data
     */
    public void buildDepartmentData(){
        setDlist(FXCollections.observableArrayList());
        getDepartmentList().setItems(getDlist());
    }
    
    /**
     * build program data from department data
     */
    public void buildProgramData(){
        setPlist(FXCollections.observableArrayList());
        getProgramList().setItems(getPlist());
    }
    
    /**
     * @return the facultyLabel
     */
    public Label getFacultyLabel() {
        return facultyLabel;
    }

    /**
     * @param facultyLabel the facultyLabel to set
     */
    public void setFacultyLabel(Label facultyLabel) {
        this.facultyLabel = facultyLabel;
    }

    /**
     * @return the facultyPane
     */
    public AnchorPane getFacultyPane() {
        return facultyPane;
    }

    /**
     * @param facultyPane the facultyPane to set
     */
    public void setFacultyPane(AnchorPane facultyPane) {
        this.facultyPane = facultyPane;
    }

    /**
     * @return the facultyList
     */
    public ListView<String> getFacultyList() {
        return facultyList;
    }

    /**
     * @param facultyList the facultyList to set
     */
    public void setFacultyList(ListView<String> facultyList) {
        this.facultyList = facultyList;
    }

    /**
     * @return the departmentLabel
     */
    public Label getDepartmentLabel() {
        return departmentLabel;
    }

    /**
     * @param departmentLabel the departmentLabel to set
     */
    public void setDepartmentLabel(Label departmentLabel) {
        this.departmentLabel = departmentLabel;
    }

    /**
     * @return the departmentPane
     */
    public AnchorPane getDepartmentPane() {
        return departmentPane;
    }

    /**
     * @param departmentPane the departmentPane to set
     */
    public void setDepartmentPane(AnchorPane departmentPane) {
        this.departmentPane = departmentPane;
    }

    /**
     * @return the departmentList
     */
    public ListView<String> getDepartmentList() {
        return departmentList;
    }

    /**
     * @param departmentList the departmentList to set
     */
    public void setDepartmentList(ListView<String> departmentList) {
        this.departmentList = departmentList;
    }

    /**
     * @return the programLabel
     */
    public Label getProgramLabel() {
        return programLabel;
    }

    /**
     * @param programLabel the programLabel to set
     */
    public void setProgramLabel(Label programLabel) {
        this.programLabel = programLabel;
    }

    /**
     * @return the programPane
     */
    public AnchorPane getProgramPane() {
        return programPane;
    }

    /**
     * @param programPane the programPane to set
     */
    public void setProgramPane(AnchorPane programPane) {
        this.programPane = programPane;
    }

    /**
     * @return the programList
     */
    public ListView<String> getProgramList() {
        return programList;
    }

    /**
     * @param programList the programList to set
     */
    public void setProgramList(ListView<String> programList) {
        this.programList = programList;
    }

    /**
     * @return the courseLabel
     */
    public Label getCourseLabel() {
        return courseLabel;
    }

    /**
     * @param courseLabel the courseLabel to set
     */
    public void setCourseLabel(Label courseLabel) {
        this.courseLabel = courseLabel;
    }

    /**
     * @return the coursePane
     */
    public AnchorPane getCoursePane() {
        return coursePane;
    }

    /**
     * @param coursePane the coursePane to set
     */
    public void setCoursePane(AnchorPane coursePane) {
        this.coursePane = coursePane;
    }

    /**
     * @return the courseList
     */
    public ListView<String> getCourseList() {
        return courseList;
    }

    /**
     * @param courseList the courseList to set
     */
    public void setCourseList(ListView<String> courseList) {
        this.courseList = courseList;
    }

    /**
     * @return the flist
     */
    public ObservableList<String> getFlist() {
        return flist;
    }

    /**
     * @param flist the flist to set
     */
    public void setFlist(ObservableList<String> flist) {
        this.flist = flist;
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

    /**
     * @return the plist
     */
    public ObservableList<String> getPlist() {
        return plist;
    }

    /**
     * @param plist the plist to set
     */
    public void setPlist(ObservableList<String> plist) {
        this.plist = plist;
    }

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
     * @return the fsql
     */
    public String getFsql() {
        return fsql;
    }

    /**
     * @param fsql the fsql to set
     */
    public void setFsql(String fsql) {
        this.fsql = fsql;
    }
    
}
