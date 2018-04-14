package org.quickweb.session;

import org.quickweb.utils.RequireUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class QuickSessionProxy implements InvocationHandler {
    private QuickSession quickSession;
    private boolean isEnd = false;

    private QuickSessionProxy(QuickSession quickSession) {
        this.quickSession = quickSession;
    }

    public static QuickSession of(QuickSession quickSession) {
        RequireUtils.requireNotNull(quickSession);
        QuickSession proxy = (QuickSession) Proxy.newProxyInstance(quickSession.getClass().getClassLoader(),
                quickSession.getClass().getInterfaces(), new QuickSessionProxy(quickSession));
        quickSession.initProxy(proxy);
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "isEnd":
                return isEnd;
            case "end":
                isEnd = true;
                return null;
            default:
                if (isEnd) {
                    Class<?> returnType = method.getReturnType();
                    if (returnType.equals(QuickSession.class))
                        return proxy;
                    if (returnType.equals(void.class))
                        return null;
                }
        }
        return method.invoke(quickSession, args);
    }
}
