package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;


public interface ChatBoxRepository extends JpaRepository<ChatBox, UUID> {
    Collection<ChatBox> findChatBoxByBuyerIDOrSellerId(User buyerID, User sellerID);
}
