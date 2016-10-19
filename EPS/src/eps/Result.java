package eps;

import javafx.beans.property.SimpleStringProperty;
import java.math.BigDecimal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Chukwuka Odina
 */
public class Result{
    private final SimpleIntegerProperty sn;
    private final SimpleStringProperty name;
    private final SimpleStringProperty regno;
    private final SimpleStringProperty c_code;
    private final ObjectProperty<BigDecimal> attendance;
    private final ObjectProperty<BigDecimal> assignment;
    private final ObjectProperty<BigDecimal> quiz;
    private final ObjectProperty<BigDecimal> exam;
    private final ObjectProperty<BigDecimal> total;
    private final SimpleStringProperty grade;
    
    
    public Result(){
        this.sn = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.regno = new SimpleStringProperty();
        this.c_code = new SimpleStringProperty();
        this.attendance = new SimpleObjectProperty<>();
        this.assignment = new SimpleObjectProperty<>();
        this.quiz = new SimpleObjectProperty<>();
        this.exam = new SimpleObjectProperty<>();
        this.total = new SimpleObjectProperty<>();
        this.grade = new SimpleStringProperty();
    }

    /**
     * @return the sn
     */
    public int getSn() {
        return sn.get();
    }

    /**
     * @param s the sn to set
     */
    public void setSn(int s) {
        sn.set(s);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     * @param n the name to set
     */
    public void setName(String n) {
        name.set(n);
    }

    /**
     * @return the regno
     */
    public String getRegno() {
        return regno.get();
    }

    /**
     * @param rno the regno to set
     */
    public void setRegno(String rno) {
        regno.set(rno);
    }

    /**
     * @return the attendance
     */
    public BigDecimal getAttendance() {
        return attendance.get();
    }

    /**
     * @param attn the attendance to set
     */
    public void setAttendance(BigDecimal attn) {
        attendance.set(attn);
    }

    /**
     * @return the assignment
     */
    public BigDecimal getAssignment() {
        return assignment.get();
    }

    /**
     * @param assn the assignment to set
     */
    public void setAssignment(BigDecimal assn) {
        assignment.set(assn);
    }

    /**
     * @return the quiz
     */
    public BigDecimal getQuiz() {
        return quiz.get();
    }

    /**
     * @param qz the quiz to set
     */
    public void setQuiz(BigDecimal qz) {
        quiz.set(qz);
    }

    /**
     * @return the exam
     */
    public BigDecimal getExam() {
        return exam.get();
    }

    /**
     * @param ex the exam to set
     */
    public void setExam(BigDecimal ex) {
        exam.set(ex);
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total.get();
    }

    /**
     * @param tot the total to set
     */
    public void setTotal(BigDecimal tot) {
        total.set(tot);
    }

    /**
     * @return the grade
     */
    public String getGrade() {
        return grade.get();
    }

    /**
     * @param grd the grade to set
     */
    public void setGrade(String grd) {
        grade.set(grd);
    }

    /**
     * @return the c_code
     */
    public String getC_code() {
        return c_code.get();
    }

    /**
     * @param code the c_code to set
     */
    public void setC_code(String code) {
        c_code.set(code);
    }
}
