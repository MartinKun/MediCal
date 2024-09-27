import { NotificationI } from "@/interfaces/notificationInterface";
import { CheckIcon, TrashIcon } from "lucide-react";
import { NotificationIcon } from "./NotificationIcon";

type Props = {
  notification: NotificationI;
  markAsRead: (value: number) => void;
  deleteNotification: (value: number) => void;
};

export const NotificationCard = ({
  notification,
  markAsRead,
  deleteNotification,
}: Props) => {
  return (
    <li
      key={notification.id}
      className={`bg-white
                  bg-opacity-50
                  rounded-lg
                  p-4
                  shadow-md
                  transition
                  duration-300
                  ease-in-out ${
                    notification.isRead ? "opacity-60" : "opacity-100"
                  }`}
    >
      <div
        className="flex
                   items-start
                   justify-between"
      >
        <div
          className="flex
                     items-start
                     space-x-3"
        >
          <div className="flex-shrink-0 mt-1">
            <NotificationIcon type={notification.type} />
          </div>
          <div>
            <p className="text-gray-800 font-medium">{notification.content}</p>
            <p className="text-sm text-gray-600">{notification.date}</p>
          </div>
        </div>
        <div className="flex space-x-2">
          {!notification.isRead && (
            <button
              onClick={() => markAsRead(notification.id)}
              className="text-blue-500
                         hover:text-blue-600
                         transition
                         duration-300
                         ease-in-out"
              aria-label="Marcar como leída"
            >
              <CheckIcon className="h-5 w-5" />
            </button>
          )}
          <button
            onClick={() => deleteNotification(notification.id)}
            className="text-red-500
                       hover:text-red-600
                       transition
                       duration-300
                       ease-in-out"
            aria-label="Eliminar notificación"
          >
            <TrashIcon className="h-5 w-5" />
          </button>
        </div>
      </div>
    </li>
  );
};
