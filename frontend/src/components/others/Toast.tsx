import { useBoundStore } from "@/store/store";
import { AlertLogo } from "@/svg/AlertLogo";
import { BanLogo } from "@/svg/BanLogo";
import { CheckLogo } from "@/svg/CheckLogo";
import { CloseLogo } from "@/svg/CloseLogo";
import { capitalizeWords } from "@/util/stringUtils";
import { useEffect } from "react";

export const Toast = () => {
  const toast = useBoundStore((state) => state.toast);
  const hideToast = useBoundStore((state) => state.hideToast);

  useEffect(() => {
    if (toast?.isShowing) {
      const timer = setTimeout(() => {
        hideToast();
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [toast?.isShowing]);

  return (
    <>
      {toast?.isShowing && (
        <div
          className="fixed w-full
                     bottom-0 z-[900]
                     flex justify-center
                     pb-[20px] animate-slideInBottom"
        >
          <div
            className={`lg:w-[1000px] w-[90%]
                        ${toast.status === "error" && "bg-[#d0342c]"}
                        ${toast.status === "warning" && "bg-[#ffd151]"}
                        ${toast.status === "success" && "bg-[#198754]"}
                         rounded flex py-[20px]
                         px-[40px] ${
                           (toast.status === "error" ||
                             toast.status === "success") &&
                           "text-white"
                         }`}
          >
            <div
              className="w-full flex
                           gap-x-[20px]"
            >
              <div className="w-[40px] lg:w-[50px]">
                {toast.status === "error" && <BanLogo />}
                {toast.status === "warning" && <AlertLogo />}
                {toast.status === "success" && <CheckLogo />}
              </div>
              <div className="flex flex-col">
                <h3 className="text-ls">{capitalizeWords(toast.status)}</h3>
                <p className="text-sm lg:text-base">{toast.message}</p>
              </div>
            </div>

            <div
              className="w-[40px] lg:w-[50px] cursor-pointer"
              onClick={() => hideToast()}
            >
              <CloseLogo
                color={
                  toast.status == "error" || toast.status == "success"
                    ? "white"
                    : "#556970"
                }
              />
            </div>
          </div>
        </div>
      )}
    </>
  );
};
