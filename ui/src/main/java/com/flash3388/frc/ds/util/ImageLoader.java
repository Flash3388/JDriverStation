package com.flash3388.frc.ds.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;

public class ImageLoader {

    private final ClassLoader mClassLoader;

    public ImageLoader(ClassLoader classLoader) {
        mClassLoader = classLoader;
    }

    public Image loadFromResource(String resource) throws IOException {
        URL url = mClassLoader.getResource(resource);
        if (url == null) {
            throw new IOException("resource not found " + resource);
        }

        return new Image(url.openStream());
    }
}
