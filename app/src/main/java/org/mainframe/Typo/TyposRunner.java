package org.mainframe.Typo;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import org.mainframe.Typo.Annotations.Host;
import org.mainframe.Typo.Annotations.InitTypo;
import org.mainframe.Typo.Annotations.JSON.JSONCOMPONENT;
import org.mainframe.Typo.Annotations.web.PostMapping;
import org.mainframe.Typo.Annotations.web.RequestMapping;
import org.mainframe.Typo.JInjector.InjectVal;
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
import java.util.stream.Collectors;
import java.nio.file.Paths;
import java.io.*;
import java.lang.reflect.Field;

public class TyposRunner {

    private static Map<Class<?>,Object> Classinstance= new HashMap<>();

    private static List<Class<?>> list;
    private static Map<Class,List<Method>> map;
    private static Optional<String> value;
    private static HttpServerV2 server;
    @InjectVal("server.port")
    public static int PORT=8080;
    public static void run(Class clss) {
         setValueFromXml(clss);
         value = getAnnotationValue(clss);
         list = filterClassesWithAnnotations(clss);
         map=getAllAnnotatedMethods(list).get();
         server = new HttpServerV2(map,PORT);
         server.setVal(Classinstance);
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

    public static void setValueFromXml(Class ss){
        String path=Paths.get("").toAbsolutePath().toString()+"/src/main/resources/Config.properties";
        var p = new Properties();
        
        try{
            p.load(new FileInputStream(path));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        if(ss.isAnnotationPresent(InitTypo.class)){
            Set<Class<?>> classes= getAllClasses(((InitTypo)ss.getAnnotation(InitTypo.class)).value());
             classes.stream()
            .collect(Collectors.toMap(x->x,j->Arrays.asList(j.getDeclaredFields())))
            .forEach((clzz,Objs)->{
                  Objs.forEach(x->{
                     if(x.isAnnotationPresent(InjectVal.class)) {
                          try{
                             if(clzz.getDeclaredConstructors().length>0){
                                 if(Classinstance.get(clzz)==null){     
                                //   System.out.println(clzz.getName());       
                                  Classinstance.put(clzz,clzz.newInstance());
                              }
                           }
                          }catch(Exception e){
                             e.printStackTrace();
                          }
                                
                         if(x.getType()==int.class){
                             try {  
                                x.setAccessible(true);          
                               Object ins =  Classinstance.get(x.getDeclaringClass());        
                               x.set(ins,Integer.parseInt((String)p.get(fetchValue(x))));   
                             }catch(Exception e){
                                 e.printStackTrace();
                             }
                         }else if(x.getType()==String.class){
                               try{   
                               x.setAccessible(true);     
                               Object ins =  Classinstance.get(x.getDeclaringClass());      
                                x.set(ins,(String)p.get(fetchValue(x)));
                               }catch(Exception s){s.printStackTrace();};
                         }
                     }
                  });  
             });
            
        }
        System.out.println(Classinstance);
    }
    
    public static String fetchValue(Field f){
        return ((InjectVal) f.getAnnotation(InjectVal.class)).value();
    }
    
    
    
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
