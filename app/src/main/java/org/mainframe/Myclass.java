package org.mainframe;
import org.mainframe.Typo.Annotations.Host;
import java.util.*;
import org.mainframe.Typo.Annotations.RequestType;
import org.mainframe.Typo.Annotations.web.*;
import org.mainframe.Typo.Annotations.web.RequestMapping;

@Host
public class Myclass {
  
    @RequestMapping(value = "/api/post",type=RequestType.GET)
    public Map<String,String> Test(){
        Map<String,String> map = new HashMap<>();
        map.put("Date",new Date().toLocaleString());
        System.out.println(map);
        return map;
    }
    
    @RequestMapping(value = "/api/vi",type=RequestType.GET)
    public Map<String,String> Test2(){
        Map<String,String> map = new HashMap<>();
        map.put("Date",new Date().toLocaleString());
        map.put("Air pressure","10000");
        System.out.println(map);
        return map;
    }
    
    @RequestMapping(value = "/api/post/data",type=RequestType.POST)
    public void Response(String response){
        System.out.println(response);
    }
}

