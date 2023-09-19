package com.beetech.springsecurity.domain.service;

import java.util.List;

import com.beetech.springsecurity.domain.entity.*;
import com.beetech.springsecurity.domain.enums.OrderStatus;
import com.beetech.springsecurity.domain.enums.Role;
import com.beetech.springsecurity.domain.repository.*;
import com.beetech.springsecurity.web.dto.request.Order.CreateOrderDto;
import com.beetech.springsecurity.web.dto.request.Order.OrderUpdateDto;
import com.beetech.springsecurity.web.dto.response.Order.OrderDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final AuthenticationManager authenticationManager;
    private final OrderShippingDetailRepository orderShippingDetailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public OrderDto currentUserOrder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MstUser user = (MstUser) authentication.getPrincipal();
        Optional<Order> orderOptional = orderRepository.findByUser_id(user.getId());

        if(orderOptional.isEmpty()){
            ResponseDto response = new ResponseDto();
            response.setStatus(200);
            response.setMessage("User Order empty ...");
        }
        Order order = orderOptional.get();
        order.setUser(null);
        order.setOrderDetails(null);
        order.setOrderShippingDetail(null);
        order.setOrderDetails(null);

        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        return orderDto;
    }
    @Transactional
    public ResponseDto updateOrder(OrderUpdateDto orderUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MstUser loginUser = null;
        ResponseDto response = new ResponseDto();
        if (authentication != null) {  //check if there are user login
            loginUser = (MstUser) authentication.getPrincipal();
        }

        if (OrderStatus.getOrderStatus(orderUpdateDto.getStatus()) == null) {
            response.setStatus(401);
            response.setMessage("Status not found ...");
            return response;

        }

        if (loginUser.getRole() == Role.ADMIN) {
            Optional<Order> orderOptional = orderRepository.findByIdAndDisplayId(orderUpdateDto.getId(), orderUpdateDto.getDisplayId());

                if(orderOptional.isEmpty()){
                response.setStatus(401);
                response.setMessage("Order not found ...");
                return response;
            }

            Order order = orderOptional.get();

            order.setStatus(OrderStatus.getOrderStatus(orderUpdateDto.getStatus()));
            orderRepository.save(order);
            response.setStatus(200);
            response.setMessage("Order updated ...");

        }
        else if (loginUser.getRole() == Role.USER) {
            Optional<Order> orderOptional = orderRepository.findByUser_idAndDisplayId(loginUser.getId()
                    , orderUpdateDto.getDisplayId());
            if(orderOptional.isEmpty()){
                response.setStatus(401);
                response.setMessage("Order not found ...");
                return response;
            }

            Order order = orderOptional.get();

            // Update the order status
            order.setStatus(OrderStatus.getOrderStatus(orderUpdateDto.getStatus()));
            orderRepository.save(order);
            response.setStatus(200);
            response.setMessage("Order updated ...");


        }
        return response;
    }

    @Transactional
    public ResponseDto createOrder(CreateOrderDto createOrderDto){
        ResponseDto response = new ResponseDto();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MstUser user = (MstUser) authentication.getPrincipal();
        Optional<Cart> cartOptional = cartRepository.findByUser_id(user.getId());
        Order savedOrder;

        if(cartOptional.isEmpty()){
            response.setMessage("Cart empty ...");
            response.setStatus(401);
        }

        Cart userCart = cartOptional.get();
        Order order = new Order();
        order.setDisplayId("B"+0+ RandomString.make(3));
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(userCart.getTotal_price());
        order.setUserNote(userCart.getUserNote());
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);


        try{
            savedOrder = orderRepository.save(order);
            //create order shipping
            OrderShippingDetail orderShippingDetail = new OrderShippingDetail();
            orderShippingDetail.setOrder(savedOrder);
            orderShippingDetail.setAddress(createOrderDto.getAddress());

            City city = cityRepository.findByName(createOrderDto.getCity());
            District district = districtRepository.findByName(createOrderDto.getDistrict());
            orderShippingDetail.setCity(city);
            orderShippingDetail.setDistrict(district);
            orderShippingDetail.setPhone_number(createOrderDto.getPhoneNumber());
            orderShippingDetailRepository.save(orderShippingDetail);
        }catch (RuntimeException exception){
            response.setMessage("Some thing went wrong ...");
            response.setStatus(200);
            return response;
        }

        List<CartDetail> cartDetails =  cartDetailRepository.findAllByCart_id(userCart.getId());
        for (CartDetail cartDetail: cartDetails){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(savedOrder);
                orderDetail.setQuantity(cartDetail.getQuantity());
                orderDetail.setPrice(cartDetail.getPrice());
                orderDetail.setTotal_price(cartDetail.getTotal_price());
                orderDetail.setProduct(cartDetail.getProduct());
                orderDetailRepository.save(orderDetail);

        }

        //clear old cart
        try{
            cartDetailRepository.deleteAll(cartDetails);
            cartRepository.delete(userCart);
            response.setMessage("Order created ...");
            response.setStatus(200);
        }
        catch (RuntimeException exception){
            response.setMessage("Some thing went wrong ...");
            response.setStatus(200);

        }


        return response;

    }
}
