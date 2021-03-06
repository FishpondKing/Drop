package com.fishpondking.android.drop.engine;

import java.util.ArrayList;

/**
 * Author: FishpondKing
 * Date: 2016/12/3:16:41
 * Email: song511653502@gmail.com
 * Description:
 */

public class Dormitory {

    private String mId;
    private String mName;
    private String mLeader;
    private ArrayList<String> mMembers;

    public Dormitory(){
        mId = null;
        mName = null;
        mLeader = null;
        mMembers = null;
    }

    public void clear(){
        mId = null;
        mName = null;
        mLeader = null;
        mMembers = null;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLeader() {
        return mLeader;
    }

    public void setLeader(String leader) {
        mLeader = leader;
    }

    public ArrayList getMembers() {
        return mMembers;
    }

    public void setMembers(ArrayList member) {
        mMembers = member;
    }
}
