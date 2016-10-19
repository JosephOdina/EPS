package eps;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Chukwuka Odina
 */
public class CourseReg {
    private final SimpleStringProperty student, level, course, course_code, session;
    private final SimpleIntegerProperty semester;
    
    public CourseReg(){
        this.student = new SimpleStringProperty();
        this.level = new SimpleStringProperty();
        this.course = new SimpleStringProperty();
        this.course_code = new SimpleStringProperty();
        this.session = new SimpleStringProperty();
        this.semester = new SimpleIntegerProperty();
    }
    
    /**
     * 
     * @return the student
     */
    public String getStudent(){
        return student.get();
    }
    
    /**
     * 
     * @param std the student to set
     */
    public void setStudent(String std){
        student.set(std);
    }
    
    /**
     * 
     * @return the level
     */
    public String getLevel(){
        return level.get();
    }
    
    /**
     * 
     * @param lvl the level to set
     */
    public void setLevel(String lvl){
        level.set(lvl);
    }
    
    /**
     * 
     * @return the course
     */
    public String getCourse(){
        return course.get();
    }
    
    /**
     * 
     * @param crs the course to set
     */
    public void setCourse(String crs){
        course.set(crs);
    }
    
    /**
     * 
     * @return the course_code
     */
    public String getCourse_code(){
        return course_code.get();
    }
    
    /**
     * 
     * @param c_code the course_code to set
     */
    public void setCourse_code(String c_code){
        course_code.set(c_code);
    }
    
    /**
     * 
     * @return the session
     */
    public String getSession(){
        return session.get();
    }
    
    /**
     * 
     * @param sess the session to set
     */
    public void setSession(String sess){
        session.set(sess);
    }
    /**
     * 
     * @return the semester
     */
    public int getSemester(){
        return semester.get();
    }
    
    /**
     * 
     * @param sem the semester to set
     */
    public void setSemester(int sem){
        semester.set(sem);
    }
}
