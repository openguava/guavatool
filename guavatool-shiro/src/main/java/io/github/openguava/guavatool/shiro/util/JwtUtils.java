package io.github.openguava.guavatool.shiro.util;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import io.github.openguava.guavatool.core.lang.ApiResult;
import io.github.openguava.guavatool.core.lang.FuncP;
import io.github.openguava.guavatool.core.lang.Result;

/**
 * jwt 工具类
 * @author openguava
 *
 */
public class JwtUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
	
	/**
	 * jwt签名 (HMAC with SHA-256)
	 * @param secret
	 * @param jwtId
	 * @param subject
	 * @param issuer
	 * @param issuedAt
	 * @param expiresAt
	 * @return
	 */
	public static Result<String> signWithHS256(String secret, final String jwtId, final String subject, final String issuer, final long issuedAt, final long expiresAt) {
		return signWithHS256(secret, new FuncP<Builder>() {
			
			@Override
			public void call(Builder builder) {
				builder.withJWTId(jwtId);
				builder.withSubject(subject);
				builder.withIssuer(issuer);
				builder.withIssuedAt(new Date(issuedAt));
				builder.withExpiresAt(new Date(expiresAt));
			}
		});
	}
	
	/**
	 * jwt签名 (HMAC with SHA-256)
	 * @param secret 密钥
	 * @param funcBuilder
	 * @return
	 */
	public static Result<String> signWithHS256(String secret, FuncP<Builder> funcBuilder) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    Builder builder = JWT.create();
		    if(funcBuilder != null) {
		    	funcBuilder.call(builder);
		    }
		    String token = builder.sign(algorithm);
		    return ApiResult.ok(token);
		} catch (JWTCreationException e){
		    return ApiResult.fail(e.getMessage());
		}
	}
	
	/**
	 * jwt签名 (HMAC with SHA-512)
	 * @param secret
	 * @param jwtId
	 * @param subject
	 * @param issuer
	 * @param issuedAt
	 * @param expiresAt
	 * @return
	 */
	public static Result<String> signWithHS512(String secret, final String jwtId, final String subject, final String issuer, final long issuedAt, final long expiresAt) {
		return signWithHS512(secret, new FuncP<Builder>() {
			
			@Override
			public void call(Builder builder) {
				builder.withJWTId(jwtId);
				builder.withSubject(subject);
				builder.withIssuer(issuer);
				builder.withIssuedAt(new Date(issuedAt));
				builder.withExpiresAt(new Date(expiresAt));
			}
		});
	}
	
	/**
	 * jwt签名 (HMAC with SHA-512)
	 * @param secret 密钥
	 * @param funcBuilder
	 * @return
	 */
	public static Result<String> signWithHS512(String secret, FuncP<Builder> funcBuilder) {
		try {
		    Algorithm algorithm = Algorithm.HMAC512(secret);
		    Builder builder = JWT.create();
		    if(funcBuilder != null) {
		    	funcBuilder.call(builder);
		    }
		    String token = builder.sign(algorithm);
		    return ApiResult.ok(token);
		} catch (JWTCreationException e){
		    return ApiResult.fail(e.getMessage());
		}
	}
	
	/**
	 * jwt签名 (RSASSA-PKCS1-v1_5 with SHA-256)
	 * @param publicKey 公钥
	 * @param privateKey 私钥
	 * @param funcBuilder 
	 * @return
	 */
	public static Result<String> signWithRS256(RSAPublicKey publicKey, RSAPrivateKey privateKey, FuncP<Builder> funcBuilder) {
		try {
		    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
		    Builder builder = JWT.create();
		    if(funcBuilder != null) {
		    	funcBuilder.call(builder);
		    }
		    String token = builder.sign(algorithm);
		    return ApiResult.ok(token);
		} catch (JWTCreationException e){
		    return ApiResult.fail(e.getMessage());
		}
	}
	
	/**
	 * jwt验证 token (HMAC with SHA-256)
	 * @param secret 密钥
	 * @param token token
	 * @return
	 */
	public static Result<DecodedJWT> verifyWithHS256(String secret, String token) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    JWTVerifier verifier = JWT.require(algorithm).build();
		    DecodedJWT jwt = verifier.verify(token);
		    return ApiResult.ok(jwt);
		} catch (SignatureVerificationException e) {
			LOGGER.debug(e.getMessage(), e);
		    return ApiResult.fail("签名验证失败！");
		} catch (JWTVerificationException e){
			LOGGER.debug(e.getMessage(), e);
		    return ApiResult.fail(e.getMessage());
		}
	}
	
	/**
	 * jwt验证 token (HMAC with SHA-512)
	 * @param secret 密钥
	 * @param token token
	 * @return
	 */
	public static Result<DecodedJWT> verifyWithHS512(String secret, String token) {
		try {
		    Algorithm algorithm = Algorithm.HMAC512(secret);
		    JWTVerifier verifier = JWT.require(algorithm).build();
		    DecodedJWT jwt = verifier.verify(token);
		    return ApiResult.ok(jwt);
		} catch (SignatureVerificationException e) {
			LOGGER.debug(e.getMessage(), e);
		    return ApiResult.fail("签名验证失败！");
		} catch (JWTVerificationException e){
			LOGGER.debug(e.getMessage(), e);
		    return ApiResult.fail(e.getMessage());
		}
	}
	
	/**
	 * jwt验证 (RSASSA-PKCS1-v1_5 with SHA-256)
	 * @param publicKey 公钥
	 * @param privateKey 私钥
	 * @param token token
	 * @return
	 */
	public static Result<DecodedJWT> verifyWithRSA256(RSAPublicKey publicKey, RSAPrivateKey privateKey, String token) {
		try {
		    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
		    JWTVerifier verifier = JWT.require(algorithm).build();
		    DecodedJWT jwt = verifier.verify(token);
		    return ApiResult.ok(jwt);
		} catch (SignatureVerificationException e) {
			LOGGER.debug(e.getMessage(), e);
		    return ApiResult.fail("签名验证失败！");
		}  catch (JWTVerificationException e){
			LOGGER.debug(e.getMessage(), e);
		    return ApiResult.fail(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		
	}
}
