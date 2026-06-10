package top.daoha.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataShowRequestDTO {

    //用户id
    private Long userId;
    //查看前几次记录
    private Integer count;

}
