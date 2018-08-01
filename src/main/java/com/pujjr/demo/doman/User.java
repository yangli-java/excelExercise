package com.pujjr.demo.doman;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class User {

    @JsonProperty("编码")
    private Integer id;

    @JsonProperty("用户名")
    private String username;

    @JsonProperty("密码")
    private String password;

    @JsonProperty("地址")
    private String address;

    @JsonProperty("生日")
    private Date birthday;

    @JsonProperty("性别")
    private String sex;

    public String getSex() {
        return sex == "0" ? "男" : "女";
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}