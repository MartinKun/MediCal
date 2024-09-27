import { BellIcon, CalendarIcon, MessageSquareIcon } from "lucide-react";

type Props = {
  type: string;
};
export const NotificationIcon = ({ type }: Props) => {
  switch (type) {
    case "appointment":
      return <CalendarIcon className="h-5 w-5 text-blue-500" />;
    case "reminder":
      return <BellIcon className="h-5 w-5 text-yellow-500" />;
    case "message":
      return <MessageSquareIcon className="h-5 w-5 text-green-500" />;
    default:
      return <BellIcon className="h-5 w-5 text-gray-500" />;
  }
};
