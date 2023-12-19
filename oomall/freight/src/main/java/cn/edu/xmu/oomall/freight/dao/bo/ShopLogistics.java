package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.retobj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.mapper.po.ShopLogisticsPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
=======
import lombok.*;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(ShopLogisticsPo.class)
<<<<<<< HEAD
public class ShopLogistics extends OOMallObject implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ShopLogistics.class);

=======
@Data
public class ShopLogistics extends OOMallObject implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ShopLogistics.class);
    //假设account为属性
    private String account;
    //假设shopId为属性
    private Long shopId;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
    public Logistics getLogistics() {
        //TODO：dgn3-009（包裹api）用到了get，但没有添加所有属性和对应的DAO，所以直接new一个返回了，后续应从数据库获得
        return new Logistics("顺丰快递","123","123","123","123","zTOAdaptor",1L);
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }

    private Logistics logistics;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ShopLogisticsDao shopLogisticsDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ExpressDao expressDao;

    @ToString.Exclude
    @JsonIgnore
    private LogisticsAdaptor logisticsAdaptor;

    /**
     * 2023-dgn3-009
     *
     * @author huangzian
     */
    public Express createExpress(Long shopId, Express express, UserDto user) {
        try {
            express.setShopId(shopId);
            express.setStatus(Express.UNSHIPPED);
            logger.debug("createExpress: logisticsAdaptor = {}", logisticsAdaptor);
            PostCreatePackageAdaptorDto adaptorDto=this.logisticsAdaptor.createPackage(this,express);
            if (adaptorDto.getBillCode() != null) {
                express.setBillCode(adaptorDto.getBillCode());
            }
            this.expressDao.insert(express, user);
            logger.debug("createExpress: dto = {}", adaptorDto);
            return express;
        }catch (NoSuchAlgorithmException noSuchAlgorithmException)
        {
            throw new IllegalArgumentException("logisticsAdaptor 签名生成算法不合法");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
    public void setLogisticsAdaptor(LogisticsAdaptorFactory factory) {
        this.logisticsAdaptor = factory.createLogisticAdaptor(this.getLogistics());
    }

}
