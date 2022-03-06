package com.alex.exercise;

import java.util.Objects;

public class Student {
    
    private int flowID;
    private int type;
    private String IDCard;
    private String examCard;
    private String name;
    private String location;
    private int grade;


    public Student() {
    }

    public Student(int flowID, int type, String IDCard, String examCard, String name, String location, int grade) {
        this.flowID = flowID;
        this.type = type;
        this.IDCard = IDCard;
        this.examCard = examCard;
        this.name = name;
        this.location = location;
        this.grade = grade;
    }

    public int getFlowID() {
        return this.flowID;
    }

    public void setFlowID(int flowID) {
        this.flowID = flowID;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIDCard() {
        return this.IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return this.examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Student flowID(int flowID) {
        setFlowID(flowID);
        return this;
    }

    public Student type(int type) {
        setType(type);
        return this;
    }

    public Student IDCard(String IDCard) {
        setIDCard(IDCard);
        return this;
    }

    public Student examCard(String examCard) {
        setExamCard(examCard);
        return this;
    }

    public Student name(String name) {
        setName(name);
        return this;
    }

    public Student location(String location) {
        setLocation(location);
        return this;
    }

    public Student grade(int grade) {
        setGrade(grade);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return flowID == student.flowID && type == student.type && Objects.equals(IDCard, student.IDCard) && Objects.equals(examCard, student.examCard) && Objects.equals(name, student.name) && Objects.equals(location, student.location) && grade == student.grade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowID, type, IDCard, examCard, name, location, grade);
    }

    @Override
    public String toString() {
        return "{" +
            " flowID='" + getFlowID() + "'" +
            ", type='" + getType() + "'" +
            ", IDCard='" + getIDCard() + "'" +
            ", examCard='" + getExamCard() + "'" +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", grade='" + getGrade() + "'" +
            "}";
    }

}
