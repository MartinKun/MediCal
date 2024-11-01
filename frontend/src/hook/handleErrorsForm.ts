import { ErrorI } from "@/interfaces/errorsInterface";
import { useState } from "react";

type DynamicFormState = {
  [key: string]: string;
};


const handleErrorsForm = () => {
  const [errors, setErrors] = useState<ErrorI[]>([]);


  const validateForm = (formState: DynamicFormState) => {
    setErrors([])
    let errorsFinded = 0;
      if (formState.password && !validatePassword(formState.password)){
        setErrors((prevErrors) => [
            ...prevErrors,
            {
              value: "password",
              message:
                "La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial, y debe tener al menos 8 caracteres.",
            },
          ]);
          errorsFinded++;
      }
    
      if (formState.birthDate && !validateAge(formState.birthDate)) {
        setErrors( (prevErrors) => [
            ...prevErrors,
          {
            value: "birthDate",
            message: "Debes tener más de 18 años.",
          },
        ]);
        errorsFinded++;
      }

      if(formState.password && formState.confirmPassword && (formState.password !== formState.confirmPassword)) {
        setErrors( (prevErrors) => [
          ...prevErrors,
        {
          value: "confirmPassword",
          message: "Las contraseñas no coinciden.",
        },
      ]);
      errorsFinded++;
      }
      return errorsFinded > 0;
  }


    return {
      validateForm,
      errors
    };
};

export const validatePassword = (value: string) => {
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&#]).{8,}$/;
  
    if (passwordRegex.test(value)) {
      return true;
    }
  
    return false;
  }
  
  export const validateAge = (birthDate: string): boolean => {
      const birth = new Date(birthDate);
      const today = new Date();
    
      const age = today.getFullYear() - birth.getFullYear();
      const isBirthdayPassedThisYear =
        today.getMonth() > birth.getMonth() ||
        (today.getMonth() === birth.getMonth() && today.getDate() >= birth.getDate());
    
      const isOver18 = age > 18 || (age === 18 && isBirthdayPassedThisYear);
    
      if (!isOver18) return false;
      
    
      return true;
    }

    export default handleErrorsForm;
