package top.daoha.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataShowRequestDTO {

    // 用户 ID
    private Long userId;

    // 页码（从 1 开始）
    private Integer page;

    // 每页条数
    private Integer pageSize;

    // 兼容旧版 count 参数
    private Integer count;

}
