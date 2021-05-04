package model;

import model.dataStructures.MessageInfoContainer.ListMessageInfoContainer;
import model.dataStructures.SFAlgoTrieContainer.ListSFAlgoTrieContainer;
import service.EncodingDecodingService;

public class Model {

    private ListMessageInfoContainer messageInfoContainer;
    private ListSFAlgoTrieContainer SFAlgoTrieContainer;
    private EncodingDecodingService encCodeService;

    public Model(ListMessageInfoContainer messageInfoContainer,
                 ListSFAlgoTrieContainer SFAlgoTrieContainer) {
        this.messageInfoContainer = messageInfoContainer;
        this.SFAlgoTrieContainer = SFAlgoTrieContainer;
        this.encCodeService = EncodingDecodingService.getInstance();
    }

    public Model() {

    }

    public ListMessageInfoContainer getMessageInfoContainer() {
        return messageInfoContainer;
    }

    public void setMessageInfoContainer(ListMessageInfoContainer messageInfoContainer) {
        this.messageInfoContainer = messageInfoContainer;
    }

    public ListSFAlgoTrieContainer getSFAlgoTrieContainer() {
        return SFAlgoTrieContainer;
    }

    public void setSFAlgoTrieContainer(ListSFAlgoTrieContainer SFAlgoTrieContainer) {
        this.SFAlgoTrieContainer = SFAlgoTrieContainer;
    }

    public EncodingDecodingService getEncCodeService() {
        return encCodeService;
    }

    public void setEncCodeService(EncodingDecodingService encCodeService) {
        this.encCodeService = encCodeService;
    }
}
