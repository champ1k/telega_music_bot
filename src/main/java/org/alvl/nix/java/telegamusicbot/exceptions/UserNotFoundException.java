package org.alvl.nix.java.telegamusicbot.exceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UserNotFoundException extends TelegramApiException {
    public UserNotFoundException(Integer id) {
        super("Could not find user with this" + id + "id");
    }


}
