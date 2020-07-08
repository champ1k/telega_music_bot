package org.alvl.nix.java.telegamusicbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.alvl.nix.java.telegamusicbot.utils.botutils.BotState;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "chatId")
    private Long chatId;
    @Column(name = "username")
    private String nickname;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "stateId")
    private Integer stateId;
    @Column(name = "isAdmin")
    private boolean isAdmin;


    @Override
    public String toString() {
        return "id: " + id +
                "\nChat Id: " + chatId +
                "\nName: " + firstName +
                "\nSurname: " + lastName +
                "\nTelegram nickname: @" + nickname +
                "\nIs this user admin:" + isAdmin +
                "\nStage=" + BotState.byId(stateId).name();
    }
}