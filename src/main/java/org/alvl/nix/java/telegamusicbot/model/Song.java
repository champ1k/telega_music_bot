package org.alvl.nix.java.telegamusicbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "file_id")
    private String fileId;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "file_size")
    private Integer fileSize;
    @Column(name = "title")
    private String title;
    @Column(name = "performer")
    private String performer;
}
