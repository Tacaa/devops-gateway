package com.devops.devops_gateway.config;

import com.devops.devops_gateway.security.auth.CustomAuthenticationFailureHandler;
import com.devops.devops_gateway.security.auth.RestAuthenticationEntryPoint;
import com.devops.devops_gateway.security.auth.TokenAuthenticationFilter;
import com.devops.devops_gateway.service.impl.CustomUserDetailsService;
import com.devops.devops_gateway.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class WebSecurityConfig {

    // Handler za vracanje 401 kada klijent sa neodogovarajucim korisnickim imenom i lozinkom pokusa da pristupi resursu
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    // Injektujemo implementaciju iz TokenUtils klase kako bismo mogli da koristimo njene metode za rad sa JWT u TokenAuthenticationFilteru
    @Autowired
    private TokenUtils tokenUtils;

    // Servis koji se koristi za citanje podataka o korisnicima aplikacije
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    // Implementacija PasswordEncoder-a koriscenjem BCrypt hashing funkcije.
    // BCrypt po defalt-u radi 10 rundi hesiranja prosledjene vrednosti.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() throws BadCredentialsException {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
        // prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
        authProvider.setUserDetailsService(userDetailsService());
        // 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu
        // da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // Registrujemo authentication manager koji ce da uradi autentifikaciju korisnika za nas
    // Centralizuje proces autentifikacije
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    // Definisemo prava pristupa za zahteve ka odredjenim URL-ovima/rutama
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()); //Omogućava CORS politiku kako bi aplikacija mogla da obradi zahteve sa različitih domena
        http.csrf((csrf) -> csrf.disable()); //CSRF zaštita je isključena
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //Postavlja politiku upravljanja sesijama na STATELESS
        http.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(restAuthenticationEntryPoint));
        http.authorizeHttpRequests(request -> {
            request.requestMatchers(new AntPathRequestMatcher("/auth/login")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/auth/signup")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()

                    // User service endpoints
                    .requestMatchers(new AntPathRequestMatcher("/api/user/register")).permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/user/all").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/user/search").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/api/user/{id}").permitAll()

                    // Accommodation endpoints
                    .requestMatchers(HttpMethod.GET, "/api/accommodation/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/accommodation").hasRole("HOST")
                    .requestMatchers(HttpMethod.POST, "/api/accommodation/search").permitAll()

                    // Availability endpoints
                    .requestMatchers(HttpMethod.GET, "/api/availability/**").permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/availability/**")).hasRole("HOST")

                    // Reservation endpoints
                    .requestMatchers(HttpMethod.POST, "/api/reservation").hasRole("GUEST")
                    .requestMatchers(HttpMethod.DELETE, "/api/reservation/{id}").hasAnyRole("GUEST", "HOST")
                    .requestMatchers(HttpMethod.GET, "/api/reservation/all-host-pending-accommodation/**").hasRole("HOST")
                    .requestMatchers(HttpMethod.POST, "/api/reservation/save-manually-approved").hasRole("HOST")
                    .requestMatchers(HttpMethod.GET, "/api/reservation/did-guest-had-reservation-in-accommodation").hasRole("GUEST")
                    .requestMatchers(HttpMethod.GET, "/api/reservation/did-guest-had-reservation-in-host-accommodation").hasRole("GUEST")

                    // Review endpoints
                    .requestMatchers(HttpMethod.GET, "/api/accommodation-review/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/accommodation-review").hasRole("GUEST")
                    .requestMatchers(HttpMethod.PUT, "/api/accommodation-review/{id}").hasRole("GUEST")
                    .requestMatchers(HttpMethod.DELETE, "/api/accommodation-review/{id}").hasRole("GUEST")

                    .requestMatchers(HttpMethod.GET, "/api/host-review/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/host-review").hasRole("GUEST")
                    .requestMatchers(HttpMethod.PUT, "/api/host-review/{id}").hasRole("GUEST")
                    .requestMatchers(HttpMethod.DELETE, "/api/host-review/{id}").hasRole("GUEST")

                    .anyRequest().authenticated();
        });
        http.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService()), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Autentifikacija ce biti ignorisana ispod navedenih putanja (kako bismo ubrzali pristup resursima)
        // Zahtevi koji se mecuju za web.ignoring().antMatchers() nemaju pristup SecurityContext-u
        // Dozvoljena POST metoda na ruti /auth/login, za svaki drugi tip HTTP metode greska je 401 Unauthorized
        return (web) -> web.ignoring().requestMatchers(HttpMethod.POST, "/auth/login")


                // Ovim smo dozvolili pristup statickim resursima aplikacije
                .requestMatchers(HttpMethod.GET, "/", "/webjars/*", "/*.html", "favicon.ico",
                        "/*/*.html", "/*/*.css", "/*/*.js");

    }
    //Podesavanja CORS-a
    //https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH")); // or simply "*"
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
