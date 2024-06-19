
<div align="center">
<h1>📋 티켓 기반 게시판 서비스 👨‍💻</h1>
매일 선착순으로 티켓을 발급 받아 게시글을 작성하는 게시판 서비스 프로젝트입니다.
</div>

# Introduction
 - 개발 인원: 1명
 - 개발 기간: 2024.02-(진행 중)
 - 기술 스택: Java 17, Spring Boot 3.2.2, MySQL 8, Spring Data JPA, Redis, Caffeine, JJWT, AWS

# Main Feature

- **게시글**: CRUD 기능, 게시글 작성 시 게시글 티켓 확인
- **댓글, 회원**: CRUD 기능
- **첨부 파일(이미지)**: 첨부 파일 업로드/조회(게시글 조회 시) 기능
- **티켓(게시글)**: 선착순 티켓 발급, 발급 티켓 조회 기능
- **인증**: 인증 방식(세션 기반, JWT 기반)을 애플리케이션 실행 시점에 선택


# Project Structure

```
main/java/com/boldfaced7/board

├── auth // 인증
│   ├── interceptor
│      ├── jwt
│      └── session
│   └── jwt
├── config        // 설정 관련(AWS, Cache, DB, JPA ..)
├── controller    // 컨트롤러
├── domain        // 도메인
├── dto
│   ├── request
│   └── response
├── error         // 커스텀 예외 및 에러 클래스
│   └── exception
├── repository    // 리포지토리
│   └── filestore // 이미지 저장 관련 클래스
├── service       // 서비스
└── resources
```

