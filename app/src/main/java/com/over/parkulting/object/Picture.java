package com.over.parkulting.object;

import java.io.File;

public class Picture {
    private String type;
    private String path;

    public Picture(File file) {
        this.type = file.getName().split("_")[0];
        this.path = file.getAbsolutePath();
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}
