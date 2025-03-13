package com.devops.devops_gateway.routes;


import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    private static final String USER_SERVICE_BASE_URL = "http://devops-user:8081";
    private static final String USER_API_PATH = "/api/user";

    private static final String ACCOMMODATION_SERVICE_BASE_URL = "http://devops-accommodation:8082";
    private static final String ACCOMMODATION_API_PATH = "/api/accommodation";
    private static final String AVAILABILITY_API_PATH = "/api/availability";
    private static final String RESERVATION_API_PATH = "/api/reservation";


    private static final String REVIEW_SERVICE_BASE_URL = "http://devops-review:8084";
    private static final String ACCOMMODATION_REVIEW_API_PATH = "/api/accommodation-review";
    private static final String HOST_REVIEW_API_PATH = "/api/host-review";


    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return GatewayRouterFunctions.route("user_service")
                //USER-SERVICE
                .route(RequestPredicates.GET(USER_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(USER_SERVICE_BASE_URL + USER_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(USER_API_PATH + "/all"),
                        HandlerFunctions.http(USER_SERVICE_BASE_URL + USER_API_PATH + "/all"))
                .route(RequestPredicates.GET(USER_API_PATH),
                        HandlerFunctions.http(USER_SERVICE_BASE_URL + USER_API_PATH))
                .route(RequestPredicates.GET(USER_API_PATH + "/search"),
                        HandlerFunctions.http(USER_SERVICE_BASE_URL + USER_API_PATH + "/search"))
                .route(RequestPredicates.PUT(USER_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(USER_SERVICE_BASE_URL + USER_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.POST(USER_API_PATH + "/register"),
                        HandlerFunctions.http(USER_SERVICE_BASE_URL + USER_API_PATH + "/register"))
                .route(RequestPredicates.DELETE(USER_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(USER_SERVICE_BASE_URL + USER_API_PATH + "/" + req.pathVariable("id")).handle(req))


                //ACCOMMODATION-SERVICE
                // GET /api/accommodation/{id}
                .route(RequestPredicates.GET(ACCOMMODATION_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + ACCOMMODATION_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(ACCOMMODATION_API_PATH),
                        HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + ACCOMMODATION_API_PATH))
                .route(RequestPredicates.POST(ACCOMMODATION_API_PATH + "/search"),
                        HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + ACCOMMODATION_API_PATH + "/search"))
                .route(RequestPredicates.POST(ACCOMMODATION_API_PATH),
                        HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + ACCOMMODATION_API_PATH))

                //Availability routes
                .route(RequestPredicates.GET(AVAILABILITY_API_PATH + "/{accommodationId}"),
                        HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + AVAILABILITY_API_PATH))
                .route(RequestPredicates.POST(AVAILABILITY_API_PATH),
                        HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + AVAILABILITY_API_PATH))
                .route(RequestPredicates.PUT(AVAILABILITY_API_PATH + "/{availabilityId}"),
                        req -> HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + AVAILABILITY_API_PATH + "/" + req.pathVariable("availabilityId")).handle(req))

                // Reservation routes
                .route(RequestPredicates.POST(RESERVATION_API_PATH),
                        HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + RESERVATION_API_PATH))
                .route(RequestPredicates.DELETE(RESERVATION_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + RESERVATION_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(RESERVATION_API_PATH + "/all-host-pending-accommodation/{hostId}"),
                        req -> HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + RESERVATION_API_PATH + "/all-host-pending-accommodation/" + req.pathVariable("hostId")).handle(req))
                .route(RequestPredicates.POST(RESERVATION_API_PATH + "/save-manually-approved"),
                        HandlerFunctions.http(ACCOMMODATION_SERVICE_BASE_URL + RESERVATION_API_PATH + "/save-manually-approved"))


                // Accommodation Review routes
                .route(RequestPredicates.GET(ACCOMMODATION_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + ACCOMMODATION_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(ACCOMMODATION_REVIEW_API_PATH + "/all/{accommodationId}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + ACCOMMODATION_REVIEW_API_PATH + "/all/" + req.pathVariable("accommodationId")).handle(req))
                .route(RequestPredicates.POST(ACCOMMODATION_REVIEW_API_PATH),
                        HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + ACCOMMODATION_REVIEW_API_PATH))
                .route(RequestPredicates.PUT(ACCOMMODATION_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + ACCOMMODATION_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.DELETE(ACCOMMODATION_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + ACCOMMODATION_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))

                // Host Review routes
                .route(RequestPredicates.GET(HOST_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + HOST_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(HOST_REVIEW_API_PATH + "/all/{hostId}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + HOST_REVIEW_API_PATH + "/all/" + req.pathVariable("hostId")).handle(req))
                .route(RequestPredicates.POST(HOST_REVIEW_API_PATH),
                        HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + HOST_REVIEW_API_PATH))
                .route(RequestPredicates.PUT(HOST_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + HOST_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.DELETE(HOST_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(REVIEW_SERVICE_BASE_URL + HOST_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))

                .build();
    }

}
