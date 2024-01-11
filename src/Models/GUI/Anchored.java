package Models.GUI;

import Models.Size;
import Window.Window;
import org.joml.Vector3f;

public class Anchored extends Absolute {
    public float left;
    public float right;
    public float top;
    public float bottom;
    public float width;
    public float height;

    public GuiElement child;

    public Anchored(GuiElement child) {
        this.top = -1;
        this.bottom = -1;
        this.right = -1;
        this.left = -1;
        this.width = -1;
        this.height = -1;
        this.child = child;
        child.SetParent(this);
    }

    public Size GetSize() {
        Size parentSize = GetParentSize();

        Size size = new Size(-1, -1);

        if (left != -1 && right != -1) {
            size.width = parentSize.width - right - left;
        }

        if (top != -1 && bottom != -1) {
            size.height = parentSize.height - top - bottom;
        }

        if (size.width == -1 || size.height == -1) {
            if (width != -1 && size.width == -1) {
                size.width = width;
            }
            if (height != -1 && size.height == -1) {
                size.height = height;
            }
        }

        return size;
    }

    private void CalculateOffset(Vector3f offset) {
        Size parentSize = new Size(Window.ActiveWindow().GetWidth(), Window.ActiveWindow().GetHeight());

        if (parent != null) {
            parentSize = parent.GetSize();
        }

        Size size = GetSize();

        if (left != -1) {
            offset.x = left;
        } else if (right != -1) {
            offset.x = parentSize.width - size.width - right;
        }
        if (bottom != -1) {
            offset.y = bottom;
        } else if (top != -1) {
            offset.y = parentSize.height - size.height - top;
        }
    }

    public void Render(Vector3f offset) {
        CalculateOffset(offset);
        child.Render(offset);
    }
}
