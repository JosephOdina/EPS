/*
 * The MIT License
 *
 * Copyright 2016 Chukwuka Odina.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eps;

/**
 *
 * @author Chukwuka Odina
 */
public class sessionmanager {
    private static String fname;
    private static String mname;
    private static String lname;
    private static String sex;
    private static String sid;
    private static String fac;
    private static String dep;
    private static String rank;
    private static String pwrd;
    
    public sessionmanager(){
        sessionmanager.fname =  new String();
        sessionmanager.mname = new String();
        sessionmanager.lname = new String();
        sessionmanager.sex = new String();
        sessionmanager.sid = new String();
        sessionmanager.fac = new String();
        sessionmanager.dep = new String();
        sessionmanager.rank = new String();
        sessionmanager.pwrd = new String();
    }

    /**
     * @return the fname
     */
    public static String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public static void setFname(String fname) {
        sessionmanager.fname = fname;
    }

    /**
     * @return the mname
     */
    public static String getMname() {
        return mname;
    }

    /**
     * @param mname the mname to set
     */
    public static void setMname(String mname) {
        sessionmanager.mname = mname;
    }

    /**
     * @return the lname
     */
    public static String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public static void setLname(String lname) {
        sessionmanager.lname = lname;
    }

    /**
     * @return the sex
     */
    public static String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public static void setSex(String sex) {
        sessionmanager.sex = sex;
    }

    /**
     * @return the sid
     */
    public static String getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public static void setSid(String sid) {
        sessionmanager.sid = sid;
    }

    /**
     * @return the fac
     */
    public static String getFac() {
        return fac;
    }

    /**
     * @param fac the fac to set
     */
    public static void setFac(String fac) {
        sessionmanager.fac = fac;
    }

    /**
     * @return the dep
     */
    public static String getDep() {
        return dep;
    }

    /**
     * @param dep the dep to set
     */
    public static void setDep(String dep) {
        sessionmanager.dep = dep;
    }

    /**
     * @return the rank
     */
    public static String getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public static void setRank(String rank) {
        sessionmanager.rank = rank;
    }

    /**
     * @return the pwrd
     */
    public static String getPwrd() {
        return pwrd;
    }

    /**
     * @param pwrd the pwrd to set
     */
    public static void setPwrd(String pwrd) {
        sessionmanager.pwrd = pwrd;
    }
}
