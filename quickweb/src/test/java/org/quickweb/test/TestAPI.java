package org.quickweb.test;

import org.junit.Test;
import org.quickweb.MoocRequest;
import org.quickweb.MoocResponse;
import org.quickweb.QuickWeb;
import org.quickweb.session.scope.Scope;

import java.util.Calendar;

public class TestAPI {

    /**
     * 添加留言
     */
    @Test
    public void test1() {
        QuickWeb.server(new MoocRequest(), new MoocResponse())
                .requireParamNotEmpty("content", "aid", "uid")
                .putParam("ctime", Calendar.getInstance().getTimeInMillis())

                .modal("comment")
                .insert("content", "uid", "aid", "ctime")

                .view("article.jsp");
    }

    /**
     * 查询文章的留言数
     */
    @Test
    public void test2() {
        QuickWeb.server(new MoocRequest(), new MoocResponse())
                .requireParamNotEmpty("aid")

                .modal("comment")
                .where("aid = $aid")
                .count("commentCount")

                .view("count.jsp");
    }

    /**
     * 用户登录
     */
    @Test
    public void test3() {
        QuickWeb.server(new MoocRequest(), new MoocResponse())
                .requireParamNotEmpty("username", "password", "code")
                .requireParamEqualsWith("code", Scope.SESSION)

                .modal("user")
                .select("password", "salt")
                .find()

                .putParamBy("password", quickSession -> encrypt(
                        quickSession.getParam("password", Scope.MODAL),
                        quickSession.getParam("salt", Scope.MODAL)))
                .requireParamEqualsWith("password", Scope.MODAL)

                .view("index.jsp");
    }

    private String encrypt(String password, String salt) {
        return password + salt;
    }

    /**
     * 用户注册
     */
    @Test
    public void test4() {
        QuickWeb.server(new MoocRequest(), new MoocResponse())
                .requireParamNotEmpty("username", "password", "name", "code")
                .requireParamEqualsWith("code", Scope.SESSION)
                .putParam("ctime", Calendar.getInstance().getTimeInMillis())

                .modal("user")
                .insert("username", "password", "name", "ctime")

                .view("login.jsp");
    }

    /**
     * 删除留言
     */
    @Test
    public void test5() {
        QuickWeb.server(new MoocRequest(), new MoocResponse())
                .requireParamNotEmpty("cid", "uid")

                .modal("comment")
                .where("cid = $cid and uid = $uid")
                .delete()

                .view("comment.jsp");
    }

    /**
     * 修改留言
     */
    @Test
    public void test6() {
        QuickWeb.server(new MoocRequest(), new MoocResponse())
                .requireParamNotEmpty("content", "cid", "uid")

                .modal("comment")
                .where("cid = $cid and uid = $uid")
                .update("content")

                .view("comment.jsp");
    }
}