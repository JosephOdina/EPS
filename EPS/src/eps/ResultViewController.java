package eps;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class ResultViewController implements Initializable{
    
    @FXML private Label viewByCourseBtn;
    @FXML private Label viewByStudentBtn;
    @FXML private TextField course_or_studentTextField;
    @FXML private ComboBox<String> sessionCombo;
    @FXML private ComboBox<Integer> semesterCombo;
    @FXML private Label goBtn;
    @FXML private TableView<Result> resultTable;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Result, String> regnoCol;
    @FXML private TableColumn<Result, String> codeCol;
    @FXML private TableColumn<Result, BigDecimal> attendanceCol;
    @FXML private TableColumn<Result, BigDecimal> assignmentCol;
    @FXML private TableColumn<Result, BigDecimal> quizCol;
    @FXML private TableColumn<Result, BigDecimal> examCol;
    @FXML private TableColumn<Result, BigDecimal> totalCol;
    @FXML private TableColumn<Result, Character> gradeCol;
    
    @FXML private Label gpaLabel;
    @FXML private Label printBtn;
    
    private DBCon dbcon;
    private Connection c;
    
    private ObservableList<Result> data;
    
    private String sql;
    private String nameSQL;
    
    private int i;
    
    private ObservableList<String> se_list;
    private ObservableList<Integer> sm_list;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        getCourse_or_studentTextField().setDisable(true);
        getGoBtn().setDisable(true);
        getCodeCol().setVisible(false);
        getResultTable().setPlaceholder(new Text("Click on \"course\" to view results by course.\nClick on \"student\" to view results by student"));
        fillSessionCombo();
        fillSemesterCombo();
    }
    
    /**
     * Set up scene for viewing result by course
     */
    public void viewByCourse(){
        //reset viewByStudentBtn
        resetViewByStudentBtn();
        //increase the font size and make it bold
        getViewByCourseBtn().setStyle("-fx-font-size:20;-fx-font-weight:bold");
        //align the button
        getViewByCourseBtn().setTranslateY(-7);
        getViewByCourseBtn().setTranslateX(-2);
        //disable semester combobox
        getSemesterCombo().setDisable(true);
        //enable course_or_studentTextField
        getCourse_or_studentTextField().setDisable(false);
        //enable goBtn
        getGoBtn().setDisable(false);
        //clear course_or_studentTextField and set prompt text
        getCourse_or_studentTextField().clear();
        getCourse_or_studentTextField().setPromptText("Enter course code");
        //clear previous table content
        getResultTable().setItems(null);
        
        setI(1);
    }
    
    /**
     * Set up scene for viewing result by student
     */
    public void viewByStudent(){
        //reset viewByCourseBtn
        resetViewByCourseBtn();
        //increase the font size and make it bold
        getViewByStudentBtn().setStyle("-fx-font-size:20;-fx-font-weight:bold");
        //align the button
        getViewByStudentBtn().setTranslateY(-7);
        getViewByStudentBtn().setTranslateX(-2);
        //enable semester combobox
        getSemesterCombo().setDisable(false);
        //enable course_or_studentTextField
        getCourse_or_studentTextField().setDisable(false);
        //enable goBtn
        getGoBtn().setDisable(false);
        //clear course_or_studentTextField and set prompt text
        getCourse_or_studentTextField().clear();
        getCourse_or_studentTextField().setPromptText("Enter reg. number");
        //clear previous table content
        getResultTable().setItems(null);
        
        setI(2);
    }
    
    /**
     * reset viewByStudentBtn to initial state when
     * user clicks viewByCourseBtn
     */
    public void resetViewByStudentBtn(){
        getViewByStudentBtn().setStyle("-fx-font-size:13;-fx-font-weight:regular");
        getViewByStudentBtn().setTranslateX(7);
        getViewByStudentBtn().setTranslateY(2);
    }
    
    /**
    * reset viewByCourseBtn to initial state when
    * user clicks viewByStudentBtn
    */
    public void resetViewByCourseBtn(){
        getViewByCourseBtn().setStyle("-fx-font-size:13;-fx-font-weight:regular");
        getViewByCourseBtn().setTranslateX(7);
        getViewByCourseBtn().setTranslateY(2);
    }
    
    /**
     * Set result table cell value factory
     */
    public void setResultTableCellValueFactory(){
        try {
            getNameCol().setCellValueFactory(new PropertyValueFactory<>("name"));
            getRegnoCol().setCellValueFactory(new PropertyValueFactory<>("regno"));
            getCodeCol().setCellValueFactory(new PropertyValueFactory<>("c_code"));
            getAttendanceCol().setCellValueFactory(new PropertyValueFactory<>("attendance"));
            getAssignmentCol().setCellValueFactory(new PropertyValueFactory<>("assignment"));
            getQuizCol().setCellValueFactory(new PropertyValueFactory<>("quiz"));
            getExamCol().setCellValueFactory(new PropertyValueFactory<>("exam"));
            getTotalCol().setCellValueFactory(new PropertyValueFactory<>("total"));
            getGradeCol().setCellValueFactory(new PropertyValueFactory<>("grade"));
            
            setDbcon(new DBCon());
            setC(getDbcon().getCon());
            
            buildData();
        } catch (SQLException ex) {
            Logger.getLogger(ResultViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Build result table data
     */
    private String name;
    public void buildData(){
        if(getI()==1){ //buildData for viewByCourse
            String csField = getCourse_or_studentTextField().getText().toUpperCase();
            String sess = getSessionCombo().getSelectionModel().getSelectedItem();
            
            setSql("SELECT * FROM RESULTS WHERE COURSE_CODE = '"+csField+"' AND SESSION = '"+sess+"'");
            
            //hide course code column (redundant, eh?)
            getCodeCol().setVisible(false);
            //print method
            getPrintBtn().setOnMouseClicked(e->printAllResults());
            //clear gpa label
            getGpaLabel().setText("");
            
            //populate rows
            try {
                setData(FXCollections.observableArrayList());
                ResultSet rs = getC().createStatement().executeQuery(getSql());
                while(rs.next()){
                    setNameSQL("SELECT * FROM STUDENT WHERE REG_NO = '"+rs.getString(2)+"'");
                    ResultSet rss = getC().createStatement().executeQuery(getNameSQL());
                    while(rss.next()){
                        setName(rss.getString(3)+", "+rss.getString(1)+" "+rss.getString(2));
                    }
                    Result result = new Result();
                    
                    System.out.println(getName());

                    result.setName(name);
                    result.setRegno(rs.getString(2));
                    result.setC_code(rs.getString(3));
                    result.setAttendance(new BigDecimal(rs.getDouble(9)));
                    result.setAssignment(new BigDecimal(rs.getDouble(10)));
                    result.setQuiz(new BigDecimal(rs.getDouble(11)));
                    result.setExam(new BigDecimal(rs.getDouble(12)));
                    result.setTotal(new BigDecimal(rs.getDouble(13)));
                    result.setGrade(rs.getString(14));

                    getData().add(result);
                    getResultTable().setItems(getData());
                }
            }catch (SQLException ex) {
                Logger.getLogger(ResultViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(getI()==2){ //buildData for viewByStudent
            String csField = getCourse_or_studentTextField().getText();
            String sess = getSessionCombo().getSelectionModel().getSelectedItem();
            Integer sem = getSemesterCombo().getSelectionModel().getSelectedItem();
            
            if(sem!=null&sess!=null){ //do if viewing result for semester
                setSql("SELECT * FROM RESULTS WHERE STUDENT = '"+csField+"' AND SESSION = '"+sess+"' AND SEMESTER = "+sem+"");
                //show course code column (MAGIC!)
                getCodeCol().setVisible(true);
                //print method
                getPrintBtn().setOnMouseClicked(e->printSemesterResult());
                //set gpaLabel to show GPA
                getGpaLabel().setText("GPA:  3.6");
                
            }else if(sem==null&sess!=null){ //do if viewing result for session
                setSql("SELECT * FROM RESULTS WHERE STUDENT = '" + csField + "' AND SESSION = '"+sess+"'");
                //show course code column (MAGIC!)
                getCodeCol().setVisible(true);
                //print method
                getPrintBtn().setOnMouseClicked(e->printSessionResult());
                //set gpaLabel to show CGPA
                getGpaLabel().setText("CGPA:  3.6");
                
            }else if(sem==null&sess==null){ //do if viewing result for session
                setSql("SELECT * FROM RESULTS WHERE STUDENT = '" + csField + "' AND SESSION = '"+sess+"'");
                //hide course code column
                getCodeCol().setVisible(false);
                //print method
                getPrintBtn().setOnMouseClicked(e->System.out.println("Cannot print empty table"));
                //set gpaLabel to show CGPA
                getGpaLabel().setText("");
            }
            
            //populate rows
            try{
                setNameSQL("SELECT * FROM STUDENT WHERE REG_NO = '"+csField+"'");
                setData(FXCollections.observableArrayList());
                ResultSet rs = getC().createStatement().executeQuery(getSql());
                ResultSet rss = getC().createStatement().executeQuery(getNameSQL());
                rss.next();
                while(rs.next()){
                    Result result = new Result();
                    
                    String name = rss.getString(3)+", "+rss.getString(1)+" "+rss.getString(2);
                    result.setName(name);
                    System.out.println(name);
                    
                    result.setRegno(rs.getString(2));
                    result.setC_code(rs.getString(3));
                    result.setAttendance(new BigDecimal(rs.getDouble(9)));
                    result.setAssignment(new BigDecimal(rs.getDouble(10)));
                    result.setQuiz(new BigDecimal(rs.getDouble(11)));
                    result.setExam(new BigDecimal(rs.getDouble(12)));
                    result.setTotal(new BigDecimal(rs.getDouble(13)));
                    result.setGrade(rs.getString(14));

                    getData().add(result);
                    getResultTable().setItems(getData());
                }
            }catch (SQLException ex){
                Logger.getLogger(ResultViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(sql);
    }
    
    /**
     * find the gp by multiplying the credit unit of
     * the course by the grade value
     * @param val the grade value
     * @param cu the credit unit
     * @return 
     */
    public BigDecimal findGp(int val, int cu){
        BigDecimal gp;
        gp = new BigDecimal(val).multiply(new BigDecimal(cu));
        return gp;
    }
    
    /**
     * calculate the gpa by dividing the gp for the semester
     * by the total credit unit of the semester
     * @param gp
     * @param cu
     * @return 
     */
    public BigDecimal calculateGPA(BigDecimal gp,BigDecimal cu){
        BigDecimal gpa;
        gpa = gp.divide(cu);        
        return gpa;
    }
    
    /**
     * print all results for a course in a session
     */
    private void printAllResults(){
        if(getData()==null){
            System.out.println("Can't print empty table.");
        }else{
            System.out.println("Printing all results for this course...");
        }
    }
    
    /**
     * print all results for a student in a semester
     */
    private void printSemesterResult(){
        if(getData()==null){
            System.out.println("Can't print empty table.");
        }else{
            System.out.println("Printing student's result by semester...");
        }
    }
    
    /**
     * print all results for a student in a session
     */
    private void printSessionResult(){
        if(getData()==null){
            System.out.println("Can't print empty table.");
        }else{
            System.out.println("Printing student's result by session...");
        }
    }
    
    /**
     * populate sessioncombo from db
     */
    private void fillSessionCombo(){
        setSe_list(FXCollections.observableArrayList());
        try{
            getSe_list().addAll("2015/2016","2016/2017");
            getSessionCombo().setItems(getSe_list());
        }catch (Exception ex){
            System.out.println("Error occured building session data");
            System.out.println(ex);
        }
    }
    
    /**
     * populate semestercombo from db
     */
    private void fillSemesterCombo(){
        setSm_list(FXCollections.observableArrayList());
        try{
            getSm_list().addAll(null,1,2);
            getSemesterCombo().setItems(getSm_list());
        }catch (Exception ex){
            System.out.println("Error occured building semester data");
            System.out.println(ex);
        }
    }
    
    /**
     * This method runs when the add result button is clicked
     * @throws IOException 
     */
    public void openAddResultView() throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddResultView.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            
            AddResultViewController controller = fxmlLoader.<AddResultViewController>getController();

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
     * @return the viewByCourseBtn
     */
    public Label getViewByCourseBtn() {
        return viewByCourseBtn;
    }

    /**
     * @param viewByCourseBtn the viewByCourseBtn to set
     */
    public void setViewByCourseBtn(Label viewByCourseBtn) {
        this.viewByCourseBtn = viewByCourseBtn;
    }

    /**
     * @return the viewByStudentBtn
     */
    public Label getViewByStudentBtn() {
        return viewByStudentBtn;
    }

    /**
     * @param viewByStudentBtn the viewByStudentBtn to set
     */
    public void setViewByStudentBtn(Label viewByStudentBtn) {
        this.viewByStudentBtn = viewByStudentBtn;
    }

    /**
     * @return the course_or_studentTextField
     */
    public TextField getCourse_or_studentTextField() {
        return course_or_studentTextField;
    }

    /**
     * @param course_or_studentTextField the course_or_studentTextField to set
     */
    public void setCourse_or_studentTextField(TextField course_or_studentTextField) {
        this.course_or_studentTextField = course_or_studentTextField;
    }

    /**
     * @return the sessionCombo
     */
    public ComboBox<String> getSessionCombo() {
        return sessionCombo;
    }

    /**
     * @param sessionCombo the sessionCombo to set
     */
    public void setSessionCombo(ComboBox<String> sessionCombo) {
        this.sessionCombo = sessionCombo;
    }

    /**
     * @return the semesterCombo
     */
    public ComboBox<Integer> getSemesterCombo() {
        return semesterCombo;
    }

    /**
     * @param semesterCombo the semesterCombo to set
     */
    public void setSemesterCombo(ComboBox<Integer> semesterCombo) {
        this.semesterCombo = semesterCombo;
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
     * @return the resultTable
     */
    public TableView<Result> getResultTable() {
        return resultTable;
    }

    /**
     * @param resultTable the resultTable to set
     */
    public void setResultTable(TableView<Result> resultTable) {
        this.resultTable = resultTable;
    }

    /**
     * @return the nameCol
     */
    public TableColumn<Student, String> getNameCol() {
        return nameCol;
    }

    /**
     * @param nameCol the nameCol to set
     */
    public void setNameCol(TableColumn<Student, String> nameCol) {
        this.nameCol = nameCol;
    }

    /**
     * @return the regnoCol
     */
    public TableColumn<Result, String> getRegnoCol() {
        return regnoCol;
    }

    /**
     * @param regnoCol the regnoCol to set
     */
    public void setRegnoCol(TableColumn<Result, String> regnoCol) {
        this.regnoCol = regnoCol;
    }

    /**
     * @return the attendanceCol
     */
    public TableColumn<Result, BigDecimal> getAttendanceCol() {
        return attendanceCol;
    }

    /**
     * @param attendanceCol the attendanceCol to set
     */
    public void setAttendanceCol(TableColumn<Result, BigDecimal> attendanceCol) {
        this.attendanceCol = attendanceCol;
    }

    /**
     * @return the assignmentCol
     */
    public TableColumn<Result, BigDecimal> getAssignmentCol() {
        return assignmentCol;
    }

    /**
     * @param assignmentCol the assignmentCol to set
     */
    public void setAssignmentCol(TableColumn<Result, BigDecimal> assignmentCol) {
        this.assignmentCol = assignmentCol;
    }

    /**
     * @return the quizCol
     */
    public TableColumn<Result, BigDecimal> getQuizCol() {
        return quizCol;
    }

    /**
     * @param quizCol the quizCol to set
     */
    public void setQuizCol(TableColumn<Result, BigDecimal> quizCol) {
        this.quizCol = quizCol;
    }

    /**
     * @return the examCol
     */
    public TableColumn<Result, BigDecimal> getExamCol() {
        return examCol;
    }

    /**
     * @param examCol the examCol to set
     */
    public void setExamCol(TableColumn<Result, BigDecimal> examCol) {
        this.examCol = examCol;
    }

    /**
     * @return the totalCol
     */
    public TableColumn<Result, BigDecimal> getTotalCol() {
        return totalCol;
    }

    /**
     * @param totalCol the totalCol to set
     */
    public void setTotalCol(TableColumn<Result, BigDecimal> totalCol) {
        this.totalCol = totalCol;
    }

    /**
     * @return the gradeCol
     */
    public TableColumn<Result, Character> getGradeCol() {
        return gradeCol;
    }

    /**
     * @param gradeCol the gradeCol to set
     */
    public void setGradeCol(TableColumn<Result, Character> gradeCol) {
        this.gradeCol = gradeCol;
    }

    /**
     * @return the gpaLabel
     */
    public Label getGpaLabel() {
        return gpaLabel;
    }

    /**
     * @param gpaLabel the gpaLabel to set
     */
    public void setGpaLabel(Label gpaLabel) {
        this.gpaLabel = gpaLabel;
    }

    /**
     * @return the printBtn
     */
    public Label getPrintBtn() {
        return printBtn;
    }

    /**
     * @param printBtn the printBtn to set
     */
    public void setPrintBtn(Label printBtn) {
        this.printBtn = printBtn;
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
    public ObservableList<Result> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ObservableList<Result> data) {
        this.data = data;
    }

    /**
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * @param sql the sql to set
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * @return the i
     */
    public int getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * @return the codeCol
     */
    public TableColumn<Result, String> getCodeCol() {
        return codeCol;
    }

    /**
     * @param codeCol the codeCol to set
     */
    public void setCodeCol(TableColumn<Result, String> codeCol) {
        this.codeCol = codeCol;
    }

    /**
     * @return the nameSQL
     */
    public String getNameSQL() {
        return nameSQL;
    }

    /**
     * @param nameSQL the nameSQL to set
     */
    public void setNameSQL(String nameSQL) {
        this.nameSQL = nameSQL;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
    
}
