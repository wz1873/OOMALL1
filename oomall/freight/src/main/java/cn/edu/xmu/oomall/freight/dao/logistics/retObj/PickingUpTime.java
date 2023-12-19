package cn.edu.xmu.oomall.freight.dao.logistics.retObj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author 丁圳杰
 * @Task 2023-dgn3-001
 * @description:揽件时间
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PickingUpTime {
    /**
     * 物流公司上门取货开始时间 yyyy-MM-dd HH:mm:ss
     */
    private String sendStartTime;

    public void setSendStartTime(LocalDateTime time) {

        this.sendStartTime = convertToString(time);
    }
    public String convertToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
    public void setSendEndTime(LocalDateTime time) {

        this.sendEndTime = convertToString(time);
    }
    /**
     * 物流公司上门取货结束时间 yyyy-MM-dd HH:mm:ss
     */
    private String sendEndTime;
}
