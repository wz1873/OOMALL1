package cn.edu.xmu.oomall.freight.dao.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.retobj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.openfeign.ZTOExpressMapper;
import cn.edu.xmu.oomall.freight.mapper.openfeign.zt.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 * 中通适配器
 * 传入适配器的对象都应该是满血对象
 */
@Repository("zTOAdaptor")
public class ZTOAdaptor implements LogisticsAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(ZTOAdaptor.class);

    private ZTOExpressMapper ztoExpressMapper;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RegionDao regionDao;

    private static final String SUCCESS = "SYS000";

    @Autowired
    public ZTOAdaptor(ZTOExpressMapper ztoExpressMapper) {
        this.ztoExpressMapper = ztoExpressMapper;
    }

    /**
     * 创建运单
     * https://open.zto.com/#/interface?resourceGroup=20&apiName=zto.open.createOrder
     * @param shopLogistics
     * @param express
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public PostCreatePackageAdaptorDto createPackage(ShopLogistics shopLogistics, Express express) throws NoSuchAlgorithmException {

        Logistics logistics = shopLogistics.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }

        /*Set param*/
        CreateExpressOrderParam param = new CreateExpressOrderParam();

        /**
         * 运单基本信息
         */
        param.setOrderType(express.getOrderType());
        param.setPartnerOrderCode(express.getOrderCode());

        /**
         * 寄件人信息
         */
        SenderInfo senderInfo = new SenderInfo();
        //暂定senderId是Express对象中的属性
        senderInfo.setSenderId(express.getSendId().toString());
        senderInfo.setSenderName(express.getSendName());
        senderInfo.setSenderMobile(express.getSendMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> senderParentRegions = express.getSendRegion().getAncestors();
        //设置省
        senderInfo.setSenderProvince(senderParentRegions.get(1).getName());
        //设置市
        senderInfo.setSenderCity(senderParentRegions.get(0).getName());
        //设置区
        senderInfo.setSenderDistrict(express.getSendRegion().getName());
        senderInfo.setSenderAddress(express.getSendAddress());

        param.setSenderObject(senderInfo);

        /**
         * 收件人信息
         */
        ReceiveInfo receiveInfo = new ReceiveInfo();
        receiveInfo.setReceiverName(express.getReceivName());
        receiveInfo.setReceiverMobile(express.getReceivMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> receivParentRegion = express.getReceivRegion().getAncestors();
        //设置省
        receiveInfo.setReceiverProvince(receivParentRegion.get(1).getName());
        //设置市
        receiveInfo.setReceiverCity(receivParentRegion.get(0).getName());
        //设置区
        receiveInfo.setReceiverDistrict(express.getReceivRegion().getName());
        receiveInfo.setReceiverAddress(express.getReceivAddress());

        param.setReceiverObject(receiveInfo);

<<<<<<< HEAD
        ReturnObj retObj = this.ztoExpressMapper.createExpressOrder(logistics.getAppAccount(), param);
=======
        ReturnObj retObj = this.ztoExpressMapper.createExpressOrder(logistics.getAppId(), param);
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
            return null;
        } else {
            CreateExpressOrderRetObj createExpressOrderRetObj = (CreateExpressOrderRetObj) retObj.getResult();

            PostCreatePackageAdaptorDto dto = new PostCreatePackageAdaptorDto();
            /*dto中的运单id属性暂定从数据库中获取*/
            dto.setBillCode(createExpressOrderRetObj.getBillCode());
            return dto;
        }
    }

    /**
     * 查询运单
     * https://open.zto.com/#/interface?schemeCode=&resourceGroup=20&apiName=zto.open.getOrderInfo
     * @param shopLogistics
     * @param billCode
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public Express getPackage(ShopLogistics shopLogistics, String billCode) throws NoSuchAlgorithmException {

        Logistics logistics = shopLogistics.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("get package:logistics is null");
        }

        /*Set Param*/
        GetExpressOrderParam param = new GetExpressOrderParam();
        param.setBillCode(billCode);

<<<<<<< HEAD
        ReturnObj retObj = this.ztoExpressMapper.getExpressOrder(logistics.getAppAccount(), param);
