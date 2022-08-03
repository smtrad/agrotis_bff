package br.com.agrotis.core.utils;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.function.Function;


public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Class<?> getParameterizedType(Class<?> type, int idxType) {
        ParameterizedType pt = (ParameterizedType) type.getGenericSuperclass();
        Class<?> pClass = (Class<?>) pt.getActualTypeArguments()[idxType];
        return pClass;
    }
    
    @SuppressWarnings("unchecked")
	public static <T> void updateValueOfType(Object instance, Class<T> type, Function<T, T> updater) {
    	Arrays.stream(instance.getClass().getDeclaredFields())
    		.filter(field -> {    			
    			return type.isAssignableFrom(field.getType());
    		})
    		.forEach(field -> {
    			field.setAccessible(true);
    			try {
					T value = (T) field.get(instance);
					field.set(instance, updater.apply(value));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
    			
    		});
    }
    
}
