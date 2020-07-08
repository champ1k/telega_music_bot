package org.alvl.nix.java.telegamusicbot.exceptions;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SongNotFoundException extends TelegramApiException {
    public SongNotFoundException(Integer id) {
        super("Could not find song with this" + id + "id.");
    }
    public SongNotFoundException() {
        super("Could not find song.");
    }
}
