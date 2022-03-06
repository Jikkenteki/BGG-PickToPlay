(ns bbg-reframe.components.game-comp
  (:require
   [bbg-reframe.model.sort-filter :refer [game->best-rec-not playability time-rating playability-time]]
   [goog.string :as gstring]
   [goog.string.format]))

(def SHOW_PLAYABILITY true)

(defn game-comp
  [game players time]
  (let [playability (gstring/format "%.2f" (playability
                                            game players))
        time-rating (gstring/format "%.2f" (time-rating
                                            game time))]
    ^{:key (random-uuid)}
    [:p
     (when SHOW_PLAYABILITY
       (str
        " [ " time-rating " - " (gstring/format "%5d" (:playingtime game)) " - "
        (gstring/format "%.2f" (playability-time game players time)) " ] "
                            ;;  (:id game) " "
        (case (game->best-rec-not game players)
          0 "Best"
          1 "Rec "
          2 "Not "
          "loading") " - "
        playability " - " (gstring/format "%.2f"  (:rating game))  " - "))
     (:name game)]))