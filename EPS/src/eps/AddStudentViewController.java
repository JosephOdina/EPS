package eps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Chukwuka Odina
 */
public class AddStudentViewController implements Initializable {
    
    private FileChooser fileChooser = new FileChooser();
    //textfields
    @FXML private TextField firstName;
    @FXML private TextField middleName;
    @FXML private TextField lastName;
    @FXML private TextField regNumber;
    //fac,dep,prog,level comboboxes
    @FXML private ComboBox<String> facultyCombo;
    @FXML private ComboBox<String> departmentCombo;
    @FXML private ComboBox<String> programCombo;
    @FXML private ComboBox<String> levelCombo;
    //sex buttons
    @FXML private RadioButton femaleRadioButton;
    @FXML private RadioButton maleRadioButton;
    //alert info
    @FXML private Label info_bar;
    //add photo button
    @FXML private Label addPhotoButton;
    
    @FXML private Pane imagePane;
    //student photo
    @FXML private ImageView imageView;
    //submit button
    @FXML private Button createStudentButton;
    
    @FXML private ToggleGroup sexRadioGroup;
    //db connection
    private DBCon dbcon;
    private Connection c;
    // SQL
    private String updateSQL;
    private String getFacultySQL;
    private String getDepartmentSQL;
    private String getProgramSQL;
    private String getLevelSQL;
    // resultsets
    private ResultSet getFacultyRs;
    private ResultSet getDepartmentRs;
    private ResultSet getProgramRs;
    private ResultSet getLevelRs;
    //combobox selected items
    private String fac_selected;
    private String dep_selected;
    private String prog_selected;
    private String level_selected;
    //observablelists for comboboxes
    private ObservableList<String> flist;
    private ObservableList<String> dlist;
    private ObservableList<String> plist;
    private ObservableList<String> llist;
    //infobar response
    private String response;
    
