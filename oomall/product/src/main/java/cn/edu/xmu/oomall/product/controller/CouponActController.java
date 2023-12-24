//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.controller.dto.*;
import cn.edu.xmu.oomall.product.controller.vo.OrderInfoVo;
import cn.edu.xmu.oomall.product.dao.bo.CouponAct;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import cn.edu.xmu.oomall.product.service.CouponActService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CouponActController {
    private static final Logger logger = LoggerFactory.getLogger(CouponActController.class);

    private CouponActService couponActService;

    @Autowired
    public CouponActController(CouponActService couponActService) {
        this.couponActService = couponActService;
    }

    /**
     * 计算优惠
     * @author Liang nan
     */
    /**
     * 计算商品优惠价格
     * 2023-12-09
     * @author yuhao shi
     * dgn2-010
     */
    @PostMapping("/couponactivities/{id}/caculate")
    public ReturnObject cacuCoupon(@PathVariable("id") Long id,
                                   @Validated @RequestBody List<OrderInfoVo> orderInfoVoList) {

        List<Item> itemList =this.couponActService.cacuCoupon(id,orderInfoVoList);
        return new ReturnObject(itemList.stream().map(item -> CloneFactory.copy(new DiscountDto(), item)).collect(Collectors.toList()));
    }

    /**
     * 查看上线的优惠活动列表
     * 注：在beginTime和endTime参数添加注解@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)，规定前端传入的时间格式
     *
     * @param shopId
     * @param productId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     *
     */
    @GetMapping("/couponactivities")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findAllOnlineCouponActivities(@RequestParam(required = false) Long shopId,
                                                               @RequestParam(required = false) Long productId,
                                                               @RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginTime,
                                                               @RequestParam(required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                                               @RequestParam(required = false, defaultValue = "1") Integer page,
                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        List<CouponAct> acts = this.couponActService.retrieveValidByShopIdAndProductIdAndTime(shopId, productId, beginTime, endTime, page, pageSize);
        List<SimpleCouponActDto> dtos = acts.stream().map(o -> CloneFactory.copy(new SimpleCouponActDto(), o)).collect(Collectors.toList());
        return new ReturnObject(new PageDto<>(dtos, page, pageSize));
    }

    /**
     * 查看优惠活动
     *
     * @param id
     *
     */
    @GetMapping("/couponactivities/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findCouponActById(@PathVariable Long id){
        CouponAct act = this.couponActService.findById(PLATFORM, id);
        return new ReturnObject(CloneFactory.copy(new CouponActDto(),act));
    }

    /**
     * 查看优惠活动中的商品列表
     *
     * @param id
     * @param page  (not required)
     * @param pageSize  (not required)
     *
     */
    @GetMapping("/couponactivities/{id}/products")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findCouponActProductById(@PathVariable Long id,
                                                          @RequestParam(required = false, defaultValue = "1") Integer page,
                                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        List<Product> products = this.couponActService.retrieveProduct(id);
        List<SimpleProductDto> dtos = products.stream().map(product -> new SimpleProductDto(product)).collect(Collectors.toList());
        return new ReturnObject(new PageDto<>(dtos.subList((page-1) * pageSize, Math.min(dtos.size(), page * pageSize)), page, pageSize));
    }

    /**
     * 买家领取活动优惠券，上线状态才能领取
     * @param id
     * @param user
     * 695 优惠卷领取成功
     * 696 未到优惠卷领取时间
     * 697 优惠卷领罄
     * 698 优惠卷活动终止
     * 699 不可重复领优惠卷
     */
    @GetMapping("/couponactivities/{id}/coupons")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findCouponsById(@PathVariable Long id,@LoginUser UserDto user) {
//        CouponAct act = this.couponActService.findById(PLATFORM, id);
        CouponAct act  = this.couponActService.findCouponActById(id);
        if(act.getValidTerm()<1){
            return new ReturnObject(ReturnNo.COUPON_END);//698
        }else if(act.getQuantity()<1){
            return new ReturnObject(ReturnNo.COUPON_FINISH);//697
        }else if(act.getCouponTime().isAfter(LocalDateTime.now())){
            return new ReturnObject(ReturnNo.COUPON_NOTBEGIN);//696
        }


        CouponAct reAct=couponActService.addCouponactivity(act,user);
        if(reAct!=null){
            return new ReturnObject(ReturnNo.COUPON_OK); //695
        }else{
            return new ReturnObject(ReturnNo.COUPON_EXIST); //699
        }

    }
}
