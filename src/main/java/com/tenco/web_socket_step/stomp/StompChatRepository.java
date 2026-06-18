package com.tenco.web_socket_step.stomp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StompChatRepository extends JpaRepository<StompChat, Long> {
    // findAll() 전체조회

    /**
     * 특정 방 번호(roomId)에 해당하는 채팅 내역만 조회하는 메소드
     * 쿼리 메소드 (메소드 작성시 이름 규칙만 잘 지키면 자동으로 쿼리만들어줌 JPA기능)
     * @Query("select c from StompChat c where c.roomId = :rommId order by c.id asc")
     */
    List<StompChat> findByRoomId(Long roomId);
}
