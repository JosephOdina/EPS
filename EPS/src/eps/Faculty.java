package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
class Faculty {
    private final SimpleStringProperty facultyName;

    public Faculty() {
        this.facultyName = new SimpleStringProperty();
    }

    /**
     * @return the facultyName
     */
    public String getFacultyName() {
        return facultyName.get();
    }

    /**
     * @param facultyName the facultyName to set
     */
    public void setFacultyName(String facName) {
        facultyName.set(facName);
    }
    
}
