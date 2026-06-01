package top.daoha.domain.record.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class nowStatus {
    //用户id
    private Long userId;
    //开始时间
    private Date startDate;
    //结束时间
    private Date endDate;
    //是否为当前最新,是否属于生理期之间
    private Integer isActive;
}
