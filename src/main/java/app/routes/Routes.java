package app.routes;

import app.controllers.HotelController;
import app.entities.Room;
import io.javalin.apibuilder.EndpointGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    private HotelRoutes hotelRoutes = new HotelRoutes();
    private static final Logger logger = LoggerFactory.getLogger(Routes.class);

    public EndpointGroup getRoutes () {

        return () -> {
            get("/", ctx -> {
                logger.info("Handling request to /");
                ctx.result("Hej hotelvenner");
            });
            path("/hotels", hotelRoutes.getHotelRoutes());
        };
    }
}
