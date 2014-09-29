package com.payu.discovery.scanner;

import static com.payu.discovery.model.MethodBuilder.aMethod;
import static com.payu.discovery.model.ServiceBuilder.aService;
import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withModifier;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import com.payu.discovery.model.Service;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AnnotationScanner {

    private String packageToScan;

    private String appAddress;

    private Reflections reflections;

    public AnnotationScanner(String packageToScan, String appAddress) {
        this.packageToScan = packageToScan;
        this.appAddress = appAddress;
        reflections = buildReflection();

    }

    public Stream<Service> scan() {
        return scanClasses().stream().
                map(it ->
                        aService()
                                .withName(it.getName())
                                .withAddress(appAddress)
                                .withPath(findAnnotation(it, Path.class).value())
                                .withMethods(scanMethods(it))
                                .build());

    }

    private List<Service.Method> scanMethods(Class<?> aClass) {
        List<Service.Method> methods = scanMethods(aClass, GET.class);
        methods.addAll(scanMethods(aClass, POST.class));
        return methods;
    }
    private List<Service.Method> scanMethods(Class<?> aClass, Class<? extends java.lang.annotation.Annotation>
            annotation) {
        return getAllMethods(aClass, withModifier(Modifier.PUBLIC),
                withAnnotation(annotation)).stream()
                .map(it -> aMethod()
                        .withName(it.getName())
                        .withMethodType(annotation)
                        .withPath(findAnnotation(it, Path.class) != null ? findAnnotation(it,
                                Path.class).value() : "")
                        .build())
                .collect(Collectors.toList());
    }

    protected Set<Class<?>> scanClasses() {
        return reflections.getTypesAnnotatedWith(Path.class);
    }


    private Reflections buildReflection() {

        return new Reflections(packageToScan, new TypeAnnotationsScanner(), new MethodAnnotationsScanner());

    }
}
