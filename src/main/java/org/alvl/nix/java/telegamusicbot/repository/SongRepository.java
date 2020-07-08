package org.alvl.nix.java.telegamusicbot.repository;

import org.alvl.nix.java.telegamusicbot.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SongRepository extends JpaRepository<Song, Integer> {

    List<Song> findAllByTitleContains(String title);

    List<Song> findByTitleLike(String title);

    Song findSongByFileId(String fileId);

}
