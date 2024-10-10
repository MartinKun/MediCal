"use client";

export const Loader = () => {
  return (
    <div
      className="fixed
                 inset-0
                 z-[400]
                 flex
                 items-center
                 justify-center
                 transition-opacity
                 duration-500
                 ease-out
                 opacity-0
                 animate-fadeIn"
    >
      <div
        className="absolute
                   inset-0
                   bg-gradient
                   to-br
                   from-purple-400
                   via-pink-500
                   to-red-500
                   bg-opacity-75
                   backdrop-blur-sm"
      ></div>
      <div
        className="relative
                   w-full
                   h-full
                   flex
                   items-center
                   justify-center
                   bg-white
                   bg-opacity-20
                   backdrop-blur-lg
                   shadow-lg
                   transition-transform
                   transform scale-95
                   animate-scaleUp"
      >
        <div
          className="flex
                     flex-col
                     items-center"
        >
          <div
            className="relative
                       w-24 h-24"
          >
            <div
              className="absolute
                         top-0
                         left-0
                         w-full
                         h-full
                         border-8
                         border-white
                         border-opacity-25
                         rounded-full"
            ></div>
            <div
              className="absolute
                         top-0
                         left-0
                         w-full
                         h-full
                         border-8
                         border-transparent
                         border-t-white
                         rounded-full
                         animate-spin"
            ></div>
          </div>
          <h2
            className="mt-4
                       text-2xl
                       font-bold
                       text-white"
          >
            Cargando
          </h2>
          <p
            className="mt-2
                       text-white
                       text-opacity-80"
          >
            Por favor, espere un momento...
          </p>
        </div>
      </div>
    </div>
  );
};
