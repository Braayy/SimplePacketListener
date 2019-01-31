package braayy.fun.packetlistener;

import java.lang.reflect.Field;
import java.util.Optional;

public class Util {

    public static Optional<Object> getFieldValue(Class<?> clazz, Object obj, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);

            return Optional.ofNullable(field.get(obj));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> T defaultIfNull(T obj, T def) {
        return obj != null ? obj : def;
    }

}