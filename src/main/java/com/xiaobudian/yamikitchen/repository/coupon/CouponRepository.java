package com.xiaobudian.yamikitchen.repository.coupon;

import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by Johnson on 2015/5/3.
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    public Coupon findFirstByUid(Long uid);

    @Query("from Coupon c where c.uid = ?1 and c.expireDate>=?2 order by c.availableDate desc")
    public List<Coupon> findAvailableCoupons(Long uid, Date date);

    @Query("from Coupon c where c.uid = ?1 and c.expireDate<?2 order by c.availableDate desc")
    public List<Coupon> findExpiredCoupons(Long uid, Date date);

    @Query("from Coupon c where c.uid = ?1 and c.usageCondition <= ?2 and c.expireDate >?3 and c.hasUsed<>true and c.locked <> true order by c.amount desc, c.availableDate asc")
    public List<Coupon> findFirstByAmountAndExpireDate(Long uid, Long price, Date date, Pageable pageable);
}
