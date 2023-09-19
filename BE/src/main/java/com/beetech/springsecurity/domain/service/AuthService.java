package com.beetech.springsecurity.domain.service;


import com.beetech.springsecurity.domain.entity.ChangePasswordToken;
import com.beetech.springsecurity.domain.entity.MstUser;
import com.beetech.springsecurity.domain.repository.ChangePasswordTokenRepository;
import com.beetech.springsecurity.domain.repository.UserRepository;
import com.beetech.springsecurity.domain.utility.MailSenderMessages;
import com.beetech.springsecurity.web.dto.request.User.UserLoginDto;
import com.beetech.springsecurity.web.dto.request.User.UserRecoverPasswordDto;
import com.beetech.springsecurity.web.dto.request.User.UserRegisterDto;
import com.beetech.springsecurity.web.dto.request.User.UserResetPasswordDto;
import com.beetech.springsecurity.web.dto.response.EmailDetailDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import com.beetech.springsecurity.web.security.JwtHelper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ChangePasswordTokenRepository changePasswordTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    public ResponseDto login(UserLoginDto userLoginDto) {
        var response = new ResponseDto();
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userLoginDto.getLoginId(),
                    userLoginDto.getPassword()
            );

            // Use AuthenticationManager authenticate Authentication object
            Authentication login = authenticationManager.authenticate(authentication);

            // Prepare to create JWT token
            MstUser mstUser = (MstUser) login.getPrincipal();

            // Create JWT token
            String token = jwtHelper.createToken(mstUser);


            //response message

            response.setMessage("Logged in ...");
            response.setStatus(200);
            response.setData(token);
            return response;
        } catch (AuthenticationException e) {

            // If authentication fails, handle the exception and return a response indicating login failure

            response.setMessage("Invalid username or password");
            response.setStatus(401);
            return response;
        }
    }


    public ResponseDto register(UserRegisterDto userRegisterDto) {
        var response = new ResponseDto();
        try {
            // Create new MstUser
            response = userService.registerNewUser(userRegisterDto);
            return response;
        } catch (AuthenticationException e) {
            // If authentication fails, handle the exception and return a response indicating login failure
            response.setMessage("Can't register, some thing was wrong ...");
            response.setStatus(400);
            return response;
        }
    }

    public ResponseDto recoverPassword(UserRecoverPasswordDto userRecoverPasswordDto) {
        var response = new ResponseDto();

        MstUser user = (MstUser) userRepository.findByEmail(userRecoverPasswordDto.getLoginId());
        if (user == null) {
            response.setMessage("User email not exist");
            response.setStatus(401);
            return response;
        }

        LocalDate expireDate = LocalDateTime.now().plusMinutes(30).toLocalDate();

        String token = jwtHelper.createToken(user);
        var changePasswordToken = new ChangePasswordToken(user.getId(), token, expireDate);
        changePasswordToken.setUser(user);

        changePasswordTokenRepository.save(changePasswordToken);


        //send mail
        var emailDetail = new EmailDetailDto();
        emailDetail.setSubject("Reset Password");
        emailDetail.setRecipient(user.getEmail());
        emailDetail.setMsgBody(MailSenderMessages.resetPasswordMessage(token));

        emailService.sendMail(emailDetail);


        //response message
        response.setMessage("Please check your email");
        response.setStatus(200);

        return response;
    }

    public ResponseDto resetPassword(UserResetPasswordDto userResetPasswordDto) {

        //check confirm password and password
        var response = new ResponseDto();
        if (userResetPasswordDto.getNewPassword().matches(userResetPasswordDto.getConfirmPassword()) != true) {
            response.setMessage("Confirm password not match ...");
            response.setStatus(401);
            return response;
        }

//        validate token

        var userName = "";
        //get user
        try {
            if (!jwtHelper.validateToken(userResetPasswordDto
                            .getChangePasswordToken()
                    , userRepository.findByEmail(jwtHelper
                            .extractUsername(userResetPasswordDto.getChangePasswordToken())))) {
                response.setMessage("Invalid or expired token ...");
                response.setStatus(401);
                return response;
            }

            userName = jwtHelper.getUsernameFromToken(userResetPasswordDto.getChangePasswordToken());
        } catch (RuntimeException e) {
            response.setMessage("Invalid or expired token ...");
            response.setStatus(401);
            return response;
        }
        var user = userRepository.findByEmail(userName);


        user.setPassword(passwordEncoder.encode(userResetPasswordDto.getNewPassword()));

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            response.setMessage("Some thing wrong please try again ...");
            response.setStatus(401);
        }

        response.setMessage("Password updated ...");
        response.setStatus(200);
        return response;
    }
}
