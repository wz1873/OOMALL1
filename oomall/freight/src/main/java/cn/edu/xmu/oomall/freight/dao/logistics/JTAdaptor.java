//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.dao.logistics;

<<<<<<< HEAD
/**
 * 极兔适配器
 */
public class JTAdaptor {
=======
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PickingUpTime;
import cn.edu.xmu.oomall.freight.dao.retobj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.mapper.openfeign.JTExpressMapper;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.freight.mapper.openfeign.jt.*;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 丁圳杰
 * @Task 2023-dgn3-001
 * 极兔适配器
 */
@Repository("jTAdaptor")
@RequiredArgsConstructor
public class JTAdaptor implements LogisticsAdaptor {
    //feignClient
    private final JTExpressMapper jtExpressMapper;
    private final RegionMapper regionMapper;
    private final JacksonUtil jacksonUtil;
    private final ExpressDao expressDao ;//由于jtMapper不返回对应express基本信息,于是加入ExpressDao查询信息
    private static final Map<String,Byte> stringToStatusCode=new HashMap<>();
    static {
        stringToStatusCode.put("快件揽收",Express.SHIPPED);
        stringToStatusCode.put("快件签收",Express.SIGNED);
        stringToStatusCode.put("客户拒收",Express.REJECTED);
        stringToStatusCode.put("包裹异常-网点",Express.LOST);
        stringToStatusCode.put("包裹异常",Express.LOST);
        stringToStatusCode.put("包裹异常-中心",Express.LOST);
        stringToStatusCode.put("退回件-网点",Express.RETURNED);
        stringToStatusCode.put("退回件-中心",Express.RETURNED);
        stringToStatusCode.put("物流轨迹长时间不改变",Express.LOST);
    }
    private PersonInfo fillSenderInfo(Express express, PersonInfo senderInfo) {
        senderInfo.setName(express.getSendName());
        senderInfo.setMobile(express.getSendMobile());
        senderInfo.setPhone(express.getSendMobile());
        fillRegion(express, senderInfo);
        senderInfo.setAddress(express.getSendAddress());//详细地址
        return senderInfo;
    }
    private PersonInfo fillReceiverInfo(Express express, PersonInfo receiverInfo) {
        receiverInfo.setName(express.getSendName());
        receiverInfo.setMobile(express.getSendMobile());
        receiverInfo.setPhone(express.getSendMobile());
        fillRegion(express, receiverInfo);
        receiverInfo.setAddress(express.getReceivAddress());//详细地址
        return receiverInfo;
    }
    private void fillRegion(Express express, PersonInfo sendeInfo) {
        //todo 暂定Express中的RegionId的行政区划级别为”区“
        List<RegionPo> regionPos=null;
        RegionPo areaRegion=null;
        try {
            InternalReturnObject<List<RegionPo>> internalReturnObject = regionMapper.retrieveParentRegionsById(express.getSendRegionId());
            regionPos = internalReturnObject.getData();
            InternalReturnObject<RegionPo> areaRegionRet = regionMapper.findRegionById(express.getSendRegionId());
            areaRegion = areaRegionRet.getData();
        } catch (Exception e) {
            throw new RuntimeException("JTAdapter调用regionMapper方法异常");
        }
        if (regionPos.isEmpty()) {
            throw new RuntimeException("regionPos为empty");
        }
        //根据retrieveParentsRegions源码获取得到的list是倒序

        sendeInfo.setArea(areaRegion.getName());//区县
        //sendeInfo.setCountryCode();//国,默认中国,所以不设
        sendeInfo.setCity(regionPos.get(0).getName());//市
        sendeInfo.setProv(regionPos.get(1).getName());//省
    }
    @Override
    public PostCreatePackageAdaptorDto createPackage(ShopLogistics shopLogistics, Express express) throws NoSuchAlgorithmException {
       return createPackage(shopLogistics, express, null);
    }
    public PostCreatePackageAdaptorDto createPackage(ShopLogistics shopLogistics, Express express, PickingUpTime pickingUpTime) {

        Logistics logistics = shopLogistics.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }
        AddExpressOrderParam param = new AddExpressOrderParam();
//        param.setCustomerCode(String.valueOf(shopLogistics.getAccount()));
//        param.setDigest(shopLogistics.getSecret());
        param.setTxlogisticId(express.getOrderCode());
        //默认值
//        param.setExpressType();
//        param.setOrderType();
//        param.setServiceType();
//        param.setDeliveryType();
//        param.setPayType();
//param.setServiceType();
        PersonInfo senderInfo = new PersonInfo();
        fillSenderInfo(express, senderInfo);
        express.setShopLogistics(shopLogistics);

