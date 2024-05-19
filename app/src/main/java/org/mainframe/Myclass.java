package org.mainframe;
import org.mainframe.Typo.Annotations.Host;
import java.util.*;
import org.mainframe.Typo.Annotations.RequestType;
import org.mainframe.Typo.Annotations.web.*;
import org.mainframe.Typo.Annotations.web.RequestMapping;
import org.mainframe.Typo.Server.Enployee;
import org.mainframe.Typo.JInjector.InjectVal;
@Host
public class Myclass {

    public  List<Enployee> data= new ArrayList<>();

    @InjectVal("meta.project.name")
    public static String name;

    @RequestMapping(value = "/api/vi",type=RequestType.GET)
    public List<Enployee> Test2(){
        return this.data;
    }
    @RequestMapping(value = "/api/vi/size",type=RequestType.GET)
    public Map<String,String> getSize(){
        Map<String,String> sets = new HashMap<>();
        sets.put("Size of Data",""+data.size());
        return sets;
    }

    @RequestMapping(value = "/api/post/data",type=RequestType.POST)
    public void Response(Enployee response){
        System.out.println(name);
        data.add(response);
    }

}