=======
        ReturnObj retObj = this.ztoExpressMapper.getExpressOrder(logistics.getAppId(), param);
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
            return null;
        } else {
            GetExpressOrderRetObj getExpressOrderRetObj = (GetExpressOrderRetObj) retObj.getResult();

            Long sendRegionId=regionDao.retrieveRegionIdByName(getExpressOrderRetObj.getSendCounty());
            Long receivRegionId=regionDao.retrieveRegionIdByName(getExpressOrderRetObj.getReceivCounty());
            Region sendRegion=regionDao.findRegionById(sendRegionId);
            Region receivRegion=regionDao.findRegionById(receivRegionId);

            Express express = new Express();

            express.setSendRegionId(sendRegionId);
            express.setReceivRegionId(receivRegionId);
            express.setSendRegion(sendRegion);
            express.setReceivRegion(receivRegion);

            express.setOrderType(getExpressOrderRetObj.getOrderType());
            express.setReceivName(getExpressOrderRetObj.getReceivName());
            express.setStatus((byte) getExpressOrderRetObj.getOrderStatus());
            express.setReceivAddress(getExpressOrderRetObj.getReceivAddress());
            express.setSendMobile(getExpressOrderRetObj.getSendMobile());
            express.setReceivMobile(getExpressOrderRetObj.getReceivMobile());
            express.setSendName(getExpressOrderRetObj.getSendName());
            express.setBillCode(getExpressOrderRetObj.getBillCode());
            express.setSendAddress(getExpressOrderRetObj.getSendAddress());
            express.setOrderCode(getExpressOrderRetObj.getOrderCode());
            /*暂定取消原因和取消类型是Express中的属性*/
            express.setCancelReason(getExpressOrderRetObj.getCancelReason());
            express.setSecStatus(getExpressOrderRetObj.getSecStatus());

            return express;
        }
    }

    /**
     * 取消订单
     * https://open.zto.com/#/interface?schemeCode=&resourceGroup=20&apiName=zto.open.cancelPreOrder
     * @param shopLogistics
     * @param express
     * @throws NoSuchAlgorithmException
     */
    @Override
    public void cancelPackage(ShopLogistics shopLogistics, Express express) throws NoSuchAlgorithmException {

        Logistics logistics = shopLogistics.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }

        /*Set Param*/
        CancelExpressOrderParam param = new CancelExpressOrderParam();
        /*暂定secStatus是Express对象中的属性*/
        param.setCancelType(express.getSecStatus());
        param.setBillCode(express.getBillCode());

<<<<<<< HEAD
        ReturnObj retObj = this.ztoExpressMapper.cancelExpressOrder(logistics.getAppAccount(), param);
=======
        ReturnObj retObj = this.ztoExpressMapper.cancelExpressOrder(logistics.getAppId(), param);
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
        }
    }

    /**
     * 商户发出揽收
     * @param shopLogistics
     * @param billCode
     */
    @Override
    public void sendPackage(ShopLogistics shopLogistics, String billCode) throws NoSuchAlgorithmException {

        Logistics logistics = shopLogistics.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }

        Express express=getPackage(shopLogistics, billCode);

        /*Set param*/
        CreateExpressOrderParam param = new CreateExpressOrderParam();

        /**
         * 运单基本信息
         */
        SummaryInfo summaryInfo=new SummaryInfo();
        summaryInfo.setStartTime(new Date());

        param.setOrderType(express.getOrderType());
        param.setPartnerOrderCode(express.getOrderCode());
        param.setSummaryInfo(summaryInfo);

        /**
         * 寄件人信息
         */
        SenderInfo senderInfo = new SenderInfo();
        //暂定senderId是Express对象中的属性
        senderInfo.setSenderId(express.getSendId().toString());
        senderInfo.setSenderName(express.getSendName());
        senderInfo.setSenderMobile(express.getSendMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> senderParentRegions = express.getSendRegion().getAncestors();
        //设置省
        senderInfo.setSenderProvince(senderParentRegions.get(1).getName());
        //设置市
        senderInfo.setSenderCity(senderParentRegions.get(0).getName());
        //设置区
        senderInfo.setSenderDistrict(express.getSendRegion().getName());
        senderInfo.setSenderAddress(express.getSendAddress());

        param.setSenderObject(senderInfo);

        /**
         * 收件人信息
         */
        ReceiveInfo receiveInfo = new ReceiveInfo();
        receiveInfo.setReceiverName(express.getReceivName());
        receiveInfo.setReceiverMobile(express.getReceivMobile());

        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<Region> receivParentRegion = express.getReceivRegion().getAncestors();
        //设置省
        receiveInfo.setReceiverProvince(receivParentRegion.get(1).getName());
        //设置市
        receiveInfo.setReceiverCity(receivParentRegion.get(0).getName());
        //设置区
        receiveInfo.setReceiverDistrict(express.getReceivRegion().getName());
        receiveInfo.setReceiverAddress(express.getReceivAddress());

        param.setReceiverObject(receiveInfo);

<<<<<<< HEAD
        ReturnObj retObj = this.ztoExpressMapper.createExpressOrder(logistics.getAppAccount(), param);
=======
        ReturnObj retObj = this.ztoExpressMapper.createExpressOrder(logistics.getAppId(), param);
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
        if (!retObj.getStatusCode().equals(SUCCESS)) {
            logger.error("createPackage：param = {}, code = {}, message = {}", param, retObj.getStatusCode(), retObj.getMessage());
            /*由于物流模块的错误码还未确定，此处暂时先不抛出异常*/
        }
    }
}