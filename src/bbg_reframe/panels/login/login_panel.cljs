(ns bbg-reframe.panels.login.login-panel
  (:require [bbg-reframe.firebase.firebase-events :as firebase-events]
            [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.forms.subs :as form-subs]
            [bbg-reframe.forms.utils :refer [if-nil?->value]]
            [bbg-reframe.panels.home.components.fetch-collection-comp :refer [fetch-collection-comp]]
            [bbg-reframe.panels.login.login-events :as login-events]
            [bbg-reframe.panels.login.login-subs :as login-subs]
            [clojure.string :refer [trim]]
            [re-frame.core :as re-frame]))

(defn save-games
  []
  (re-frame/dispatch [::firebase-events/fb-save-games]))

(defn login-comp
  []
  (let [email @(re-frame/subscribe [::form-subs/get-value [:login-form :email]])
        password @(re-frame/subscribe [::form-subs/get-value [:login-form :password]])]
    [:<>
     [:h1 "HMPWTP account"]
     [:h1 " Signed-in user email: " (if-nil?->value @(re-frame/subscribe [::login-subs/email]) "(not signed-in)")]
     [input-element {:class "input-box min-w-0 grow h-full"
                     :type :text
                     :placeholder "email"
                     :path [:login-form :email]
                     :post-fn trim}]
     [input-element {:class "input-box min-w-0 grow h-full"
                     :type :password
                     :placeholder "password"
                     :path [:login-form :password]}]
     [:button.button
      {:on-click #(re-frame/dispatch [::login-events/sign-in email password])} "Sign in"]
     [:button.button
      {:on-click #(re-frame/dispatch [::login-events/sign-up email password])} "Sign up"]
     [:button.button
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email]))
       :on-click #(re-frame/dispatch
                   [::login-events/sign-out])} "Sign out"]
     [:button.button
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click save-games} "Save games to account"]
     [:button.button
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click #(re-frame/dispatch [::firebase-events/fetch-games])} "Fetch games from account"]]))

(defn login-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [:h1 "Login"]
   [login-comp]
   [fetch-collection-comp]])