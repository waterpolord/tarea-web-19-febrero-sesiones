import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import models.User;
import org.eclipse.jetty.server.Authentication;

import static io.javalin.apibuilder.ApiBuilder.before;

public class main {
    public static void main(String[] args) {

        User admin  = new User("admin","admin");

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles("/public");
            config.registerPlugin(new RouteOverviewPlugin("/rutas"));
            config.enableCorsForAllOrigins();

        }).start(7777);

        app.get("/", ctx -> {
                    User user = ctx.sessionAttribute("user");
                    if(user == null){
                        ctx.render("public/form.html");
                    }
                    else{
                        ctx.render("public/home.html");
                    }
                });

        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            // simula que se loguea, solo hay un usuario admin
            if(admin.getUsername().equalsIgnoreCase(username) && admin.getPassword().equalsIgnoreCase(password)){
                ctx.sessionAttribute("user", admin);
                ctx.redirect("/");
            }
            else{
                ctx.result("Nombre de usuario o contrasena incorrecta");
            }

        });

    }
}
