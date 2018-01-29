package com.duaruang.pnmportal.data;

/**
 * Created by user on 11/17/2017.
 */

public class Usergroup {
    private String usergroup, position;
    private int id;

    public Usergroup(String usergroup, String position, int id){
        this.usergroup=usergroup;
        this.position=position;
        this.id=id;
    }

    public String getUserGroup(){
        return this.usergroup;
    }

    public String getPosition(){
        return this.position;
    }

    public int getId()
    {
        return this.id;
    }

    @Override
    public String toString()
    {
        return String.format(
                "[usergroup: id=%1$s, usergroup=%2$s, position=%3$s]",
                id, usergroup, position);
    }
}
