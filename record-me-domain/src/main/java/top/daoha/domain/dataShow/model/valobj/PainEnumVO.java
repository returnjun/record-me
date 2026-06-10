package top.daoha.domain.dataShow.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PainEnumVO {
    SLIGHRT(0,"轻微"),
    NORMAL(1,"正常"),
    severe(2,"剧烈"),
    ;


    private Integer code;
    private String info;

    public static PainEnumVO valueOf(Integer code){
        switch (code){
            case 0:
                return SLIGHRT;
            case 1:
                return NORMAL;
            case 2:
                return severe;
        }
        throw  new RuntimeException("err code not exits");
    }
}
