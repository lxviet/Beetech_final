package com.beetech.springsecurity.domain.service;

import com.beetech.springsecurity.domain.entity.MstUser;
import com.beetech.springsecurity.domain.entity.Order;
import com.beetech.springsecurity.domain.enums.DeleteFlag;
import com.beetech.springsecurity.domain.enums.Role;
import com.beetech.springsecurity.domain.enums.UserStatus;
import com.beetech.springsecurity.domain.repository.OrderRepository;
import com.beetech.springsecurity.domain.repository.UserRepository;
import com.beetech.springsecurity.domain.utility.Constants.Constants;
import com.beetech.springsecurity.web.dto.request.User.*;
import com.beetech.springsecurity.web.dto.response.User.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

//    public List<UserDto> searchUsers(UserRequestDto userListDto , Pageable pageable) {
//        List<MstUser> users = userRepository.findAll();
//        List<UserDto> userDtoList = modelMapper
//                .map(users, new TypeToken<List<UserDto>>() {
//                }
//                        .getType());
//
//
//        return userDtoList;
//    }

    public List<UserDto> searchUsers(UserRequestDto userListDto, Pageable pageable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);

        String username = userListDto.getUserName();
        String email = userListDto.getLoginId();
        LocalDate startDate = LocalDate.parse(userListDto.getStartBirthDay(), formatter);
        LocalDate endDate = LocalDate.parse(userListDto.getEndBirthDay(), formatter);
        long totalPrice = userListDto.getTotalPrice();

        List<MstUser> users = userRepository.searchUsers(username, email,
                startDate, endDate, totalPrice, pageable);

        List<UserDto> userDtos = new ArrayList<>();
        for (MstUser user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setLoginId(user.getEmail());
            userDto.setUserName(user.getFullName());
            userDto.setBirthDay(user.getBirthDay());

            List<UserOrderDto> orderDtos = new ArrayList<>();
            List<Order> orders = user.getOrders();
            long orderTotalPrice = 0;
            for (Order order : orders) {
                UserOrderDto orderDto = new UserOrderDto();
                orderDto.setTotalPrice(order.getTotalPrice());
                orderDtos.add(orderDto);
                orderTotalPrice += orderDto.getTotalPrice();
            }
            userDto.setOrderDtos(orderDtos);
            userDto.setTotalPrice(orderTotalPrice);
            userDtos.add(userDto);
        }


        return userDtos;

    }

    public UserProfileDto getUser(int id) {
        MstUser user = userRepository.findById(id);

        return modelMapper.map(user, UserProfileDto.class);
    }

    public UserProfileDto userProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MstUser user = (MstUser) authentication.getPrincipal();
        return modelMapper.map(user, UserProfileDto.class);
    }

    public ResponseDto registerNewUser(UserRegisterDto userRegisterDto) {
        var response = new ResponseDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);
        MstUser existUser = userRepository.findByEmail(userRegisterDto.getLoginId());
        if (checkExistUser(existUser)) {
            if (existUser.getStatus() == UserStatus.LOCKED) {
                response.setMessage("Account has been locked ...");
                response.setStatus(400);
                return response;
            }
            response.setMessage("Account already used");
            response.setStatus(400);
            return response;
        }

        MstUser mstUser = new MstUser();
        mstUser.setEmail(userRegisterDto.getLoginId());
        mstUser.setBirthDay(LocalDate.parse(userRegisterDto.getBirthDay(), formatter));
        mstUser.setUserName(userRegisterDto.getUserName());
        mstUser.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        mstUser.setRole(Role.USER);
        mstUser.setStatus(UserStatus.NORMAL);
        mstUser.setDeleteflag(DeleteFlag.NORMAL);

        userRepository.save(mstUser);


        response.setMessage("OK");
        response.setStatus(200);

        return response;
    }

    public Boolean checkExistUser(MstUser user) {
        return user != null;
    }


    public MstUser createUser(UserCreateDto userCreateDto) {
        MstUser mstUser = modelMapper.map(userCreateDto, MstUser.class);
        return userRepository.save(mstUser);
    }

    public ResponseDto lockUser(String email) {
        var userResponse = new ResponseDto();

        var user = userRepository.findByEmail(email);
        user.setStatus(UserStatus.LOCKED);
        try {
            userRepository.save(user);
            userResponse.setMessage("User has been locked ...");
            userResponse.setStatus(200);
        } catch (RuntimeException exception) {
            userResponse.setMessage("Some thing wrong, please try again ...");
            userResponse.setStatus(401);
        }

        return userResponse;
    }
    public ResponseDto deleteUserByAdmin(String email){
        ResponseDto response = new ResponseDto();


        MstUser user = userRepository.findByEmail(email);
        try{
            user.setDeleteflag(DeleteFlag.DELETED);
            userRepository.save(user);
            response.setStatus(200);
            response.setMessage("User deleted ... ");
        }catch (RuntimeException exception){
            response.setStatus(401);
            response.setMessage("Some thing went wrong ...");
        }

        return response;
    }
    public ResponseDto deleteAccount(){
        ResponseDto response = new ResponseDto();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MstUser user = (MstUser) authentication.getPrincipal();

        user.setDeleteflag(DeleteFlag.DELETED);
        try{
            userRepository.save(user);
            response.setStatus(200);
            response.setMessage("User deleted ... ");
        }catch (RuntimeException exception){
            response.setStatus(401);
            response.setMessage("Some thing went wrong ...");
        }

        return response;
    }

    public ResponseDto userChangePassword(UserChangePasswordDto userChangePasswordDto) {

        var response = new ResponseDto();
        if (!userChangePasswordDto.getNewPassword().matches(userChangePasswordDto.getConfirmPassword())) {
            response.setMessage("Confirm password not match ...");
            response.setStatus(401);
            return response;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MstUser user = (MstUser) authentication.getPrincipal();

        if (user != null && passwordEncoder.matches(userChangePasswordDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
            try {

                userRepository.save(user);
                response.setStatus(200);
                response.setMessage("Password updated ...");
            } catch (RuntimeException e) {
                response.setMessage("Some thing wrong please try again ...");
                response.setStatus(401);
            }

            return response;

        }

        response.setMessage("Wrong old password ...");
        response.setStatus(401);
        return response;
    }

    public ResponseDto updateUser(UserUpdateDto userUpdateDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);

        MstUser user = userRepository.findByEmail(userUpdateDto.getLoginId());


        user.setUserName(userUpdateDto.getFullName());
        user.setBirthDay(LocalDate.parse(userUpdateDto.getBirthday(), formatter));

        var response = new ResponseDto();

        try {
            userRepository.save(user);
            response.setMessage("Updated user ...");
            response.setStatus(200);
        } catch (RuntimeException e) {
            response.setMessage("Some thing wrong please try again ...");
            response.setStatus(401);
        }

        return response;
    }

//    public UserManagerResponse deleteUser() {
//        var userResponse = new UserManagerResponse();
//
//
//        return userResponse;
//    }
}
