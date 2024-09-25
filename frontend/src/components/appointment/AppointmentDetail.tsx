import { AppointmentI } from "@/interfaces/appointmentInterface";
import { Trash2Icon } from "lucide-react";

type Props = {
  appointment: AppointmentI;
  setShowModal: (value: boolean) => void;
};

export const AppointmentDetail = ({ appointment, setShowModal }: Props) => {
  return (
    <div className="font-sans">
      <div className="flex items-center mb-4">
        <div
          className="w-16 h-16
                     rounded-full
                     overflow-hidden
                     bg-gray-200
                     mr-4"
        >
          <img
            src={appointment.image}
            alt={appointment.name}
            className="w-full h-full object-cover"
            onError={(e) => {
              e.currentTarget.onerror = null;
              e.currentTarget.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(
                appointment.name
              )}&color=7F9CF5&background=EBF4FF`;
            }}
          />
        </div>
        <div>
          <h3
            className="text-lg
                       font-semibold
                       text-gray-800"
          >
            {appointment.name}
          </h3>
          <p className="text-sm text-gray-600">
            {appointment.date.toLocaleDateString()} -{" "}
            {appointment.date.toLocaleTimeString([], {
              hour: "2-digit",
              minute: "2-digit",
            })}
          </p>
        </div>
      </div>
      <p className="text-gray-700 mb-2">
        <strong>Motivo:</strong> {appointment.chiefComplaintReason}
      </p>
      <p className="text-gray-700 mb-4">
        <strong>Dirección:</strong> {appointment.address}
      </p>
      <div
        className="mt-4 w-full
                   bg-red-500
                   text-white
                   py-2 px-4
                   rounded
                   hover:bg-red-600
                   transition
                   duration-200
                   ease-in-out
                   flex items-center
                   justify-center
                   cursor-pointer"
        onClick={() => {
          //setCitas(citas.filter((c) => c.id !== citaSeleccionada.id));
          setShowModal(false);
        }}
      >
        <Trash2Icon className="h-4 w-4 mr-2" />
        Cancelar Cita
      </div>
    </div>
  );
};
