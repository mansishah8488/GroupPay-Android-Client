package ours.team20.com.groupay.listadapter;

/**
 * Created by Ken on 4/26/2015.
 */

public class Item {
    private String name;
    private String detail;

    public Item(String name, String detail) {
        super();
        this.name = name;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
