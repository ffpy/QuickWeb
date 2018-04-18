package org.quickweb.session.param;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParamMember)) return false;
        ParamMember that = (ParamMember) o;
        return isArrayMember == that.isArrayMember &&
                Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, isArrayMember);
    }

    @Override
    public String toString() {
        return "ParamMember{" +
                "member='" + member + '\'' +
                ", isArrayMember=" + isArrayMember +
                '}';
    }
}
