(ns bbg-reframe.components.sort-buttons-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.model.sort-filter :refer [sorting-fun]]))

(defn sort-buttons-comp []
  (let [options (keys sorting-fun)
        value @(re-frame/subscribe [::subs/form :sort-id])]
    [:div.grid.grid-cols-2.grid-rows-2.gap-3.mb-1
     (doall (map
             (fn [option]
               ^{:key option}
               [:div.button.flex {:class (when (= value option) "active")
                                  :on-click #(re-frame/dispatch [::events/update-form :sort-id option])}
                [:p.m-auto (name option)]]) options))]))



