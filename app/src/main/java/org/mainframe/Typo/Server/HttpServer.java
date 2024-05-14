package org.mainframe.Typo.Server;
import java.util.concurrent.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.net.URLDecoder;
import java.net.InetAddress;
import java.lang.reflect.Method;
import java.util.regex.*;
import org.mainframe.Typo.Annotations.web.*;
public class HttpServer {
   /* 
    private ServerSocket socket;
    private int PORT=8081;
    private Socket client;
    private OutputStream op;
    private BufferedReader reader;
    private static ExecutorService service;
    private Map<Class,List<Method>> metods;  
    private InputStream input;
    
    public HttpServer(Map<Class,List<Method>> met){
        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.metods=met;
         try {
          run();	
         } catch(Exception err) {
       	err.printStackTrace();
         }
        
    }
    
    
    public void WriteToClient(Map<String,String> a){
         if(client!=null){
             try {
                 op.write(a.toString().getBytes());
                 op.flush();
                 op.close();
                 client.close();	
             } catch(Exception err) {
             	err.printStackTrace();
             }
         }
    }
    
    
    
    
   public  String getResponse() {
    
    StringBuilder builder = new StringBuilder();
    int empty=0;
    int check=0;     
    if (client != null) {
        try {
            String line;
            long startTime = System.currentTimeMillis();
            
               
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("{")){
                     
                    builder.append(line);
                    break;    
                }else{
                    System.out.println(line);  
                    continue;
                }
                // Check for end of input or timeout
               
            }
        } catch (SocketTimeoutException e) {
            // Handle timeout
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
       }
        
    return builder.toString();
}
    
 public String getResponse2(){
    
    StringBuilder builder = new StringBuilder("");
    if(client!=null){
        try{
            String line;
            while((line = reader.readLine())!=null){
               builder.append(line);
                
             //  if(reader.read() ==0 || reader.read()==-1){
                  break;  
               
            }
             
           return builder.toString();
        }catch(Exception e){
            return "Null object";
        }
    }
       
    return builder.toString();
}
    
    public void run()throws Exception{
            socket = new ServerSocket(PORT,1,InetAddress.getByName("localhost"));
            while(true){
              client = socket.accept();
              service.execute(()->{
                
                   try{
                       input=client.getInputStream();
                       op = client.getOutputStream(); 
                       reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                       op.write("HTTP/1.1 200 OK\r\n".getBytes());
                       op.write("\r\n".getBytes());
                       op.write("success".getBytes());
                       op.flush();
                       var a= 5;
                       
                       String response =getResponse2();
                       
                       System.out.println("done");
                       String route = response.split("\n")[0].split(" ")[1];
                       System.out.println("resp"+response+"----"+route);
                       metods.forEach((clzz,methods)->{
                           methods.forEach(method->{
                               method.setAccessible(true);
                               try {
                                 // System.out.println(parseFormData(response).toString()); 
                                //  System.out.println(getResponse());                 
                                  Map<String,String> map =(Map<String,String>) method.invoke(clzz.newInstance()); 
                                  String methodroute =(String) method.getAnnotation(GetMapping.class).value();
                                  if(matchroute(route,methodroute)){
                                      
                                        //WriteToClient(map);
                                          
                                        System.out.println("Accepted");  
                                  }
                               } catch(Exception err) {
                               	err.printStackTrace();
                               }finally{
                                  try{       
                                  client.close();
                                 }catch(Exception e){
                                     e.printStackTrace();
                                 }
                               }        
                           });
                       }); 
                     }catch(Exception e){
                        e.printStackTrace();
                   }
                   
             }); 
            }
         
        
    }
    
    
    
    
    private boolean matchroute(String r1,String r2){
        Pattern p = Pattern.compile("^"+r1+"(?:\\?[^\\s]*)?$");
        Matcher m1 = p.matcher(r2);
        return m1.matches();
    }
    */
}

