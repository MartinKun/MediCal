type Props = {
  value: string;
};

export const SubmitButton = ({ value }: Props) => {
  return (
    <button
      type="submit"
      className="w-full
                 py-2 px-4
                 bg-white
                 bg-opacity-20
                 hover:bg-opacity-30
                 text-white
                 font-semibold
                 rounded-md
                 transition
                 duration-300
                 ease-in-out
                 focus:outline-none
                 focus:ring-2
                 focus:ring-purple-600
                 focus:ring-opacity-50"
    >
      {value}
    </button>
  );
};
