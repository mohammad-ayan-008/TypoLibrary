package org.mainframe.Typo;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import org.mainframe.Typo.Annotations.Host;
import org.mainframe.Typo.Annotations.InitTypo;
import org.mainframe.Typo.Annotations.JSON.JSONCOMPONENT;
import org.mainframe.Typo.Annotations.web.PostMapping;
import org.mainframe.Typo.Annotations.web.RequestMapping;
import org.mainframe.Typo.Server.HttpServer;
import org.mainframe.Typo.Server.HttpServerV2;
import org.reflections.Reflections;
import java.util.*;
import org.reflections.scanners.SubTypesScanner;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.reflect.Method;

public class TyposRunner {

    
    private static List<Class<?>> list;
    private static Map<Class,List<Method>> map;
    private static Optional<String> value;
    private static HttpServerV2 server;
    public static void run(Class clss,int port) {
         value = getAnnotationValue(clss);
         list = filterClassesWithAnnotations(clss);
         map=getAllAnnotatedMethods(list).get();
         server = new HttpServerV2(map,port);
        // server.setClassesAnnotatedWithJSONCOMPONENT(getAllClassesAnnotatedWithJSONCOMPONENT());
    }

    private static Optional<String> getAnnotationValue(Class clss) {
        if (clss.isAnnotationPresent(InitTypo.class)) {
            InitTypo typo = (InitTypo) clss.getAnnotation(InitTypo.class);
            return Optional.of(typo.value());
        }
        System.out.println("InitTypo Annotation is Missing");
        return Optional.fromNullable(null);
    }
//
//    private static List<Class<?>> getAllClassesAnnotatedWithJSONCOMPONENT(){
//        if (value.isPresent()) {
//            var classes = getAllClasses(value.get()).stream().filter(TyposRunner::isAnnotatedWithJSONCOMPONENT).toList();
//            return classes;
//        }
//        System.out.println("No Json Component Found");
//        return null;
//    }

//    private static  boolean isAnnotatedWithJSONCOMPONENT(Class<?> c){
//        return c.isAnnotationPresent(JSONCOMPONENT.class);
//    }

    private static Set<Class<?>> getAllClasses(String pkg) {
        Reflections ref = new Reflections(pkg, new SubTypesScanner(false));
        Set<Class<?>> reffs = ref.getSubTypesOf(Object.class);
        return reffs;
    }

    private static List<Class<?>> filterClassesWithAnnotations(Class clss) {

        if (value.isPresent()) {
            String val = value.get();
            Set<Class<?>> list = getAllClasses(val);
            List<Class<?>> clz = list.stream().filter(x -> x.isAnnotationPresent(Host.class))
                    .collect(Collectors.toList());
            return clz;
        }
        return null;
    }

    private static boolean isNotNull(Class<?> cls) {
        return cls != null;
    }
   
    private static Optional<Map<Class,List<Method>>> getAllAnnotatedMethods(List<Class<?>> cls){
        if(!cls.isEmpty()){
            return  Optional.of(
                cls.stream().collect(
                    Collectors.toMap(
                        x-> x,
                        x-> 
                        Arrays.asList(x.getDeclaredMethods())
                        .stream()
                        .filter(TyposRunner::isAnnotatedWithMappings)
                        .collect(Collectors.toList())))
                );
        }
        return Optional.fromNullable(null);
    }
    
    private static boolean isAnnotatedWithMappings(Method x){
       return x.isAnnotationPresent(RequestMapping.class);
    }
    
    public static <T extends Object> T toclass(String json,Class<T> cls){
        return  new Gson().fromJson(json,cls);
    }
}
