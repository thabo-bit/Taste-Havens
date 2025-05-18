package com.example.tastehavens;

public class Helper {
    String Name, username, Email, Password, role;
    int Amount;

    String Employee_Number, Phone_Number;
   int Salary;

    public Helper(String name, String username, String email, String password, String role, int amount, String employee_Number, int salary, String phone_Number) {
        Name = name;
        this.username = username;
        Email = email;
        Password = password;
        this.role = role;
        Amount = amount;
        Employee_Number = employee_Number;
        Salary = salary;
        Phone_Number = phone_Number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmployee_Number() {
        return Employee_Number;
    }

    public void setEmployee_Number(String employee_Number) {
        Employee_Number = employee_Number;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }
}

