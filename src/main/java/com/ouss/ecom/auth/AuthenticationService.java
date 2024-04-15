package com.ouss.ecom.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouss.ecom.config.JwtService;
import com.ouss.ecom.dao.RoleRepo;
import com.ouss.ecom.dao.UserRepo;
import com.ouss.ecom.entities.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepo repository;
  private final RoleRepo roleRepositor;
//  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {

    var user = AppUser.builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
//            change it when y finish the project to be for USER
        .role(roleRepositor.findByRole("MANAGER"))
        .build();
    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
//    var refreshToken = jwtService.generateRefreshToken(user);
//    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
//            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse login(AuthenticationRequest request) {

    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    System.out.println(user);
    var jwtToken = jwtService.generateToken(user);
//    try{
//      authenticationManager.authenticate(
//              new UsernamePasswordAuthenticationToken(
//                      user.getEmail(),
//                      user.getPassword(),
//                      user.getAuthorities()
//              )
//      );
//    }catch (Exception e){
//      System.out.println(e.toString());
//    }
//    var refreshToken = jwtService.generateRefreshToken(user);
//    revokeAllUserTokens(user);
//    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
//            .refreshToken(refreshToken)
        .build();
  }

//  private void saveUserToken(User user, String jwtToken) {
//    var token = Token.builder()
//        .user(user)
//        .token(jwtToken)
//        .tokenType(TokenType.BEARER)
//        .expired(false)
//        .revoked(false)
//        .build();
//    tokenRepository.save(token);
//  }
//
//  private void revokeAllUserTokens(User user) {
//    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//    if (validUserTokens.isEmpty())
//      return;
//    validUserTokens.forEach(token -> {
//      token.setExpired(true);
//      token.setRevoked(true);
//    });
//    tokenRepository.saveAll(validUserTokens);
//  }

//  public void refreshToken(
//          HttpServletRequest request,
//          HttpServletResponse response
//  ) throws IOException {
//    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//    final String refreshToken;
//    final String userEmail;
//    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//      return;
//    }
//    refreshToken = authHeader.substring(7);
//    userEmail = jwtService.extractUsername(refreshToken);
//    if (userEmail != null) {
//      var user = this.repository.findByEmail(userEmail)
//              .orElseThrow();
//      if (jwtService.isTokenValid(refreshToken, user)) {
//        var accessToken = jwtService.generateToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, accessToken);
//        var authResponse = AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//      }
//    }
//  }
}
