# Security Package

### RouteRole interface :

````
public enum Role implements RouteRole {
ANYONE,
USER,
ADMIN;
}
````

// RouteRole gør at man kan koble roller direkte på ens routes
````
get("/user_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from USER Protected")),Role.USER);
````

