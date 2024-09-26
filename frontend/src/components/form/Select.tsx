type Option = {
  value: string;
  label: string;
};

type Props = {
  name: string;
  label: string;
  icon: React.ComponentType;
  handleChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  value: string;
  options: Option[];
};

export const Select = ({
  name,
  label,
  icon: Icon,
  handleChange,
  value,
  options,
}: Props) => {
  return (
    <div className="space-y-2">
      <label
        htmlFor="gender"
        className="block
                   text-white
                   text-sm
                   font-medium"
      >
        {label}
      </label>
      <div className="relative">
        <select
          id={name}
          name={name}
          value={value}
          onChange={handleChange}
          className="w-full
                     pl-10
                     pr-3
                     py-2
                     rounded-md
                     bg-white
                     bg-opacity-50
                     border
                     border-white
                     border-opacity-50
                     text-gray-800
                     focus:outline-none
                     focus:ring-2
                     focus:ring-purple-600
                     focus:border-transparent"
          required
        >
          {options.map((option, index) => (
            <option key={index} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
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
