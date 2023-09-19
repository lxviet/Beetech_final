package com.beetech.springsecurity.domain.service;

import com.beetech.springsecurity.domain.entity.Cart;
import com.beetech.springsecurity.domain.entity.CartDetail;
import com.beetech.springsecurity.domain.entity.MstUser;
import com.beetech.springsecurity.domain.entity.Product;
import com.beetech.springsecurity.domain.repository.CartDetailRepository;
import com.beetech.springsecurity.domain.repository.CartRepository;
import com.beetech.springsecurity.domain.repository.ProductRepository;
import com.beetech.springsecurity.domain.repository.UserRepository;
import com.beetech.springsecurity.web.dto.request.Cart.AddProductToCartDto;
import com.beetech.springsecurity.web.dto.request.Cart.CartRequestDto;
import com.beetech.springsecurity.web.dto.request.Cart.DeleteCartDto;
import com.beetech.springsecurity.web.dto.request.Cart.SyncCartDto;
import com.beetech.springsecurity.web.dto.request.CartQuantityDto;
import com.beetech.springsecurity.web.dto.response.Cart.CartDetailDto;
import com.beetech.springsecurity.web.dto.response.Cart.CartDto;
import com.beetech.springsecurity.web.dto.response.Cart.CartQuantityResponseDto;
import com.beetech.springsecurity.web.dto.response.Cart.ProductInCartDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CartService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CartDto getCarts(CartRequestDto cartRequestDto) {
        Optional<Cart> optionalCart = null;
        if (cartRequestDto.getToken() == null) {
            //get current logged in user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MstUser user = (MstUser) authentication.getPrincipal();
            //get user cart details
            optionalCart = cartRepository.findByUser_id(user.getId());

        } else if (cartRequestDto.getToken() != null) {

            optionalCart = cartRepository.findByToken(cartRequestDto.getToken());

        }

        if(optionalCart.isEmpty()){
            return null;
        }

        Cart cart = optionalCart.get();
        List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_id(cart.getId());

        var cartDto = modelMapper.map(cart, CartDto.class);

        List<CartDetailDto> cartDetailDtos = new ArrayList<>();

        for (CartDetail cartDetail : cartDetails) {
            CartDetailDto cartDetailDto = modelMapper.map(cartDetail, CartDetailDto.class);
            cartDetailDto.setProductInCartDto(modelMapper.map(cartDetail.getProduct(), ProductInCartDto.class));
            cartDetailDtos.add(cartDetailDto);
        }

        cartDto.setCartDetailDtos(cartDetailDtos);
        return cartDto;


    }

    @Transactional
    public ResponseDto deleteCart(DeleteCartDto deleteCartDto) {
        ResponseDto response = new ResponseDto();
        Cart cart = null;

        // Get the current logged-in user (if available)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (deleteCartDto.getToken() != null) {
            // Find the cart based on the provided token
            cart = cartRepository.findByToken(deleteCartDto.getToken()).orElse(null);
        } else if (authentication != null) {
            // Find the cart for the logged-in user
            MstUser user = (MstUser) authentication.getPrincipal();
            cart = cartRepository.findByUser_id(user.getId()).orElse(null);
        }

        if (cart == null) {
            response.setStatus(401);
            response.setMessage("Cart not found ...");
            return response;
        }

        if (cart.getVersion_no() != deleteCartDto.getVersionNo()) {
            response.setStatus(401);
            response.setMessage("Cart version does not match ...");
            return response;
        }

        if (deleteCartDto.getClearCart() == 1) {
            // Clear the entire cart
            List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_id(cart.getId());
            cartDetailRepository.deleteAll(cartDetails);
            cartRepository.delete(cart);
            response.setStatus(200);
            response.setMessage("Cart deleted ...");
            int totalQuantity = cartDetailRepository.sumQuantityByCardId(cart.getId());
            response.setData("Cart quantity: "+ totalQuantity);
        } else if (deleteCartDto.getClearCart() == 0) {
            // Remove a specific cart detail from the cart
            CartDetail cartDetail = cartDetailRepository.findById(deleteCartDto.getDetailId()).orElse(null);
            if (cartDetail == null || cartDetail.getCart().getId() != cart.getId()) {
                response.setStatus(401);
                response.setMessage("Cart detail not found in the cart ...");
                return response;
            }

            // Update the cart details and cart total price
            int newVersionNo = cart.getVersion_no() + 1;
            cart.setVersion_no(newVersionNo);
            cart.setTotal_price(cart.getTotal_price() - (cartDetail.getPrice() * cartDetail.getQuantity()));
            cartDetailRepository.delete(cartDetail);
            cartRepository.save(cart);

            // Calculate and set the total quantity for the response
            int totalQuantity = cartDetailRepository.sumQuantityByCardId(cart.getId());
            response.setData("Cart quantity: "+ totalQuantity);
            response.setStatus(200);
            response.setMessage("Cart detail deleted ...");
        } else {
            response.setStatus(401);
            response.setMessage("Invalid clearCart value ...");
        }

        return response;
    }

    public Boolean validateIsNewCartVersion(Cart cart, int versionNo) {
        return cart.getVersion_no() != versionNo ? false : true;
    }

    @Transactional
    public ResponseDto syncCart(SyncCartDto syncCartDto) {
        //get current logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MstUser user = (MstUser) authentication.getPrincipal();
        ResponseDto response = new ResponseDto();
        Optional<Cart> optionalDraftCart = cartRepository.findByToken(syncCartDto.getToken());
        if(optionalDraftCart.isEmpty()){
            response.setStatus(200);
            response.setMessage("Cart is empty ...");
            return response;
        }


        Cart draftCart = optionalDraftCart.get();

        List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_id(draftCart.getId());
        Optional<Cart> userCartOptional = cartRepository.findByUser_id(user.getId());


        if(userCartOptional.isEmpty()){
            draftCart.setUser(user);


            Cart savedCart = cartRepository.save(draftCart);
            for (CartDetail cartDetail : cartDetails){
                CartDetail newCartDetail = new CartDetail();
                newCartDetail.setPrice(cartDetail.getPrice());
                newCartDetail.setTotal_price(cartDetail.getPrice()*cartDetail.getQuantity());
                newCartDetail.setQuantity(cartDetail.getQuantity());
                newCartDetail.setCart(savedCart);
                newCartDetail.setProduct(cartDetail.getProduct());
                cartDetailRepository.save(newCartDetail);

            }
            cartDetailRepository.deleteAll(cartDetails);
            response.setStatus(200);
            response.setMessage("Cart synchronized ...");
        }
        else {
            Cart userCart = userCartOptional.get();
            List<CartDetail> userCartDetails = userCart.getCartDetails();
            List<CartDetail> draftCartDetails = draftCart.getCartDetails();

            for (CartDetail draftCartDetail : draftCartDetails) {
                boolean found = false;
                for (CartDetail userCartDetail : userCartDetails) {
                    if (userCartDetail.getProduct().getId() == draftCartDetail.getProduct().getId()) {

                        int quantity = userCartDetail.getQuantity() +draftCartDetail.getQuantity();
                        long totalPrice = userCart.getTotal_price() +  draftCartDetail.getPrice() * draftCartDetail.getQuantity();
                        userCartDetail.setQuantity(quantity);
                        userCartDetail.setTotal_price(totalPrice);
                        cartDetailRepository.save(userCartDetail);

                        userCart.setTotal_price(cartDetailRepository.sumTotalPriceByCartId(user.getId())
                                +quantity*userCartDetail.getPrice());
                        userCart.setVersion_no(userCart.getVersion_no()+draftCart.getVersion_no());
                        cartRepository.save(userCart);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    CartDetail newCartDetail = new CartDetail();
                    newCartDetail.setPrice(draftCartDetail.getPrice());
                    newCartDetail.setTotal_price(draftCartDetail.getPrice() * draftCartDetail.getQuantity());
                    newCartDetail.setQuantity(draftCartDetail.getQuantity());
                    newCartDetail.setCart(userCart);
                    newCartDetail.setProduct(draftCartDetail.getProduct());
                    cartDetailRepository.save(newCartDetail);

                    userCart.setTotal_price(userCart.getTotal_price()+ newCartDetail.getTotal_price());
                    userCart.setVersion_no(userCart.getVersion_no()+1);

                }
            }
            try {
                cartDetailRepository.deleteAll(cartDetails);
                cartRepository.delete(draftCart);
                response.setStatus(200);
                response.setMessage("Cart synchronized ...");
            }
            catch (RuntimeException exception){
                response.setStatus(401);
                response.setMessage("Something went wrong ...");
            }
        }

        return response;
    }

    public CartQuantityResponseDto getCartQuantity(CartQuantityDto cartQuantityDto) {

        Cart cart = null;
        var quantity = 0;
        if (cartQuantityDto.getToken() != null) {
            cart = cartRepository.findByToken(cartQuantityDto.getToken()).orElse(null);
            if (cart == null) {
                return new CartQuantityResponseDto(0, 0);
            }

            List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_id(cart.getId());
            for (CartDetail cartDetail : cartDetails) {
                quantity += cartDetail.getQuantity();
            }

        } else if (cartQuantityDto.getToken() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            //get current logged in user
            MstUser user = (MstUser) authentication.getPrincipal();
            cart = cartRepository.findByUser_id(user.getId()).get();
            List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_id(cart.getId());

            for (CartDetail cartDetail : cartDetails) {
                quantity += cartDetail.getQuantity();
            }
        }

        return new CartQuantityResponseDto(cart.getVersion_no(), quantity);

    }

    @Transactional
    public ResponseDto addProductToCart(AddProductToCartDto addProductToCartDto) {
        ResponseDto response = new ResponseDto();
        MstUser user = null;

        // Get current logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            user = (MstUser) authentication.getPrincipal();
        } catch (RuntimeException e) {

        }
        Optional<Product> optionalProduct = productRepository.findBySku(addProductToCartDto.getSku());

        if(optionalProduct.isEmpty()){
            response.setMessage("Product not found...");
            response.setStatus(401);
            return response;
        }
        Product product = optionalProduct.get();

        if (user != null) {
            // Find product

            Optional<Cart> cartOptional = cartRepository.findByUser_id(user.getId());

            if (cartOptional.isEmpty()) {

                Cart cart = new Cart(1, 0, user); // Set initial total price to 0
                cartRepository.save(cart);

                CartDetail cartDetail = new CartDetail();
                cartDetail.setProduct(product);
                cartDetail.setCart(cart);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(addProductToCartDto.getQuantity());
                cartDetail.setTotal_price(product.getPrice() * addProductToCartDto.getQuantity());

                try {
                    cartDetailRepository.save(cartDetail);

                    // Update cart's total price
                    cart.setTotal_price(cart.getTotal_price() + cartDetail.getTotal_price());
                    cartRepository.save(cart);

                    response.setMessage("Added product to cart ...");
                    response.setStatus(200);
                } catch (RuntimeException exception) {
                    response.setMessage("Something went wrong ...");
                    response.setStatus(401);
                }

            } else {
                boolean found = false;
                Cart cart = cartOptional.get();
                List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_id(cart.getId());

                for (CartDetail cartDetail : cartDetails) {
                    if (cartDetail.getProduct().getSku().equals(product.getSku())) {
                        cartDetail.setQuantity(cartDetail.getQuantity() + addProductToCartDto.getQuantity());
                        cartDetail.setTotal_price(cartDetail.getTotal_price() + (cartDetail.getPrice() * addProductToCartDto.getQuantity()));
                        cart.setTotal_price(cart.getTotal_price() + (cartDetail.getPrice() * addProductToCartDto.getQuantity()));
                        cart.setVersion_no(cart.getVersion_no() + 1);
                        found = true;
                        try {
                            cartDetailRepository.save(cartDetail);
                            cartRepository.save(cart);

                            response.setMessage("Updated product to cart ...");
                            response.setStatus(200);
                        } catch (RuntimeException exception) {
                            response.setMessage("Something went wrong ...");
                            response.setStatus(401);
                        }
                        break;
                    }
                }

                if (!found) {
                    CartDetail newCartDetail = new CartDetail();
                    newCartDetail.setProduct(product);
                    newCartDetail.setCart(cart);
                    newCartDetail.setPrice(product.getPrice());
                    newCartDetail.setQuantity(addProductToCartDto.getQuantity());
                    newCartDetail.setTotal_price(product.getPrice() * addProductToCartDto.getQuantity());

                    try {
                        cartDetailRepository.save(newCartDetail);
                        cart.setTotal_price(cart.getTotal_price() + newCartDetail.getTotal_price());
                        cartRepository.save(cart);

                        response.setMessage("Added product to cart ...");
                        response.setStatus(200);
                    } catch (RuntimeException exception) {
                        response.setMessage("Something went wrong ...");
                        response.setStatus(401);
                    }
                }
            }
        } else if (addProductToCartDto.getToken() != null) {
            Cart cart = cartRepository.findByToken(addProductToCartDto.getToken()).orElse(null);
            boolean found = false;

            if (cart == null) {
                Cart cartCreated = cartRepository.save(cart);
                CartDetail cartDetail = new CartDetail();
                cartDetail.setProduct(product);
                cartDetail.setTotal_price(product.getPrice() * addProductToCartDto.getQuantity());
                cartDetail.setQuantity(addProductToCartDto.getQuantity());
                cartDetail.setCart(cartCreated);

                try {
                    cartDetailRepository.save(cartDetail);
                    response.setMessage("Added product to cart ...");
                    response.setData(cart.getToken());
                    response.setStatus(200);
                    return response;

                } catch (RuntimeException exception) {
                    response.setMessage("Something went wrong ...");
                    response.setStatus(401);
                    return response;
                }
            }

            List<CartDetail> cartDetails = cartDetailRepository.findAllByCart_id(cart.getId());
            for (CartDetail cartDetail : cartDetails) {
                if (cartDetail.getProduct().getSku().equals(addProductToCartDto.getSku())) {

                    cartDetail.setQuantity(cartDetail.getQuantity() + addProductToCartDto.getQuantity());
                    cartDetail.setTotal_price(cartDetail.getTotal_price() + (cartDetail.getPrice() * addProductToCartDto.getQuantity()));
                    cart.setTotal_price(cart.getTotal_price() + (cartDetail.getPrice() * addProductToCartDto.getQuantity()));
                    cart.setVersion_no(cart.getVersion_no() + 1);
                    found = true;
                    try {
                        cartDetailRepository.save(cartDetail);
                        cartRepository.save(cart);

                        response.setMessage("Added product to cart ...");
                        response.setData(cart.getToken());
                        response.setStatus(200);
                        return response;
                    } catch (RuntimeException exception) {
                        response.setMessage("Something went wrong ...");
                        response.setStatus(401);
                        return response;
                    }
                }
            }
            if (!found) {
                CartDetail newCartDetail = new CartDetail();
                newCartDetail.setProduct(product);
                newCartDetail.setCart(cart);
                newCartDetail.setPrice(product.getPrice());
                newCartDetail.setQuantity(addProductToCartDto.getQuantity());
                newCartDetail.setTotal_price(product.getPrice() * addProductToCartDto.getQuantity());

                try {
                    cartDetailRepository.save(newCartDetail);
                    cart.setTotal_price(cart.getTotal_price() + newCartDetail.getTotal_price());
                    cart.setVersion_no(cart.getVersion_no() + 1);
                    cartRepository.save(cart);
                    response.setMessage("Added product to cart ...");
                    response.setData(cart.getToken());
                    response.setStatus(200);
                    return response;
                } catch (RuntimeException exception) {
                    response.setMessage("Something went wrong ...");
                    response.setStatus(401);
                    return response;
                }
            }

        } else {
            Cart cart = new Cart(1, 0, RandomString.make(20));
            try {
                Cart savedCart = cartRepository.save(cart);
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(savedCart);
                cartDetail.setProduct(product);
                cartDetail.setTotal_price(product.getPrice() * addProductToCartDto.getQuantity());
                cartDetail.setQuantity(addProductToCartDto.getQuantity());
                cartDetail.setPrice(product.getPrice());
                cartDetailRepository.save(cartDetail);
                // Update cart's total price
                cart.setTotal_price(cart.getTotal_price() + cartDetail.getTotal_price());
                cartRepository.save(cart);

                response.setMessage("Added product to cart ...");
                response.setStatus(200);
                response.setData(cart.getToken());
                return response;
            } catch (RuntimeException e) {
                response.setMessage("Something went wrong ...");
                response.setStatus(401);
                return response;
            }
        }
        return response;
    }


}
