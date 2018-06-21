package com.example.adelson_pc.a4shopping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by adelson-pc on 04/11/17.
 */

public class Members<S, O> {
    private Map<String, Object> members = new HashMap<String, Object>();

    public Members() {

    }

    public Map<String, Object> getMember() {
        return members;
    }

    public void setMember(Map<String, Object> members) {
        this.members = members;
    }

    public void deleteMember(String member) {
        if (!this.members.isEmpty()) {
            this.members.remove(member);
        }
    }

    public Integer size() {
        return this.members.size();
    }
}
