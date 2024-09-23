type Props = {
  index: number;
  day: string;
};

export const OutOfMonthDay = ({ index, day }: Props) => {
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
                  text-[#e0e0e0]`}
    >
      <div
        className={`inline-block
                    whitespace-nowrap
                    px-[4px]
                    relative`}
      >
        {day}
      </div>
    </td>
  );
};
