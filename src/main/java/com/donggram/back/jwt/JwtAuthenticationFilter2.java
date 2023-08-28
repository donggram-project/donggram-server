//package com.donggram.back.jwt;
//
//import com.donggram.back.dto.SignInDto;
//import com.donggram.back.entity.Member;
//import com.donggram.back.service.PrincipalDetailService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.security.Principal;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter2 extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        log.info("로그인 시도 : JwtAuthenticationFilter2.attemptAuthentication");
//
//        ObjectMapper om = new ObjectMapper();
//        try{
//            log.info("1. username, password를 받는다.");
//            SignInDto login = om.readValue(request.getInputStream(), SignInDto.class);
//            log.info(login.toString());
//
//            //username, password 이용해서 token 발급
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(login.getStudentId(), login.getPassword());
//            log.info(authenticationToken.getPrincipal().toString());
//            log.info(authenticationToken.getCredentials().toString());
//            log.info("================================================");
//            // 2. 정상적인 로그인 시도 여부를 검증한다.
//            log.info("2. 정상적인 로그인 시도 여부를 검증한다.");
//            // -> 로그인 정보를 가지고 임시로 Auth 토큰을 생성해서 인증을 확인한다.
//            // -> DI 받은 authenticationManager로 로그인 시도한다.
//            // -> DetailService를 상속받은 PrincipalDetailsService가 호출되고 loadUserByUsername() 함수가  실행된다.
//            // -> authenticate()에 토큰을 넘기면 PrincipalDetailsService.class -> loadUserByUsername() 메서드 실행된다.
//            // DB에 저장되어있는 username & password가 일치하면 authentication이 생성된다.
//            log.info("-> Authenticate Start");
//            Authentication authentication =
//                    authenticationManager.authenticate(authenticationToken);
//            log.info("-> Authentication End");
//            log.info("==========================================");
//            // 3. PrincipalDetails를 세션에 저장한다. (권한 관리를 위해서 세션에 저장)
//
//            log.info("3. 로그인 성공.");
//            Member member = (Member) authentication.getPrincipal();
//            log.info("username : " + member.getUsername());
//            log.info("password : " + member.getPassword());
//            log.info("============================================");
//            return authentication;
//
//        } catch(IOException e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        log.info("인증완료 : JwtAuthenticationFilter.successfulAuthentication");
//
//        super.successfulAuthentication(request, response, chain, authResult);
//    }
//}
