(ns bbg-reframe.components.sliders-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.components.slider-comp :refer [slider-comp]]
   [bbg-reframe.components.checkbox-with-connector-comp :refer [checkbox-with-connector-comp]]))

(defn sliders-comp []
  (let [show-expansions? @(re-frame/subscribe [::subs/form :show-expansions?])]

    [:<>
  ;;  [slider-comp :take "Take" 1 100 1]
     [slider-comp :higher-than "Rating higher than" 0 10 0.1]
     [slider-comp :players "For number of players" 1 10 1]
  ;;  [slider-comp :threshold "Playability threshold" 0 0.95 0.05]
     [slider-comp :time-available "Time available" 10 500 10]
     [:div.flex
      [:div.pr-1 {:class "basis-1/2"}
       [checkbox-with-connector-comp
        {:label "Show expansions"
         :checked? show-expansions?
         :on-click #(re-frame/dispatch [::events/update-form :show-expansions? (not show-expansions?)])}]]]]))