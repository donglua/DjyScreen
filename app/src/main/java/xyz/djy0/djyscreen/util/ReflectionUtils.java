package xyz.djy0.djyscreen.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wuwen02
 */
public class ReflectionUtils {

  public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
    Method method = null;

    for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        method = clazz.getDeclaredMethod(methodName, parameterTypes);
        return method;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static Method getDeclaredMethod(Class<?> clazz1, String methodName, Class<?>... parameterTypes) {
    Method method = null;

    for (Class<?> clazz = clazz1; clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        method = clazz.getDeclaredMethod(methodName, parameterTypes);
        return method;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
    Method method = getDeclaredMethod(object, methodName, parameterTypes);

    if (null != method) {
      method.setAccessible(true);
      try {
        return method.invoke(object, parameters);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        ;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  public static Object invokeMethod(String className, String methodName, Class<?>[] parameterTypes,
      Object[] parameters) {
    try {
      Class<?> clazz = Class.forName(className);
      Method method = getDeclaredMethod(clazz, methodName, parameterTypes);

      if (null != method) {
        method.setAccessible(true);
        return method.invoke(null, parameters);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static Object invokeMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
    Method method = getDeclaredMethod(clazz, methodName, parameterTypes);

    if (null != method) {
      method.setAccessible(true);
      try {
        return method.invoke(null, parameters);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  public static Field getDeclaredField(Object object, String fieldName) {
    Field field = null;
    Class<?> clazz = object.getClass();
    for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        field = clazz.getDeclaredField(fieldName);
        return field;
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static void setFieldValue(Object object, String fieldName, Object value) {
    Field field = getDeclaredField(object, fieldName);

    try {
      if (field != null) {
        field.setAccessible(true);
        field.set(object, value);
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static Object getFieldValue(Object object, String fieldName) {
    Field field = getDeclaredField(object, fieldName);
    try {
      if (field != null) {
        field.setAccessible(true);
        return field.get(object);
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
