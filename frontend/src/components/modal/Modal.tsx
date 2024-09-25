import { X } from "lucide-react";

type Props = {
  setShowModal: (value: boolean) => void;
} & React.ComponentProps<"div">;
export const Modal = ({ children, setShowModal }: Props) => {
  return (
    <div
      className="fixed inset-0
                 bg-black
                 bg-opacity-50
                 flex
                 items-center
                 justify-center p-4
                 z-[200]"
      onClick={() => setShowModal(false)}
    >
      <div
        className="bg-white
                   rounded-lg
                   shadow-xl
                   max-w-md
                   w-full
                   relative"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="p-6">{children}</div>

        <div
          className="absolute
                     top-0 right-0
                     mt-4 mr-4
                     text-gray-500
                     hover:text-gray-700
                     cursor-pointer"
          onClick={() => setShowModal(false)}
        >
          <X className="h-6 w-6" />
        </div>
      </div>
    </div>
  );
};
