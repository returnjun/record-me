package top.daoha.domain.health.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum FlowEnumVO {
    LITTLE(0, "流量少"),
    NORMAL(1, "流量正常"),
    MANY(2, "流量多");

    private Integer code;
    private String info;

    public static FlowEnumVO valueOf(Integer code) {
        switch (code) {
            case 0:
                return LITTLE;
            case 1:
                return NORMAL;
            case 2:
                return MANY;
            default:
                throw new RuntimeException("err code not exits");
        }
    }
}
