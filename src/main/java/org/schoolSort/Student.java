package org.schoolSort;

import java.util.Objects;

public class Student {

    private Student(){}
    private String firstName;
    private String lastName;
    private String eid;
    private School school;
    private Boolean po;
    private Boolean exec;
    private Boolean nonRegister;
    //should i add an attribute for hyperloop
    //eventuylaly add email? idk if useful

    private int monday1;
    private int monday2;
    private int tuesday1;
    private int tuesday2;
    private int wednesday1;
    private int wednesday2;
    private int thursday1;
    private int thursday2;

    public Student(String firstName, String lastName, String eid, School school, Boolean po, Boolean exec, Boolean nonRegister, int monday1, int monday2, int tuesday1, int tuesday2, int wednesday1, int wednesday2, int thursday1, int thursday2){
        this.firstName = firstName;
        this.lastName = lastName;
        this.eid = eid;
        this.school = school;
        this.po = po;
        this.exec = exec;
        this.nonRegister = nonRegister;
        this.monday1 = monday1;
        this.monday2 = monday2;
        this.tuesday1 = tuesday1;
        this.tuesday2 = tuesday2;
        this.wednesday1 = wednesday1;
        this.wednesday2 = wednesday2;
        this.thursday1 = thursday1;
        this.thursday2 = thursday2;
    }

    public String getFirstName(){ return firstName;}
    public String getLastName(){ return lastName;}
    public String getEid(){ return eid;}
    public School getSchool(){ return school;}
    public Boolean getPo(){ return po;}
    public Boolean getExec(){ return exec;}
    public Boolean getNonRegister(){ return nonRegister;}

    public int getMonday1(){ return monday1;}
    public int getMonday2(){ return monday2;}
    public int getTuesday1(){ return tuesday1;}
    public int getTuesday2(){ return tuesday2;}
    public int getWednesday1(){ return wednesday1;}
    public int getWednesday2(){ return wednesday2;}
    public int getThursday1(){ return thursday1;}
    public int getThursday2(){ return thursday2;}

    public void setFirstName(String firstName){ this.firstName = firstName;}
    public void setLastName(String lastName){ this.lastName = lastName;}
    public void setEid(String eid){ this.eid = eid;}
    public void setSchool(School school){ this.school = school;}
    public void setPo(Boolean po){ this.po = po;}
    public void setExec(Boolean exec){this.exec = exec;}
    public void setNonRegister(Boolean nonRegister){ this.nonRegister = nonRegister;}

    public void setMonday1(int monday1){ this.monday1 = monday1;}
    public void setMonday2(int monday2){ this.monday2 = monday2;}
    public void setTuesday1(int tuesday1){ this.tuesday1 = tuesday1;}
    public void setTuesday2(int tuesday2){ this.tuesday2 = tuesday2;}
    public void setWednesday1(int wednesday1){ this.wednesday1 = wednesday1;}
    public void setWednesday2(int wednesday2){ this.wednesday2 = wednesday2;}
    public void setThursday1(int thursday1){ this.thursday1 = thursday1;}
    public void setThursday2(int thursday2){ this.thursday2 = thursday2;}

    @Override
    public String toString(){
        String output = this.firstName + " " + this.lastName + " " + this.eid;
        if(Objects.equals(this.po, true)){ output += ", is a PO"; }
        if(Objects.equals(this.exec, true)){ output += ", is an exec";}
        if(Objects.equals(this.nonRegister, true)){ output += ", will not register";}
        if(this.school == null){ output+= ", unassigned";}
        else{ output += ", assigned to " + this.school.getName();}
        output += ", available at ";
        if(this.monday1 == 1){ output += "Monday at 2:30, "; }
        if(this.monday1 == 2){ output += "Monday at 2:30 (preferred), "; }
        if(this.monday2 == 1){ output += "Monday at 3:30, "; }
        if(this.monday2 == 2){ output += "Monday at 3:30 (preferred), "; }

        if(this.tuesday1 == 1){ output += "Tuesday at 2:30, "; }
        if(this.tuesday1 == 2){ output += "Tuesday at 2:30 (preferred), "; }
        if(this.tuesday2 == 1){ output += "Tuesday at 3:30, "; }
        if(this.tuesday2 == 2){ output += "Tuesday at 3:30 (preferred), "; }

        if(this.wednesday1 == 1){ output += "Wednesday at 2:30, "; }
        if(this.wednesday1 == 2){ output += "Wednesday at 2:30 (preferred), "; }
        if(this.wednesday2 == 1){ output += "Wednesday at 3:30, "; }
        if(this.wednesday2 == 2){ output += "Wednesday at 3:30 (preferred), "; }

        if(this.thursday1 == 1){ output += "Thursday at 2:30, "; }
        if(this.thursday1 == 2){ output += "Thursday at 2:30 (preferred), "; }
        if(this.thursday2 == 1){ output += "Thursday at 3:30, "; }
        if(this.thursday2 == 2){ output += "Thursday at 3:30 (preferred), "; }
        return output;
    }
}
