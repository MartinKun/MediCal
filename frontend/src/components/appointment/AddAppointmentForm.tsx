type Props = {
  setShowModal: (value: boolean) => void;
};

export const AddAppointmentForm = ({ setShowModal }: Props) => {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TO DO add apointment logic.

    setShowModal(false);
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="space-y-4 font-sans">
        <div
          className="grid
                     grid-cols-4
                     items-center
                     gap-4"
        >
          <div className="text-right text-gray-700">Nombre</div>
          <input
            id="nombre"
            name="nombre"
            className="col-span-3
                       border
                       border-gray-300
                       rounded
                       px-3
                       py-2
                       focus:outline-none
                       focus:ring-2
                       focus:ring-blue-500"
          />
        </div>
        <div
          className="grid
                     grid-cols-4
                     items-center
                     gap-4"
        >
          <div className="text-right text-gray-700">Fecha</div>
          <input
            id="fecha"
            name="fecha"
            type="date"
            className="col-span-3
                       border
                       border-gray-300
                       rounded
                       px-3
                       py-2
                       focus:outline-none
                       focus:ring-2
                       focus:ring-blue-500"
          />
        </div>
        <div
          className="grid grid-cols-4
                     items-center
                     gap-4"
        >
          <div className="text-right text-gray-700">Hora</div>
          <input
            id="hora"
            name="hora"
            type="time"
            className="col-span-3
                       border
                       border-gray-300
                       rounded
                       px-3 py-2
                       focus:outline-none
                       focus:ring-2
                       focus:ring-blue-500"
          />
        </div>
        <div
          className="grid grid-cols-4
                     items-center
                     gap-4"
        >
          <div
            className="text-right
                       text-gray-700"
          >
            Motivo
          </div>
          <input
            id="motivo"
            name="motivo"
            className="col-span-3
                       border
                       border-gray-300
                       rounded
                       px-3 py-2
                       focus:outline-none
                       focus:ring-2
                       focus:ring-blue-500"
          />
        </div>
        <div
          className="grid
                     grid-cols-4
                     items-center
                     gap-4"
        >
          <div className="text-right text-gray-700">Dirección</div>
          <input
            id="direccion"
            name="direccion"
            className="col-span-3
                       border
                       border-gray-300
                       rounded
                       px-3 py-2
                       focus:outline-none
                       focus:ring-2
                       focus:ring-blue-500"
          />
        </div>
        <button
          type="submit"
          className="w-full
                     bg-blue-500
                     text-white
                     py-2 px-4
                     rounded
                     hover:bg-blue-600
                     transition
                     duration-200
                     ease-in-out
                     flex items-center
                     justify-center
                     cursor-pointer"
        >
          Agregar Cita
        </button>
      </div>
    </form>
  );
};
