//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.controller.vo;


import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 */
@Data
@NoArgsConstructor
public class CouponVo {
    private Long id;


    /**
     * 优惠券序号
     */
    private String coupon_sn;
    /**
     * 顾客id
     */
    private Long customerid;
    /**
     * 参与活动id
     */
    private String coupon_activity_id;
    /**
     * 优惠券名称
     */
    @NotBlank(message = "优惠券名称不能为空")
    private String name;
    /**
     * 开始时间
     */
    private Date begin_time;
    /**
     * 结束时间
     */
    private Date end time;
    /**
     * 0末用1已使用
     */
    private int used;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoupon_sn() {
        return coupon_sn;
    }

    public void setCoupon_sn(String coupon_sn) {
        this.coupon_sn = coupon_sn;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public String getCoupon_activity_id() {
        return coupon_activity_id;
    }

    public void setCoupon_activity_id(String coupon_activity_id) {
        this.coupon_activity_id = coupon_activity_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}
