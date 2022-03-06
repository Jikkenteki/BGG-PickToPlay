(ns bbg-reframe.components.sliders-comp
  (:require
   [bbg-reframe.components.slider-comp :refer [slider-comp]]))

(defn sliders-comp []
  [:<>
   (slider-comp :take "Take" 1 100 1)
   (slider-comp :higher-than "Rating higher than" 0 10 0.1)
   (slider-comp :players "For number of players" 1 10 1)
   (slider-comp :threshold "Playability threshold" 0 0.95 0.05)
   (slider-comp :time-available "Time available" 10 500 10)])