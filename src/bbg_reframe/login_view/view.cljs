(ns bbg-reframe.login-view.view
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.forms.forms :refer [input]]
            [bbg-reframe.forms.subs :as subs]
            [bbg-reframe.login-view.events :as events]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.login-view.subs :as login-subs]
            [bbg-reframe.forms.utils :refer [if-nil?->value]]
            [clojure.string :refer [trim]]))

(defn login-comp
  []
  (let [email @(re-frame/subscribe [::subs/get-value [:login-form :email]])
        password @(re-frame/subscribe [::subs/get-value [:login-form :password]])]
    [:div
     [:h1 "HMPWTP account"]
     [:h1 " User: " (if-nil?->value @(re-frame/subscribe [::login-subs/email]) "(none)")]
     [input {:label "email"
             :type :text
             :path [:login-form :email]
             :post-fn trim}]
     [input {:label "password"
             :type :password
             :path [:login-form :password]}]


     [:button.button.min-w-fit.px-2.ml-1
      {:on-click #(re-frame/dispatch [::events/sign-in email password])} "Sign in"]
     [:button.button.min-w-fit.px-2.ml-1
      {:on-click #(re-frame/dispatch [::events/sign-up email password])} "Sign up"]
     [:button.button.min-w-fit.px-2.ml-1
      {:on-click #(re-frame/dispatch
                   [::events/sign-out])} "Sign out"]]))

(defn login-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [naive-nav-bar]

   [:h1 "Login"]
   [login-comp]])