
---

# Typos HTTP Server

This is a lightweight HTTP server framework for handling web requests and responses in Java.

## Features

**Annotation-Based Routing:*** Easily map HTTP requests to Java methods using the ***@RequestMapping*** annotation.
- **Automatic JSON Serialization/Deserialization:** Convert Java objects to JSON and vice versa with ease.
- **Simple Configuration:** Initialize the server with just a few lines of code.

## Usage

1. **Installation:**

   Add the Typos HTTP Server dependency to your project's `build.gradle` file:

   ```groovy
   implementation files('libs/typos-http-server.jar')
   or
   implementation fileTree(dir: 'libs', include: ['*.jar'])
   ```
   **File Tree**
   ```
     ├── build.gradle
     ├── libs
     │   └── HttpJavaServer.jar
     └── src
     ├── main
     │   ├── java
     │   │   └── org
     │   │       └── example
     │   │           ├── App.java
     │   │           └── Employee.java
     │   └── resources
     └── test
         ├── java
         │   └── org
         │       └── example
         │           └── AppTest.java
         └── resources
   ```
   

3. **Initializing the Server:**

   Initialize the Typos HTTP Server in your `main` method:

   ```java
   public static void main(String[] args) {
       TyposRunner.run(App.class, 8080);
   }
   ```

4. **Defining Endpoints:**

   Define your API endpoints within your `App` class using annotations:

   ```java
   import org.mainframe.Typo.Annotations.web.*;
   import java.util.*;

   @Host
   public class Myclass {

      @RequestMapping(value = "/api/post", type = RequestType.GET)
      public Map<String, String> Test() {
          // Your code here
      }

      @RequestMapping(value = "/api/vi", type = RequestType.GET)
      public Map<String, String> Test2() {
        // Your code here
      }

      @RequestMapping(value = "/api/post/data", type = RequestType.POST)
      public void Response(String response) {
        // Your code here
      }
    }
   ```


5. **Request Handling:**
Accessing Endpoints:Annotate methods with @RequestMapping.Specify the endpoint path and request type using the value and type attributes, respectively.Request Handling:For GET requests, the method should return the desired response object.For POST requests, the method should accept parameters as defined by your application's needs.

6. **Running the Server:**

   Compile and run your application. The server will start listening for incoming HTTP requests on port That you provide in TypoRunner.run(class,Port)

## Initialize using Main Class

```java
import org.mainframe.Typo.Annotations.web.*;
import org.mainframe.Typo.TyposRunner;
import java.util.*;


@InitTypo("org.example")
public class App {

    public static void main(String[] args) {
        TyposRunner.run(App.class, 8080);
    }
}
```
##Output 
```
Run tasks : [:app:run]
> Task :app:compileJava
> Task :app:processResources NO-SOURCE
> Task :app:classes

> Task :app:run
 _____  __   __  ____     ___  
|_   _| \ \ / / |  _ \   / _ \ 
  | |    \ V /  | |_) | | | | |
  | |     | |   |  __/  | |_| |
  |_|     |_|   |_|      \___/ 

 Warning :- Same methods  but  with diffrent Request Type wont work Its a bug 


 
 Available Routes
http://localhost:8080/api/post RequestType GET
http://localhost:8080/api/vi RequestType GET
http://localhost:8080/api/post/data RequestType POST
http://localhost:8080/api/data RequestType GET


```

## Contributing

Contributions are welcome! If you have any suggestions, bug reports, or feature requests, please open an issue or submit a pull request.

---

Feel free to customize and expand upon this README file based on your project's specific requirements and features.
