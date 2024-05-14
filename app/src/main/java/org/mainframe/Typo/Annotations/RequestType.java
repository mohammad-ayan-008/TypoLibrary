package org.mainframe.Typo.Annotations;
import org.mainframe.Typo.Annotations.RequestType;
import org.mainframe.Typo.Annotations.web.RequestMapping;

public enum RequestType {
    POST("POST"),
    GET("GET");

    private final String type;
    private RequestType(String type){
        this.type=type;
    }
}
