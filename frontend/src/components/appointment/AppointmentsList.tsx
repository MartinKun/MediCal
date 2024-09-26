import { AppointmentI } from "@/interfaces/appointmentInterface";
import { appointmentsData } from "@/util/data";
import { AppointmentCard } from "./AppointmentCard";
import { PlusIcon } from "lucide-react";

type Props = {
  dateSelected: {
    day: undefined | number;
    month: number;
    year: number;
  };
  setShowModal: (value: boolean) => void;
  setAppointmentSelected: (value: AppointmentI | undefined) => void;
};

export const AppointmentsList = ({
  dateSelected,
  setAppointmentSelected,
  setShowModal,
}: Props) => {
  if (!dateSelected.day) return;

  return (
    <div
      className="flex
                 flex-col
                 gap-y-6"
    >
      <h2
        className="text-xl
                   font-semibold
                   text-white
                   mt-[32px]"
      >
        Mis citas:
      </h2>
      <ul className="w-full">
        {!appointmentsData.some(
          (appointment) =>
            appointment.date.getDate() === dateSelected.day &&
            appointment.date.getMonth() === dateSelected.month &&
            appointment.date.getFullYear() === dateSelected.year
        ) && (
          <li>
            <h2>No hay citas para este día</h2>
          </li>
        )}

        {dateSelected.day &&
          appointmentsData
            .filter(
              (appointment) =>
                appointment.date.getDate() === dateSelected.day &&
                appointment.date.getMonth() === dateSelected.month &&
                appointment.date.getFullYear() === dateSelected.year
            )
            .map((appointment) => (
              <li>
                <AppointmentCard
                  appointment={appointment}
                  setShowModal={setShowModal}
                  setAppointmentSelected={setAppointmentSelected}
                />
              </li>
            ))}
      </ul>

      <button
        className="w-full
                   mt-1
                   text-white
                   bg-white
                   bg-opacity-10
                   hover:bg-opacity-20
                   hover:text-black
                   flex
                   justify-center
                   py-3
                   rounded-lg"
        onClick={() => {
          setAppointmentSelected(undefined);
          setShowModal(true);
        }}
      >
        <PlusIcon className="h-4 w-4" />
      </button>
    </div>
  );
};
