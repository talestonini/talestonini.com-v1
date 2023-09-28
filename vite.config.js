import { defineConfig } from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";

export default defineConfig({
  plugins: [scalaJSPlugin()],

  // Tales 28 Sep 2023:
  // Uncomment perhaps for deployment, but certainly not for Node development, as the tags module goes stale/cached in 
  // the browser.
  //
  // optimizeDeps: {
  //   // Tales 28 Sep 2023: 
  //   // Removing the tags from the include list still makes the tags be delivered to the browser.
  //   // I need to figure out the difference.
  //   include: ['tags'],
  // },
});
