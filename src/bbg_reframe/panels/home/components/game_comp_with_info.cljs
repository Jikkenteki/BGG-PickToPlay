(ns bbg-reframe.panels.home.components.game-comp-with-info
  (:require
   [goog.string :as gstring]
   [goog.string.format]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]
   [bbg-reframe.panels.game.components.game-css-class :refer [game-icon-span]]
   [bbg-reframe.subs :as subs]))

(defn game-comp-with-info
  [game]
  (let [rating (gstring/format "%.1f"  (:rating game))
        players @(re-frame/subscribe [::subs/form :players])
        time @(re-frame/subscribe [::subs/form :time-available])]
    [:div.p-2.my-2.bg-slate-700.rounded.cursor-pointer
     {:class "hover:bg-slate-600"}
     (game-icon-span game players time rating)
     [:a {:on-click #(re-frame/dispatch [::events/navigate [:game-view :id (:id game)]])}
      (:name game)]]))
