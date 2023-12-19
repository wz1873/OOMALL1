//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.dao.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;

import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
<<<<<<< HEAD
=======
import cn.edu.xmu.oomall.freight.dao.logistics.exception.MethodNoSupportException;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PickingUpTime;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
import cn.edu.xmu.oomall.freight.dao.retobj.PostCreatePackageAdaptorDto;

import java.security.NoSuchAlgorithmException;
/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 * 物流平台接口
 */

/**
 * 物流渠道适配器接口
 * 适配器模式
 */
public interface LogisticsAdaptor {
<<<<<<< HEAD

=======
    /**
     * @Author:丁圳杰
     * @return
     */
    default MethodNoSupportException notSupport() {
        return new MethodNoSupportException();
    }
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
    /**
     * 创建运单
     * @param shopLogistics
     * @param express
     * @return
     * @throws NoSuchAlgorithmException
     */
<<<<<<< HEAD
    PostCreatePackageAdaptorDto createPackage(ShopLogistics shopLogistics,Express express) throws NoSuchAlgorithmException;
=======
    PostCreatePackageAdaptorDto createPackage(ShopLogistics shopLogistics, Express express) throws NoSuchAlgorithmException;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920

    /**
     * 查询运单
     * @param shopLogistics
     * @param billCode
     * @return
     * @throws NoSuchAlgorithmException
     */
    Express getPackage(ShopLogistics shopLogistics, String billCode) throws NoSuchAlgorithmException;

    /**
     * 取消运单
     * @param shopLogistics
     * @param express
     * @throws NoSuchAlgorithmException
     */
    void cancelPackage(ShopLogistics shopLogistics,Express express) throws NoSuchAlgorithmException;

    /**
     * 商户发出揽收
     * @param shopLogistics
     * @param billCode
     */
    void sendPackage(ShopLogistics shopLogistics,String billCode) throws NoSuchAlgorithmException;
<<<<<<< HEAD
=======


>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
}