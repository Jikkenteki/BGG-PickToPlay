module.exports = {
  mode: "jit",
  content: {
    files: ["./src/**/*.cljs", "./test/**/*.cljs"],
  },
  theme: {},
  plugins: [require("@tailwindcss/forms")],
};