    private File file;
    private FileInputStream pic;
    /**
     * submit input to db
     * @throws java.sql.SQLException
     * @throws java.sql.SQLTimeoutException
     * @throws java.lang.InterruptedException
     * @throws java.io.FileNotFoundException
     */
    public void handleCreateStudentAction() throws SQLException, SQLTimeoutException, InterruptedException, FileNotFoundException{
        PreparedStatement ps;
        try{
            getC().setAutoCommit(false);
            String fname = getFirstName().getText();
            String mname = getMiddleName().getText();
            String lname = getLastName().getText();
            String regno = getRegNumber().getText();
            String gender = "Male";
            if(getFemaleRadioButton().isSelected()){
                gender = "Female";
            }
            String lvl = getLevelCombo().getValue();
            String dep = getDepartmentCombo().getValue();
            String fac = getFacultyCombo().getValue();            
            String prog = getProgramCombo().getValue();                      
            setPic(new FileInputStream(getFile()));
            
            ps = getC().prepareStatement(getUpdateSQL());
            ps.setString(1, fname);
            ps.setString(2, mname);
            ps.setString(3, lname);
            ps.setString(4, regno);
            ps.setString(5, gender);
            ps.setString(6, lvl);
            ps.setString(7, dep);
            ps.setString(8, fac);
            ps.setString(9, prog);
            ps.setBinaryStream(10, getPic(), (int) file.length());
            int ro = ps.executeUpdate();
            getC().commit();

            if(ro>0){
                getInfo_bar().setText(fname+" "+lname+" "+getResponse());
                getFirstName().clear();
                getMiddleName().clear();
                getLastName().clear();
                getRegNumber().clear();
                getFacultyCombo().getSelectionModel().clearSelection();
                getDepartmentCombo().getItems().clear();
                getProgramCombo().getItems().clear();
                getLevelCombo().getSelectionModel().clearSelection();
                getImageView().setImage(null);
            }else{
                getInfo_bar().setText("Error creating student.");
            }
        }catch(SQLException sex){
            System.out.println(sex);
            getInfo_bar().setText("Error creating student. If error persists, contact Administrator.");
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            dbcon = new DBCon();
            setC(dbcon.getCon());
            fillFacultyCombo();
            fillLevelCombo();
        } catch (SQLException ex) {
            Logger.getLogger(AddStudentViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Populate Faculty ComboBox from Faculty table in db.
     * Runs on initialization
     * @throws SQLException 
     */
    public void fillFacultyCombo() throws SQLException{
        setFlist(FXCollections.observableArrayList());
        try{
            setGetFacultyRs(getC().createStatement().executeQuery("SELECT * FROM FACULTY"));
            while(getGetFacultyRs().next()){
                Faculty faculty = new Faculty();
                faculty.setFacultyName(getGetFacultyRs().getString(1));
                
                getFlist().add(faculty.getFacultyName());
                getFacultyCombo().setItems(getFlist());
                
                getDepartmentCombo().setItems(null);
                getProgramCombo().setItems(null);
            }
        }catch(Exception ex){
            System.out.println("Error building Faculty data");
            System.out.println(ex);
        }
    }
    
    /**
     * Populate Department ComboBox from Department table in db
     * based on selected Faculty in Faculty ComboBox
     * @throws SQLException 
     */
    @FXML public void fillDepartmentCombo() throws SQLException{
        setFac_selected(getFacultyCombo().getSelectionModel().getSelectedItem());
        setDlist(FXCollections.observableArrayList());
        try{
            setGetDepartmentRs(getC().createStatement().executeQuery("SELECT * FROM DEPARTMENT WHERE FACULTY = '"+getFac_selected()+"'"));
            while(getGetDepartmentRs().next()){
                Department department = new Department();
                department.setDepartmentName(getGetDepartmentRs().getString(1));
                
                getDlist().add(department.getDepartmentName());
                getDepartmentCombo().setItems(getDlist());
                getProgramCombo().setItems(null);
            }
        }catch(Exception ex){
            System.out.println("Error building Department data");
            System.out.println(ex);
        }
    }
    
    /**
     * Populate Program ComboBox from Course table in db
     * based on selected Department in Department ComboBox
     * @throws SQLException 
     */
    @FXML public void fillProgramCombo() throws SQLException{
        setGetProgramSQL("SELECT * FROM PROGRAM WHERE DEPARTMENT = '"+getDep_selected()+"'");
        setDep_selected(getDepartmentCombo().getSelectionModel().getSelectedItem());
        setPlist(FXCollections.observableArrayList());
        try{
            setGetProgramRs(getC().createStatement().executeQuery(getGetProgramSQL()));
            while(getGetProgramRs().next()){
                Program program = new Program();
                program.setProgramName(getGetProgramRs().getString(1));
                
                getPlist().add(program.getProgamName());
                getProgramCombo().setItems(getPlist());
            }
        }catch(Exception ex){
            System.out.println("Error building Program data");
            System.out.println(ex);
        }
    }
    
    /**
     * Populate Level ComboBox from level table in db
     * @throws java.sql.SQLException
     */
    public void fillLevelCombo() throws SQLException{
        setLlist(FXCollections.observableArrayList());
        setGetLevelSQL("SELECT * FROM LEVEL");
        try{
            setGetLevelRs(getC().createStatement().executeQuery(getGetLevelSQL()));
            while(getGetLevelRs().next()){
                Level_ level = new Level_();
                level.setLevel(getGetLevelRs().getString(1));
                
                getLlist().add(level.getLevel());
                getLevelCombo().setItems(getLlist());
            }
        }catch(Exception ex){
            System.out.println("Error building Level data");
            System.out.println(ex);
        }
    }
    
    /**
     *  Runs when add photo is clicked
     */
    @FXML private void selectIimage() {
        configureFileChooser(getFileChooser());
        setFile(getFileChooser().showOpenDialog(null));
        if (getFile()!=null) {
            System.out.println(getFile().getAbsolutePath());
            try {
                BufferedImage bufferedImage = ImageIO.read(getFile());
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                
                getImageView().setImage(image);
                
                getImageView().setFitHeight(getImagePane().getHeight());
                getImageView().setPreserveRatio(true);
                
                //hide overflow
                getImagePane().setClip(new Rectangle(220,250));
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    
    /**
     * Configure file chooser start directory and file type
     * @param fileChooser
     */
    private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("Select student photo");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG","*.jpg")
        );
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
     * @return the regNumber
     */
    public TextField getRegNumber() {
        return regNumber;
    }

    /**
     * @param regNumber the regNumber to set
     */
    public void setRegNumber(TextField regNumber) {
        this.regNumber = regNumber;
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
     * @return the addPhotoButton
     */
    public Label getAddPhotoButton() {
        return addPhotoButton;
    }

    /**
     * @param addPhotoButton the addPhotoButton to set
     */
    public void setAddPhotoButton(Label addPhotoButton) {
        this.addPhotoButton = addPhotoButton;
    }

    /**
     * @return the imageView
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * @param imageView the imageView to set
     */
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * @return the createStudentButton
     */
    public Button getCreateStudentButton() {
        return createStudentButton;
    }

    /**
     * @param createStudentButton the createStudentButton to set
     */
    public void setCreateStudentButton(Button createStudentButton) {
        this.createStudentButton = createStudentButton;
    }

    /**
     * @return the sexRadioGroup
     */
    public ToggleGroup getSexRadioGroup() {
        return sexRadioGroup;
    }

    /**
     * @param sexRadioGroup the sexRadioGroup to set
     */
    public void setSexRadioGroup(ToggleGroup sexRadioGroup) {
        this.sexRadioGroup = sexRadioGroup;
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
     * @return the maleRadioButton
     */
    public RadioButton getMaleRadioButton() {
        return maleRadioButton;
    }

    /**
     * @param maleRadioButton the maleRadioButton to set
     */
    public void setMaleRadioButton(RadioButton maleRadioButton) {
        this.maleRadioButton = maleRadioButton;
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
     * @return the programCombo
     */
    public ComboBox<String> getProgramCombo() {
        return programCombo;
    }

    /**
     * @param programCombo the programCombo to set
     */
    public void setProgramCombo(ComboBox<String> programCombo) {
        this.programCombo = programCombo;
    }

    /**
     * @return the levelCombo
     */
    public ComboBox<String> getLevelCombo() {
        return levelCombo;
    }

    /**
     * @param levelCombo the levelCombo to set
     */
    public void setLevelCombo(ComboBox<String> levelCombo) {
        this.levelCombo = levelCombo;
    }

    /**
     * @return the fileChooser
     */
    public FileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * @param fileChooser the fileChooser to set
     */
    public void setFileChooser(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * @return the imagePane
     */
    public Pane getImagePane() {
        return imagePane;
    }

    /**
     * @param imagePane the imagePane to set
     */
    public void setImagePane(Pane imagePane) {
        this.imagePane = imagePane;
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
     * @return the llist
     */
    public ObservableList<String> getLlist() {
        return llist;
    }

    /**
     * @param llist the llist to set
     */
    public void setLlist(ObservableList<String> llist) {
        this.llist = llist;
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
     * @return the getFacultySQL
     */
    public String getGetFacultySQL() {
        return getFacultySQL;
    }

    /**
     * @param getFacultySQL the getFacultySQL to set
     */
    public void setGetFacultySQL(String getFacultySQL) {
        this.getFacultySQL = getFacultySQL;
    }

    /**
     * @return the getDepartmentSQL
     */
    public String getGetDepartmentSQL() {
        return getDepartmentSQL;
    }

    /**
     * @param getDepartmentSQL the getDepartmentSQL to set
     */
    public void setGetDepartmentSQL(String getDepartmentSQL) {
        this.getDepartmentSQL = getDepartmentSQL;
    }

    /**
     * @return the getProgramSQL
     */
    public String getGetProgramSQL() {
        return getProgramSQL;
    }

    /**
     * @param getProgramSQL the getProgramSQL to set
     */
    public void setGetProgramSQL(String getProgramSQL) {
        this.getProgramSQL = getProgramSQL;
    }

    /**
     * @return the getLevelSQL
     */
    public String getGetLevelSQL() {
        return getLevelSQL;
    }

    /**
     * @param getLevelSQL the getLevelSQL to set
     */
    public void setGetLevelSQL(String getLevelSQL) {
        this.getLevelSQL = getLevelSQL;
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
     * @return the getProgramRs
     */
    public ResultSet getGetProgramRs() {
        return getProgramRs;
    }

    /**
     * @param getProgramRs the getProgramRs to set
     */
    public void setGetProgramRs(ResultSet getProgramRs) {
        this.getProgramRs = getProgramRs;
    }

    /**
     * @return the getLevelRs
     */
    public ResultSet getGetLevelRs() {
        return getLevelRs;
    }

    /**
     * @param getLevelRs the getLevelRs to set
     */
    public void setGetLevelRs(ResultSet getLevelRs) {
        this.getLevelRs = getLevelRs;
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
     * @return the prog_selected
     */
    public String getProg_selected() {
        return prog_selected;
    }

    /**
     * @param prog_selected the prog_selected to set
     */
    public void setProg_selected(String prog_selected) {
        this.prog_selected = prog_selected;
    }

    /**
     * @return the level_selected
     */
    public String getLevel_selected() {
        return level_selected;
    }

    /**
     * @param level_selected the level_selected to set
     */
    public void setLevel_selected(String level_selected) {
        this.level_selected = level_selected;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
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

    /**
     * @return the pic
     */
    public FileInputStream getPic() {
        return pic;
    }

    /**
     * @param pic the pic to set
     */
    public void setPic(FileInputStream pic) {
        this.pic = pic;
    }

}
