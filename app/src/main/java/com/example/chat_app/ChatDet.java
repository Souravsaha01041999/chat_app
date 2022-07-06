package com.example.chat_app;

public class ChatDet
{
    private String chatid,chat,chatto,chatfrm,seen;
    ChatDet(String chatid,String chat,String chatto,String chatfrm,String seen)
    {
        this.chatid=chatid;
        this.chat=chat;
        this.chatto=chatto;
        this.chatfrm=chatfrm;
        this.seen=seen;
    }
    String getChatid()
    {
        return this.chatid;
    }
    String getChat()
    {
        return this.chat;
    }
    String getChatto()
    {
        return this.chatto;
    }
    String getChatfrm()
    {
        return this.chatfrm;
    }
    String getSeen()
    {
        return this.seen;
    }
}
