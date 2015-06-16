package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.CartValidator;
import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderStatus;
import com.xiaobudian.yamikitchen.service.CouponService;
import com.xiaobudian.yamikitchen.service.MerchantService;
import com.xiaobudian.yamikitchen.service.OrderService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.util.Date;
import java.util.List;

/**
 * Created by johnson1 on 4/27/15.
 */
@RestController
public class OrderController {
    @Inject
    private OrderService orderService;
    @Inject
    private MerchantService merchantService;
    @Inject
    private CartValidator cartValidator;
    @Inject
    private CouponService couponService;

    @RequestMapping(value = "/carts/merchants/{rid}/{productId}/today/{isToday}", method = RequestMethod.POST)
    public Result addProductForCart(@PathVariable Long rid, @PathVariable Long productId, @PathVariable boolean isToday, @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantBy(rid);
        if (merchant == null || merchant.getIsRest()) throw new RuntimeException("order.merchant.rest");
        Product product = merchantService.getProductBy(productId);
        Cart cart = orderService.getCart(user.getId());
        if (product == null || !product.getAvailable() || product.isSoldOut(isToday, cart))
            throw new RuntimeException("order.product.sold.out");
        if (!product.getMerchantId().equals(rid)) throw new RuntimeException("order.product.unauthorized");
        return Result.successResult(orderService.addProductInCart(user.getId(), rid, productId, isToday));
    }

