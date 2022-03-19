(ns bbg-reframe.login-view.view
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.forms.forms :refer [input]]
            [bbg-reframe.forms.subs :as subs]
            [bbg-reframe.login-view.events :as login-events]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.login-view.subs :as login-subs]
            [bbg-reframe.forms.utils :refer [if-nil?->value]]
            [clojure.string :refer [trim]]
            [bbg-reframe.forms.events :as form-events]
            [bbg-reframe.events :as events]
            [bbg-reframe.network-events :as network-events]))

(defn save-games
  []
  (let [games @(re-frame/subscribe [::form-events/get-value [:games]])]
    (println "Save" games)
    (re-frame/dispatch [::events/fb-set ["cached-games"] (str games)])))

(defn fetch-games
  []
  (re-frame/dispatch [::network-events/fetch-games]))

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
      {:on-click #(re-frame/dispatch [::login-events/sign-in email password])} "Sign in"]
     [:button.button.min-w-fit.px-2.ml-1
      {:on-click #(re-frame/dispatch [::login-events/sign-up email password])} "Sign up"]
     [:button.button.min-w-fit.px-2.ml-1
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email]))
       :on-click #(re-frame/dispatch
                   [::login-events/sign-out])} "Sign out"]
     [:button.button.min-w-fit.px-2.ml-1
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click save-games} "Save games to account"]
     [:button.button.min-w-fit.px-2.ml-1
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click fetch-games} "Fetch games from account"]]))

(defn login-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [naive-nav-bar]

   [:h1 "Login"]
   [login-comp]])