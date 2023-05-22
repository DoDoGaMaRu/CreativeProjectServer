package util;

import java.lang.reflect.Method;

public class BuilderUtil {
    public static void dataBinding(Object builder, String setterName, Object data) {
        Method[] methods = builder.getClass().getMethods();

        for (Method method : methods) {
            if (setterName.equals(method.getName())) {
                try {
                    method.invoke(builder, data);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
