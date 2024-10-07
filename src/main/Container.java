package main;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Container {
    private final ArrayList<Image> images;
    private int added;

    public Container() {
        this.images =  new ArrayList<>();
        this.added = 0;
    }

    public void add(Image image){
        this.images.add(image);
        addCounter();
    }

    void silentAdd(Image image){
        this.images.add(image);
    }

    private void addCounter(){
        this.added++;
    }

    public int getAdded(){
        return added;
    }

    public Image getImage() {
        if (!images.isEmpty()) {
            return images.remove(images.size() - 1);
        }
        return null;
    }

    public void removeImage(Image img){
        images.remove(img);
    }

}
