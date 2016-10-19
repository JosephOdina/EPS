package eps;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
public class Course{
    private final SimpleStringProperty courseName;
    private final SimpleStringProperty courseCode;
    private final SimpleStringProperty level_;
    private final SimpleStringProperty department;
    private final SimpleStringProperty teacher;
    private final SimpleIntegerProperty semester;
    private final SimpleIntegerProperty creditUnit;

    public Course(){
        this.courseName = new SimpleStringProperty();
        this.courseCode = new SimpleStringProperty();
        this.level_ = new SimpleStringProperty();
        this.department = new SimpleStringProperty();
        this.semester = new SimpleIntegerProperty();
        this.teacher = new SimpleStringProperty();
        this.creditUnit = new SimpleIntegerProperty();
    }

    /**
     * @return the courseName
     */
    public String getCourseName() {
        return courseName.get();
    }

    /**
     * @param cName the courseName to set
     */
    public void setCourseName(String cName) {
        courseName.set(cName);
    }
    
    /**
     * 
     * @return the courseCode
     */
    public String getCourseCode(){
        return courseCode.get();
    }
    
    /**
     * 
     * @param c_code the c_code to set
     */
    public void setCourseCode(String c_code){
        courseCode.set(c_code);
    }
    
    /**
     * 
     * @return the creditUnit
     */
    public int getCreditUnit(){
        return creditUnit.get();
    }
    
    /**
     * 
     * @param credit the creditUnit to set
     */
    public void setCreditUnit(int credit){
        creditUnit.set(credit);
    }
    
    /**
     * 
     * @return the level
     */
    public String getLevel(){
        return level_.get();
    }
    
    /**
     * 
     * @param lvl the level to set
     */
    public void setLevel(String lvl){
        level_.set(lvl);
    }
    
    /**
     * 
     * @return the department
     */
    public String getDepartment(){
        return department.get();
    }
    
    /**
     * 
     * @param  dept the department to set
     */
    public void setDepartment(String dept){
        department.set(dept);
    }
    
    /**
     * 
     * @return the semester
     */
    public int getSemester(){
        return semester.get();
    }
    
    /**
     * @param sem the semester to set
     */
    public void setSemester(int sem){
        semester.set(sem);
    }
    
    /**
     *
     * @return the teacher
     */
    public String getTeacher(){
        return teacher.get();
    }
    
    /**
     * @param tchr the teacher to set
     */
    public void setTeacher(String tchr){
        teacher.set(tchr);
    }
}
