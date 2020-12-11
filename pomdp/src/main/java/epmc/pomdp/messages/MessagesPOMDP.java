package epmc.pomdp.messages;

import epmc.messages.Message;

public final class MessagesPOMDP {
    private final static String MESSAGES_POMDP = "MessagesPOMDP";

    public final static Message POMDP_IGNORE_NEG_ALWAYS = newMessage().setIdentifier("pomdp-ignore-neg-always").build();
    public final static Message POMDP_IGNORE_NEG_BETTER = newMessage().setIdentifier("pomdp-ignore-neg-better").build();
    public final static Message POMDP_IGNORE_ENGINE = newMessage().setIdentifier("pomdp-ignore-engine").build();
    public final static Message PURE_PROB_WITH_DIR = newMessage().setIdentifier("pure-prob-with-dir").build();

    private static Message.Builder newMessage() {
        return new Message.Builder().setBundle(MESSAGES_POMDP);
    }

    private MessagesPOMDP() {
    }
}
