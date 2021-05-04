import controller.Controller;
import model.Model;
import model.dataStructures.MessageInfoContainer.ListMessageInfoContainer;
import model.dataStructures.MessageInfoContainer.ListMessageInfoContainerImpl;
import model.dataStructures.SFAlgoTrieContainer.ListSFAlgoTrieContainer;
import model.dataStructures.SFAlgoTrieContainer.ListSFAlgoTrieContainerImpl;
import view.View;

public class Main {

    public static void main(String[] args) {

        View view = new View();
        ListMessageInfoContainer messageInfoContainer = new ListMessageInfoContainerImpl();
        ListSFAlgoTrieContainer SFAlgoTrieContainer = new ListSFAlgoTrieContainerImpl();
        Model model = new Model(messageInfoContainer, SFAlgoTrieContainer);

        Controller controller = new Controller(model, view);
        controller.processUser();

    }

}
