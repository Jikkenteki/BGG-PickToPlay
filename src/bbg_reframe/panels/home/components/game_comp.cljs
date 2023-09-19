(ns bbg-reframe.panels.home.components.game-comp
  (:require [goog.string.format]))

(defn game-comp [game on-click]
  [:div.p-2.my-2.bg-slate-700.rounded.cursor-pointer
   {:class "hover:bg-slate-600 last:mb-0"
    :on-click #(on-click (:id game))}
   [:a    (:name game)]])

