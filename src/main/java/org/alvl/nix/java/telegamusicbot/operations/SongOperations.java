package org.alvl.nix.java.telegamusicbot.operations;

import org.alvl.nix.java.telegamusicbot.exceptions.SongNotFoundException;
import org.alvl.nix.java.telegamusicbot.model.Song;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface SongOperations {
    Song findSongById(Integer id) throws SongNotFoundException;

    List<Song> findAll();

    List<Integer> findAllIds();

    void save(Song audio);

    void delete(Integer id);

    List<Song> findAllByTitle(String title);

    List<Song> findByTitleLike(String title);

    Song findSongByFileId(String fileId);
}
