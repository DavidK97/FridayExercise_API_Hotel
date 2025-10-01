package app.routes;

import app.controllers.HotelController;
import app.controllers.RoomController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRoutes {
    private final HotelController hotelController = new HotelController();
    private final RoomController roomController = new RoomController();

    public EndpointGroup getHotelRoutes () {
        return () -> {
            get("/", ctx -> hotelController.getAllHotels(ctx));
            get("/{id}", hotelController::getHotelById);
            get("/{id}/rooms", hotelController::getRoomsForHotel);

            post("/{id}/rooms", roomController::createRoom);
            post("/", hotelController::createHotel);

            put("/{id}", hotelController::updateHotel);

            delete("/{id}", hotelController::deleteHotel);
            delete("/{id}/rooms/{roomId}", roomController::removeRoom);
        };
    }
}
