package booking.security.config;

import booking.security.dto.UserReadDto;
import booking.security.mapper.UserReadMapper;
import booking.security.service.JwtService;
import booking.security.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static String BEARER = "Bearer";

    private final JwtService jwtService;
    private final UserService userService;
    private final UserReadMapper userReadMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(BEARER.length() + 1);
        final String login = jwtService.extractLogin(jwt);
        if (login != null) {
            Optional<UserReadDto> userReadDto = userService.findUserByLogin(login);
            if (userReadDto.isEmpty()) throw new EntityNotFoundException();
            UserDetails userDetails = (UserDetails) userReadMapper.mapToEntity(userReadDto.get());
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // session and ip
                SecurityContextHolder.getContext().setAuthentication(authToken);
                request.setAttribute("userId", userReadDto.get().getId());
                request.setAttribute("username", userDetails.getUsername());
                request.setAttribute("authorities", userDetails.getAuthorities());
            }
        }
        filterChain.doFilter(request, response);
    }
}
