package org.mainframe.Typo.Server;
import org.mainframe.Typo.Annotations.Host;
import org.mainframe.Typo.Annotations.RequestType;
import org.mainframe.Typo.Annotations.web.RequestMapping;
import java.util.*;
@Host
public class myclass2 {
    @RequestMapping(value="/api/data",type = RequestType.GET)
    public Map<String,String> getData(){
       Map<String,String> p =new HashMap<>();
       p.put("status","200");
       return p; 
    }
}
