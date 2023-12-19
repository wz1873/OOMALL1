//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.bo.template.PieceTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
<<<<<<< HEAD
=======
import org.springframework.data.mongodb.core.mapping.Field;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@Document("regionTemplate")
@CopyFrom({PieceTemplate.class, RegionTemplate.class})
public class PieceTemplatePo {

    @MongoId
    private String objectId;
<<<<<<< HEAD

    private Integer firstItem;

    private Long firstItemPrice;

    private Integer additionalItems;

    private Long additionalItemsPrice;
=======
    private Integer firstItems;
    private Long firstPrice;
    private Integer additionalItems;
    private Long additionalPrice;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

<<<<<<< HEAD
    public Integer getFirstItem() {
        return firstItem;
    }

    public void setFirstItem(Integer firstItem) {
        this.firstItem = firstItem;
    }

    public Long getFirstItemPrice() {
        return firstItemPrice;
    }

    public void setFirstItemPrice(Long firstItemPrice) {
        this.firstItemPrice = firstItemPrice;
=======
    public Integer getFirstItems() {
        return firstItems;
    }

    public void setFirstItems(Integer firstItems) {
        this.firstItems = firstItems;
    }

    public Long getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Long firstPrice) {
        this.firstPrice = firstPrice;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
    }

    public Integer getAdditionalItems() {
        return additionalItems;
    }

    public void setAdditionalItems(Integer additionalItems) {
        this.additionalItems = additionalItems;
    }

<<<<<<< HEAD
    public Long getAdditionalItemsPrice() {
        return additionalItemsPrice;
    }

    public void setAdditionalItemsPrice(Long additionalItemsPrice) {
        this.additionalItemsPrice = additionalItemsPrice;
=======
    public Long getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(Long additionalPrice) {
        this.additionalPrice = additionalPrice;
>>>>>>> 3a200f3f810107b4c3bb38ad75f6101ae897f920
    }
}
