//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.WeightTemplate;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@NoArgsConstructor
@Document(collection = "regionTemplate")
@CopyFrom({WeightTemplate.class, RegionTemplate.class})
public class WeightTemplatePo {

    @MongoId
    private String objectId;

    private Integer firstWeight;

<<<<<<< HEAD
    private Long firstWeightFreight;
=======
    private Long firstWeightPrice;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920

    private List<WeightThresholdPo> thresholds;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

<<<<<<< HEAD
    public Long getFirstWeightFreight() {
        return firstWeightFreight;
    }

    public void setFirstWeightFreight(Long firstWeightFreight) {
        this.firstWeightFreight = firstWeightFreight;
=======
    public Long getFirstWeightPrice() {
        return firstWeightPrice;
    }

    public void setFirstWeightPrice(Long firstWeightPrice) {
        this.firstWeightPrice = firstWeightPrice;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
    }

    public List<WeightThresholdPo> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<WeightThresholdPo> thresholds) {
        this.thresholds = thresholds;
    }
}
