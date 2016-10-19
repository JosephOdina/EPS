package eps;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class AddResultViewController implements Initializable {
    @FXML private TextField couseCodeField;
    @FXML private ComboBox<String> sessionCombo;
    @FXML private Label goBtn;
    
    @FXML private TableView<Result> resultEntryTable;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, String> regnoCol;
    @FXML private TableColumn<Result, BigDecimal> attendanceCol;
    @FXML private TableColumn<Result, BigDecimal> assignmentCol;
    @FXML private TableColumn<Result, BigDecimal> quizCol;
    @FXML private TableColumn<Result, BigDecimal> examCol;
    @FXML private TableColumn<Result, Boolean> submitCol;

    private ObservableList<Result> data;
    private ObservableList<String> slist;
    private DBCon dbcon;
    private Connection c;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getResultEntryTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            getResultEntryTable().setPlaceholder(new Label("Double-click on a cell to edit it.\nPress 'ENTER' when done to commit edit."));
            getResultEntryTable().setEditable(true);
            getNameCol().setEditable(false);
            getRegnoCol().setEditable(false);
            
            getSubmitCol().setStyle("-fx-alignment:center");
            
            fillSessionCombo();
            
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(AddResultViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * clears table content before buildData
     * @throws java.lang.Exception
     */
    public void refreshTable() throws Exception{
        getResultEntryTable().setItems(null);
        setResultEntryTableCellValueFactory();
    }
    
    /**
     * set cell value factory for result entry table
     * @throws Exception 
     */
    public void setResultEntryTableCellValueFactory() throws Exception{
        try{
            getNameCol().setCellValueFactory(new PropertyValueFactory<>("name"));
            getRegnoCol().setCellValueFactory(new PropertyValueFactory<>("regno"));
            
            getAttendanceCol().setCellValueFactory(new PropertyValueFactory<>("attendance"));
            getAttendanceCol().cellFactoryProperty().set(TextFieldTableCell.<Result,BigDecimal>forTableColumn(new BigDecimalStringConverter()));
            getAttendanceCol().setOnEditCommit((CellEditEvent<Result, BigDecimal> event) -> {
                event.getTableView().getSelectionModel().getSelectedItem().setAttendance(event.getNewValue());
            });
            
            getAssignmentCol().setCellValueFactory(new PropertyValueFactory<>("assignment"));
            getAssignmentCol().cellFactoryProperty().set(TextFieldTableCell.<Result,BigDecimal>forTableColumn(new BigDecimalStringConverter()));
            getAssignmentCol().setOnEditCommit((CellEditEvent<Result, BigDecimal> event) -> {
                event.getTableView().getSelectionModel().getSelectedItem().setAssignment(event.getNewValue());
            });
            
            getQuizCol().setCellValueFactory(new PropertyValueFactory<>("quiz"));
            getQuizCol().cellFactoryProperty().set(TextFieldTableCell.<Result,BigDecimal>forTableColumn(new BigDecimalStringConverter()));
            getQuizCol().setOnEditCommit((CellEditEvent<Result, BigDecimal> event) -> {
                event.getTableView().getSelectionModel().getSelectedItem().setQuiz(event.getNewValue());
            });
            
            getExamCol().setCellValueFactory(new PropertyValueFactory<>("exam"));
            getExamCol().cellFactoryProperty().set(TextFieldTableCell.<Result,BigDecimal>forTableColumn(new BigDecimalStringConverter()));
            getExamCol().setOnEditCommit((CellEditEvent<Result, BigDecimal> event) -> {
                event.getTableView().getSelectionModel().getSelectedItem().setExam(event.getNewValue());
            });
            
            getSubmitCol().setSortable(false);

            setDbcon(new DBCon());
            setC(getDbcon().getCon());
            
            buildData();
        }catch(IllegalStateException | SQLException e){
            System.out.println(e);
            Logger.getLogger(AddResultViewController.class.getName()).log(Level.ALL, null, e);
        }
    }

    /**
     * build result entry table data
     * @throws java.sql.SQLException
     */
    public void buildData() throws SQLException{
        setData(FXCollections.observableArrayList());
        String csel = getCouseCodeField().getText().toUpperCase();
        String ssel = getSessionCombo().getSelectionModel().getSelectedItem();
        try {
            ResultSet rs = getC().createStatement().executeQuery("SELECT * FROM COURSE_REG WHERE COURSE_CODE = '"+csel+"' AND SESSION = '"+ssel+"'");
            ResultSet rss = getC().createStatement().executeQuery("SELECT * FROM STUDENT WHERE REG_NO IN (SELECT STUDENT FROM COURSE_REG WHERE COURSE_CODE = '"+csel+"' AND SESSION = '"+ssel+"')");
            
            while (rs.next() & rss.next()) {
                Result result = new Result();
                result.setName(rss.getString(3)+" "+rss.getString(1)+" "+rss.getString(2));
                result.setRegno(rs.getString(1));
                
                getSubmitCol().setSortable(false);
                // define a simple boolean cell value for the submit column so that the column will only be shown for non-empty rows.
                getSubmitCol().setCellValueFactory((TableColumn.CellDataFeatures<Result, Boolean> features) -> new SimpleBooleanProperty(features.getValue() != null));

                // create a cell value factory with an add button for each row in the table.
                getSubmitCol().setCellFactory((TableColumn<Result, Boolean> param) -> {
                    return new ButtonCell();
                });

                getData().add(result);
                getResultEntryTable().setItems(getData());
            }
        } catch (SQLException | NumberFormatException ex) {
            System.out.println("Error building course registration data");
            System.out.println(ex);
            Logger.getLogger(AddResultViewController.class.getName()).log(Level.ALL, null, ex);
        }
    }
    
    /**
     * fill session combobox
     *
     * @throws java.sql.SQLException
     */
    public void fillSessionCombo() throws SQLException, NullPointerException{
        slist = FXCollections.observableArrayList();
        String SQL = "SELECT SESSION FROM SESSION";
        try {
//            ResultSet rs = getC().createStatement().executeQuery(SQL);
//            while (rs.next()) {
//                Session session = new Session();
//                session.setSession(rs.getString(1));
//
//                slist.add(session.getSession());
//                sessionCombo.setItems(slist);
//            }
            
            // got frustrated trying to debug the session resultset error.
            // for what I'm trying to do, this works.
            slist.addAll("2015/2016","2016/2017");
            sessionCombo.setItems(slist);
        } catch (Exception ex) {
            System.out.println("Error occured building session data");
            System.out.println(ex);
            Logger.getLogger(AddResultViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * runs when submit button is clicked.
     */
    private char grade;
    public void submitResult() throws SQLException{
        Result r = getResultEntryTable().getSelectionModel().getSelectedItem();
        
        String reg_no = r.getRegno();
        String course_code = getCouseCodeField().getText().toUpperCase();
        String session = getSessionCombo().getSelectionModel().getSelectedItem();
        int semester;
        String lecturer;
        String level;
        String department;        
        BigDecimal attendance = r.getAttendance();
        BigDecimal assignment = r.getAssignment();
        BigDecimal quiz = r.getQuiz();
        BigDecimal exam = r.getExam();
        BigDecimal total;
        
        
        int unit;
        
        String getSemSQL = "SELECT SEMESTER FROM COURSE WHERE COURSE_CODE = '"+course_code+"'";
        String getLecSQL = "SELECT LECTURER FROM LEC_COURSE WHERE COURSE = '"+course_code+"'";
        String getLevSQL = "SELECT LEVEL FROM COURSE WHERE COURSE_CODE = '"+course_code+"'";
        String getDepSQL = "SELECT DEPARTMENT FROM COURSE WHERE COURSE_CODE = '"+course_code+"'";
        String getUnitSQL = "SELECT CREDIT_UNIT FROM COURSE WHERE COURSE_CODE = '"+course_code+"'";
        
        ResultSet semRs = getC().createStatement().executeQuery(getSemSQL);
        ResultSet lecRs = getC().createStatement().executeQuery(getLecSQL);
        ResultSet levRs = getC().createStatement().executeQuery(getLevSQL);
        ResultSet depRs = getC().createStatement().executeQuery(getDepSQL);
        ResultSet unitRs = getC().createStatement().executeQuery(getUnitSQL);
        
        semRs.next();
        semester = semRs.getInt(1);
        
        lecRs.next();
        lecturer = lecRs.getString(1);
        
        levRs.next();
        level = levRs.getString(1);
        
        depRs.next();
        department = depRs.getString(1);
        
        unitRs.next();
        unit = unitRs.getInt(1);
        
        total = exam.add(quiz).add(assignment).add(attendance);
        
        if((total.compareTo(new BigDecimal(-1)) == 1) & (total.compareTo(new BigDecimal(40)) == -1)){
            setGrade('F');
        }else if((total.compareTo(new BigDecimal(39)) == 1) & (total.compareTo(new BigDecimal(45)) == -1)){
            setGrade('E');
        }else if((total.compareTo(new BigDecimal(44)) == 1) & (total.compareTo(new BigDecimal(50)) == -1)){
            setGrade('D');
        }else if((total.compareTo(new BigDecimal(49)) == 1) & (total.compareTo(new BigDecimal(60)) == -1)){
            setGrade('C');
        }else if((total.compareTo(new BigDecimal(59)) == 1) & (total.compareTo(new BigDecimal(70)) == -1)){
            setGrade('B');
        }else if(total.compareTo(new BigDecimal(70)) == 1){
            setGrade('A');
        }
        
        String submitSQL = "INSERT INTO RESULTS("
                + "STUDENT,"
                + "COURSE_CODE,"
                + "SESSION,"
                + "SEMESTER,"
                + "LECTURER,"
                + "LEVEL,"
                + "DEPARTMENT,"
                + "ATTENDANCE,"
                + "ASSIGNMENT,"
                + "QUIZ,"
                + "EXAM,"
                + "TOTAL,"
                + "GRADE) VALUES ("
                + "'"+reg_no+"',"
                + "'"+course_code+"',"
                + "'"+session+"',"
                + ""+semester+","
                + "'"+lecturer+"',"
                + "'"+level+"',"
                + "'"+department+"',"
                + ""+attendance+","
                + ""+assignment+","
                + ""+quiz+","
                + ""+exam+","
                + ""+total+","
                + "'"+getGrade()+"')";
        
        int ro = getC().createStatement().executeUpdate(submitSQL);
        
        if(ro>0){
            Alert alert = new Alert(AlertType.CONFIRMATION, "Result submitted.", ButtonType.OK);
            alert.showAndWait();
        }
        System.out.println(r.getName()+" submitted.");
        
    }
    
    /**
     * @return the couseCodeField
     */
    public TextField getCouseCodeField() {
        return couseCodeField;
    }

    /**
     * @param couseCodeField the couseCodeField to set
     */
    public void setCouseCodeField(TextField couseCodeField) {
        this.couseCodeField = couseCodeField;
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
     * @return the resultEntryTable
     */
    public TableView<Result> getResultEntryTable() {
        return resultEntryTable;
    }

    /**
     * @param resultEntryTable the resultEntryTable to set
     */
    public void setResultEntryTable(TableView<Result> resultEntryTable) {
        this.resultEntryTable = resultEntryTable;
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
    public TableColumn<Student, String> getRegnoCol() {
        return regnoCol;
    }

    /**
     * @param regnoCol the regnoCol to set
     */
    public void setRegnoCol(TableColumn<Student, String> regnoCol) {
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
     * @return the submitCol
     */
    public TableColumn<Result, Boolean> getSubmitCol() {
        return submitCol;
    }

    /**
     * @param submitCol the submitCol to set
     */
    public void setSubmitCol(TableColumn<Result, Boolean> submitCol) {
        this.submitCol = submitCol;
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
     * @return the grade
     */
    public char getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(char grade) {
        this.grade = grade;
    }
    
    /**
     * The ButtonCell class
     */
    private class ButtonCell extends TableCell<Result, Boolean> {

        final Button cellButton = new Button("Submit");

        ButtonCell() {

            cellButton.setOnAction((ActionEvent t) -> {
                try {
                    // do something when button clicked
                    //...
                    submitResult();
                } catch (SQLException ex) {
                    Logger.getLogger(AddResultViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            cellButton.setAlignment(Pos.CENTER);
            cellButton.setStyle("-fx-background-color:#00507F; -fx-text-fill: #f2f2f2;");
            cellButton.setCursor(Cursor.HAND);
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    
}
