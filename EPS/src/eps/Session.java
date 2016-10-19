package eps;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Chukwuka Odina
 */
class Session {
    private final SimpleStringProperty session;
    
    public Session(){
        this.session = new SimpleStringProperty();
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
}
