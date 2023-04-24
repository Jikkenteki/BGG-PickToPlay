(ns bbg-reframe.components.nav-bar-comp
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.components.error-box-comp :refer [error-box-comp]]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.panels.login.login-subs :as login-subs]))

(defn naive-nav-bar
  []
  (let [error-msg (re-frame/subscribe [::subs/error-msg])
        connected? @(re-frame/subscribe [::login-subs/connected-to-fb])]
    [:<>
     (when @error-msg (error-box-comp @error-msg))
     [:div.flex-nowrap
      " User: " @(re-frame/subscribe [::login-subs/email])
      [:span (if connected? {:class "fa fa-plug"} {:class "fa fa-exclamation-triangle"})]]]))