package com.basisttha.Kreta.Service;

import com.basisttha.Kreta.Repository.RevokedTokenRepository;
import com.basisttha.Kreta.Repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basisttha.Kreta.DTO.LoginRequest;
import com.basisttha.Kreta.DTO.RefreshTokenRequest;
import com.basisttha.Kreta.DTO.RefreshTokenResponse;
import com.basisttha.Kreta.DTO.AuthResponse;
import com.basisttha.Kreta.DTO.RegisterRequest;
import com.basisttha.Kreta.Exception.EmailAlreadyExistsException;
import com.basisttha.Kreta.Exception.InvalidCredentialsException;
import com.basisttha.Kreta.Exception.InvalidRefreshTokenException;
import com.basisttha.Kreta.Model.Plan;
import com.basisttha.Kreta.Model.RefreshToken;
import com.basisttha.Kreta.Model.RevokedToken;
import com.basisttha.Kreta.Model.Role;
import com.basisttha.Kreta.Model.Subscription;
import com.basisttha.Kreta.Model.Users;
import com.basisttha.Kreta.Repository.PlanRepository;
import com.basisttha.Kreta.Repository.RefreshTokenRepository;
import com.basisttha.Kreta.Repository.UsersRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final RevokedTokenRepository revokedTokenRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PlanRepository planRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    //Stripe makes a customer BEFORE saving it in db because if Stripe fails to create a user, then the user wont be saved. Hence no issues
    @Transactional
    public AuthResponse register(RegisterRequest req){
        //1. check if email exists, if yes throw UserAlreadyExists exception, if not, hash password, ask
        // stripe to make a new user, get back the customer id, make a new user object, save in DB, generate new JWT and return it
        String email = req.getEmail();
        Optional<Users> userTemp = userRepository.findByEmail(email);
        if(userTemp.isPresent()){
            throw new EmailAlreadyExistsException("Email is already in use. Please log in or use another email.");
        }
        //email is valid, password strength check will be frontends job, so all good here
        String hash = passwordEncoder.encode(req.getPassword());
        String stripeCustomerId = createStripeCustomer(email);
        Users newUser = Users.builder().email(email).passwordHash(hash).stripeCustomerId(stripeCustomerId).role(Role.USER).build();
        userRepository.save(newUser);

        //create a free subscription row
        Plan freePlan = planRepository.findByPlanName("FREE").orElseThrow(() -> new RuntimeException("Free plan not found. Check DataInitializer"));
        Subscription freeSub = Subscription.builder().user(newUser).plan(freePlan).subscriptionStatus("active").build();
        subscriptionRepository.save(freeSub);

        String token = jwtService.generateToken(newUser);
        UUID refreshToken = createRefreshToken(newUser);//Already uses SecureRandom so didnt use Secure Random manually
        return AuthResponse.builder().token(token).refreshToken(refreshToken).email(newUser.getEmail()).role(newUser.getRole()).build();
    }

    private String createStripeCustomer(String email){
        try{
            CustomerCreateParams params = CustomerCreateParams.builder().setEmail(email).build();
            Customer customer = Customer.create(params);
            return customer.getId();
        }catch(StripeException e){
            throw new RuntimeException("Error creating a Stripe customer" + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest req){
        //check if email exists, hash given password,match the hashes, generate new JWT, return
        Users user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Please check the username/password"));
        //email exists, now check password
        if(!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())){
            throw new InvalidCredentialsException("Please check the username/password");

        }
        //user is legitimate
        String token = jwtService.generateToken(user);
        UUID refreshToken = createRefreshToken(user);
        return AuthResponse.builder().token(token).refreshToken(refreshToken).email(user.getEmail()).role(user.getRole()).build();
    }

    @Transactional
    public RefreshTokenResponse refreshJWT(RefreshTokenRequest req){
        //check if the refreshToken exists for the user. check if they match, if yes, generate new jwt and send back
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshTokenAndIsRevokedFalse(req.getRefreshToken()).orElseThrow(() -> new InvalidRefreshTokenException("No refresh tokens exist"));
        if(refreshToken.getExpiry().isBefore(LocalDateTime.now())){
            throw new InvalidRefreshTokenException("Refresh token is expired");
        }
        //the token is valid, the current owner has that refresh token, revoke current refreshtoken, generate and send back a new jwt and refreshtoken
        refreshToken.setIsRevoked(true);
        refreshTokenRepository.save(refreshToken);
        //as owner must be present for a refreshtoken, if user isnt found then the token is invalid
        Users user = refreshToken.getOwner();
        String token = jwtService.generateToken(user);
        //We return a new refresh token every time a new JWT is requested.
        UUID newRefreshToken = createRefreshToken(user);
        return RefreshTokenResponse.builder().token(token).newRefreshToken(newRefreshToken).build();//kept as dto as sending only jwt would be insecure? or not?
    }

    private UUID createRefreshToken(Users currentUser){
        //create refresh token,save in DB
        UUID refreshToken = UUID.randomUUID();
        RefreshToken r = RefreshToken.builder().refreshToken(refreshToken).owner(currentUser).expiry(LocalDateTime.now().plusHours(168)).isRevoked(false).build();
        refreshTokenRepository.save(r);
        return r.getRefreshToken();
    }

    @Transactional
    public void logout(){
        //revoke jwt, revoke refresh token
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        LocalDateTime expiry = jwtService.extractExpiresAt(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        RevokedToken revokedToken = RevokedToken.builder().token(token).revokedAt(LocalDateTime.now()).expiresAt(expiry).build();
        revokedTokenRepository.save(revokedToken);
        List<RefreshToken> refreshToken = refreshTokenRepository.findByOwnerAndIsRevokedFalse(user);
        refreshToken.forEach( rt -> {
            rt.setIsRevoked(true);
        });
        refreshTokenRepository.saveAll(refreshToken);
    }
}
