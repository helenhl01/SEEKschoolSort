package org.schoolSort;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class School {
    private String name;
    private String time;
    private int cap;
    private List<Student> studentList = new ArrayList<>();
    //full or not?
    //number of students assigned

    public int getCarCap(){
        int count = 0;
        for(Student x : studentList){
            count += x.getCarSpace();
        }
        return count;
    }
    public Boolean enoughRides(){
        if(getCarCap() >= studentList.size()){ return true;}
        return false;
    }
    public Boolean enoughDrivers(){
        if(getCarCap() >= cap){return true;}
        return false;
    }
    public Boolean full(){
        if(cap > studentList.size()){return false;}
        return true;
    }
    public int ridesNeeded(){
        assert enoughRides() == false : "This school already has enough rides";
        return studentList.size() - getCarCap();
    }
    public School(String name, String time, int cap){
        this.name = name;
        this.time = time;
        this.cap = cap;
    }
    public String getName(){return name;}
    public String getTime(){return time;}
    public int getCap(){return cap;}
    public List<Student> getStudentList(){return studentList;}

    public void setName(String name){ this.name = name;}
    public void setTime(String time){this.time = time;}
    public void setCap(int cap){this.cap = cap;}
    public void setStudentList(List<Student> studentList){this.studentList = studentList;}
    public void addStudent(Student student){this.studentList.add(student);}

    @Override
    public String toString(){
        String output = this.name + ", ";
        if(Objects.equals(this.time, "monday1")){ output+= "Monday at 2:30";}
        if(Objects.equals(this.time, "monday2")){ output+= "Monday at 3:30";}
        if(Objects.equals(this.time, "tuesday1")){ output+= "Tuesday at 2:30";}
        if(Objects.equals(this.time, "tuesday2")){ output+= "Tuesday at 3:30";}
        if(Objects.equals(this.time, "wednesday1")){ output+= "Wednesday at 2:30";}
        if(Objects.equals(this.time, "wednesday2")){ output+= "Wednesday at 3:30";}
        if(Objects.equals(this.time, "thursday1")){ output+= "Thursday at 2:30";}
        if(Objects.equals(this.time, "thursday2")){ output+= "Thursday at 3:30";}
        output+=", capacity " + cap + " mentors";
        output+=", mentor list: ";
        for(Student x : studentList){
            output+= x.getFirstName() + " " + x.getLastName() + ", ";
        }
        return output;
    }
}
