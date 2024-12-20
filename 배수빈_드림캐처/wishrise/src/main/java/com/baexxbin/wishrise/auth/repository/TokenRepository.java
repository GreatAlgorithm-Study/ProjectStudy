//package com.baexxbin.wishrise.auth.repository;
//
//import com.baexxbin.wishrise.auth.domain.Token;
//import org.springframework.data.repository.CrudRepository;
//
//import java.util.Optional;
//
//public interface TokenRepository extends CrudRepository<Token, String> {
//    Optional<Token> findByRefreshToken(String refreshToken);
//    Optional<Token> findByAccessToken(String accessToken);
//    Optional<Token> findByAuthId(String authId);
//}


// 이건 jwt를 데이터베이스에 저장하는 용도.
// jwt를 데이터베이스가 아닌 redis에서 관리할 것이기 때문에 필요없음