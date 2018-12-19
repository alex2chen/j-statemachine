package com.kxtx.fsm.core.utils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.kxtx.fsm.builder.machine.support.AbstractStateMachine;
import com.kxtx.fsm.builder.StateMachine;
import com.kxtx.fsm.exception.ErrorCode;
import com.kxtx.fsm.exception.StateMachineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/2/28
 */
public class ReflectUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 实例化
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) {
        return newInstance(clazz, null, null);
    }

    /**
     * 实例化
     *
     * @param constructor
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Constructor<T> constructor, Object[] args) {
        return newInstance(null, constructor, args);
    }

    public static <T> T newInstance(Constructor<T> constructor) {
        return newInstance(null, constructor, null);
    }

    /**
     * 实例化
     *
     * @param clazz
     * @param constructor
     * @param args
     * @param <T>
     * @return
     */
    private static <T> T newInstance(Class<T> clazz, Constructor<T> constructor, Object[] args) {
        if ((clazz == null) && (constructor == null)) {
            throw new StateMachineException("clazz/constructor不能为空");
        }
        if (constructor == null) {
            constructor = getConstructor(clazz, (Class[]) null);
        }
        boolean oldAccessible = constructor.isAccessible();
        try {
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(args);

        } catch (Throwable t) {
            Throwable cause = (t instanceof InvocationTargetException) ? ((InvocationTargetException) t).getTargetException() : t;
            throw new StateMachineException("newInstance:" + cause.getMessage());
        } finally {
            constructor.setAccessible(oldAccessible);
        }
    }

    /**
     * get Constructor
     *
     * @param clazz
     * @param parameterTypes
     * @param <T>
     * @return
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>[] parameterTypes) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new StateMachineException("NoSuchMethodException:" + e.getMessage());
        }
    }

    /**
     * getClass
     *
     * @param className
     * @return
     */
    public static Class<?> getClass(final String className) {
        if (className == null || className.length() == 0)
            throw new StateMachineException("参数不能为空");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new StateMachineException("未找到指定的类" + className);
        }
    }

    /**
     * 是否可实例化
     *
     * @param type
     * @return
     */
    public static boolean isInstanceType(Class<?> type) {
        return type != null && !type.isInterface() && !Modifier.isAbstract(type.getModifiers()) &&
                (type.getEnclosingClass() == null || Modifier.isStatic(type.getModifiers()));
    }

    /**
     * final string field
     *
     * @param field
     * @return
     */
    public static boolean isFinalString(Field field) {
        return (field.getType().equals(String.class) && Modifier.isFinal(field.getModifiers()) && Modifier
                .isPublic(field.getModifiers()));
    }

    /**
     * is AbstractStateMachine type
     *
     * @param stateMachineClz
     * @return
     */
    public static boolean isStateMachineType(Class<?> stateMachineClz) {
        return stateMachineClz != null && AbstractStateMachine.class != stateMachineClz && AbstractStateMachine.class.isAssignableFrom(stateMachineClz);
    }

    /**
     * is StateMachine type
     *
     * @param stateMachineClz
     * @return
     */
    public static boolean isStateMachineInterface(Class<?> stateMachineClz) {
        return stateMachineClz != null && stateMachineClz.isInterface() && StateMachine.class.isAssignableFrom(stateMachineClz);
    }

    /**
     * 获取Annotation类型
     *
     * @param stateMachineClz
     * @param annotationClass
     * @param <M>
     * @return
     */
    public static <M extends Annotation> M findAnnotation(Class<?> stateMachineClz, final Class<M> annotationClass) {
        final AtomicReference<M> genericsParametersRef = new AtomicReference<M>();
        processStateMachineClass(stateMachineClz, new Function<Class<?>, Boolean>() {
            @Override
            public Boolean apply(Class<?> input) {
                M anno = input.getAnnotation(annotationClass);
                if (anno != null) {
                    genericsParametersRef.set(anno);
                    return false;
                }
                return true;
            }
        });
        M genericsParameters = genericsParametersRef.get();
        return genericsParameters;
    }

    /**
     * 处理所有继承状体机类
     *
     * @param stateMachineClz
     * @param func            注解处理function
     */
    public static void processStateMachineClass(Class<?> stateMachineClz, Function<Class<?>, Boolean> func) {
        Stack<Class<?>> stack = new Stack<Class<?>>();
        stack.push(stateMachineClz);
        while (!stack.isEmpty()) {
            Class<?> k = stack.pop();
            boolean isContinue = func.apply(k);
            if (!isContinue) break;
            for (Class<?> i : k.getInterfaces()) {
                if (isStateMachineInterface(i)) {
                    stack.push(i);
                }
            }
            if (isStateMachineType(k.getSuperclass())) {
                stack.push(k.getSuperclass());
            }
        }
    }

    /**
     * get Method
     *
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        return getMethod(clazz, methodName, parameterTypes, clazz);
    }

    private static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Class<?> original) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (SecurityException e) {
            throw new StateMachineException("getMethod error:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null) {
                return getMethod(clazz.getSuperclass(), methodName, parameterTypes, original);
            } else {
                throw new StateMachineException("no such method error:" + e.getMessage());
            }
        }
        return method;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        return getField(clazz, fieldName, clazz);
    }

    private static Field getField(Class<?> clazz, String fieldName, Class<?> original) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            throw new StateMachineException("reflect " + fieldName + "SecurityException error:" + e.getMessage());
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName, original);
            } else {
                throw new StateMachineException("reflect " + fieldName + " NoSuchFieldException error:" + e.getMessage());
            }
        }
        return field;
    }

    public static Object getStatic(Field field) {
        return get(field, null);
    }

    public static Object get(Field field, Object object) {
        if (field == null) {
            throw new StateMachineException("校验异常：field is null");
        }
        boolean oldAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            Object value = field.get(object);
            return value;
        } catch (Exception e) {
            throw new StateMachineException("操作异常：get static field error:" + e.getMessage());
        } finally {
            field.setAccessible(oldAccessible);
        }
    }

    @Deprecated
    public static boolean hasTheRightParameters(Method method) {
        Class<?> paramTypes[] = method.getParameterTypes();
        if (paramTypes == null || paramTypes.length != 1) {
            return false;
        }
        return true;
    }

    /**
     * getAnnotation for Class
     *
     * @param theClass
     * @param theAnnotation
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Class<?> theClass, Class<T> theAnnotation) {
        T aAnnotation = null;
        if (theClass.isAnnotationPresent(theAnnotation)) {
            aAnnotation = theClass.getAnnotation(theAnnotation);
        } else {
            if (!Object.class.equals(theClass.getSuperclass()) && theClass.getSuperclass() != null)
                aAnnotation = getAnnotation(theClass.getSuperclass(), theAnnotation);
            if (aAnnotation == null) {
                for (Class<?> aInt : theClass.getInterfaces()) {
                    aAnnotation = getAnnotation(aInt, theAnnotation);
                    if (aAnnotation != null) {
                        break;
                    }
                }
            }
        }
        return aAnnotation;
    }

    /**
     * @param targetClass
     * @param annotationClass
     * @return
     */
    public static Field[] getAnnotatedFields(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        List<Field> annotatedFields = Lists.newArrayList();
        Class<?> k = targetClass;
        while (!Object.class.equals(k) && k != null) {
            for (Field f : k.getFields()) {
                if (f.getAnnotation(annotationClass) != null) {
                    annotatedFields.add(f);
                }
            }
            k = k.getSuperclass();
        }
        return annotatedFields.toArray(new Field[0]);
    }

    /**
     * get annotation for class
     *
     * @param targetClass
     * @param annotationClass
     * @param extraParamTypes
     * @return
     */
    public static List<Method> getAnnotatedMethods(Class<?> targetClass, Class<? extends Annotation> annotationClass, Class<?>[] extraParamTypes) {
        List<Method> annotatedMethods = Lists.newArrayList();
        Class<?> k = targetClass;
        while (!Object.class.equals(k) && k != null) {
            for (Method m : k.getDeclaredMethods()) {
                if (m.getAnnotation(annotationClass) != null && arrayContentsEq(m.getParameterTypes(), extraParamTypes)) {
                    annotatedMethods.add(m);
                }
            }
            k = k.getSuperclass();
        }
        return annotatedMethods;
    }

    private static boolean arrayContentsEq(Object[] a1, Object[] a2) {
        if (a1 == null) {
            return a2 == null || a2.length == 0;
        }
        if (a2 == null) {
            return a1.length == 0;
        }
        if (a1.length != a2.length) {
            return false;
        }
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * isAnnotatedWith for Class/Field/Method
     *
     * @param obj:
     * @param theAnnotation
     * @return
     */
    public static boolean isAnnotatedWith(Object obj, Class<? extends Annotation> theAnnotation) {
        if (obj instanceof Class) {
            return getAnnotation((Class<?>) obj, theAnnotation) != null;
        } else if (obj instanceof Field) {
            return ((Field) obj).getAnnotation(theAnnotation) != null;
        } else if (obj instanceof Method) {
            return ((Method) obj).getAnnotation(theAnnotation) != null;
        }
        return false;
    }

    /**
     * 获取某个名称的方法
     *
     * @param targetClass
     * @param superClass
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method searchMethod(Class<?> targetClass, Class<?> superClass, String methodName, Class<?>[] parameterTypes) {
        if (superClass.isAssignableFrom(targetClass)) {
            Class<?> clazz = targetClass;
            while (!superClass.equals(clazz)) {
                try {
                    return clazz.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    clazz = clazz.getSuperclass();
                }
            }
        }
        return null;
    }

    /**
     * invoke
     *
     * @param method
     * @param target
     * @param args
     * @return
     */
    public static Object invoke(Method method, Object target, Object[] args) {
        if (method == null) {
            throw new StateMachineException("method is null");
        }
        boolean oldAccessible = method.isAccessible();
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            throw new StateMachineException(targetException, ErrorCode.METHOD_INVOKE_ERROR, method, Arrays.toString(args), target, targetException.getCause());
        } catch (Exception e) {
            throw new StateMachineException(e, ErrorCode.METHOD_INVOKE_ERROR, method, Arrays.toString(args), target, e.getMessage());
        } finally {
            method.setAccessible(oldAccessible);
        }
    }

    public static Object invoke(Method method, Object target) {
        return invoke(method, target, new Object[0]);
    }

    public static void doWithMethods(Class<?> clazz, MethodFilter mf, MethodCallback mc) throws IllegalArgumentException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (mf != null && !mf.matches(method)) {
                continue;
            }
            mc.doWith(method);
        }
        if (clazz.getSuperclass() != null) {
            doWithMethods(clazz.getSuperclass(), mf, mc);
        } else if (clazz.isInterface()) {
            for (Class<?> superIfc : clazz.getInterfaces()) {
                doWithMethods(superIfc, mf, mc);
            }
        }
    }


    public interface MethodCallback {
        void doWith(Method method);
    }

    public interface MethodFilter {
        boolean matches(Method method);
    }
}
