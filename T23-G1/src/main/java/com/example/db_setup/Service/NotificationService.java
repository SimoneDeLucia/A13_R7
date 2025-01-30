/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.example.db_setup.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.db_setup.Notification;
import com.example.db_setup.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Salvare una nuova notifica
    public void saveNotification(int playerID, String titolo, String message) {
        Notification notification = new Notification(playerID, titolo, message, LocalDateTime.now(), false);
        notificationRepository.save(notification);
    }

    // Ottenere notifiche con paginazione e ordine per timestamp
    public Page<Notification> getNotificationsByPlayer(int playerID, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        return notificationRepository.findByPlayerID(playerID, pageable);
    }

    // Segnare una singola notifica come letta
    @Transactional
    public void markNotificationAsRead(Long notificationID) {
        if (!notificationRepository.existsById(notificationID)) {
            throw new RuntimeException("Notifica con ID " + notificationID + " non trovata.");
        }
        notificationRepository.markAsRead(notificationID);
    }

    // Segnare tutte le notifiche di un utente come lette
    @Transactional
    public void markAllNotificationsAsRead(int playerID) {
        notificationRepository.markAllAsReadByPlayerID(playerID);
    }

    // Eliminare una notifica
    @Transactional
    public void deleteNotification(Long notificationID) {
        try {
            notificationRepository.deleteById(notificationID);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Notifica con ID " + notificationID + " non trovata.");
        }
    }

    // Eliminare tutte le notifiche di un utente in un'unica query
    @Transactional
    public void clearNotificationsByPlayer(int playerID) {
        notificationRepository.deleteByPlayerID(playerID);
    }

    public List<Notification> getUnreadNotificationsByPlayer(int playerID) {
        return notificationRepository.findByPlayerIDAndIsReadFalseOrderByTimestampDesc(playerID);
    }
    
}
