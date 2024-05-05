package com.chapterly.dto;


import java.io.Serializable;

public class EmailRequestDto implements Serializable {
    private String fName;
    private String lName;
    private String phone;
    private String fromEmail;
    private String body;


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "EmailRequestDto{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", phone='" + phone + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
