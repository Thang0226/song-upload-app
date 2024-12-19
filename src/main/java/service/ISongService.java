package service;

import model.Song;

import java.util.List;

public interface ISongService {
	List<Song> findAll();

	void add(Song song);

	Song findById(int id);
}
