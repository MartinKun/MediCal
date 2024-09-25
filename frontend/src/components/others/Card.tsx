type Props = {
  title: string;
} & React.ComponentProps<"div">;

export const Card = ({ title, children }: Props) => {
  return (
    <div
      className="bg-white
                 bg-opacity-20
                 backdrop-blur-lg
                 rounded-xl
                 p-8
                 shadow-lg"
    >
      <h1
        className="text-3xl
                   font-bold
                   text-white
                   mb-6
                   text-center"
      >
        {title}
      </h1>
      {children}
    </div>
  );
};
