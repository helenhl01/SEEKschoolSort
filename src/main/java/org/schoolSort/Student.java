package org.schoolSort;

import java.util.Objects;

public class Student {

    private Student(){}
    private String firstName;
    private String lastName;
    private String eid;
    private School school;
    private String schoolName;
    private Boolean po = false; //default false, have to set true
    private Boolean exec = false; //default false, have to set true
    private Boolean nonRegister = false; //default false, have to set true
    private String email;
    private String phone;
    private int carSpace;
    private Boolean hyperloop = false; //default false, have to set to true

    private int monday1;
    private int monday2;
    private int tuesday1;
    private int tuesday2;
    private int wednesday1;
    private int wednesday2;
    private int thursday1;
    private int thursday2;

    public String getSchoolName(){return schoolName;}
    public String getFirstName(){ return firstName;}
    public String getLastName(){ return lastName;}
    public String getEid(){ return eid;}
    public School getSchool(){ return school;}
    public Boolean getPo(){ return po;}
    public Boolean getExec(){ return exec;}
    public Boolean getNonRegister(){ return nonRegister;}
    public String getEmail(){return email;}
    public String getPhone(){return phone;}
    public int getCarSpace(){return carSpace;}
    public Boolean getHyperloop(){return hyperloop;}

    public int getMonday1(){ return monday1;}
    public int getMonday2(){ return monday2;}
    public int getTuesday1(){ return tuesday1;}
    public int getTuesday2(){ return tuesday2;}
    public int getWednesday1(){ return wednesday1;}
    public int getWednesday2(){ return wednesday2;}
    public int getThursday1(){ return thursday1;}
    public int getThursday2(){ return thursday2;}

    public Boolean isUnassigned(){
        if(school == null){ return true;}
        return false;
    }

    public Boolean hasPreference(){
        return (prefers("monday1") || prefers("monday2") ||
                prefers("tuesday1") || prefers("tuesday2") ||
                prefers("wednesday1") || prefers("wednesday2") ||
                prefers("thursday1") || prefers("thursday2"));
    }

    public Boolean prefers(String time){
        switch(time){
            case "monday1":
                return monday1 == 2;
            case "monday2":
                return monday2 == 2;
            case "tuesday1":
                return tuesday1 == 2;
            case "tuesday2":
                return tuesday2 == 2;
            case "wednesday1":
                return wednesday1 == 2;
            case "wednesday2":
                return wednesday2 == 2;
            case "thursday1":
                return thursday1 == 2;
            case "thursday2":
                return thursday2 == 2;
        }
        return false;
    }
    public Boolean isAvailableAt(String time){
        switch(time){
            case "monday1":
                return monday1 > 0;
            case "monday2":
                return monday2 > 0;
            case "tuesday1":
                return tuesday1 > 0;
            case "tuesday2":
                return tuesday2 > 0;
            case "wednesday1":
                return wednesday1 > 0;
            case "wednesday2":
                return wednesday2 > 0;
            case "thursday1":
                return thursday1 > 0;
            case "thursday2":
                return thursday2 > 0;
        }
        return false;
    }
    public void setFirstName(String firstName){ this.firstName = firstName;}
    public void setLastName(String lastName){ this.lastName = lastName;}
    public void setEid(String eid){ this.eid = eid;}
    public void setSchool(School school){ this.school = school;}
    public void setPo(Boolean po){ this.po = po;}
    public void setExec(Boolean exec){this.exec = exec;}
    public void setNonRegister(Boolean nonRegister){ this.nonRegister = nonRegister;}
    public void setEmail(String email){ this.email = email;}
    public void setPhone(String phone){ this.phone = phone;}
    public void setCarSpace(int carSpace){this.carSpace = carSpace;}
    public void setHyperloop(Boolean hyperloop){this.hyperloop = hyperloop;}
    public void setSchoolName(String schoolName){this.schoolName = schoolName;}

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
        String output = firstName + " " + lastName;
        if(Objects.equals(po, true)){ output += ", is a PO"; }
        if(Objects.equals(exec, true)){ output += ", is an exec";}
        if(Objects.equals(hyperloop, true)){output+=" (hyperloop)";}
        if(Objects.equals(nonRegister, true)){ output += ", will not register";}
        //if(school == null){ output+= ", unassigned";}
        //else{ output += ", assigned to " + school.getName();}
        if(!(school == null)){output += ", assigned to " + school.getName();}
        if(carSpace > 0){ output += ", can drive " + carSpace + " people";}
        output += ", available at ";
        if(monday1 == 1){ output += "Monday at 2:30, "; }
        if(monday1 == 2){ output += "Monday at 2:30 (preferred), "; }
        if(monday2 == 1){ output += "Monday at 3:30, "; }
        if(monday2 == 2){ output += "Monday at 3:30 (preferred), "; }

        if(tuesday1 == 1){ output += "Tuesday at 2:30, "; }
        if(tuesday1 == 2){ output += "Tuesday at 2:30 (preferred), "; }
        if(tuesday2 == 1){ output += "Tuesday at 3:30, "; }
        if(tuesday2 == 2){ output += "Tuesday at 3:30 (preferred), "; }

        if(wednesday1 == 1){ output += "Wednesday at 2:30, "; }
        if(wednesday1 == 2){ output += "Wednesday at 2:30 (preferred), "; }
        if(wednesday2 == 1){ output += "Wednesday at 3:30, "; }
        if(wednesday2 == 2){ output += "Wednesday at 3:30 (preferred), "; }

        if(thursday1 == 1){ output += "Thursday at 2:30, "; }
        if(thursday1 == 2){ output += "Thursday at 2:30 (preferred), "; }
        if(thursday2 == 1){ output += "Thursday at 3:30, "; }
        if(thursday2 == 2){ output += "Thursday at 3:30 (preferred), "; }
        return output;
    }
}
