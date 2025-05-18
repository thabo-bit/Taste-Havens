package com.example.tastehavens;

public class Employee_Details {
    String Name, Employee_Number,Employee_Role,salary,Phone_Number,password,Email;

    public Employee_Details() {
    }

    public Employee_Details(String name, String employee_Number, String employee_Role, String salary, String phone_Number, String password, String email) {
        Name = name;
        Employee_Number = employee_Number;
        Employee_Role = employee_Role;
        this.salary = salary;
        Phone_Number = phone_Number;
        this.password = password;
        Email = email;
    }

    public String getEmpName() {
        return Name;
    }

    public void setEmpName(String name) {
        Name = name;
    }

    public String getEmployee_Number() {
        return Employee_Number;
    }

    public void setEmployee_Number(String employee_Number) {
        Employee_Number = employee_Number;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEmployee_Role() {
        return Employee_Role;
    }

    public void setEmployee_Role(String employee_Role) {
        Employee_Role = employee_Role;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
