package org.quickweb.view;

import org.quickweb.session.QuickSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class QuickViewProxy implements InvocationHandler {
    private QuickView quickView;

    private QuickViewProxy(QuickView quickView) {
        this.quickView = quickView;
    }

    public static QuickView of(QuickView quickView) {
        QuickView proxy = (QuickView) Proxy.newProxyInstance(quickView.getClass().getClassLoader(),
                quickView.getClass().getInterfaces(), new QuickViewProxy(quickView));
        quickView.initProxy(proxy);
        return proxy;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (quickView.getQuickSession().isEnd()) {
            Class<?> returnType = method.getReturnType();
            if (returnType.equals(QuickView.class))
                return proxy;
            if (returnType.equals(QuickSession.class))
                return quickView.getQuickSession();
            if (returnType.equals(void.class))
                return null;
        }
        return method.invoke(quickView, args);
    }
}
