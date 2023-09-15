(ns bbg-reframe.panels.home.components.bottom-overlay-comp
  (:require
   [bbg-reframe.panels.home.components.sliders-comp :refer [sliders-comp]]))

(defn bottom-overlay-comp []
  [:div.mb-2
   [sliders-comp]])