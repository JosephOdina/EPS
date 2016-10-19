package eps;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Chukwuka Odina
 */
class Semester {
    private final SimpleIntegerProperty semester;
    
    public Semester(){
        this.semester = new SimpleIntegerProperty();
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
