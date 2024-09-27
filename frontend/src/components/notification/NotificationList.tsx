import { useState } from "react";
import { NotificationCard } from "./NotificationCard";
import { notificationsData } from "@/util/data";
import { NotificationI } from "@/interfaces/notificationInterface";

export const NotificationList = () => {
  const [notifications, setNotifications] =
    useState<NotificationI[]>(notificationsData);

  const markAsRead = (id: number) => {
    setNotifications(
      notifications.map((notif) =>
        notif.id === id ? { ...notif, isRead: true } : notif
      )
    );
  };

  const deleteNotification = (id: number) => {
    setNotifications(notifications.filter((notif) => notif.id !== id));
  };

  return (
    <>
      {notifications.length === 0 ? (
        <p className="text-white text-center">No tienes notificaciones.</p>
      ) : (
        <ul className="space-y-4">
          {notifications.map((notif) => (
            <NotificationCard
              notification={notif}
              markAsRead={markAsRead}
              deleteNotification={deleteNotification}
            />
          ))}
        </ul>
      )}
    </>
  );
};
