package com.example.dream_team.owner.Adapter;

public class employeeObject {
    String Name, Number, Type, Status = "false";

    employeeObject(){
    }

    public employeeObject(String name, String number, String type, String status){
        this.Name = name;
        this.Number = number;
        this.Type = type;
        this.Status = status;
    }
}
