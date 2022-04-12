package com.over.parkulting.object;

public class Park {
    private String name;
    private String Base64IMG;

    public Park(String name, String Base64IMG){
        this.name = name;
        this.Base64IMG = Base64IMG;
    }

    public String getName() {
        return name;
    }

    public String getBase64IMG() { return Base64IMG; }
}
