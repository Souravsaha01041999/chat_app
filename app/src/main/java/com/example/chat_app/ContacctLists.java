package com.example.chat_app;

public class ContacctLists
{
    private String name,number;
    ContacctLists(String na,String no)
    {
        this.name=na;
        this.number=no;
    }
    String getContName()
    {
        return this.name;
    }
    String getContNumber()
    {
        return this.number;
    }
}
