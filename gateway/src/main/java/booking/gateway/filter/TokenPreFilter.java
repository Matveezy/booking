package booking.gateway.filter;

import booking.gateway.dto.ValidateTokenResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TokenPreFilter extends AbstractGatewayFilterFactory<TokenPreFilter.Config> {

    private final RouteValidator routeValidator;
    private final WebClient.Builder webClientBuilder;

    public TokenPreFilter(RouteValidator routeValidator, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (routeValidator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorization header");
                }
                String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                return webClientBuilder.build().get()
                        .uri("http://security/auth/validate")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken)
                        .retrieve()
                        .bodyToMono(ValidateTokenResponse.class)
                        .map(response -> {
                            exchange.getRequest().mutate().header("userId", String.valueOf(response.getUserId()));
                            exchange.getRequest().mutate().header("username", response.getLogin());
                            exchange.getRequest().mutate().header("authorities", response.getAuthorities()
                                    .get(0).toString());
                            return exchange;

                        }).flatMap(chain::filter).onErrorResume(error -> {
                            return Mono.error(new RuntimeException());
                        });

            }

            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
