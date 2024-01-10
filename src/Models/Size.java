package Models;

public class Size {

    public float width;
    public float height;

    public Size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
