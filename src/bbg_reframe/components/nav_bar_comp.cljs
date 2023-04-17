(ns bbg-reframe.components.nav-bar-comp
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.components.error-box-comp :refer [error-box-comp]]
            [bbg-reframe.events :as events]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.views.login-view.login-subs :as login-subs]))

(defn naive-nav-bar
  []
  (let [error-msg (re-frame/subscribe [::subs/error-msg])]
    [:<>
     (when @error-msg (error-box-comp @error-msg))
     [:div.flex-nowrap
      [:a {:on-click #(re-frame/dispatch [::events/navigate [:home]])} "Home"] " | "
      [:a {:on-click #(re-frame/dispatch [::events/navigate [:login-view]])} "Sign in/up/out"] " | "
      " User: " @(re-frame/subscribe [::login-subs/email])]]))