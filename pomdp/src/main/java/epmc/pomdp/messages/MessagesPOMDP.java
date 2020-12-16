package epmc.pomdp.messages;

import epmc.messages.Message;

public final class MessagesPOMDP {
    private final static String MESSAGES_POMDP = "MessagesPOMDP";

    public final static Message BUILDING_DD_MODEL = newMessage().setIdentifier("building-dd-model").build();
    public final static Message BUILDING_DD_MODEL_DONE = newMessage().setIdentifier("building-dd-model-done").build();
    public final static Message EXPLORING = newMessage().setIdentifier("exploring").build();
    public final static Message EXPLORING_DONE = newMessage().setIdentifier("exploring-done").build();
    public final static Message PURE_PROB_WITH_DIR = newMessage().setIdentifier("pure-prob-with-dir").build();
    public final static Message START_PARSING = newMessage().setIdentifier("start-parsing").build();
    public final static Message DONE_PARSING = newMessage().setIdentifier("done-parsing").build();

    private static Message.Builder newMessage() {
        return new Message.Builder().setBundle(MESSAGES_POMDP);
    }

    private MessagesPOMDP() {
    }
}
