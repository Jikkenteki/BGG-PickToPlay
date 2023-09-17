(ns bbg-reframe.panels.home.components.sliders-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.panels.home.components.slider-comp :refer [slider-comp]]
   [bbg-reframe.panels.home.components.checkbox-with-connector-comp :refer [checkbox-with-connector-comp]]
   [bbg-reframe.panels.home.components.filter-collections-comp :refer [filter-collections-comp]]
   [bbg-reframe.panels.login.login-subs :as login-subs]))

(defn sliders-comp []
  (let [only-available? @(re-frame/subscribe [::subs/form :only-available?])
        show-expansions? @(re-frame/subscribe [::subs/form :show-expansions?])
        email @(re-frame/subscribe [::login-subs/email])]
    [:<>
    ;;  [slider-comp :take "Take" 1 100 1]
     [slider-comp :players "For number of players" 1 10 1]
    ;;  [slider-comp :threshold "Playability  threshold" 0 0.95 0.05]
     [slider-comp :time-available "Time available" 10 500 10]
     [filter-collections-comp]
     [:div.flex.flex-col.gap-4
      {:class "w-[50%]"}
      [checkbox-with-connector-comp
       {:label "Show also expansions"
        :checked? show-expansions?
        :on-click #(re-frame/dispatch [::events/update-form :show-expansions? (not show-expansions?)])}]
      [checkbox-with-connector-comp
       {:label "Show only available"
        :disabled? (nil? email)
        :checked? only-available?
        :on-click #(when (not (nil? email))
                     (re-frame/dispatch [::events/update-form :only-available? (not only-available?)]))}]]]))