package org.example;
import java.util.*;
public class School {
    private String name;
    private String time;
    private int cap;
    private List<Student> studentList = new ArrayList<>();

    public String getName(){return name;}
    public String getTime(){return time;}
    public int getCap(){return cap;}
    public List<Student> getStudentList(){return studentList;}

    public void setName(String name){ this.name = name;}
    public void setTime(String time){this.time = time;}
    public void setCap(int cap){this.cap = cap;}
    public void setStudentList(List<Student> studentList){this.studentList = studentList;}
    public void addStudent(Student student){this.studentList.add(student);}
}
