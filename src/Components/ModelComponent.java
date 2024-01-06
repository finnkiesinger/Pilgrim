package Components;

import ECS.Component;
import Models.Model;

public class ModelComponent extends Component {
    public Model model;

    public ModelComponent(Model model) {
        this.model = model;
    }
}
