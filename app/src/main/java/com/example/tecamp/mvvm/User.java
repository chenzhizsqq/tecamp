package com.example.tecamp.mvvm;


import androidx.databinding.ObservableField;

public class User {
    //用户名
    public ObservableField<String > userName=new ObservableField<>();   //ObservableField   可观测场
    //密码
    public ObservableField<String >password=new ObservableField<>();
    //反馈
    public ObservableField<String >status=new ObservableField<>();

    public User() {
    }
}