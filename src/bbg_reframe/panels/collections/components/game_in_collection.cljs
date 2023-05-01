(ns bbg-reframe.panels.collections.components.game-in-collection
  (:require             [bbg-reframe.events :as events]

                        [bbg-reframe.panels.collections.collections-events :as collections-events]
                        [re-frame.core :as re-frame]))


(defn game-in-collection
  [id game]
  ^{:key (:id game)}
  [:div.mb-2.last:mb-0
   [:button.button.text-slate-950.bg-orange-400.py-1.mr-2.border-none.h-auto {:on-click #(re-frame/dispatch [::collections-events/remove-game-from-collection [(keyword id) (:id game)]])}
    [:i.fa-solid.fa-minus]]
   [:button {:on-click #(re-frame/dispatch [::events/navigate [:game-view :id (:id game)]])}

    (:name game)]])
