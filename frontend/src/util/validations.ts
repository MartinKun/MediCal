
export const validatePassword = (value: string) => {
  const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&#]).{8,}$/;

  if (!passwordRegex.test(value)) {
    return true;
  }

  return false;
}

export const validateAge = (birthDate: string): string | null => {
    if (!birthDate) {
      return "Birth date is required";
    }
  
    const birth = new Date(birthDate);
    const today = new Date();
  
    const age = today.getFullYear() - birth.getFullYear();
    const isBirthdayPassedThisYear =
      today.getMonth() > birth.getMonth() ||
      (today.getMonth() === birth.getMonth() && today.getDate() >= birth.getDate());
  
    const isOver18 = age > 18 || (age === 18 && isBirthdayPassedThisYear);
  
    if (!isOver18) {
      return "You must be at least 18 years old";
    }
  
    return null;
  }