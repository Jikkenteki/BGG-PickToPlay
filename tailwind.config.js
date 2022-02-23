module.exports = {
  mode: "jit",
  content: {
    files: ["./src/**/*.cljs", "./test/**/*.cljs"],
  },
  theme: {
    extend: {},
  },
  plugins: [require("@tailwindcss/forms")],
};
