package org.mainframe.Typo.Server;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.lang.reflect.Method;
import java.io.*;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.net.URLDecoder;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import org.mainframe.Typo.Annotations.RequestType;
import org.mainframe.Typo.Annotations.web.RequestMapping;
import org.mainframe.Typo.Server.HttpServerV2;
import org.mainframe.Typo.TyposRunner;

public class HttpServerV2 {
    private HttpServer server;
    private int PORT=8080;
    private Gson gson;
//    private List<Class<?>> ClassesAnnotatedWithJSONCOMPONENT;
    private Map<Class<?>,Object> Classinstance;

    private ExecutorService exeService;
    public HttpServerV2(Map<Class,List<Method>> map,int port){
        gson = new Gson();
        Classinstance = new HashMap<>();
        try{
         exeService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
         server = HttpServer.create(new InetSocketAddress(port),1);
         System.out.println(" _____  __   __  ____     ___  ");
         System.out.println("|_   _| \\ \\ / / |  _ \\   / _ \\ ");
         System.out.println("  | |    \\ V /  | |_) | | | | |");
         System.out.println("  | |     | |   |  __/  | |_| |");
         System.out.println("  |_|     |_|   |_|      \\___/ ");
        
         System.out.println("\n Warning :- Same methods  but  with diffrent Request Type wont work Its a bug \n");
               
         List<String> routes= new ArrayList<>();
         map.forEach((clazz,met)->{
             try {
                 Classinstance.put(clazz, clazz.newInstance());
             }catch (Exception e){e.printStackTrace();}
             met.forEach(method->{
             RequestMapping annotation = method.getAnnotation(RequestMapping.class);  
                  routes.add(annotation.value()+" RequestType "+annotation.type().name());        
                  server.createContext(annotation.value(),new RequestHolder(method,annotation.type()));
            });
         });
         server.setExecutor(exeService);
         System.out.println("\n \n Available Routes");
         routes.forEach(rt->{
            System.out.println("http://localhost:"+port+rt);
         });
         server.start();
       }catch(Exception e){
           e.printStackTrace();
       }
    }
    
    
    public String getRouteFromString(Method method){
        return method.isAnnotationPresent(RequestMapping.class) ?  ((String) method.getAnnotation(RequestMapping.class).value()):null;
    }
    
    
    public class RequestHolder implements HttpHandler{
        private Method m;

        private Gson gson;
        private RequestType type;
        
        public RequestHolder(Method m,RequestType type) {
            this.m = m;
            gson= new Gson();
            this.type=type;
        }

        @Override
        public void handle(HttpExchange arg0) throws IOException {
            System.out.println("TypeNamw"+type.name()+arg0.getRequestMethod());
            if (Classinstance.get(m.getDeclaringClass())==null){
                System.out.println(m.getClass());
                System.out.println("error");
                return;
            }
            if("GET".equals(arg0.getRequestMethod())){
                   handleGet(arg0,m,gson,Classinstance.get(m.getDeclaringClass()));
            }else if("POST".equalsIgnoreCase(arg0.getRequestMethod())){
                 if(type.name().equals("POST")){
                   handlePost(arg0,m,gson,Classinstance.get(m.getDeclaringClass()));
                 }
            }
         }
     }


    public void handleGet(HttpExchange arg0,Method m,Gson gson,Object clzz){

              try{
                 Object data = m.invoke(clzz);
                 if(data instanceof List lst){
                     Type type = new TypeToken<List<?>>(){}.getType();
                     byte[] data2 = gson.toJson(data,type).getBytes();
                     arg0.sendResponseHeaders(200, data2.length);
                     OutputStream os = arg0.getResponseBody();
                     var bos = new BufferedOutputStream(os);
                     bos.write(data2);
                     bos.flush();
                     bos.close();
                 }else if(data instanceof Map){
                     byte[] data2 = gson.toJson(data).getBytes();
                     arg0.sendResponseHeaders(200, data2.length);
                     OutputStream os = arg0.getResponseBody();
                     var bos = new BufferedOutputStream(os);
                     bos.write(data2);
                     bos.flush();
                     bos.close();
                 }
              }catch(Exception e){
                  e.printStackTrace();
              }
            
    }
    
    public void handlePost(HttpExchange arg0,Method m,Gson gson,Object clzz){
      try{
        var reader = 
                new InputStreamReader(
                  arg0.getRequestBody(),StandardCharsets.UTF_8);
              var br = new BufferedReader(reader);
              var builder = new StringBuilder("");
              String line;  
              while((line=br.readLine())!=null){
                  builder.append(line);
              }
              br.close();
              Class parm =m.getParameters()[0].getType();
              if (parm == String.class){
                  Object C = line.toString();
                  m.invoke(clzz, C);
                  String response = "{\"status\":\"Ok\"}";
                  arg0.sendResponseHeaders(200, response.getBytes().length);
                  OutputStream op = arg0.getResponseBody();
                  op.write(response.getBytes());
                  op.flush();
                  op.close();
              }else {
                  Object C = TyposRunner.toclass(builder.toString(), parm);
                  m.invoke(clzz, C);
                  String response = "{\"status\":\"Ok\"}";
                  arg0.sendResponseHeaders(200, response.getBytes().length);
                  OutputStream op = arg0.getResponseBody();
                  op.write(response.getBytes());
                  op.flush();
                  op.close();
              }
            }catch(Exception e){
              e.printStackTrace();
           }
    }

//
//    public List<Class<?>> getClassesAnnotatedWithJSONCOMPONENT() {
//        return ClassesAnnotatedWithJSONCOMPONENT;
//    }
//
//    public void setClassesAnnotatedWithJSONCOMPONENT(List<Class<?>> classesAnnotatedWithJSONCOMPONENT) {
//        ClassesAnnotatedWithJSONCOMPONENT = classesAnnotatedWithJSONCOMPONENT;
//    }



}
