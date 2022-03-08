(ns bbg-reframe.components.game-comp
  (:require
   [bbg-reframe.model.sort-filter :refer [game->best-rec-not playability time-rating playability-time]]
   [goog.string :as gstring]
   [goog.string.format]))

(def SHOW_PLAYABILITY true)

(defn game-comp
  [game players time]
  (let [playing-time (gstring/format "%d" (:playingtime game))
        playability-time (gstring/format "%.1f" (playability-time game players time))
        playability (gstring/format "%.1f" (playability game players))
        time-rating (gstring/format "%.1f" (time-rating game time))
        rating (gstring/format "%.1f"  (:rating game))]
    [:p
     (when SHOW_PLAYABILITY
       (str "[" time-rating " : " playing-time " : " playability-time  "]"
                            ;;  (:id game) " "
            (case (game->best-rec-not game players)
              0 "B"
              1 "R "
              2 "N "
              "_") " , "
            playability " : " rating  " : "))
     (:name game)]))