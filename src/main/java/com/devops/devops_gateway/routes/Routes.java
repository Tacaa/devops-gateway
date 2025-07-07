package com.devops.devops_gateway.routes;


import com.devops.devops_gateway.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class Routes {
    
    @Value("${user.service.url}")
    private String userServiceUrl;
    private static final String USER_API_PATH = "/api/user";

    @Value("${accommodation.service.url}")
    private String accommodationServiceUrl;
    private static final String ACCOMMODATION_API_PATH = "/api/accommodation";
    private static final String AVAILABILITY_API_PATH = "/api/availability";
    private static final String RESERVATION_API_PATH = "/api/reservation";

    @Value("${review.service.url}")
    private String reviewServiceUrl;
    private static final String ACCOMMODATION_REVIEW_API_PATH = "/api/accommodation-review";
    private static final String HOST_REVIEW_API_PATH = "/api/host-review";
    
    @Value("${notification.service.url}")
    private String notificationServiceUrl;
    private static final String NOTIFICATION_API_PATH = "/api/notifications";
    private static final String NOTIFICATIONS_PREFERENCES_API_PATH = "/api/notifications-preferences";


    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return GatewayRouterFunctions.route("user_service")
                //USER-SERVICE
                .route(RequestPredicates.GET(USER_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(userServiceUrl + USER_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(USER_API_PATH + "/all"),
                        HandlerFunctions.http(userServiceUrl + USER_API_PATH + "/all"))
                .route(RequestPredicates.GET(USER_API_PATH),
                        HandlerFunctions.http(userServiceUrl + USER_API_PATH))
                .route(RequestPredicates.GET(USER_API_PATH + "/search"),
                        HandlerFunctions.http(userServiceUrl + USER_API_PATH + "/search"))
                .route(RequestPredicates.PUT(USER_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(userServiceUrl + USER_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.POST(USER_API_PATH + "/register"),
                        HandlerFunctions.http(userServiceUrl + USER_API_PATH + "/register"))
                .route(RequestPredicates.DELETE(USER_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(userServiceUrl + USER_API_PATH + "/" + req.pathVariable("id")).handle(req))


                //ACCOMMODATION-SERVICE
                // GET /api/accommodation/{id}
                .route(RequestPredicates.GET(ACCOMMODATION_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(accommodationServiceUrl + ACCOMMODATION_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(ACCOMMODATION_API_PATH),
                        HandlerFunctions.http(accommodationServiceUrl + ACCOMMODATION_API_PATH))
                .route(RequestPredicates.POST(ACCOMMODATION_API_PATH + "/search"),
                        HandlerFunctions.http(accommodationServiceUrl + ACCOMMODATION_API_PATH + "/search"))
                .route(RequestPredicates.POST(ACCOMMODATION_API_PATH),
                        HandlerFunctions.http(accommodationServiceUrl + ACCOMMODATION_API_PATH))

                //Availability routes
                .route(RequestPredicates.GET(AVAILABILITY_API_PATH + "/{accommodationId}"),
                        HandlerFunctions.http(accommodationServiceUrl + AVAILABILITY_API_PATH))
                .route(RequestPredicates.POST(AVAILABILITY_API_PATH),
                req -> {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                    if (auth != null && auth.isAuthenticated()) {
                        User user = (User) auth.getPrincipal();
                        String userId = String.valueOf(user.getId());

                        try {
                            // Pročitaj tijelo zahtjeva
                            String requestBody = req.body(String.class);

                            RestTemplate restTemplate = new RestTemplate();

                            HttpHeaders headers = new HttpHeaders();
                            headers.setAll(req.headers().asHttpHeaders().toSingleValueMap());
                            headers.add("X-User-Id", userId);

                            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
                            String url = accommodationServiceUrl + AVAILABILITY_API_PATH;

                            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                            return ServerResponse.status(response.getStatusCode())
                                    .body(response.getBody());

                        } catch (HttpClientErrorException | HttpServerErrorException ex) {
                            // Detekcija poznatih grešaka po status kodu
                            HttpStatusCode status = ex.getStatusCode();
                            String responseBody = ex.getResponseBodyAsString();

                            return ServerResponse.status(status).body(responseBody);

                        } catch (Exception ex) {
                            // Bilo koja druga greška
                            ex.printStackTrace();
                            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Unexpected error occurred: " + ex.getMessage());
                        }
                    } else {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                    }
                })

                .route(RequestPredicates.PUT(AVAILABILITY_API_PATH + "/{availabilityId}"),
                        req -> {
                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                            if (auth != null && auth.isAuthenticated()) {
                                User user = (User) auth.getPrincipal();
                                String userId = String.valueOf(user.getId());

                                try {
                                    // Pročitaj tijelo zahtjeva
                                    String requestBody = req.body(String.class);

                                    // Inicijalizuj RestTemplate
                                    RestTemplate restTemplate = new RestTemplate();

                                    HttpHeaders headers = new HttpHeaders();
                                    headers.setAll(req.headers().asHttpHeaders().toSingleValueMap());
                                    headers.add("X-User-Id", userId);

                                    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

                                    // Formiraj URL
                                    String url = accommodationServiceUrl + AVAILABILITY_API_PATH + "/" + req.pathVariable("availabilityId");

                                    // Pozovi servis
                                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

                                    return ServerResponse.status(response.getStatusCode())
                                            .body(response.getBody());

                                } catch (HttpClientErrorException | HttpServerErrorException ex) {
                                    // Greške iz backend servisa – proslijedi status i poruku
                                    HttpStatusCode status = ex.getStatusCode();
                                    String responseBody = ex.getResponseBodyAsString();

                                    return ServerResponse.status(status).body(responseBody);

                                } catch (Exception ex) {
                                    // Neočekivane greške
                                    ex.printStackTrace();
                                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                            .body("Unexpected error occurred: " + ex.getMessage());
                                }

                            } else {
                                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                            }
                        })

                // Reservation routes
                .route(RequestPredicates.POST(RESERVATION_API_PATH),
                        HandlerFunctions.http(accommodationServiceUrl + RESERVATION_API_PATH))
                .route(RequestPredicates.DELETE(RESERVATION_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(accommodationServiceUrl + RESERVATION_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(RESERVATION_API_PATH + "/all-host-pending-accommodation/{hostId}"),
                        req -> HandlerFunctions.http(accommodationServiceUrl + RESERVATION_API_PATH + "/all-host-pending-accommodation/" + req.pathVariable("hostId")).handle(req))
                .route(RequestPredicates.POST(RESERVATION_API_PATH + "/save-manually-approved"),
                        HandlerFunctions.http(accommodationServiceUrl + RESERVATION_API_PATH + "/save-manually-approved"))


                // Accommodation Review routes
                .route(RequestPredicates.GET(ACCOMMODATION_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + ACCOMMODATION_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(ACCOMMODATION_REVIEW_API_PATH + "/all/{accommodationId}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + ACCOMMODATION_REVIEW_API_PATH + "/all/" + req.pathVariable("accommodationId")).handle(req))
                .route(RequestPredicates.POST(ACCOMMODATION_REVIEW_API_PATH),
                        HandlerFunctions.http(reviewServiceUrl + ACCOMMODATION_REVIEW_API_PATH))
                .route(RequestPredicates.PUT(ACCOMMODATION_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + ACCOMMODATION_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.DELETE(ACCOMMODATION_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + ACCOMMODATION_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))

                // Host Review routes
                .route(RequestPredicates.GET(HOST_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + HOST_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.GET(HOST_REVIEW_API_PATH + "/all/{hostId}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + HOST_REVIEW_API_PATH + "/all/" + req.pathVariable("hostId")).handle(req))
                .route(RequestPredicates.POST(HOST_REVIEW_API_PATH),
                        HandlerFunctions.http(reviewServiceUrl + HOST_REVIEW_API_PATH))
                .route(RequestPredicates.PUT(HOST_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + HOST_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))
                .route(RequestPredicates.DELETE(HOST_REVIEW_API_PATH + "/{id}"),
                        req -> HandlerFunctions.http(reviewServiceUrl + HOST_REVIEW_API_PATH + "/" + req.pathVariable("id")).handle(req))

                // Notifications routes
                .route(RequestPredicates.GET(NOTIFICATION_API_PATH + "/{userId}"),
                        req -> HandlerFunctions.http(notificationServiceUrl + NOTIFICATION_API_PATH + "/" + req.pathVariable("userId")).handle(req))
                .route(RequestPredicates.PUT(NOTIFICATION_API_PATH + "/read"),
                        HandlerFunctions.http(notificationServiceUrl + NOTIFICATIONS_PREFERENCES_API_PATH + "/read"))

                // Notifications preferences routes
                .route(RequestPredicates.GET(NOTIFICATIONS_PREFERENCES_API_PATH + "/{userId}"),
                        req -> HandlerFunctions.http(notificationServiceUrl + NOTIFICATIONS_PREFERENCES_API_PATH + "/" + req.pathVariable("userId")).handle(req))
                .route(RequestPredicates.PUT(NOTIFICATIONS_PREFERENCES_API_PATH),
                        HandlerFunctions.http(notificationServiceUrl + NOTIFICATIONS_PREFERENCES_API_PATH))


                .build();
    }

}
