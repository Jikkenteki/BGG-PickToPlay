(ns bbg-reframe.components.bottom-overlay-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.components.fetch-collection-comp :refer [fetch-collection-comp]]
   [bbg-reframe.components.sliders-comp :refer [sliders-comp]]
   [bbg-reframe.components.sort-buttons-comp :refer [sort-buttons-comp]]))

(defn bottom-overlay-comp []
  (let [open-tab @(re-frame/subscribe [::subs/ui :open-tab])]
    [:div.mb-2
     {:style {:display (when-not open-tab
                         "none")}}
     (case open-tab
       :sliders-tab [sliders-comp]
       :user-name-tab [fetch-collection-comp]
       :sort-tab [sort-buttons-comp]
       nil)]))