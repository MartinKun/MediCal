import { useState } from "react";

const useFormState = <T>(initialState: T) => {
  const [form, setForm] = useState(initialState);

  const setFormState = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    let { name, value } = e.target;

    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const resetForm = () => {
    setForm(initialState);
  };

  return { formState: form, setFormState, resetForm };
};

export default useFormState;