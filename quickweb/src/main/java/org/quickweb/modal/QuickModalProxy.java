package org.quickweb.modal;

import org.quickweb.session.QuickSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class QuickModalProxy implements InvocationHandler {
    private QuickModal quickModal;

    private QuickModalProxy(QuickModal quickModal) {
        this.quickModal = quickModal;
    }

    public static QuickModal of(QuickModal quickModal) {
        QuickModal proxy = (QuickModal) Proxy.newProxyInstance(quickModal.getClass().getClassLoader(),
                quickModal.getClass().getInterfaces(), new QuickModalProxy(quickModal));
        quickModal.initProxy(proxy);
        return proxy;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (quickModal.getQuickSession().isEnd()) {
            Class<?> returnType = method.getReturnType();
            if (returnType.equals(QuickModal.class))
                return proxy;
            if (returnType.equals(QuickSession.class))
                return quickModal.getQuickSession();
            if (returnType.equals(void.class))
                return null;
        }
        return method.invoke(quickModal, args);
    }
}
