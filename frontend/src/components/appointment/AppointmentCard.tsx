import { Trash2Icon } from "lucide-react";

interface AppointmentI {
  id: number;
  name: string;
  date: Date;
  image: string;
  chiefComplaintReason: string;
}

type Props = {
  appointment: AppointmentI;
};

export const AppointmentCard = ({ appointment }: Props) => {
  return (
    <div
      key={appointment.id}
      className="bg-white
                 p-2 mb-2
                 rounded-lg
                 shadow-md
                 cursor-pointer
                 hover:shadow-lg
                 transition-shadow
                 duration-200
                 relative
                 min-w-[200px]"
    >
      <div
        className="flex
                   items-start
                   justify-between"
      >
        <div
          className="flex
                     items-start
                     space-x-2
                     overflow-hidden"
        >
          <div
            className="flex-shrink-0
                       w-8 h-8
                       rounded-full
                       overflow-hidden
                       bg-gray-200"
          >
            <img
              src={appointment.image}
              alt={appointment.name}
              className="w-full
                         h-full
                         object-cover"
              onError={(e) => {
                e.currentTarget.onerror = null;
                e.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(
                  appointment.name
                )}&color=7F9CF5&background=EBF4FF`;
              }}
            />
          </div>
          <div className="min-w-0 flex-1">
            <div
              className="text-sm
                         font-semibold
                         text-gray-800
                         truncate"
            >
              {appointment.name}
            </div>
            <div
              className="text-xs
                         text-gray-600"
            >
              {appointment.date.toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })}
            </div>
            <div
              className="text-xs
                         text-gray-600
                         truncate"
            >
              {appointment.chiefComplaintReason}
            </div>
          </div>
        </div>
        <div className="flex-shrink-0">
          <button
            className="h-6 w-6
                       p-0
                       text-gray-400
                       hover:text-red-600
                       hover:bg-transparent
                       absolute
                       top-2
                       right-2"
          >
            <Trash2Icon className="h-4 w-4" />
          </button>
        </div>
      </div>
    </div>
  );
};
