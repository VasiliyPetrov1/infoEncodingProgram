package utility.convertationUtilities;

import model.MessageInfo;

@FunctionalInterface
public interface InputProcessingUtility<T, U> {

    MessageInfo<T> retrieveInputInfo(U... input);

}
