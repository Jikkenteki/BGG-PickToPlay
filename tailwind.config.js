module.exports = {
  mode: "jit",
  content: {
    files: ["./src/**/*.cljs", "./test/**/*.cljs"],
  },
  theme: {
    extend: {
      colors: {
        primary: {
          light: "#9e4c3a",
          DEFAULT: "#833f2f",
          dark: "#582a1f",
        },
        stone: {
          750: "#312c2b",
        },
      },
    },
  },
  plugins: [],
};
