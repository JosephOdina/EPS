package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
public class Program {
    private final SimpleStringProperty programName;
    private final SimpleStringProperty programDepartment;
    private final SimpleStringProperty programFaculty;
    
    public Program(){
        this.programName = new SimpleStringProperty();
        this.programDepartment = new SimpleStringProperty();
        this.programFaculty = new SimpleStringProperty();
    }
    
    /**
     * @return the programName
     */
    public String getProgamName(){
        return programName.get();
    }
    
    /**
     * @param progname the programName to set
     */
    public void setProgramName(String progname){
        programName.set(progname);
    }
    
    /**
     * @return the programDepartment
     */
    public String getProgramDepartment(){
        return programDepartment.get();
    }
    
    /**
     * @param depname the programDepartment to set
     */
    public void setProgramDepartment(String depname){
        programDepartment.set(depname);
    }
    
    /**
     * @return the programFaculty
     */
    public String getProgramFaculty(){
        return programFaculty.get();
    }
    
    /**
     * @param facname the programFaculty to set
     */
    public void setProgramFaculty(String facname){
        programDepartment.set(facname);
    }
}
