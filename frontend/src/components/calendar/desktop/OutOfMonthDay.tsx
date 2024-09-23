type Props = {
  index: number;
  day: string;
};
export const OutOfMonthDay = ({ day, index }: Props) => {
  return (
    <td
      key={index}
      className="p-2 h-36
                 bg-black
                 bg-opacity-20
                 backdrop-blur-sm
                 rounded-lg"
    >
      <div
        className="font-bold
                   mb-1
                   text-white"
      >
        {day}
      </div>
    </td>
  );
};
