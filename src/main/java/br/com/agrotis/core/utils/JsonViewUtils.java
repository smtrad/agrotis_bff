package br.com.agrotis.core.utils;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.fasterxml.jackson.annotation.JsonView;

public final class JsonViewUtils {

    private JsonViewUtils() {
    }
    
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    public static String[] getViewFields(Class<?> target, Class<?> view) {
        List<String> viewFields = new ArrayList<>();
        Field[] fields = target.getDeclaredFields();
        for(Field f : fields) {
            if (f.isAnnotationPresent(JsonView.class)){
                JsonView viewField = f.getAnnotation(JsonView.class);
                if (Arrays.stream(viewField.value()).filter(view::isAssignableFrom).count() > 0) {
                    viewFields.add(f.getName());
                }
            }
        }
        return viewFields.toArray(String[]::new);
    }

    public static String[] getIgnoreFields(Class<?> target, Class<?> view) {
        if (Objects.isNull(view)){
            return null;
        }
        Field[] fields = target.getDeclaredFields();
        List<String> viewFields = Arrays.asList(getViewFields(target, view));
        List<String> ignoreFields = Arrays.stream(fields).
                 filter(field -> !viewFields.contains(field.getName())).
                 map(Field::getName).
                collect(Collectors.toList());
        return ignoreFields.toArray(String[]::new);
    }

}
