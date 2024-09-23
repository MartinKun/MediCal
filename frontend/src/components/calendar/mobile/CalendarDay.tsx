type Props = {
  index: number;
  day: string;
};

export const CalendarDay = ({ day, index }: Props) => {
  return (
    <td
      key={index}
      className={`col-span-1 
                  w-full
                  h-[29px]
                  text-center
                  text-2xl
                  text-inter
                  flex
                  justify-center
                  text-[#323232]`}
    >
      <button
        className={`inline-block
                    whitespace-nowrap
                    px-[4px] relative`}
      >
        {day}
      </button>
    </td>
  );
};
