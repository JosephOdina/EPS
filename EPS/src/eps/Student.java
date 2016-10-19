package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
public class Student {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty regNumber;
    private final SimpleStringProperty sex;
    private final SimpleStringProperty level;
    private final SimpleStringProperty faculty;
    private final SimpleStringProperty department;
    private final SimpleStringProperty program;
    
    public Student(){
        this.firstName = new SimpleStringProperty();
        this.middleName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.regNumber = new SimpleStringProperty();
        this.sex = new SimpleStringProperty();
        this.level = new SimpleStringProperty();
        this.faculty = new SimpleStringProperty();
        this.department = new SimpleStringProperty();
        this.program = new SimpleStringProperty();
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * @param fName the firstName to set
     */
    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName.get();
    }

    /**
     * @param mName the middleName to set
     */
    public void setMiddleName(String mName) {
        middleName.set(mName);
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * @param lName the lastName to set
     */
    public void setLastName(String lName) {
        lastName.set(lName);
    }

    /**
     * @return the regNumber
     */
    public String getRegNumber() {
        return regNumber.get();
    }

    /**
     * @param regno the regNumber to set
     */
    public void setRegNumber(String regno) {
        regNumber.set(regno);
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex.get();
    }

    /**
     * @param sx the sex to set
     */
    public void setSex(String sx) {
        sex.set(sx);
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level.get();
    }

    /**
     * @param lvl the level to set
     */
    public void setLevel(String lvl) {
        level.set(lvl);
    }

    /**
     * @return the faculty
     */
    public String getFaculty() {
        return faculty.get();
    }

    /**
     * @param fac the faculty to set
     */
    public void setFaculty(String fac) {
        faculty.set(fac);
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department.get();
    }

    /**
     * @param dept the department to set
     */
    public void setDepartment(String dept) {
        department.set(dept);
    }

    /**
     * @return the program
     */
    public String getProgram() {
        return program.get();
    }

    /**
     * @param prog the program to set
     */
    public void setProgram(String prog) {
        program.set(prog);
    }
    
}
