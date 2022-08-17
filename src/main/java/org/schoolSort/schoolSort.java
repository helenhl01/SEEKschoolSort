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
        for(School x : schoolList){
            if(Objects.equals(x.getName(), "NYOS Hyperloop")){
                fillHyperloop(x, studentArray);
            }
            else{fillSchool(x, studentArray);}
        }
        createReport("test.csv", "test.txt", schoolList, studentArray);
        //implementation: list of schools, list of students, iterate through both, have a bool for full/not full, student assigned/unassigned
    }

    public static void createReport(String csvFilename, String txtFilename, List<School> schoolList, Student[] studentArray){
        createSpreadsheet(csvFilename, schoolList);
        createTxtReport(txtFilename, schoolList, studentArray);
    }
    public static void createSpreadsheet(String filename, List<School> schoolList){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for(School x : schoolList){
                writer.append(x.getName());
                writer.newLine();
                for(Student y : x.getStudentList()){
                    writer.append(y.getFirstName() + " " + y.getLastName());
                    if(y.getCarSpace() > 0){writer.append("," + y.getCarSpace());}
                    else{writer.append(",");}
                    if(y.getPo()){writer.append("," + "PO");}
                    else if (y.getExec()){writer.append("," + "Exec");}
                    else{writer.append(",");}
                    writer.append("," + y.getEid() + "," + y.getEmail() + "," + y.getPhone());
                    if(y.getNonRegister()){ writer.append("," + "Won't register");}
                    writer.newLine();
                }
            }
            writer.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTxtReport(String filename, List<School> schoolList, Student[] studentArray){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.append(unfilledSchools(schoolList));
            writer.append(unassignedStudents(studentArray));
            writer.append(transpoReport(schoolList));
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
            return mapper.readValue(new File(path), Student[].class);
            //for(Student x : studentArray){
            //    System.out.println(x);
            //}
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String unassignedStudents(Student[] studentArray){
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n\nUnassigned students: \n");
        int count = 0;
        for(Student x : studentArray){
            if(x.getSchool()==null){
                count++;
                buffer.append(x);
                buffer.append("\n");
            }
        }
        buffer.append(count + " students unassigned");
        return buffer.toString();
    }

    public static String unfilledSchools(List<School> schoolList){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Unfilled schools: \n");
        for(School x : schoolList){
            if(x.getStudentList().size() < x.getCap()){
                buffer.append(x.getName()).append(" (").append(formatTime(x.getTime())).append(") has ").append(x.getCap() - x.getStudentList().size()).append(" slots left\n");
            }
        }
        return buffer.toString();
    }

    public static String transpoReport(List<School> schoolList){
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n\nDrivers needed: \n");
        for(School x : schoolList){
            if(!x.enoughRides()){
                buffer.append(x.getName() + " needs " + x.ridesNeeded() + " more rides\n");
            }
        }
        return buffer.toString();
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

    public static void fillHyperloop(School school, Student[] studentArray) {
        for(Student x : studentArray){
            if(x.getHyperloop()){
                x.setSchool(school);
                school.addStudent(x);
                //will have to manually assert availability
                //if there's not enough students will have to add another loop but i dont really want to put random ppl in hyperloop yet
            }
        }

    }

    public static void fillSchool(School school, Student[] studentArray) {
        assert !school.full() : "School is already full";
        for(Student x : studentArray){ //drivers first
            if(!school.enoughDrivers()){
                if(x.getCarSpace() > 0 && x.isAvailableAt(school.getTime()) &&  x.isUnassigned() && !x.getHyperloop()){
                    x.setSchool(school);
                    school.addStudent(x);
                }
            }
        }
        if(!school.full()) {
            for (Student x : studentArray) { //get preferred next
                if (x.isUnassigned() && x.prefers(school.getTime()) && !x.getHyperloop()) {
                    x.setSchool(school);
                    school.addStudent(x);
                }
                if(school.full()){
                    System.out.println(school.getName() + " has been filled");
                    //System.out.println(school);
                    break;
                }
            }
        }
        for (Student x : studentArray) {
            if (!school.full()) {
                if (x.isUnassigned() && x.isAvailableAt(school.getTime()) && !x.getHyperloop()) {
                    x.setSchool(school);
                    school.addStudent(x);
                }
            }
            if (school.full()) {
                System.out.println(school.getName() + " has been filled");
                //System.out.println(school);
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
                }
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
        schoolList.add(new School("school1", "monday1", 10));
        schoolList.add(new School("school2", "monday2", 6));
        schoolList.add(new School("school3", "tuesday1", 4));
        schoolList.add(new School("school4", "tuesday2", 10));
        schoolList.add(new School("school5", "wednesday1", 20));
        schoolList.add(new School("school6", "wednesday2", 15));
        schoolList.add(new School("school7", "thursday1", 10));
        schoolList.add(new School("school8", "thursday2", 12));
        schoolList.add(new School("NYOS Hyperloop", "thursday2", 5));
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