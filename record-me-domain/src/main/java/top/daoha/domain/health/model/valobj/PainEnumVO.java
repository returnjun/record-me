package top.daoha.domain.health.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PainEnumVO {
    SLIGHT(0, "轻微"),
    NORMAL(1, "正常"),
    SEVERE(2, "剧烈");

    private Integer code;
    private String info;

    public static PainEnumVO valueOf(Integer code) {
        switch (code) {
            case 0:
                return SLIGHT;
            case 1:
                return NORMAL;
            case 2:
                return SEVERE;
            default:
                throw new RuntimeException("err code not exits");
        }
    }
}
