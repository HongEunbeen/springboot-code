package eun.example.sparta.repository;

import eun.example.sparta.model.Folder;
import eun.example.sparta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);
}
