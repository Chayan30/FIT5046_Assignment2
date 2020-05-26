
package edu.monash.MovieMemoir.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Person {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String uid;
    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "surname")
    public String surname;
    @ColumnInfo(name = "username")
    public String username;
    @ColumnInfo(name = "gender")
    public String gender;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "state")
    public String state;
    @ColumnInfo(name = "postcode")
    public int postcode;
    //@ColumnInfo(name = "dob")
    //public Date dob;
    public Person(String uid, String firstName, String surname, String username,
                  String gender, String address, String state,
                  int postcode/*, Date dob*/) {
        this.uid = uid;
        this.firstName=firstName;
        this.surname=surname;
        this.username = username;
        this.gender = gender;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
        //this.dob = dob;
    }

    public String getId() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public int getPostcode() {
        return postcode;
    }
    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }
//    public Date getDob() {
//        return dob;
//    }
//    public void setDob(Date dob) {
//        this.dob = dob;
//    }

}
