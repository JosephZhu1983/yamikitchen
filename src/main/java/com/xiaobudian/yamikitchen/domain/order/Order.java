package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.common.Day;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
@Table(name = "[order]")
public class Order implements Serializable {
    private static final long serialVersionUID = -7595215227337455292L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Integer totalQuantity;
    private Double price;
    private Date createDate = new Date();
    private Date deliverDate;
    private Date outDate;
    private Date expectDate;
    @NotNull(message = "order.paymentMethod.not.empty")
    private Integer paymentMethod;
    private Date acceptDate;
    private Long uid;
    private String nickName;
    private String headPic;
    private String phone;
    private Long merchantId;
    private String merchantNo;
    private String merchantName;
    private String merchantHeadPic;
    private String merchantPhone;
    private String merchantAddress;
    private String remark;
    private Double deliverPrice;
    @NotNull(message = "order.deliverMethod.not.empty")
    private Integer deliverMethod;
    private Integer paymentTimeLimit = 15;
    private String address;
    @NotNull(message = "order.addressId.not.empty")
    private Long addressId;
    private String name;
    private Integer status = 1;
    private boolean cancelable = true;
    private boolean payable = true;
    private boolean hasPaid = false;
    private boolean complainable = false;
    private boolean commentable = false;
    private boolean refundable = true;
    private Double distance;
    private String checkFlag = "I";
    private Long couponId;
    private Double couponAmount = 0.00d;
    private boolean firstDeal;
    private Double longitude;
    private Double latitude;
    private Integer deliverGroup;
    private boolean directCancelable = true;
    private Double paymentAmount = 0.00d;
    private Date paymentDate;
    private Integer deliverGroupOrderStatus;
    private Integer deliveryManId;
    private String deliveryManName;
    private String deliveryManMobile;
    private Date updateTime;
    private boolean hasRefund;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantHeadPic() {
        return merchantHeadPic;
    }

    public void setMerchantHeadPic(String merchantHeadPic) {
        this.merchantHeadPic = merchantHeadPic;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getDeliverPrice() {
        return deliverMethod == 1 ? 0.00 : deliverPrice;
    }

    public void setDeliverPrice(Double deliverPrice) {
        this.deliverPrice = deliverPrice;
    }

    public Integer getPaymentTimeLimit() {
        return paymentTimeLimit;
    }

    public void setPaymentTimeLimit(Integer paymentTimeLimit) {
        this.paymentTimeLimit = paymentTimeLimit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public boolean isHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    public boolean isComplainable() {
        return complainable;
    }

    public void setComplainable(boolean complainable) {
        this.complainable = complainable;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public boolean isFirstDeal() {
        return firstDeal;
    }

    public void setFirstDeal(boolean firstDeal) {
        this.firstDeal = firstDeal;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getDeliverMethod() {
        return deliverMethod;
    }

    public void setDeliverMethod(Integer deliverMethod) {
        if (deliverMethod == 1) deliverPrice = 0.00d;
        this.deliverMethod = deliverMethod;
    }

    public Date getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public Integer getDeliverGroup() {
        return deliverGroup;
    }

    public void setDeliverGroup(Integer deliverGroup) {
        this.deliverGroup = deliverGroup;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public Integer getDeliverGroupOrderStatus() {
        return deliverGroupOrderStatus;
    }

    public void setDeliverGroupOrderStatus(Integer deliverGroupOrderStatus) {
        this.deliverGroupOrderStatus = deliverGroupOrderStatus;
    }

    public Integer getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(Integer deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public String getDeliveryManName() {
        return deliveryManName;
    }

    public void setDeliveryManName(String deliveryManName) {
        this.deliveryManName = deliveryManName;
    }

    public String getDeliveryManMobile() {
        return deliveryManMobile;
    }

    public void setDeliveryManMobile(String deliveryManMobile) {
        this.deliveryManMobile = deliveryManMobile;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void confirm() {
        setStatus(deliverMethod == 0 ? 3 : 6);
        this.setDirectCancelable(false);
        this.setAcceptDate(new Date());
    }

    public void finish() {
        setStatus(5);
        setComplainable(true);
        setCommentable(true);
    }

    public void cancel() {
        setStatus(7);
        setDirectCancelable(false);
        setCancelable(false);
        setRefundable(false);
        setHasRefund(true);
    }

    public void pay() {
        this.setPayable(false);
        this.setHasPaid(true);
        this.setPaymentDate(DateTime.now().toDate());
        this.setStatus(2);
    }

    public void deliver() {
        this.setStatus(4);
        this.setOutDate(new Date());
    }


    public boolean isDirectCancelable() {
        return directCancelable;
    }

    public void setDirectCancelable(boolean directCancelable) {
        this.directCancelable = directCancelable;
    }

    @Transient
    public boolean isToday() {
        return expectDate != null && Day.TODAY.isSameDay(expectDate);
    }

    @Transient
    public boolean isTomorrow() {
        return expectDate != null && Day.TOMORROW.isSameDay(expectDate);
    }

    private double netPrice() {
        return getPrice() - getDeliverPrice();
    }

    public double shareOfMerchant(double sharingScale) {
        return netPrice() - shareOfPlatform(sharingScale);
    }

    public double shareOfPlatform(double sharingScale) {
        return netPrice() * sharingScale;
    }

    public boolean deliverByDaDa() {
        return deliverGroup != null && deliverGroup == 2;
    }

    public Double getPaymentAmount() {
        return (getPrice() == null ? 0 : getPrice()) - (getCouponAmount() == null ? 0 : getCouponAmount());
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    @Transient
    public boolean isOwnedBy(Merchant merchant) {
        return merchantId != null && merchantId.equals(merchant.getId());
    }

    @Transient
    public boolean isCreateBy(Long uid) {
        return getUid() != null && getUid().equals(uid);
    }

    @Transient
    public boolean isAuthorizedBy(User user, Merchant merchant, boolean isMerchant) {
        if (merchant == null) return isCreateBy(user.getId());
        if (isMerchant) return isOwnedBy(merchant);
        return isCreateBy(user.getId());
    }

    public boolean canBeCanceledBy(User user) {
        if (user.isMerchant()) return isCancelable();
        return isDirectCancelable();
    }
    
    public boolean canBeCanceledByStatus() {
    	return status == 1 || status == 2; 
    }

    public boolean payIncludeCoupon() {
        return couponId != null && couponId > 0 && paymentMethod == 0;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Transient
    public boolean isPayWithCoupon() {
        return paymentMethod != null && paymentMethod == 0 && couponId != null && couponId > 0;
    }

    @Transient
    public boolean isPayOnDeliver() {
        return paymentMethod != null && paymentMethod == 1;
    }

    @Transient
    public boolean isPayOnline() {
        return paymentMethod != null && paymentMethod == 0;
    }

	public boolean isHasRefund() {
		return hasRefund;
	}

	public void setHasRefund(boolean hasRefund) {
		this.hasRefund = hasRefund;
	}
	
	public boolean isUnacceptByDADA() {
		return deliverGroupOrderStatus == null || deliverGroupOrderStatus == 1;
	}

}
