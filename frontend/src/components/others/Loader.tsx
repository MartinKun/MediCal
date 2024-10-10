export const Loader = () => {
  return (
    <div
      className="min-h-screen
                 bg-gradient-to-br
                 from-purple-400
                 via-pink-500
                 to-red-500
                 flex
                 items-center
                 justify-center"
    >
      <div
        className="bg-white
                   bg-opacity-20
                   backdrop-blur-lg
                   rounded-xl
                   p-8 shadow-lg"
      >
        <div
          className="flex
                     flex-col
                     items-center"
        >
          <div className="relative w-24 h-24">
            <div
              className="absolute
                         top-0 left-0
                         w-full h-full
                         border-8
                         border-white
                         border-opacity-25
                         rounded-full"
            ></div>
            <div
              className="absolute
                         top-0 left-0
                         w-full h-full
                         border-8
                         border-transparent
                         border-t-white
                         rounded-full
                         animate-spin"
            ></div>
          </div>
          <h2
            className="mt-4 text-2xl
                       font-bold
                       text-white"
          >
            Cargando
          </h2>
          <p
            className="mt-2 text-white
                       text-opacity-80"
          >
            Por favor, espere un momento...
          </p>
        </div>
      </div>
    </div>
  );
};
