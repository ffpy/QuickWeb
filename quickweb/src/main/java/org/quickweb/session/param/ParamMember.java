package org.quickweb.session.param;

public class ParamMember {
    private String member;
    private boolean isArrayMember;

    public ParamMember(String member, boolean isArrayMember) {
        this.member = member;
        this.isArrayMember = isArrayMember;
    }

    public String getMember() {
        return member;
    }

    public boolean isArrayMember() {
        return isArrayMember;
    }

    @Override
    public String toString() {
        return "ParamMember{" +
                "member='" + member + '\'' +
                ", isArrayMember=" + isArrayMember +
                '}';
    }
}
