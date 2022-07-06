package com.example.chat_app;

public class RcvData {
    private String name,number,image,seen;
    RcvData(String name,String number,String image,String seen)
    {
        this.name=name;
        this.number=number;
        this.image=image;
        this.seen=seen;
    }
    String getName()
    {
        return this.name;
    }
    String getNumber()
    {
        return this.number;
    }
    String getImage()
    {
        return this.image;
    }
    String getSeen()
    {
        return this.seen;
    }
}
