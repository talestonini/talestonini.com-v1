import { defineConfig } from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";

export default defineConfig({
  plugins: [scalaJSPlugin()],
  optimizeDeps: {
    include: ['tags'],
    link: ['tags'],
  },
});
