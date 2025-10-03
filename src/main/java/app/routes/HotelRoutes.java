package app.routes;

import app.controllers.HotelController;
import app.controllers.RoomController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRoutes {
    private final HotelController hotelController = new HotelController();
    private final RoomController roomController = new RoomController();

    public EndpointGroup getHotelRoutes () {
        return () -> {
            get("/", ctx -> hotelController.getAllHotels(ctx), Role.ANYONE);
            get("/{id}", hotelController::getHotelById, Role.ANYONE);
            get("/{id}/rooms", hotelController::getRoomsForHotel, Role.ANYONE);

            post("/{id}/rooms", roomController::createRoom, Role.USER, Role.ADMIN);
            post("/", hotelController::createHotel, Role.USER, Role.ADMIN);

            put("/{id}", hotelController::updateHotel, Role.ADMIN);

            delete("/{id}", hotelController::deleteHotel, Role.ADMIN);
            delete("/{id}/rooms/{roomId}", roomController::removeRoom, Role.ADMIN);
        };
    }
}
