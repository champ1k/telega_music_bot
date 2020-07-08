package org.alvl.nix.java.telegamusicbot.utils.keyboardutils;

;
import org.alvl.nix.java.telegamusicbot.utils.botutils.BotContext;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardUtils {

    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(boolean isSelective,
                                                                boolean isResizeKeyboard,
                                                                boolean isOneTimeKeyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(isSelective);
        replyKeyboardMarkup.setResizeKeyboard(isResizeKeyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(isOneTimeKeyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(List<String> answers) {
        ReplyKeyboardMarkup result
                = createReplyKeyboardMarkup(true, true, false);
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        KeyboardRow keyboardButtons2 = new KeyboardRow();

        for (int i = 0, lenght = answers.size(); i < lenght; i++) {
            if (i % 2 == 0) {
                String answer = answers.get(i);
                KeyboardButton button = new KeyboardButton(answer);
                keyboardButtons1.add(button);
            } else {
                String answer = answers.get(i);
                KeyboardButton button = new KeyboardButton(answer);
                keyboardButtons2.add(button);
            }
        }

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(keyboardButtons1);
        keyboard.add(keyboardButtons2);
        return result.setKeyboard(keyboard);
    }

    public static InlineKeyboardMarkup createInlineSongKeyboard() {
        InlineKeyboardMarkup result = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRowHelpButtons = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButtonLike
                = new InlineKeyboardButton()
                .setText("\uD83D\uDC97")
                .setCallbackData("\uD83D\uDC97");
        InlineKeyboardButton inlineKeyboardButtonDel
                = new InlineKeyboardButton()
                .setText("\u274C")
                .setCallbackData("\u274C");
        InlineKeyboardButton inlineKeyboardButtonNotLike
                = new InlineKeyboardButton()
                .setText("\uD83D\uDC94")
                .setCallbackData("\uD83D\uDC94");
        ;

        keyboardRowHelpButtons
                .add(inlineKeyboardButtonLike);
        keyboardRowHelpButtons
                .add(inlineKeyboardButtonDel);
        keyboardRowHelpButtons
                .add(inlineKeyboardButtonNotLike);

        keyboard.add(keyboardRowHelpButtons);

        result.setKeyboard(keyboard);
        return result;

    }

    public static ReplyKeyboardMarkup createSongKeyboard(String textButton) {
        ReplyKeyboardMarkup result
                = createReplyKeyboardMarkup(true, true, false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton("\uD83D\uDC97");
        KeyboardButton button3 = new KeyboardButton("\uD83D\uDC94");
        keyboardButtons.add(button1);
        keyboardButtons.add(button3);
        keyboard.add(keyboardButtons);
        result.setKeyboard(keyboard);
        return result;

    }

    public static ReplyKeyboardMarkup createMenuKeyboard(String textButton) {

        ReplyKeyboardMarkup result = createReplyKeyboardMarkup(true, true, false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(textButton);
        keyboardButtons.add(button);
        keyboard.add(keyboardButtons);
        return result.setKeyboard(keyboard);
    }


    public static ReplyKeyboardMarkup createPlaylistSongKeyboard(List<String> answers) {
        ReplyKeyboardMarkup result
                = createReplyKeyboardMarkup(true, true, false);
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        KeyboardRow keyboardButtons2 = new KeyboardRow();

        for (int i = 0, lenght = answers.size(); i < lenght; i++) {
            if (i % 2 == 0) {
                String answer = answers.get(i);
                KeyboardButton button = new KeyboardButton(answer);
                keyboardButtons1.add(button);
            } else {
                String answer = answers.get(i);
                KeyboardButton button = new KeyboardButton(answer);
                keyboardButtons2.add(button);
            }
        }
        KeyboardRow keyboardButtons = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Open Menu");
        keyboardButtons.add(button);
        List<KeyboardRow> keyboard = new ArrayList<>();

        keyboard.add(keyboardButtons1);
        keyboard.add(keyboardButtons2);
        keyboard.add(keyboardButtons);
        return result.setKeyboard(keyboard);

    }

}
