package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
public class Rank {
    private final SimpleStringProperty rankName;
    
    public Rank(){
        this.rankName = new SimpleStringProperty();
    }

    /**
     * @return the rankName
     */
    public String getRankName() {
        return rankName.get();
    }

    /**
     * @param Rname the rankName to set
     */
    public void setRankName(String Rname) {
        rankName.set(Rname);
    }
}
