package org.example;
import com.fasterxml.jackson.databind.*;

import java.util.List;
import java.io.*;

public class schoolSort1{
public static void main(String[] args){
        System.out.println("what");
        ObjectMapper mapper = new ObjectMapper();
        try{
            Student[] roster = mapper.readValue(new File("../../Downloads/seekStudents.json"), Student[].class);
            for(Student x : roster){
                System.out.println(x);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //List<Student> myObjects = mapper.readValue(jsonInput, new TypeReference<List<Student>>(){});
}
}