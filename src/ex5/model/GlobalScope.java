package ex5.model;

import java.util.ArrayList;
import java.util.List;

public class GlobalScope extends Scope{
    private final List<Method> methods;

    public GlobalScope(Scope parent) {
        super(parent);
        this.methods = new ArrayList<>();
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public Method findMethod(String methodName) {
        for (Method method : methods) {
            if (method.getMethodName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