# ERD
![board_erd](https://github.com/boldfaced7/springboot-project-board/assets/88390589/bf6653d0-1aa2-4080-8f39-f9c680972de4)


# API
## Article
- 게시글 단건 조회 `(GET /api/articles/{articleId})`
- 전체 게시글 목록 조회 `(GET /api/articles)`
- 회원 작성 게시글 조회 `(GET /api/members/{memberId}/articles)`
- 게시글 등록 `(POST /api/articles)`
- 게시글 수정 `(PATCH /api/articles/{articleId})`
- 게시글 삭제 `(DELETE /api/articles/{articleId})`

## ArticleComment
- 댓글 단건 조회 `(GET /api/articleComments/{articleCommentId})`
- 전체 댓글 목록 조회 `(GET /api/articleComments)`
- 게시글 댓글 목록 조회 `(GET /api/articles/{articleId}/articleComments)`
- 회원 작성 댓글 목록 조회 `(GET /api/members/{memberId}/articleComments)`
- 댓글 등록 `(POST /api/articleComments)`
- 댓글 수정 `(PATCH /api/articleComments/{articleCommentId})`
- 댓글 삭제 `(DELETE /api/articleComments/{articleCommentId})`

## ArticleTicket
- 티켓 단건 조회 `(GET /api/articleTickets/{articleTicketId})`
- 전체 티켓 목록 조회 `(GET /api/articleTickets)`
- 회원 발급 티켓 목록 조회 `(GET /api/members/{memberId}/articleTickets)`
- 티켓 발급 `(POST /api/articleTickets)`

## Attachment
- 첨부파일 업로드 `(POST /api/attachments)`

## Authentication
- 로그인 `(POST /api/login)`
- 로그아웃 `(POST /api/logout)`
- 액세스 토큰 재발급 `(GET /api/jwt)`

## Member
- 회원 단건 조회 `(GET /api/members/{memberId})`
- 전체 회원 목록 조회 `(GET /api/members)`
- 회원가입 `(POST /api/signUp)`
- 회원 닉네임 수정 `(PATCH /api/members/{memberId}/nicknames)`
- 회원 비밀번호 수정 `(PATCH /api/members/{memberId}/passowords)`
- 탈퇴 `(DELETE /api/members/{memberId})`


# 주요 구현
## 조회 성능 개선
### 지연 로딩 & Fetch Join
- 다대일 관계 엔티티를 지연 로딩하도록 설정하고, N+1 발생 케이스에 Fetch Join을 사용해 쿼리 양을 최소화
```java
public class ArticleComment extends BaseTimeEntity {
    // ...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    // ...
}
```
```java
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    // ...
    @Query("select ac from ArticleComment ac" +
            " join fetch ac.article" +
            " join fetch ac.member" +
            " where ac.id = :id and ac.active = true")
    public Optional<ArticleComment> findById(@Param("id") Long id);
    // ...
}
```
<div align="center">
  <b>⇒ 조회 기능 부하 테스트 시 평균 응답 속도 70%(252ms → 73ms) 감소</b>
</div>

   
### OSIV Off
- Fetch Join 사용 이점을 극대화하기 위해 OSIV 설정을 false로 변경해, DB 커넥션 리소스 사용을 최적화
```java
spring:
  jpa:
    open-in-view: false
```
<div align="center">
  <b>⇒ 조회 기능 부하 테스트 시 평균 응답 속도 53%(73ms → 34ms) 감소</b>
</div>


### Caching
- DB 부하 감소를 위해 로컬 캐시(Caffeine)와 글로벌 캐시(Redis)를 각각 사용해 평균 응답 속도를 비교하고, 프로젝트의 아키텍쳐와 배포 환경을 고려해 로컬 캐시를 적용

```java
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleCommentService {
    // ...
    @Cacheable(value = "articleComments", key = "#pageable.pageNumber")
    @Transactional(readOnly = true)
    public CustomPage<ArticleCommentDto> getArticleComments(Pageable pageable) {
        Page<ArticleComment> articleComments = articleCommentRepository.findAll(pageable);
        CustomPage<ArticleComment> converted = CustomPage.convert(articleComments);

        return converted.map(ArticleCommentDto::new);
    }
    // ...
}
```
<div align="center">
  <b>⇒ 조회 기능 부하 테스트 시 평균 응답 속도 35%(Redis, 34ms → 22ms), 76%(Caffeine, 34ms → 8ms) 감소</b>
</div>

### 관련 포스팅
- 페이징과 Lazy Loading & Fetch Join   
[0. 버전 및 테스트 설정 소개](https://blog.naver.com/boldfaced7/223393113424)   
[1. 게시글 단건 조회](https://blog.naver.com/boldfaced7/223393128917)   
[2. 게시글 리스트 조회](https://blog.naver.com/boldfaced7/223394615170)   
[3. 회원 작성 게시글 리스트 조회](https://blog.naver.com/boldfaced7/223394633799)   
[4. 피드백 적용](https://blog.naver.com/boldfaced7/223395088564)   

- OSIV: [적용 및 성능 비교](https://blog.naver.com/boldfaced7/223396989715)

- 글로벌 캐시(Redis)   
[1. Redis 설정](https://blog.naver.com/boldfaced7/223404601610)   
[2. 캐싱 어노테이션](https://blog.naver.com/boldfaced7/223404806053)   
[3. 성능 비교](https://blog.naver.com/boldfaced7/223404814179)   

- 로컬 캐시(Caffeine, ConcurrentHashMap 기반)   
[1. Caffeine 설정](https://blog.naver.com/boldfaced7/223405696636)   
[2. 성능 비교 및 분석](https://blog.naver.com/boldfaced7/223405770745)   


## 티켓 발급 관련 동시성 문제 해결

- 티켓 데이터 생성, 티켓 상태 확정 과정을 분리하고, 데이터 생성 후 id 값을 비교해 상태를 확정한 뒤 티켓을 발급

```java
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleTicketService {
    // ...
    @Value("${ticket.total}")
    private int totalTicket;
    private final Map<LocalDate, Boolean> soldOutChecker = soldOutChecker();
    // ...
    // 티켓 발급 메소드
    public long issueTicket() {
        // 티켓 발급 가능 여부 파악 이전에 티켓 데이터 생성
        ArticleTicket saved = createTicket();
        // 생성된 티켓 데이터의 id를 이용해 티켓 상태 확정
        return confirmTicket(saved);
    }

    private ArticleTicket createTicket() {
        if (soldOutChecker.get(LocalDate.now())) {
            throw new ArticleTicketSoldOutException();
        }
        ArticleTicket articleTicket = ArticleTicket.builder()
                .member(findMemberByAuthInfo())
                .build();

        return articleTicketRepository.save(articleTicket);
    }

    private long confirmTicket(ArticleTicket saved) {
        // 상태 확정 대상 티켓의 발급 순서를 확인하기 위해, 전날 제일 마지막으로 발급된 티켓의 id 조회
        Long criteria = articleTicketRepository
                .findCriteria(LocalDate.now().atStartOfDay()).orElse(0L);

        // 상태 확정 대상 티켓의 발급 순서가 총 발급 티켓의 수를 초과하면, 대상 티켓 데이터 제거
        if (criteria + totalTicket < saved.getId()) {
            soldOutChecker.put(LocalDate.now(), true);
            articleTicketRepository.delete(saved);
            throw new ArticleTicketSoldOutException();
        }
        return saved.getId();
    }
    // ...
}
```
<div align="center">
  <b>⇒ 코드를 통한 명시적인 잠금 없이 동시성 문제를 해결</b>
</div>


## 인증 기능 구현

- 스프링 시큐리티 없이 WebMvcConfigurer, HandlerInterceptor를 이용해 인증 기능을 구현
- ThreadLocal을 사용해, 인증 관련 로직 수행 시 인증 정보 접근을 간소화

```java
@Component
public class AuthInfoHolder {
    private static final ThreadLocal<AuthResponse> authInfo = new ThreadLocal<>();

    public static void setAuthInfo(AuthResponse authResponse) {
        authInfo.set(authResponse);
    }

    public static AuthResponse getAuthInfo() {
        return authInfo.get();
    }

    public static void releaseAuthInfo() {
        authInfo.remove();
    }

    public static boolean isEmpty() {
        return authInfo.get() == null;
    }
}
```

- 애플리케이션 실행 시점에 인증 방식(세션 기반, JWT 기반) 선택 가능하도록 구현
```java
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthCheckInterceptor authCheckInterceptor;         // 인터페이스
    private final AuthInfoHoldInterceptor authInfoHoldInterceptor;   // 인터페이스
    private final LoginSuccessInterceptor loginSuccessInterceptor;   // 인터페이스
    private final LogoutSuccessInterceptor logoutSuccessInterceptor; // 인터페이스

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInfoHoldInterceptor)
                .order(1)
                .addPathPatterns("/api/**");

        registry.addInterceptor(authCheckInterceptor)
                .order(2)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/signUp", "/api/jwt");

        registry.addInterceptor(loginSuccessInterceptor)
                .order(3)
                .addPathPatterns("/api/login");

        registry.addInterceptor(logoutSuccessInterceptor)
                .order(4)
                .addPathPatterns("/api/logout");
    }
}
```

- AuthInfoHoldInterceptor(인증 여부 검증) 호출 후 AuthInfoHoldInterceptor(인증 정보 저장) 호출해, 컨트롤러 단위 테스트로부터 인증 방식을 추상화
```java
class ArticleControllerTest {
    @BeforeEach
    void setSessionAndTestTemplate() {
        AuthInfoHolder.setAuthInfo(authResponse());
    }
```
<div align="center">
  <b>⇒ 인증이 필요한 요청을 수행하는 컨트롤러의 메소드를 테스트할 때, 인증 방식과 관계 없이 동일하게 로그인 처리가 가능</b>
</div>


### Session
- 세션 기반 인증 기능을 구현하기 위해 인터셉터를 다음과 같이 구현
<div align="center">
<details>
<summary><b>SessionAuthInfoHoldInterceptor</b></summary>
<div align="left">

```java
@Component
public class SessionAuthInfoHoldInterceptor implements AuthInfoHoldInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(SessionConst.AUTH_RESPONSE) != null) {
            AuthInfoHolder.releaseAuthInfo();
            AuthInfoHolder.setAuthInfo((AuthResponse) session.getAttribute(SessionConst.AUTH_RESPONSE));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthInfoHolder.releaseAuthInfo();
    }
}
```
</div>
</details>
</div>

<div align="center">
<details>
<summary><b>SessionAuthCheckInterceptor</b></summary>
<div align="left">

```java
@Component
public class SessionAuthCheckInterceptor implements AuthCheckInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (AuthInfoHolder.isEmpty()) {
            throw new UnauthorizedException();
        }
        return true;
    }
}
```
</div>
</details>
</div>

<div align="center">
<details>
<summary><b>SessionLoginSuccessInterceptor</b></summary>
<div align="left">

```java
@Component
public class SessionLoginSuccessInterceptor implements LoginSuccessInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();

        session.setMaxInactiveInterval(1800);
        session.setAttribute(SessionConst.AUTH_RESPONSE, authInfo);
    }
}

```
</div>
</details>
</div>

<div align="center">
<details>
<summary><b>SessionLogoutSuccessInterceptor</b></summary>
<div align="left">

```java
@Component
public class SessionLogoutSuccessInterceptor implements LogoutSuccessInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return true;
    }
}
```
</div>
</details>
</div>


### JWT
- XSS, CSRF 공격에 대처하기 위해, 액세스/리프레시 토큰을 각각 Authorization 헤더, 쿠키(HttpOnly, Secure 플래그 설정)에 담아 전달
```java
public class JwtLoginSuccessInterceptor implements LoginSuccessInterceptor {
    // ...
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        String refreshToken = jwtProvider.generateRefreshToken(authInfo);
        String accessToken = jwtProvider.generateAccessToken(authInfo);

        addRefreshTokenToCookie(request, response, refreshToken);
        response.addHeader(JwtProvider.HEADER_AUTHORIZATION, accessToken);
    }
    // ...
}
```

- 액세스 토큰 재발급을 위해 리프레시 토큰을 검증할 때, 만료된 액세스 토큰 검증 과정을 추가
```java
@Service
public class JwtProvider {
    // ...
    public String refreshAccessToken(String accessToken, String refreshToken) {
        long fromAccessToken = getMemberId(accessToken, false);
        long fromRefreshToken = getMemberId(refreshToken, true);

        if (fromRefreshToken != fromAccessToken) {
            throw new InvalidRefreshTokenException();
        }
        return generateAccessToken(extractAuthInfo(refreshToken));
    }

    private long getMemberId(String token, boolean expirationCheck) {
        // 액세스 토큰이 유효 기간은 지났지만 정상적으로 발급된 토큰임을 검증
        try {
            return getMemberId(parseClaims(token));
        } catch (ExpiredJwtException e) {
            if (expirationCheck) throw new InvalidRefreshTokenException();
            else return getMemberId(e.getClaims());
        } catch (Exception e) {
            throw new InvalidRefreshTokenException();
        }
    }
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private long getMemberId(Claims payload) {
        return (long) (int) payload.get("memberId");
    }
    // ...
}
```
<div align="center">
  <b>⇒ CSRF로 탈취할 수 없는 액세스 토큰과 XSS로 탈취할 수 없는 리프레시 토큰을 모두 사용해, 공격자의 토큰 재발급을 차단</b>
</div>
<br/>

- JWT 기반 인증 기능을 구현하기 위해 인터셉터를 다음과 같이 구현

<div align="center">
<details>
<summary><b>JwtAuthInfoHoldInterceptor</b></summary>
<div align="left">

```java
@Component
@RequiredArgsConstructor
public class JwtAuthInfoHoldInterceptor implements AuthInfoHoldInterceptor {
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(JwtProvider.HEADER_AUTHORIZATION);
        String accessToken = JwtProvider.extractToken(header);

        if (jwtProvider.verifyToken(accessToken)) {
            AuthInfoHolder.releaseAuthInfo();
            AuthInfoHolder.setAuthInfo(jwtProvider.extractAuthInfo(accessToken));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthInfoHolder.releaseAuthInfo();
    }
}
```
</div>
</details>
</div>

<div align="center">
<details>
<summary><b>JwtAuthCheckInterceptor</b></summary>
<div align="left">

```java
@Component
public class JwtAuthCheckInterceptor implements AuthCheckInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (AuthInfoHolder.isEmpty()) {
            throw new UnauthorizedException();
        }
        return true;
    }
}
```
</div>
</details>
</div>

<div align="center">
<details>
<summary><b>JwtLoginSuccessInterceptor</b></summary>
<div align="left">

```java
@Component
@RequiredArgsConstructor
public class JwtLoginSuccessInterceptor implements LoginSuccessInterceptor {
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        String refreshToken = jwtProvider.generateRefreshToken(authInfo);
        String accessToken = jwtProvider.generateAccessToken(authInfo);

        addRefreshTokenToCookie(request, response, refreshToken);
        response.addHeader(JwtProvider.HEADER_AUTHORIZATION, accessToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) jwtProperties.getRefreshExpiration();
        CookieUtil.addCookie(response, JwtProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }
}
```
</div>
</details>
</div>

<div align="center">
<details>
<summary><b>JwtLogoutSuccessInterceptor</b></summary>
<div align="left">

```java
@Component
public class JwtLogoutSuccessInterceptor implements LogoutSuccessInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CookieUtil.deleteCookie(request, response, JwtProvider.REFRESH_TOKEN_COOKIE_NAME);
        response.addHeader(JwtProvider.HEADER_AUTHORIZATION, "logout");

        return true;
    }
}
```
</div>
</details>
</div>


### 관련 포스팅
- 세션 기반 인증 기능 개발   
[1. Spring Interceptor, HttpSession](https://blog.naver.com/boldfaced7/223458983410)   
[2. 인증을 컨트롤러에서 처리?](https://blog.naver.com/boldfaced7/223460110680)   
[3. ThreadLocal을 사용해 인증 정보를 전달해보자](https://blog.naver.com/boldfaced7/223460419290)   
[4.1. 구현 (1): 인터셉터](https://blog.naver.com/boldfaced7/223461572175)   
[4.2. 구현 (2): 로그인/로그아웃, PasswordEncoder](https://blog.naver.com/boldfaced7/223462178993)   
[5. 실행 with Postman](https://blog.naver.com/boldfaced7/223464640355)   


- JWT 기반 인증 기능 개발   
[1. JWT를 이용한 인증과 토큰 탈취 이슈](https://blog.naver.com/boldfaced7/223473250444)   
[2. Access Token과 Refresh Token을 도입해보자](https://blog.naver.com/boldfaced7/223473818377)   
[3. JWT를 얼마나 Stateless하게 쓸 수 있을까?](https://blog.naver.com/boldfaced7/223474888473)   
[4.1. 구현 (1): JWT 관련 클래스 만들기](https://blog.naver.com/boldfaced7/223477145301)   
[4.2. 구현 (2): 인터셉터 구현, SSL 적용, 토큰 재발급](https://blog.naver.com/boldfaced7/223484252117)   
[2.5. 실행 with Postman](https://blog.naver.com/boldfaced7/223484301844)   
