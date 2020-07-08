package org.alvl.nix.java.telegamusicbot.utils.keyboardutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum KeyboardText {
      MENU_TEXT {
        @Override
        public List<String> getAnswers() {
            List<String> answers = new ArrayList<>();
            answers.add("Start Using Bot");
            return answers;
        }
    },
    MENU_REPLY_KEYBOARD_TEXT {
        @Override
        public List<String> getAnswers() {
            List<String> answers = new ArrayList<>();
            answers.add("Find Song");
            answers.add("Playlist");
            answers.add("Help");
            answers.add("Random Song");
            return answers;
        }
    };

    public List<String> getAnswers() {
        return Collections.emptyList();
    }
}
