package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
class Department {
    private final SimpleStringProperty departmentName;

    public Department() {
        this.departmentName = new SimpleStringProperty();
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName.get();
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String depName) {
        departmentName.set(depName);
    }
    
    void setDepartmentName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
