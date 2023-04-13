(ns bbg-reframe.components.game-comp
  (:require
   [bbg-reframe.model.sort-filter :refer [game->best-rec-not playability time-rating playability-time]]
   [goog.string :as gstring]
   [goog.string.format]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]
   [bbg-reframe.config :refer [debug?]]
   [bbg-reframe.components.game-css-class :refer [game-css-class game-icon-span]]))

(def SHOW_PLAYABILITY debug?)
;; (def SHOW_PLAYABILITY false)

(defn game-comp
  [game players time]
  (let [playing-time (gstring/format "%d" (:playingtime game))
        playability-time (gstring/format "%.1f" (playability-time game players time))
        playability (gstring/format "%.1f" (playability game players))
        time-rating (gstring/format "%.1f" (time-rating game time))
        rating (gstring/format "%.1f"  (:rating game))]

    [:p {:class (game-css-class game players time)}
     (when SHOW_PLAYABILITY
       (str (:last-played game)
            "[" time-rating " : " playing-time " : " playability-time  "]"
                            ;;  (:id game) " "
            (case (game->best-rec-not game players)
              0 "B"
              1 "R "
              2 "N "
              "_") " , "
            playability " : " rating  " : "))
     (game-icon-span game players time)
     [:a {:on-click #(re-frame/dispatch [::events/navigate [:game-view :id (:id game)]])}
      (:name game)]]))

