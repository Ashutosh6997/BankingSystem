package com.vasistha.bankingsystem.ModelClasses;

/**
 * Created by Ashutosh Singh on 15-Feb-18.
 */

public class CustomersData {
    String userEmail, nameTitle, firstName, middleName, lastName, customerName, gender, mobileNumber, dateOfBirth, address;

    public CustomersData()
    {

    }

    public CustomersData(String userEmail, String nameTitle, String firstName, String lastName, String customerName, String gender, String mobileNumber, String dateOfBirth, String address) {
        this.userEmail = userEmail;
        this.nameTitle = nameTitle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerName = customerName;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public CustomersData(String userEmail, String nameTitle, String firstName, String middleName, String lastName, String customerName, String gender, String mobileNumber, String dateOfBirth, String address) {
        this.userEmail = userEmail;
        this.nameTitle = nameTitle;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.customerName = customerName;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getGender() {
        return gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }
}