    @RequestMapping(value = "/carts/merchants/{rid}/{productId}", method = RequestMethod.DELETE)
    public Result removeProductForCart(@PathVariable Long rid, @PathVariable Long productId, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.removeProductInCart(user.getId(), rid, productId));
    }

    @RequestMapping(value = "/carts", method = RequestMethod.GET)
    public Result getProductForCart(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getCart(user.getId()));
    }

    @RequestMapping(value = "/carts", method = RequestMethod.DELETE)
    public Result removeCart(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.removeCart(user.getId()));
    }

    @RequestMapping(value = "/carts/deliverMethod/{deliverMethod}", method = RequestMethod.POST)
    public Result changeDeliverMethod(@PathVariable Integer deliverMethod, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.changeDeliverMethodOfCart(user.getId(), deliverMethod));
    }

    @RequestMapping(value = "/carts/paymentMethod/{paymentMethod}", method = RequestMethod.POST)
    public Result changePaymentMethod(@PathVariable Integer paymentMethod, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.changePaymentMethodOfCart(user.getId(), paymentMethod));
    }

    @RequestMapping(value = "/settlement", method = RequestMethod.GET)
    public Result settlementOrder(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getSettlement(user.getId()));
    }

    @RequestMapping(value = "/settlement/coupons/{couponId}", method = RequestMethod.PUT)
    public Result changeCoupon(@PathVariable Long couponId, @AuthenticationPrincipal User user) {
        Cart cart = orderService.getCart(user.getId());
        if (cart != null && cart.getPaymentMethod() == 1)
            throw new RuntimeException("order.change.coupon.wrongPaymentMethod");
        Coupon coupon = couponService.getCoupon(couponId);
        if (coupon != null && !coupon.getUid().equals(user.getId()))
            throw new RuntimeException("order.change.coupon.user.unauthorized");
        return Result.successResult(orderService.changeCouponForSettlement(user.getId(), couponId));
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public Result createOrder(@RequestBody @Valid Order order, @AuthenticationPrincipal User user) {
        if (!order.isToday() && !order.isTomorrow()) throw new RuntimeException("order.expect.date.error");
        Cart cart = orderService.getCart(user.getId());
        List<String> errors = cartValidator.validate(cart);
        if (!CollectionUtils.isEmpty(errors)) return Result.failResult(errors);
        Merchant merchant = merchantService.getMerchantBy(cart.getMerchantId());
        if (merchant.getIsRest()) throw new RuntimeException("order.merchant.rest");
        order.setUid(user.getId());
        return Result.successResult(orderService.createOrder(order));
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public Result getOrders(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getOrders(user.getId()));
    }

    @RequestMapping(value = "/orders/status/{status}", method = RequestMethod.GET)
    public Result getOrdersByStatus(@PathVariable Integer status,
                                    @MatrixVariable(value = "d", required = false) Long paymentTimestamp,
                                    @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if (merchant == null) throw new RuntimeException("user.merchant.not.create");
        final Date d = paymentTimestamp == null ? null : new Date(paymentTimestamp);
        return Result.successResult(orderService.getOrders(merchant.getId(), status, d));
    }

    @RequestMapping(value = "/orders/status/completed", method = RequestMethod.GET)
    public Result getCompletedOrders(@RequestParam("page") Integer page,
                                     @RequestParam("size") Integer size,
                                     @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if (merchant == null) throw new RuntimeException("user.merchant.not.create");
        return Result.successResult(orderService.getOrders(merchant.getId(), OrderStatus.COMPLETED.getIndex(), page, size));
    }

    private Order getOrder(Long orderId, User user, boolean isMerchant) {
        Order order = orderService.getOrder(orderId);
        if (order == null) throw new RuntimeException("order.does.not.exist");
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if (!order.isAuthorizedBy(user, merchant, isMerchant)) throw new RuntimeException("order.unauthorized");
        if (order.getStatus() > 3 && order.getDeliverMethod() == 0 && order.getDeliverGroup() == null)
            throw new RuntimeException("order.deliverGroup.not.empty");
        return order;
    }

    @RequestMapping(value = "/orders/{orderId}/confirm", method = RequestMethod.POST)
    public Result confirmOrder(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        Order order = getOrder(orderId, user, true);
        return Result.successResult(orderService.confirmOrder(order));
    }

    @RequestMapping(value = "/orders/{orderId}/deliverGroup/{deliverGroup}", method = RequestMethod.POST)
    public Result chooseDeliverGroup(@PathVariable Long orderId, @PathVariable Integer deliverGroup, @AuthenticationPrincipal User user) {
        Order order = orderService.getOrder(orderId);
        if (!OrderStatus.WAIT_DELIVER.equals(OrderStatus.from(order.getStatus())))
            throw new RuntimeException("order.change.deliver.group.unauthorized");
        return Result.successResult(orderService.chooseDeliverGroup(order, deliverGroup));
    }

    @RequestMapping(value = "/orders/{orderNo}", method = RequestMethod.GET)
    public Result getOrders(@PathVariable String orderNo) {
        return Result.successResult(orderService.getOrderBy(orderNo));
    }

    @RequestMapping(value = "/orders/lastMonth", method = RequestMethod.GET)
    public Result getOrdersForLastMonth(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getOrdersForLastMonth(user.getId()));
    }

    @RequestMapping(value = "/orders/inProgress", method = RequestMethod.GET)
    public Result getInProgressOrders(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getInProgressOrders(user.getId()));
    }

    @RequestMapping(value = "/orders/waitForComment", method = RequestMethod.GET)
    public Result getWaitForCommentOrders(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getWaitForCommentOrders(user.getId()));
    }

    @RequestMapping(value = "/orders/{orderId}/deliver", method = RequestMethod.POST)
    public Result deliverOrder(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        Order order = getOrder(orderId, user, true);
        return Result.successResult(orderService.deliverOrder(order));
    }

    @RequestMapping(value = "/orders/{orderId}/finish", method = RequestMethod.POST)
    public Result finishOrder(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        Order order = getOrder(orderId, user, false);
        return Result.successResult(orderService.finishOrder(order));
    }

    @RequestMapping(value = "/orders/{orderId}/cancel", method = RequestMethod.POST)
    public Result cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        Order order = getOrder(orderId, user, user.isMerchant());
        if (!order.canBeCanceledBy(user)) throw new RuntimeException("order.unauthorized");
        return Result.successResult(orderService.cancelOrder(order, user.getId()));
    }
}
