package org.alvl.nix.java.telegamusicbot.services;

import org.alvl.nix.java.telegamusicbot.exceptions.SongNotFoundException;
import org.alvl.nix.java.telegamusicbot.model.Song;
import org.springframework.stereotype.Service;
import org.alvl.nix.java.telegamusicbot.operations.SongOperations;
import org.alvl.nix.java.telegamusicbot.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SongService implements SongOperations {

    @Autowired
    private SongRepository songRepository;

    @Override
    public List<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public Song findSongById(Integer id) throws SongNotFoundException {
        return songRepository.findById(id).orElseThrow(() -> new SongNotFoundException(id));
    }

    @Override
    public void save(Song song) {
        songRepository.save(song);
    }

    @Override
    public void delete(Integer id) {
        songRepository.deleteById(id);
    }

    @Override
    public List<Song> findAllByTitle(String songtitle) {
        return songRepository.findAllByTitleContains(songtitle);
    }

    @Override
    public List<Song> findByTitleLike(String title) {
        return songRepository.findByTitleLike(title);
    }

    @Override
    public Song findSongByFileId(String fileId) {
        return songRepository.findSongByFileId(fileId);
    }


}
