package org.schoolSort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.*;
import java.util.*;

public class schoolSort {
    public static void main(String[] args) {
        Student[] studentArray = createStudentArray("../../Downloads/seekStudents.json");
        List<School> schoolList = schoolInits();
        studentsAvail("monday1", studentArray);
        studentsAvail("monday2", studentArray);
        studentsAvail("tuesday1", studentArray);
        studentsAvail("wednesday1", studentArray);
        studentsAvail("wednesday2", studentArray);
        studentsAvail("thursday1", studentArray);
        studentsAvail("thursday2", studentArray);
        for(School x : schoolList){
            if(Objects.equals(x.getName(), "NYOS Hyperloop")){
                fillHyperloop(x, studentArray);
            }
            else{fillSchool(x, studentArray);}
        }
        createReport("test.csv", "test.txt", schoolList, studentArray);
    }

    //creates report of full school sort-csv file with school sort (and markers for pos, execs, etc) and txt file
    //for unfilled schools, unassigned students, drivers needed
    //args: file names for the reports, schoolList and studentArray
    public static void createReport(String csvFilename, String txtFilename, List<School> schoolList, Student[] studentArray){
        createSpreadsheet(csvFilename, schoolList);
        createTxtReport(txtFilename, schoolList, studentArray);
    }

    //creates spreadsheet of school sort
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

    //creates txt file with unfilled schools, unassinged students, and transportation (not full school sort)
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

    //parses json file and creates array of students from it, needs path to json file
    public static Student[] createStudentArray(String path){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(new File(path), Student[].class);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //creates string with all students who haven't been assigned school (have to print it to see)
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
        buffer.append("\n" + count + " students unassigned\n");
        return buffer.toString();
    }

    //creates string with schools that have unfilled spaces (have to print to see)
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

    //creates string of schools that still need more rides (have to print to see)
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

    //helper function for pretty formatting
    public static String formatTime(String time){
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

    //fills hyperloop school with only hyperloop specific students
    public static void fillHyperloop(School school, Student[] studentArray) {
        for(Student x : studentArray){
            if(x.getHyperloop()){
                x.setSchool(school);
                school.addStudent(x);
                //would have to manually assert availability
                //if there's not enough students will have to add another loop but i dont really want to put random ppl in hyperloop yet
            }
        }
    }

    //fills school, starting with getting enough drivers to cover capacity (not rides--rides depend on # students assigned)
    //then students who have marked the time as their preference
    //then students who are available and don't prefer a different time
    //then students who are available and may prefer a diff time
    public static void fillSchool(School school, Student[] studentArray) {
        assert !school.isFull() : "School is already full";
        for(Student x : studentArray){ //drivers first
            if(!school.enoughDrivers()){
                if(x.getCarSpace() > 0 && x.isAvailableAt(school.getTime()) &&  x.isUnassigned() && !x.getHyperloop()){
                    x.setSchool(school);
                    school.addStudent(x);
                }
            }
        }
        if(!school.isFull()) {
            for (Student x : studentArray) { //get preferred next
                if (x.isUnassigned() && x.prefers(school.getTime()) && !x.getHyperloop()) {
                    x.setSchool(school);
                    school.addStudent(x);
                }
                if(school.isFull()){
                    System.out.println(school.getName() + " has been filled");
                    //System.out.println(school);
                    break;
                }
            }
        }
        for(Student x : studentArray){ //then get students without other preferences
            if(!school.isFull()){
                if(x.isUnassigned() && x.isAvailableAt(school.getTime()) && !x.getHyperloop() && !x.hasPreference()){
                    x.setSchool(school);
                    school.addStudent(x);
                }
            }
            if (school.isFull()) {
                System.out.println(school.getName() + " has been filled");
                break;
            }
        }
        for (Student x : studentArray) { //then students who may have other preferences
            if (!school.isFull()) {
                if (x.isUnassigned() && x.isAvailableAt(school.getTime()) && !x.getHyperloop()) {
                    x.setSchool(school);
                    school.addStudent(x);
                }
            }
            if (school.isFull()) {
                System.out.println(school.getName() + " has been filled");
                //System.out.println(school);
                break;
            }

        }
        if(school.getStudentList().size() < school.getCap()){System.out.println("out of students");}
    }

    //counts students available at a specific time
    public static int countStudents(String time, Student[] studentArray){
        int count = 0;
        switch(time){
            case "monday1":
                for(Student x : studentArray){
                    if(x.getMonday1() > 0){ count++;}
                }
                break;
            case "monday2":
                for(Student x : studentArray){
                    if(x.getMonday2() > 0){ count++;}
                }
                break;
            case "tuesday1":
                for(Student x : studentArray){
                    if(x.getTuesday1() > 0){ count++;}
                }
                break;
            case "tuesday2":
                for(Student x : studentArray){
                    if(x.getTuesday2() > 0){ count++;}
                }
                break;
            case "wednesday1":
                for(Student x : studentArray){
                    if(x.getWednesday1() > 0){ count++;}
                }
                break;
            case "wednesday2":
                for(Student x : studentArray){
                    if(x.getWednesday2() > 0){ count++;}
                }
                break;
            case "thursday1":
                for(Student x : studentArray){
                    if(x.getThursday1() > 0){ count++;}
                }
                break;
            case "thursday2":
                for(Student x : studentArray){
                    if(x.getThursday2() > 0){ count++;}
                }
                break;
        }
        return count;
    }

    //prints to console a list of students available at given time
    public static void studentsAvail(String time, Student[] studentArray) {
        System.out.println("There are " + countStudents(time, studentArray) + " students available at " + formatTime(time) + ":");
        for (Student x : studentArray) {
            Boolean avail = false;
            switch (time) {
                case "monday1":
                    if(x.getMonday1() > 0){ avail = true;}
                    if (x.getMonday1() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getMonday1() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
                case "monday2":
                    if(x.getMonday2() > 0){ avail = true;}
                    if (x.getMonday2() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getMonday2() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
                case "tuesday1":
                    if(x.getTuesday1() > 0){ avail = true;}
                    if (x.getTuesday1() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getTuesday1() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
                case "tuesday2":
                    if(x.getTuesday2() > 0){ avail = true;}
                    if (x.getTuesday2() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getTuesday2() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
                case "wednesday1":
                    if(x.getWednesday1() > 0){ avail = true;}
                    if (x.getWednesday1() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getWednesday1() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
                case "wednesday2":
                    if(x.getWednesday2() > 0){ avail = true;}
                    if (x.getWednesday2() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getWednesday2() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
                case "thursday1":
                    if(x.getThursday1() > 0){ avail = true;}
                    if (x.getThursday1() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getThursday1() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
                case "thursday2":
                    if(x.getThursday2() > 0){ avail = true;}
                    if (x.getThursday2() == 1) {
                        System.out.print(x.getFirstName() + " " + x.getLastName());
                    } else if (x.getThursday2() == 2) {
                        System.out.print(x.getFirstName() + " " + x.getLastName() + " (preferred)");
                    }
                    break;
            }
            if(avail){
                if (x.getSchool() == null) {
                    System.out.print(", school unassigned");
                } else {
                    System.out.print(", assigned to " + x.getSchool());
                }
                if (x.getExec()) {
                    System.out.print(", exec");
                }
                if (x.getPo()) {
                    System.out.print(", po");
                }
                if (x.getHyperloop()) {
                    System.out.print(", hyperloop");
                }
                if (x.getCarSpace() > 0) {
                    System.out.print(", can drive " + x.getCarSpace());
                }
                System.out.println("");
            }
        }
        System.out.println("");
    }

    //initialize schools manually
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
}