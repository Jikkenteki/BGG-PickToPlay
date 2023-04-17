(ns bbg-reframe.panels.home.components.bottom-overlay-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.panels.home.components.fetch-collection-comp :refer [fetch-collection-comp]]
   [bbg-reframe.panels.home.components.sliders-comp :refer [sliders-comp]]))

(defn bottom-overlay-comp []
  (let [open-tab @(re-frame/subscribe [::subs/ui :open-tab])]
    [:div.mb-2
     {:style {:display (when-not open-tab
                         "none")}}
     (case open-tab
       :sliders-tab [sliders-comp]
       :user-name-tab [fetch-collection-comp]
       nil)]))