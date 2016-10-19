
package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
public class Staff {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty sex;
    private final SimpleStringProperty course;
    private final SimpleStringProperty department;
    private final SimpleStringProperty faculty;
    private final SimpleStringProperty rank;
    private final SimpleStringProperty staffID;
    private final SimpleStringProperty password;
    
    public Staff(){
        this.firstName = new SimpleStringProperty();
        this.middleName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.sex = new SimpleStringProperty();
        this.course = new SimpleStringProperty();
        this.department = new SimpleStringProperty();
        this.faculty = new SimpleStringProperty();
        this.rank = new SimpleStringProperty();
        this.staffID = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
    }

    /**
     * @return the firstNname
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * @param Fname the firstName to set
     */
    public void setFname(String Fname) {
        firstName.set(Fname);
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName.get();
    }

    /**
     * @param Mname the middleName to set
     */
    public void setMname(String Mname) {
        middleName.set(Mname);
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * @param Lname the lastName to set
     */
    public void setLname(String Lname) {
        lastName.set(Lname);
    }
    
    /**
     * 
     * @return the sex
     */
    public String getSex(){
        return sex.get();
    }
    
    /**
     * 
     * @param Sex the sex to set
     */
    public void setSex(String Sex){
        sex.set(Sex);
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
     * @param Course the course to set
     */
    public void setCourse(String Course){
        course.set(Course);
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
     * @param Department the department to set
     */
    public void setDepartment(String Department){
        department.set(Department);
    }
    
    /**
     * 
     * @return the faculty
     */
    public String getFaculty(){
        return faculty.get();
    }
    
    /**
     * 
     * @param Faculty the faculty to set
     */
    public void setFaculty(String Faculty){
        faculty.set(Faculty);
    }
    
    /**
     * 
     * @return the rank
     */
    public String getRank(){
        return rank.get();
    }
    
    /**
     * 
     * @param Rank the rank to set
     */
    public void setRank(String Rank){
        rank.set(Rank);
    }
    
    /**
     * 
     * @return the staffID
     */
    public String getStaffID(){
        return staffID.get();
    }
    
    /**
     * 
     * @param StaffID the staffID to set
     */
    public void setStaffID(String StaffID){
        staffID.set(StaffID);
    }
    
    /**
     * 
     * @return the password
     */
    public String getPassword(){
        return password.get();
    }
    
    /**
     * 
     * @param Password the password to set
     */
    public void setPassword(String Password){
        password.set(Password);
    }
}
