package xyz.leyuna.disk.model.enums;

/**
 * @author pengli
 * @create 2021-12-22 10:40
 */
public enum  SortEnum {

    CREATE_DESC(1,"create_desc"),
    CREATE_ASC(2,"create_dasc"),
    UPDATE_DESC(3,"update_desc"),
    UPDATE_ASC(4,"update_dasc");

    private Integer type;
    private String name;

    SortEnum(Integer type,String name){
        this.type=type;
        this.name=name;
    }

    public Integer getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }
}
