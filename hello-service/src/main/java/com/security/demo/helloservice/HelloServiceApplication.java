package com.security.demo.helloservice;

import org.jose4j.jwa.AlgorithmConstraints;

import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloServiceApplication.class, args);
	}

	@GetMapping("/user")
	public String greetUser() {
		RsaJsonWebKey rsaJsonWebKey = null;
		String jwt = null;
		try {
		    // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
		    // be used to validate and process the JWT.
		    // The specific validation requirements for a JWT are context dependent, however,
		    // it typically advisable to require a (reasonable) expiration time, a trusted issuer, and
		    // and audience that identifies your system as the intended recipient.
		    // If the JWT is encrypted too, you need only provide a decryption key or
		    // decryption key resolver to the builder.
		    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
		            .setRequireExpirationTime() // the JWT must have an expiration time
		            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
		            .setRequireSubject() // the JWT must have a subject claim
		            .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
		            .setExpectedAudience("Audience") // to whom the JWT is intended for
		            .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
		            .setJweAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
		                    new AlgorithmConstraints(ConstraintType.WHITELIST, // which is only RS256 here
		                            AlgorithmIdentifiers.RSA_USING_SHA256))
		            .build(); // create the JwtConsumer instance
		    try
		    {
		        //  Validate the JWT and process it to the Claims
		        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
		        System.out.println("JWT validation succeeded! " + jwtClaims);
		        return ("Welcome User");
		    }
		    catch (InvalidJwtException e)
		    {
		        // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
		        // Hopefully with meaningful explanations(s) about what went wrong.
		        System.out.println("Invalid JWT! " + e);

		        // Programmatic access to (some) specific reasons for JWT invalidity is also possible
		        // should you want different error handling behavior for certain conditions.

		        // Whether or not the JWT has expired being one common reason for invalidity
		        if (e.hasExpired())
		        {
		            try {
						System.out.println("JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime());
					} catch (MalformedClaimException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }

		        // Or maybe the audience was invalid
		        if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID))
		        {
		            try {
						System.out.println("JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience());
					} catch (MalformedClaimException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
		        throw new Exception("UnAuthorized");
		    }
			
		} catch(Exception e) {
			return "UnAuthorized";
		}
	}
}
