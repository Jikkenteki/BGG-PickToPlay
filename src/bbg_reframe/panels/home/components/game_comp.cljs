(ns bbg-reframe.panels.home.components.game-comp
  (:require
   [goog.string.format]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]))

(defn game-comp
  [game]
  [:div.p-2.my-2.bg-slate-700.rounded.cursor-pointer
   {:class "hover:bg-slate-600 last:mb-0"}
   [:a {:on-click #(re-frame/dispatch [::events/navigate [:game-view :id (:id game)]])}
    (:name game)]])

