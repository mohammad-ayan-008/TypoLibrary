package org.mainframe.Typo.Annotations.web;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.*;
import org.mainframe.Typo.Annotations.RequestType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping   {
     String value();
     RequestType type();
}
