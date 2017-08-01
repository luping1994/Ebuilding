package net.suntrans.ebuilding.bean;

/**
 * Created by Looney on 2017/7/28.
 */

public class Ameter3 {
    public String name;
    public String value;
    public String nameCH;
    public String unit;

    public Ameter3(String name, String value, String nameCH) {
        this.name = name;
        this.value = value;
        this.nameCH = nameCH;
    }

    public Ameter3() {
    }

    public Ameter3(String name, String value) {
        this.name = name;
        this.value = value;

    }
}
