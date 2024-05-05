package com.chapterly.serviceImpl;

import com.chapterly.dto.*;
import com.chapterly.entity.Book;
import com.chapterly.entity.OrderedItem;
import com.chapterly.entity.User;
import com.chapterly.repository.BookRepo;
import com.chapterly.repository.OrderRepo;
import com.chapterly.service.BookService;
import com.chapterly.service.OrderService;
import com.chapterly.service.OrderedItemService;
import com.chapterly.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${payment.id}")
    private String id;
    @Value("${s3.endpointUrl}")
    private String parentUrl;
    @Value("${payment.secret}")
    private String secret;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger("OrderService");
    private final OrderRepo orderRepo;
    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private OrderedItemService orderedItemService;
    OrderServiceImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }


    @Transactional
    @Override
    public Order createOrder(ProductDto product) {
        String amountString = product.getAmount();
        if (amountString != null) {
            try {
                Double amount = Double.parseDouble(amountString);
                RazorpayClient client = new RazorpayClient(id, secret);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("amount", amount * 100);
                jsonObject.put("currency", "INR");
                jsonObject.put("receipt", "txn_232485");

                AtomicInteger counter = new AtomicInteger(1);
                Map<String, String> notes = new HashMap<>();
                product.getNotes().forEach((note) -> {
                    Note item = new Note();
                    item.setTitle(note.get("title").toString());
                    item.setPrice(note.get("price").toString());
                    item.setCount(note.get("count").toString());
                    notes.put("item" + counter.getAndIncrement(), item.toString());
                });
                jsonObject.put("notes", notes);
                Order order = client.orders.create(jsonObject);
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userService.userByEmail(username);
                if (username == null){
                    throw new UsernameNotFoundException("USER NOT FOUND");
                }
                com.chapterly.entity.Order userOrder = new com.chapterly.entity.Order();
                userOrder.setStatus("Ordered");
                userOrder.setOrderDate(LocalDateTime.now());
                userOrder.setTotalAmount(amount * 100);
                userOrder.setPaymentMethod("NO PAYMENT YET");
                userOrder.setRazorpayOrderId(order.get("id"));
                user.setShippingAddress(product.getShippingAddress());
                userOrder.setUser(user);
                com.chapterly.entity.Order savedOrder = null;
                List<OrderedItem> orderedItems = new ArrayList<>();
                com.chapterly.entity.Order finalOrder = null;
                try {
                    savedOrder = orderRepo.save(userOrder);
                    for(Map<String, String> note : product.getNotes()){
                        OrderedItem orderedItem = new OrderedItem();
                        Book bookByTitle = bookService.getBookByName(note.get("title"));
                        orderedItem.setBook(bookByTitle);
                        orderedItem.setItemCount(Integer.parseInt(note.get("count")));
                        orderedItem.setOrder(savedOrder);
                        OrderedItem savedOrderedItem = orderedItemService.saveOrderedItem(orderedItem);
                        orderedItems.add(savedOrderedItem);
                    }
                    savedOrder.setOrderItems(orderedItems);
                    finalOrder = orderRepo.save(savedOrder);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                    logger.error("ENTITY IS NULL", e.getMessage());
                    throw new IllegalArgumentException("ENTITY IS NULL");
                }catch (Exception e) {
                    e.printStackTrace();
                    logger.error("EXCEPTION", e.getMessage());
                    throw new Exception("SOMETHING WENT WRONG");
                }
//                logger.info("order is\n" + order.toString());
                return order;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                logger.error("ERROR PARSING AMOUNT: " + e.getMessage());
            } catch (RazorpayException e) {
                e.printStackTrace();
                throw new RuntimeException("RAZORPAY EXCEPTION",e);
            }catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            logger.error("MISSING PRODUCT AMOUNT");
        }
        return null;
    }

    @Override
    public com.chapterly.entity.Order getOrderByRazorPayOrderId(String orderId) {
        try {
            return orderRepo.findByRazorpayOrderId(orderId);
        }catch (Exception e){
            return null;
        }
    }


    @Override
    public List<OrdersResponseDto> getAllOrdersByUser(Principal principal) throws Exception {
        var user = (User) ((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        List<OrderResponse> orders = null;
        if(user == null){
            throw new IllegalArgumentException("USER NOT FOUND");
        }
        try {
            List<OrderDtoInterf> ordersRes = orderRepo.findAllOrdersByUser(user.getUserId());

            OrdersResponseDto ordersResponse = new OrdersResponseDto();

            orders = ordersRes.stream().map(item -> {
                OrderResponse order = new OrderResponse();
                order.setOrder_id(item.getOrder_id());
                order.setOrder_date(item.getOrder_date());
                order.setRazorpay_order_id(item.getRazorpay_order_id());
                order.setQuantity(item.getQuantity());
                order.setStatus(item.getStatus());
                order.setTotal_amount(item.getTotal_amount());
                order.setBook_title(item.getBook_title());
                order.setBook_price(item.getBook_price());
                order.setAuthor_name(item.getAuthor_name());
                order.setPayment_date(item.getPayment_date());
                order.setPayment_status(item.getPayment_status());
                order.setBook_image(parentUrl+"/"+item.getBook_image());
                order.setFormat(item.getFormat());
                return order;
            }).sorted(Comparator.comparing(OrderResponse :: getOrder_id)).collect(Collectors.toList());

            List<OrdersResponseDto> userOrders = new ArrayList<OrdersResponseDto>();
            List<OrderResponse> distinctOrders = orders.stream().filter(this::checkUniqueOrder).distinct().toList();
            for (OrderResponse order : distinctOrders) {
                List<OrderResponse> orderResponses = new ArrayList<OrderResponse>();
                for(OrderResponse orderRes : orders){
                    if(orderRes.getOrder_id().equals(order.getOrder_id())){
                        orderResponses.add(orderRes);
                    }
                }
                OrdersResponseDto allOrders = new OrdersResponseDto();
                allOrders.setOrderId(order.getOrder_id());
                allOrders.setOrderPaymentTimeStamp(order.getPayment_date());
                allOrders.setTotalOrderAmount(order.getTotal_amount());
                allOrders.setOrderedItems(orderResponses);
                userOrders.add(allOrders);
            }
            return userOrders;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("EXCEPTION", e.getMessage());
            throw new Exception("SOMETHING WENT WRONG");
        }
    }


    private boolean checkUniqueOrder(OrderResponse order) {
        Set<String> seenOrderIds = new HashSet<>();
        if (seenOrderIds.contains(order.getOrder_id())) {
            return false; // Duplicate order ID found
        } else {
            seenOrderIds.add(order.getOrder_id());
            return true;
        }
    }


}
