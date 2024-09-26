type Props = {
  name: string;
  label: string;
  type: string;
  placeholder: string;
  icon: React.ComponentType;
  handleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  value: string;
};

export const InputWithIcon = ({
  name,
  label,
  type,
  placeholder,
  icon: Icon,
  handleChange,
  value,
}: Props) => {
  return (
    <div className="space-y-2">
      <label
        htmlFor={name}
        className="block
                   text-white
                   text-sm
                   font-medium"
      >
        {label}
      </label>
      <div className="relative">
        <input
          id={name}
          name={name}
          type={type}
          placeholder={placeholder}
          className="w-full
                     pl-10 pr-3
                     py-2 rounded-md
                     bg-white
                     bg-opacity-50
                     border
                     border-white
                     border-opacity-50
                     text-gray-800
                     placeholder-gray-500
                     focus:outline-none
                     focus:ring-2
                     focus:ring-purple-600
                     focus:border-transparent"
          required
          value={value}
          onChange={handleChange}
        />

        <span
          className="absolute
                     left-3
                     top-1/2
                     transform
                     -translate-y-1/2
                     text-gray-500
                     h-5 w-5"
        >
          <Icon />
        </span>
      </div>
    </div>
  );
};
