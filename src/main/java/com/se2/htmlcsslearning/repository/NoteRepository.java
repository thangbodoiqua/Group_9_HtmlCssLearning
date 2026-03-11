package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}

