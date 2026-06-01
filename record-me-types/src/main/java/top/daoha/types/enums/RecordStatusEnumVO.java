package top.daoha.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RecordStatusEnumVO {
    COMING(0,"生理期内"),
    COMMON(1,"生理期外"),
    ;

    private Integer code;
    private String info;

    public static RecordStatusEnumVO valueOf(Integer code){
        switch (code){
            case 0:
                return COMING;
            case 1:
                return COMMON;
        }
        throw  new RuntimeException("err code not exits");
    }
}
