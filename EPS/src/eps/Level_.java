package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
class Level_ {
    private final SimpleStringProperty level;
    
    public Level_(){
        this.level = new SimpleStringProperty();
    }
    
    /**
     * @return the level
     */
    public String getLevel(){
        return level.get();
    }
    
    /**
     * @param lvl the level to set
     */
    public void setLevel(String lvl){
        level.set(lvl);
    }
}
