package org.quickweb.session.param;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.quickweb.exception.QuickWebException;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ParamMemberHelperTest {
    private String param;
    private Object value;

    private Object expected;
    private Class<?> exceptionType;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] objects = new Object[][]{
                {   // 0
                        "name",
                        "aaa",
                        "aaa",
                        null,
                },
                {   // 1
                        "name[0]",
                        new String[]{"aaa", "bbb"},
                        "aaa",
                        null
                },
                {   // 2
                        "name[1]",
                        new String[]{"aaa", "bbb"},
                        "bbb",
                        null,
                },
                {   // 3
                        "people.name",
                        new People("aaa", 10),
                        "aaa",
                        null,
                },
                {   // 4
                        "people.age",
                        new People("aaa", 10),
                        10,
                        null,
                },
                {   // 5
                        "people[1].age",
                        new People[]{
                                new People("aaa", 10),
                                new People("bbb", 12),
                        },
                        12,
                        null,
                },
                {   // 6
                        "people[0][0].name",
                        new People[][]{
                                { new People("aaa", 10) },
                                { new People("bbb", 12) },
                        },
                        "aaa",
                        null,
                },
                {   // 7
                        "people[0].name",
                        Arrays.asList(
                                new People("aaa", 10),
                                new People("bbb", 12)),
                        "aaa",
                        null,
                },
                {   // 8
                        "job.worker.name",
                        new Job("abc", new People("aaa", 111), true),
                        "aaa",
                        null,
                },
                {   // 9
                        "job.senior",
                        new Job("abc", new People("aaa", 111), true),
                        true,
                        null,
                },
                {   // 10
                        "job.senior",
                        new Job("abc", new People("aaa", 111), false),
                        false,
                        null,
                },
                {   // 11
                        "people.age",
                        new NoGetterPeople("aaa", 10),
                        null,
                        QuickWebException.class,
                },
                {   // 12
                        "people[a].name",
                        Arrays.asList(
                                new People("aaa", 10),
                                new People("bbb", 12)),
                        null,
                        QuickWebException.class,
                },
                {   // 12
                        "people.0.name",
                        Arrays.asList(
                                new People("aaa", 10),
                                new People("bbb", 12)),
                        null,
                        QuickWebException.class,
                },
        };
        return Arrays.asList(objects);
    }

    public ParamMemberHelperTest(String param, Object value, Object expected, Class<?> exceptionType) {
        this.param = param;
        this.value = value;
        this.expected = expected;
        this.exceptionType = exceptionType;
    }

    @Test
    public void test() {
        Exception ex = null;
        try {
            ParamNameHelper paramNameHelper = new ParamNameHelper(param);
            Object result = ParamMemberHelper.getMemberValue(value, paramNameHelper);
            assertEquals(expected, result);
        } catch (Exception e) {
            ex = e;
        }
        if (ex == null)
            assertEquals(exceptionType, null);
        else
            assertEquals(ex.getMessage(), exceptionType, ex.getClass());
    }

    private static class People {
        private String name;
        private int age;

        public People(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    private static class Job {
        private String name;
        private People worker;
        private boolean isSenior;

        public Job(String name, People worker, boolean isSenior) {
            this.name = name;
            this.worker = worker;
            this.isSenior = isSenior;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public People getWorker() {
            return worker;
        }

        public void setWorker(People worker) {
            this.worker = worker;
        }

        public boolean isSenior() {
            return isSenior;
        }

        public void setSenior(boolean senior) {
            isSenior = senior;
        }
    }

    private static class NoGetterPeople {
        private String name;
        private int age;

        public NoGetterPeople(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}