        PersonInfo receiverInfo = new PersonInfo();
        fillReceiverInfo(express,receiverInfo);
        param.setCustomerCode(shopLogistics.getAccount());
        param.setSendStartTime(pickingUpTime.getSendStartTime());
        param.setSendEndTime(pickingUpTime.getSendEndTime());
        param.setSender(senderInfo);
        param.setReceiver(receiverInfo);
        param.setGoodsType(express.getGoodType());
        param.setWeight(String.valueOf(express.getWeight()));
        if(pickingUpTime!=null){
            param.setSendStartTime(pickingUpTime.getSendStartTime());
            param.setSendEndTime(pickingUpTime.getSendEndTime());
        }else{
            //todo:之后可能会调整
            //如果没有时间的话,则默认从现在起到24小时后
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime nextDay = currentDateTime.plusDays(1);
            param.setSendStartTime(pickingUpTime.convertToString(currentDateTime));
            param.setSendEndTime(pickingUpTime.convertToString(nextDay));
            //todo:由于未定,所以暂不将对应时间更新进数据库
        }
        String jsonStr = jacksonUtil.toJson(param);
        AddExpressOrderRetObj data =null;
        try {
            ReturnObj<AddExpressOrderRetObj> addExpressOrderRetObjReturnObj = jtExpressMapper.addExpressOrder(logistics.getAppId(), System.currentTimeMillis(), jsonStr);
            data = addExpressOrderRetObjReturnObj.getData();
            String billCode = data.getBillCode();
            PostCreatePackageAdaptorDto postCreatePackageAdaptorDto = new PostCreatePackageAdaptorDto();
            postCreatePackageAdaptorDto.setId(express.getId());
            postCreatePackageAdaptorDto.setBillCode(billCode);
            return postCreatePackageAdaptorDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("createExpressOrder调用jtExpressMapper.addExpressOrder异常");
        }
    }
    @Override
    public Express getPackage(ShopLogistics shopLogistics, String billCode) throws NoSuchAlgorithmException {
        Logistics logistics = shopLogistics.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }
        GetLogisticsTraceParam logisticsTraceParam = new GetLogisticsTraceParam();
        //预设billCode为唯一订单的情况下
        logisticsTraceParam.setBillCodes(billCode);
        ReturnObj<List<GetLogisticsTraceRetObj>> returnObj = jtExpressMapper.getLogisticsTrace(logistics.getAppId(), System.currentTimeMillis(), jacksonUtil.toJson(logisticsTraceParam));
        List<GetLogisticsTraceRetObj> data = returnObj.getData();
        GetLogisticsTraceRetObj traceRetObj = data.get(0);
        //在对方list按时间排序的情况下
        ArrayList<TraceDetail> details = traceRetObj.getDetails();
        TraceDetail traceDetail = details.get(details.size() - 1);
        //来一个映射器
        String scanTypeOrProblemType= StringUtils.hasText(traceDetail.getScanType())?traceDetail.getScanType():traceDetail.getProblemType();
        Express express = expressDao.findByBillCode(shopLogistics.getShopId(), billCode);
        //todo:由于目前express没有对应的traceDetail的字段,于是只封装了scanType
        Optional<Byte> updateStatusOpt = Optional.ofNullable(stringToStatusCode.get(scanTypeOrProblemType));
        if (updateStatusOpt.isPresent()) {
            if(express.allowStatus(updateStatusOpt.get())){
                express.setStatus(updateStatusOpt.get());
            }else{
                //todo:异常未定义
                throw new RuntimeException();
            }
        }else{
            //todo:异常未定义
            throw new RuntimeException();
        }

        return express;
    }

    @Override
    public void cancelPackage(ShopLogistics shopLogistics, Express express) throws NoSuchAlgorithmException {
        Logistics logistics = shopLogistics.getLogistics();
        if (logistics == null) {
            throw new IllegalArgumentException("create package:logistics is null");
        }
        CancelExpressOrderParam cancelParam = new CancelExpressOrderParam();
        cancelParam.setTxlogisticId(express.getOrderCode());
        cancelParam.setReason(express.getCancelReason());
        String param = jacksonUtil.toJson(cancelParam);
        //todo 填入Logistics中的appAcount
        ReturnObj<CancelExpressOrderRetObj> returnObj = jtExpressMapper.cancelOrder(logistics.getAppId(),System.currentTimeMillis(), param);
        String code = returnObj.getCode();
        if ("1".equals(code)) {

        } else {
            throw new RuntimeException("错误response");
        }
        return;
    }

    @Override
    public void sendPackage(ShopLogistics shopLogistics, String billCode) throws NoSuchAlgorithmException {
        Express express = expressDao.findByBillCode(shopLogistics.getShopId(), billCode);
        //todo:目前ZTOAdapter的作者暂未实现时间相关
        //在create方法默认传null
        createPackage(shopLogistics,express,null);
    }


>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
}
