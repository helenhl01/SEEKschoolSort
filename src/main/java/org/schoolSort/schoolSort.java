package org.schoolSort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class schoolSort {
    public static void main(String[] args) {
        Student[] studentArray = createStudentArray("../../Downloads/seekStudents.json");
        List<School> schoolList = schoolInits();
        //List<Student> studentList = studentInits(); //probably there's a way to build a ton of diff possible schedules and score them by fit but im not that smart
        fillSchool(schoolList.get(0), studentArray);
        System.out.println(unassignedStudents(studentArray));
        System.out.println(unfilledSchools(schoolList));
        createReport("test.txt", schoolList, studentArray);
        //implementation: list of schools, list of students, iterate through both, have a bool for full/not full, student assigned/unassigned
    }

    public static void createReport(String filename, List<School> schoolList, Student[] studentArray){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.append("School sort:");
            writer.newLine();
            for(School x : schoolList){
                writer.append(x.toString());
                writer.newLine();
            }
            writer.append(unfilledSchools(schoolList));
            writer.append(unassignedStudents(studentArray));
            //then add unfilled schools and unassigned students
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Student[] createStudentArray(String path){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Student[] studentArray = mapper.readValue(new File(path), Student[].class);
            //for(Student x : studentArray){
            //    System.out.println(x);
            //}
            return studentArray;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String unassignedStudents(Student[] studentArray){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Unassigned students: \n");
        int count = 0;
        for(Student x : studentArray){
            if(x.getSchool()==null){
                count++;
                buffer.append(x);
                buffer.append("\n");
            }
        }
        buffer.append(count + " students unassigned");
        String output = buffer.toString();
        return output;
    }

    public static String unfilledSchools(List<School> schoolList){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Unfilled schools: \n");
        for(School x : schoolList){
            if(x.getStudentList().size() < x.getCap()){
                buffer.append(x.getName() + " (" + formatTime(x.getTime()) + ") has " + (x.getCap() - x.getStudentList().size()) + " slots left\n" );
            }
        }
        String output = buffer.toString();
        return output;
    }

    public static String formatTime(String time){ //do one for unformat? actually idk
        switch(time){
            case "monday1":
                return "Monday at 2:30";
            case "monday2":
                return "Monday at 3:30";
            case "tuesday1":
                return "Tuesday at 2:30";
            case "tuesday2":
                return "Tuesday at 3:30";
            case "wednesday1":
                return "Wednesday at 2:30";
            case "wednesday2":
                return "Wednesday at 3:30";
            case "thursday1":
                return "Thursday at 2:30";
            case "thursday2":
                return "Thursday at 3:30";
        }
        return null;
    }
    public static Boolean isStudentAvailable(Student student, String time){
        switch(time){
            case "monday1":
                return student.getMonday1() > 0;
            case "monday2":
                return student.getMonday2() > 0;
            case "tuesday1":
                return student.getTuesday1() > 0;
            case "tuesday2":
                return student.getTuesday2() > 0;
            case "wednesday1":
                return student.getWednesday1() > 0;
            case "wednesday2":
                return student.getWednesday2() > 0;
            case "thursday1":
                return student.getThursday1() > 0;
            case "thursday2":
                return student.getThursday2() > 0;
        }
        return false;
    }

    public static Boolean doesStudentPrefer(Student student, String time){
        switch(time){
            case "monday1":
                return student.getMonday1() == 2;
            case "monday2":
                return student.getMonday2() == 2;
            case "tuesday1":
                return student.getTuesday1() == 2;
            case "tuesday2":
                return student.getTuesday2() == 2;
            case "wednesday1":
                return student.getWednesday1() == 2;
            case "wednesday2":
                return student.getWednesday2() == 2;
            case "thursday1":
                return student.getThursday1() == 2;
            case "thursday2":
                return student.getThursday2() == 2;
        }
        return false;
    }

    public static void fillSchool(School school, Student[] studentArray) {
        if (school.getStudentList().size() < school.getCap()) {
            for (Student x : studentArray) { //get preferred studnets first
                if ((x.getSchool() == null) && doesStudentPrefer(x, school.getTime())) {
                    x.setSchool(school);
                    school.addStudent(x);
                }
            }
        }

        for (Student x : studentArray) {
            if (school.getStudentList().size() < school.getCap()) {
                if ((x.getSchool() == null) && isStudentAvailable(x, school.getTime())) {
                    x.setSchool(school);
                    school.addStudent(x);
                }
            }
            if (school.getStudentList().size() >= school.getCap()) {
                System.out.println(school.getName() + " has been filled");
                System.out.println(school);
                break;
            }

        }
        if(school.getStudentList().size() < school.getCap()){System.out.println("out of students");}
    }

    public static int countStudents(String time, Student[] studentArray){
        int count = 0;
        switch(time){
            case "monday1":
                for(Student x : studentArray){
                    if(x.getMonday1() > 0){ count++;}
                }
            case "monday2":
                for(Student x : studentArray){
                    if(x.getMonday2() > 0){ count++;}
                }
            case "tuesday1":
                for(Student x : studentArray){
                    if(x.getTuesday1() > 0){ count++;}
                }
            case "tuesday2":
                for(Student x : studentArray){
                    if(x.getTuesday2() > 0){ count++;}
                }
            case "wednesday1":
                for(Student x : studentArray){
                    if(x.getWednesday1() > 0){ count++;}
                }
            case "wednesday2":
                for(Student x : studentArray){
                    if(x.getWednesday2() > 0){ count++;}
                }
            case "thursday1":
                for(Student x : studentArray){
                    if(x.getThursday1() > 0){ count++;}
                }
            case "thursday2":
                for(Student x : studentArray){
                    if(x.getThursday2() > 0){ count++;}
                };
        }
        return count;
    }

    public static void studentsAvail(String time, Student[] studentArray){
        //add a switch
        if(Objects.equals(time, "monday1")){
            for(Student x : studentArray){
                if(x.getMonday1() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getMonday1() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Monday at 2:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Monday at 2:30");
        }
        if(Objects.equals(time, "monday2")){
            for(Student x : studentArray){
                if(x.getMonday2() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getMonday2() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Monday at 3:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Monday at 3:30");
        }
        if(Objects.equals(time, "tuesday1")){
            for(Student x : studentArray){
                if(x.getTuesday1() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getTuesday1() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Tuesday at 2:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Tuesday at 2:30");
        }
        if(Objects.equals(time, "tuesday2")){
            for(Student x : studentArray){
                if(x.getTuesday2() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getTuesday2() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Tuesday at 3:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Tuesday at 3:30");
        }
        if(Objects.equals(time, "wednesday1")){
            for(Student x : studentArray){
                if(x.getWednesday1() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getWednesday1() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Wednesday at 2:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Wednesday at 2:30");
        }
        if(Objects.equals(time, "wednesday2")){
            for(Student x : studentArray){
                if(x.getWednesday2() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getWednesday2() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Wednesday at 3:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Wednesday at 3:30");
        }
        if(Objects.equals(time, "thursday1")){
            for(Student x : studentArray){
                if(x.getThursday1() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getThursday1() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Thursday at 2:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Thursday at 2:30");
        }
        if(Objects.equals(time, "thursday2")){
            for(Student x : studentArray){
                if(x.getThursday2() == 1){ System.out.print(x.getFirstName() + " " + x.getLastName());}
                else if(x.getThursday2() == 2){ System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");}
                else{continue;}
                if(x.getSchool() == null){ System.out.print(", school unassigned");}
                else{System.out.print(", assigned to " + x.getSchool());}
                if(x.getExec()){ System.out.print(", exec");}
                if(x.getPo()){ System.out.print(", po");}
                System.out.println(", is available Thursday at 3:30");
            }
            System.out.println("There are " + countStudents(time, studentArray) + " students available Thursday at 3:30");
        }

    }
    public static List<School> schoolInits(){
        List<School> schoolList = new ArrayList<>();
        schoolList.add(new School("school1", "monday1", 5));
        schoolList.add(new School("school2", "monday2", 3));
        schoolList.add(new School("school3", "tuesday1", 2));
        schoolList.add(new School("school4", "tuesday2", 1));
        schoolList.add(new School("school5", "wednesday1", 4));
        schoolList.add(new School("school6", "wednesday2", 5));
        schoolList.add(new School("school7", "thursday1", 3));
        schoolList.add(new School("school8", "thursday2", 4));
        return schoolList;
    }

    public static List<Student> studentInits(){ //need 27
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Sabah", "Jamal", "sj28467", null, false, false, false,0,0,1,1,0,0,1,1));
        studentList.add(new Student("Max Chung","Richter","mcr3587",null,false, false,false,1,1,0,0,0,0,0,0));
        studentList.add(new Student("Shraavan","Divakarla","sd36977",null, false, false, false,0,0,1,1,0,2,1,1));
        studentList.add(new Student("Tanya","Goyal","tg25523",null, false, false,false,0,0,0,0,1,0,0,0));
        studentList.add(new Student("Scott","Crowe","null",null, false, false,false, 0,0,1,1,0,0,1,1));
        studentList.add(new Student("Angel","Patel","ap54292",null, false, false,false,0,0,1,0,0,0,1,0));
        studentList.add(new Student("Mitali","Khoje","muk86",null, false, false,false,0,0,2,0,0,0,1,0));
        studentList.add(new Student("Ameil","Kumar","ask2373",null, false, false, false,0,0,1,1,1,0,1,1));
        studentList.add(new Student("Ruojun","Lu","RL34634",null, false, false, false,1,0,0,1,1,1,1,0));
        studentList.add(new Student("Ashley","Settle","aes5247",null, false, false,false, 0,0,1,1,0,0,0,0));
        studentList.add(new Student("Naisha","Singh","ns35384",null, false, false, false,0,1,0,0,0,1,0,0));
        studentList.add(new Student("Alex","Koo","gk7244", null, false, false,false,1,0,0,0,0,0,0,0));
        studentList.add(new Student("Gina","Perkins","glp646",null, false, false, false,1,1,0,0,1,1,0,0));
        studentList.add(new Student("Isabel","Aguilera","ifa247",null, false, false,false,1,1,0,0,1,1,0,0));
        studentList.add(new Student("Ashwin","Purohit","ap53293",null, false, false,false,0,0,1,0,0,0,1,0));
        studentList.add(new Student("Brandi","Nguyen","bcn444",null, false, false, false,1,0,0,0,1,0,0,0));
        studentList.add(new Student("Jonathan","Alverzo","jba2365",null, false, false,false, 0,0,0,1,0,0,1,0));
        studentList.add(new Student("Morgan","Frentz","mlf2992",null, false, false,false, 0,1,0,0,0,1,0,0));
        studentList.add(new Student("Emily","Le","ehl439",null, false, false,false, 0,0,1,0,1,0,0,0));
        return studentList;

    }
}